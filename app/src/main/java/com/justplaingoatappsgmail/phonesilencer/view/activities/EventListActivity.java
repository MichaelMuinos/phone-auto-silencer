package com.justplaingoatappsgmail.phonesilencer.view.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetNormalService;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetSilentService;
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

    private static final int CHOICE_GIVEN = 9000;
    private EventListAdapter eventListAdapter;
    private Snackbar snackBar;

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
        eventListAdapter = new EventListAdapter(this);
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
        if (requestCode == CHOICE_GIVEN) {
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
        Intent intent = new Intent(EventListActivity.this, EventPostActivity.class);
        startActivityForResult(intent, CHOICE_GIVEN);
    }

    /**
     * Method for interface EventListListener
     * When a card view is clicked, we enter the EventPostActivity with the details
     * @param position
     */
    @Override
    public void onEventListCardViewClick(int position) {
//        presenter.getEvents().get(position);
    }

    @Override
    public void onEventListSwitchCheckedChanged(Event event, View switchView, View positionTagView, boolean isChecked) {
        Switch eventSwitch = (Switch) switchView;
        TagView eventTag = (TagView) positionTagView;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(isChecked) {
            eventSwitch.setText("Enabled\t");
            eventTag.setText("Enabled");
            // set our alarms
            setAlarms(event, alarmManager);
        } else {
            eventSwitch.setText("Disabled\t");
            eventTag.setText("Disabled");
            // cancel our alarms // TODO:
            cancelAlarms(event, alarmManager);
        }
    }

    /**
     * Removes an event from the recycler view when clicking on the close button
     * @param event
     */
    @Override
    public void onEventListDeleteClickListener(Event event) {
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

    /**
     * Method for the EventListContract.View
     * Used to show our snack bar if we do not have any Events
     */
    @Override
    public void showSnackBarNoEventsMessage() {
        snackBar = Snackbar.make(coordinatorLayout, "You have no events created!", Snackbar.LENGTH_INDEFINITE);
        snackBar.show();
    }

    /**
     * Method for the EventListContract.View
     * Used to show our snack bar if we want to undo the deletion of an Event
     */
    @Override
    public void showSnackBarUndoMessage() {

    }

    @Override
    public void showEventEnabledMessage(String eventName) {
        snackBar = Snackbar.make(coordinatorLayout, eventName + " has been enabled.", Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    @Override
    public void showEventDisabledMessage(String eventName) {
        snackBar = Snackbar.make(coordinatorLayout, eventName + " has been disabled.", Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    private PendingIntent createPendingIntent(Class service, int ringerMode, int requestCode) {
        Intent intent = new Intent(context, service);
        intent.putExtra(Event.RINGER_MODE, ringerMode);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            // create pending intent
            PendingIntent start = createPendingIntent(SetSilentService.class, event.getRingerMode(), startRequestCode);
            PendingIntent end = createPendingIntent(SetNormalService.class, AudioManager.RINGER_MODE_NORMAL, endRequestCode);
            // create calendars
            Calendar startCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getStartTimeHour(), event.getStartTimeMinute(), event.getStartTimeAmOrPm());
            Calendar endCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getEndTimeHour(), event.getEndTimeMinute(), event.getEndTimeAmOrPm());
            // set our alarm manager triggers
            // RTC: Fires pending intent but does not wake up device
            alarmManager.setRepeating(AlarmManager.RTC, startCalendar.getTimeInMillis(), 24 * 60 * 60 * 1000, start);
            alarmManager.setRepeating(AlarmManager.RTC, endCalendar.getTimeInMillis(), 24 * 60 * 60 * 1000, end);
        }
        presenter.addPendingIntentRequestCodes(event.getEventName(), requestCodes);
    }

    private void cancelAlarms(Event event, AlarmManager alarmManager) {

    }

}
