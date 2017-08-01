package com.justplaingoatappsgmail.phonesilencer.view.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetNormalService;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetRingerService;
import com.justplaingoatappsgmail.phonesilencer.view.adapters.EventListAdapter;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.veinhorn.tagview.TagView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventListActivity extends AppCompatActivity implements EventListContract.View, EventListListener {

    private static final int MAX_EVENTS = 2;
    private EventListAdapter eventListAdapter;

    @BindView(R.id.activity_event_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_event_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.activity_event_toolbar) Toolbar toolbar;

    @Inject EventListContract.Presenter presenter;
    @Inject Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        // bind views
        ButterKnife.bind(this);
        // allow injection
        ((PhoneSilencerApplication) getApplication()).getComponent().inject(this);
        // set action bar
        setSupportActionBar(toolbar);
        // create event list adapter
        eventListAdapter = new EventListAdapter(this, context);
        // set adapter and layout manager for the recycler view
        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // set view to null to avoid memory leak
        presenter.setView(null);
        // close our realm instance
        presenter.closeRealm();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.START_ACTIVITY_FOR_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                eventListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * On click method for our floating action button which will
     * start a new activity EventPostActivity
     */
    @OnClick(R.id.activity_event_fab)
    public void onFabClick() {
        // retrieve notification manager and check if we have permission to change ringer mode
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent settingsIntent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(settingsIntent);
        } else {
            // if there is less than 2 events, the user may create another
            if(presenter.getNumberOfEvents() < MAX_EVENTS) {
                Intent intent = new Intent(EventListActivity.this, EventPostActivity.class);
                startActivityForResult(intent, AppConstants.START_ACTIVITY_FOR_RESULT_CODE);
            // start the pay for premium popup
            } else {
                new MaterialDialog.Builder(this)
                        .title(R.string.buy_premium_title)
                        .content(R.string.buy_premium_content)
                        .positiveText(R.string.buy_premium_positive)
                        // on click for the buy premium button
                        // take user to the page of the premium app in google play
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(getString(R.string.market_id)));
                                if (!canStartActivity(intent)) {
                                    // Market (Google play) app seems not installed, let's try to open a web browser
                                    intent.setData(Uri.parse(getString(R.string.market_id_browser)));
                                    if (!canStartActivity(intent)) {
                                        // Well if this also fails, we have run out of options, inform the user.
                                        AppConstants.showSnackBarMessage(coordinatorLayout, getString(R.string.play_store_error), context, R.color.red_color);
                                    }
                                }
                            }
                        })
                        .negativeText(R.string.buy_premium_negative)
                        // on click for cancel button
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * Method for interface EventListListener
     * When a card view is clicked, we enter the EventPostActivity with the details
     * @param position
     */
    @Override
    public void onEventListCardViewClick(int position) {
        Intent intent = new Intent(EventListActivity.this, EventPostActivity.class);
        intent.putExtra(AppConstants.EVENT_OBJECT, presenter.getEvents().get(position));
        startActivityForResult(intent, AppConstants.START_ACTIVITY_FOR_RESULT_CODE);
    }

    @Override
    public void onEventListSwitchCheckedChanged(Event event, View switchView, View positionTagView) {
        Switch eventSwitch = (Switch) switchView;
        TagView eventTag = (TagView) positionTagView;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(eventSwitch.isChecked() && !event.isEnabled()) {
            eventSwitch.setText("Enabled\t");
            eventTag.setText("Enabled");
            // set our alarms
            setAlarms(event, alarmManager);
        } else if(!eventSwitch.isChecked() && event.isEnabled()){
            eventSwitch.setText("Disabled\t");
            eventTag.setText("Disabled");
            // cancel our alarms
            cancelAlarms(event, alarmManager, false);
        }
    }

    /**
     * Removes an event from the recycler view when clicking on the close button
     * @param event
     */
    @Override
    public void onEventListDeleteClickListener(Event event) {
        if(event.isEnabled()) cancelAlarms(event, (AlarmManager) getSystemService(Context.ALARM_SERVICE), true);
        // delete the event at the particular position in the list
        presenter.deleteEvent(event);
        // reset our event list for our adapter
        eventListAdapter.setEventList(presenter.getEvents());
        // notify the adapter that the list has changed and to update the view
        eventListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEvents() {
        eventListAdapter.setEventList(presenter.getEvents());
    }

    @Override
    public void showSnackBarNoEventsMessage() {
        AppConstants.showSnackBarMessage(coordinatorLayout, "No events created. Go make some!", context, R.color.white_color);
    }

    @Override
    public void showEventEnabledMessage(String eventName) {
        AppConstants.showSnackBarMessage(coordinatorLayout, eventName + " has been enabled.", context, R.color.white_color);
    }

    @Override
    public void showEventDisabledMessage(String eventName) {
        AppConstants.showSnackBarMessage(coordinatorLayout, eventName + " has been disabled.", context, R.color.white_color);
    }

    @Override
    public void removeNotification(int notificationId) {
        NotificationManager notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.cancel(notificationId);
    }

    private void setAlarms(Event event, AlarmManager alarmManager) {
        List<Integer> requestCodes = new ArrayList<>();
        // cycle through our days and set the calendars
        for(int i = 0; i < event.getDays().size(); i++) {
            // create start and end request code
            int startRequestCode = presenter.getIncrementedRequestCode();
            int endRequestCode = presenter.getIncrementedRequestCode();
            // add both to our request code list
            requestCodes.add(startRequestCode);
            requestCodes.add(endRequestCode);
            // create calendars
            Calendar startCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getStartTimeHour(), event.getStartTimeMinute());
            Calendar endCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getEndTimeHour(), event.getEndTimeMinute());
            // if we are attempting to set a time that has already passed, set both alarms forward by a week
            if(System.currentTimeMillis() > startCalendar.getTimeInMillis() && System.currentTimeMillis() > endCalendar.getTimeInMillis()) {
                startCalendar.setTimeInMillis(startCalendar.getTimeInMillis() + AppConstants.WEEK_IN_MILLISECONDS);
                endCalendar.setTimeInMillis(endCalendar.getTimeInMillis() + AppConstants.WEEK_IN_MILLISECONDS);
            }
            // create pending intent
            PendingIntent start = AppConstants.createPendingIntentForSettingAlarms(SetRingerService.class, context, event, startCalendar, startRequestCode);
            PendingIntent end = AppConstants.createPendingIntentForSettingAlarms(SetNormalService.class, context, event, endCalendar, endRequestCode);
            // set our alarm manager triggers
            // RTC: Fires pending intent but does not wake up device
            // if build version is less than 19, we can use set repeating. If it is greater than 19, setRepeating is unreliable
            // and will cause an inexact repeating alarm, thus we must use setExact.
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                if(event.getRepeat().equals("Once")) {
                    alarmManager.set(AlarmManager.RTC, startCalendar.getTimeInMillis(), start);
                    alarmManager.set(AlarmManager.RTC, endCalendar.getTimeInMillis(), end);
                } else {
                    alarmManager.setRepeating(AlarmManager.RTC, startCalendar.getTimeInMillis(), AppConstants.REPEAT_MAP.get(event.getRepeat()), start);
                    alarmManager.setRepeating(AlarmManager.RTC, endCalendar.getTimeInMillis(), AppConstants.REPEAT_MAP.get(event.getRepeat()), end);
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC, startCalendar.getTimeInMillis(), start);
                alarmManager.setExact(AlarmManager.RTC, endCalendar.getTimeInMillis(), end);
            }
        }
        presenter.updateEvent(event, true);
        presenter.addRequestCodes(event, requestCodes);
    }

    private void cancelAlarms(Event event, AlarmManager alarmManager, boolean isDeleted) {
        List<Integer> requestCodes = presenter.getEventRequestCodes(event);
        for(int i = 0; i < requestCodes.size(); i += 2) {
            PendingIntent start = AppConstants.createPendingIntentForDeletingAlarms(SetRingerService.class, context, requestCodes.get(i));
            PendingIntent end = AppConstants.createPendingIntentForDeletingAlarms(SetNormalService.class, context, requestCodes.get(i + 1));
            alarmManager.cancel(start);
            alarmManager.cancel(end);
        }
        if(!isDeleted) presenter.updateEvent(event, false);
        presenter.deleteAndRemoveShowingNotificationIfActive(event);
        presenter.deleteRequestCodes(event, isDeleted);
    }

    private boolean canStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

}
