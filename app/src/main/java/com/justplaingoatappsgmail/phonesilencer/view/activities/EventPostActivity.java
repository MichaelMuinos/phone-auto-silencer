package com.justplaingoatappsgmail.phonesilencer.view.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.RealmInteger;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetNormalService;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetRingerService;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EventPostActivity extends AppCompatActivity implements EventPostContract.View {

    private int startTimeHour = 0;
    private int startTimeMinute = 0;
    private int endTimeHour = 0;
    private int endTimeMinute = 0;
    private static Map<Integer,Integer> dayMap;
    private static Map<Integer,Boolean> daySelectedMap;

    @BindView(R.id.event_post_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.event_post_event_name_title) TextView eventNameTitle;
    @BindView(R.id.event_post_time_title) TextView timeTitle;
    @BindView(R.id.event_post_schedule_title) TextView scheduleTitle;
    @BindView(R.id.event_post_name) EditText eventName;
    @BindView(R.id.event_post_silence_button) RadioButton silenceButton;
    @BindView(R.id.event_post_vibrate_button) RadioButton vibrateButton;
    @BindView(R.id.event_post_start_time) TextView startTime;
    @BindView(R.id.event_post_end_time) TextView endTime;
    @BindView(R.id.event_post_repeat_spinner) Spinner repeatSpinner;
    @BindView(R.id.event_post_monday_text_view) TextView monday;
    @BindView(R.id.event_post_tuesday_text_view) TextView tuesday;
    @BindView(R.id.event_post_wednesday_text_view) TextView wednesday;
    @BindView(R.id.event_post_thursday_text_view) TextView thursday;
    @BindView(R.id.event_post_friday_text_view) TextView friday;
    @BindView(R.id.event_post_saturday_text_view) TextView saturday;
    @BindView(R.id.event_post_sunday_text_view) TextView sunday;
    @BindViews({R.id.event_post_monday_text_view, R.id.event_post_tuesday_text_view,
                R.id.event_post_wednesday_text_view, R.id.event_post_thursday_text_view,
                R.id.event_post_friday_text_view, R.id.event_post_saturday_text_view,
                R.id.event_post_sunday_text_view}) List<TextView> days;

    @Inject EventPostContract.Presenter presenter;
    @Inject Context context;

    static {
        dayMap = new HashMap<>();
        dayMap.put(Calendar.SUNDAY, R.id.event_post_sunday_text_view);
        dayMap.put(Calendar.MONDAY, R.id.event_post_monday_text_view);
        dayMap.put(Calendar.TUESDAY, R.id.event_post_tuesday_text_view);
        dayMap.put(Calendar.WEDNESDAY, R.id.event_post_wednesday_text_view);
        dayMap.put(Calendar.THURSDAY, R.id.event_post_thursday_text_view);
        dayMap.put(Calendar.FRIDAY, R.id.event_post_friday_text_view);
        dayMap.put(Calendar.SATURDAY, R.id.event_post_saturday_text_view);

        daySelectedMap = new HashMap<>();
        daySelectedMap.put(R.id.event_post_sunday_text_view, false);
        daySelectedMap.put(R.id.event_post_monday_text_view, false);
        daySelectedMap.put(R.id.event_post_tuesday_text_view, false);
        daySelectedMap.put(R.id.event_post_wednesday_text_view, false);
        daySelectedMap.put(R.id.event_post_thursday_text_view, false);
        daySelectedMap.put(R.id.event_post_friday_text_view, false);
        daySelectedMap.put(R.id.event_post_saturday_text_view, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_post);
        // bind our views that are part of the activity
        ButterKnife.bind(this);
        // allow injection of presenter
        ((PhoneSilencerApplication) getApplication()).getComponent().inject(this);
        // if user is trying to update event, set all ui elements to Event data
        Event event = getIntent().getParcelableExtra(AppConstants.EVENT_OBJECT);
        if(event != null) setEventDataToElements(event);
        // setup components for actionbar
        initActionBar(event);
    }

    private void setEventDataToElements(Event event) {
        // set our event name
        eventName.setText(event.getEventName());
        // set our start time
        startTimeHour = event.getStartTimeHour();
        startTimeMinute = event.getStartTimeMinute();
        startTime.setText(AppConstants.convertTimeToString(startTimeHour, startTimeMinute));
        // set our end time
        endTimeHour = event.getEndTimeHour();
        endTimeMinute = event.getEndTimeMinute();
        endTime.setText(AppConstants.convertTimeToString(endTimeHour, endTimeMinute));
        // set our repeat option
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(event.getRepeat());
        repeatSpinner.setSelection(spinnerPosition);
        // set our day text views
        for(RealmInteger realmInteger : event.getDays()) {
            int id = dayMap.get(realmInteger.getRealmInt());
            daySelectedMap.put(id, true);
            int resource = R.drawable.circle_red;
            switch(id) {
                case R.id.event_post_monday_text_view:
                    monday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_tuesday_text_view:
                    tuesday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_wednesday_text_view:
                    wednesday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_thursday_text_view:
                    thursday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_friday_text_view:
                    friday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_saturday_text_view:
                    saturday.setBackgroundResource(resource);
                    break;
                case R.id.event_post_sunday_text_view:
                    sunday.setBackgroundResource(resource);
                    break;
                default:
                    break;
            }
        }
        // set either vibrate or silence button
        if(event.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            vibrateButton.setChecked(true);
            silenceButton.setChecked(false);
        } else {
            vibrateButton.setChecked(false);
            silenceButton.setChecked(true);
        }
    }

    private void initActionBar(final Event event) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_event_post_action_bar, null);
        // add button
        TextView addEventButton = ButterKnife.findById(view, R.id.event_post_add_event);
        // if event is not null, set our text to say 'update event'
        if(event != null) addEventButton.setText("\tUPDATE EVENT");
        // setup functionality for the add button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if what we entered is valid
                boolean isValidEvent = presenter.isValidEvent(event == null ? "" : event.getEventName(), eventName.getText().toString().trim(), startTimeHour, startTimeMinute,
                        endTimeHour, endTimeMinute, getDays(), event == null ? false : true);
                if(isValidEvent) {
                    // if we are updating and it is enabled, cancel alarms for that event
                    if(event != null && event.isEnabled()) cancelAlarms(event, (AlarmManager) getSystemService(Context.ALARM_SERVICE));
                    // save our event
                    presenter.saveEvent(event == null ? UUID.randomUUID().toString() : event.getId(),
                            eventName.getText().toString().trim(), startTimeHour, startTimeMinute, endTimeHour, endTimeMinute,
                            vibrateButton.isChecked() ? AudioManager.RINGER_MODE_VIBRATE : AudioManager.RINGER_MODE_SILENT,
                            getDays(), repeatSpinner.getSelectedItem().toString(), event == null ? false : true);
                    // return to our list activity
                    returnToEventListActivity(Activity.RESULT_OK);
                }
            }
        });
        // cancel button
        ImageView cancelButton = ButterKnife.findById(view, R.id.event_post_cancel_event);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToEventListActivity(Activity.RESULT_CANCELED);
            }
        });
        // actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);
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
    }

    @Override
    public void showEventNameError() {
        eventNameTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));
        timeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        scheduleTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        AppConstants.showSnackBarMessage(coordinatorLayout, "Error: Event name is invalid!", context, R.color.red_color);
    }

    @Override
    public void showEventNameLengthError() {
        eventNameTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));
        timeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        scheduleTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        AppConstants.showSnackBarMessage(coordinatorLayout, "Error: Event name is too long (20 character max)!", context, R.color.red_color);
    }

    @Override
    public void showEventNameDuplicateError() {
        eventNameTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));
        timeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        scheduleTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        AppConstants.showSnackBarMessage(coordinatorLayout, "Error: There is already an event with that name!", context, R.color.red_color);
    }

    @Override
    public void showStartEndTimeConflictError() {
        timeTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));
        eventNameTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        scheduleTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        AppConstants.showSnackBarMessage(coordinatorLayout, "Error: Start and end time are the same!", context, R.color.red_color);
    }

    @Override
    public void showNoDaysSelectedError() {
        scheduleTitle.setTextColor(ContextCompat.getColor(context, R.color.red_color));
        eventNameTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        timeTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        AppConstants.showSnackBarMessage(coordinatorLayout, "Error: No days have been selected!", context, R.color.red_color);
    }

    @Override
    public void removeNotification(int notificationId) {
        NotificationManager notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.cancel(notificationId);
    }

    @OnClick({R.id.event_post_start_time, R.id.event_post_end_time})
    public void onTimeClick(View timeView) {
        // convert our view to a text view so that we can set the time.png.png.png.png.png string
        final TextView textView = (TextView) timeView;
        // set the callback method and call our presenter to convert what the user selected into a string
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                textView.setText(AppConstants.convertTimeToString(hourOfDay, minute));
                if(textView.getId() == R.id.event_post_start_time) {
                    startTimeHour = hourOfDay;
                    startTimeMinute = minute;
                } else if(textView.getId() == R.id.event_post_end_time){
                    endTimeHour = hourOfDay;
                    endTimeMinute = minute;
                }
            }
        };
        // create our dialog
        TimePickerDialog tpd = TimePickerDialog.newInstance(listener, false);
        tpd.setTitle(timeView.getId() == R.id.event_post_start_time ? "Start Time" : "End Time");
        tpd.vibrate(false);
        tpd.show(getFragmentManager(), "Timer");
    }

    /**
     * On click for the monday - sunday text views. When clicked, they will transition into a different drawable.
     * @param view
     */
    @OnClick({R.id.event_post_monday_text_view, R.id.event_post_tuesday_text_view, R.id.event_post_wednesday_text_view, R.id.event_post_thursday_text_view, R.id.event_post_friday_text_view, R.id.event_post_saturday_text_view, R.id.event_post_sunday_text_view})
    public void onDayClick(View view) {
        view.setBackgroundResource(daySelectedMap.get(view.getId()) ? R.drawable.circle_green : R.drawable.circle_red);
        daySelectedMap.put(view.getId(), !daySelectedMap.get(view.getId()));
    }

    /**
     * On Check Changed method for the all days check.
     * This will determine whether we need to color all of the days
     * with the red or green drawable.
     * @param isChecked
     */
    @OnCheckedChanged(R.id.event_post_check_box)
    public void onCheckBoxCheckedChanged(boolean isChecked) {
        if(isChecked) setDrawableForAllDays(ContextCompat.getDrawable(context, R.drawable.circle_red), true);
        else setDrawableForAllDays(ContextCompat.getDrawable(context, R.drawable.circle_green), false);
    }

    @OnClick({R.id.event_post_vibrate_button, R.id.event_post_silence_button})
    public void onRingerModeClick(View view) {
        switch(view.getId()) {
            case R.id.event_post_vibrate_button:
                if(silenceButton.isChecked()) silenceButton.setChecked(false);
                break;
            case R.id.event_post_silence_button:
                if(vibrateButton.isChecked()) vibrateButton.setChecked(false);
                break;
            default:
                break;
        }
    }

    /**
     * Method used to return us back to our list activity depending on if we created
     * a new event / updated event or not
     * @param code
     */
    private void returnToEventListActivity(int code) {
        Intent returnIntent = new Intent();
        setResult(code, returnIntent);
        finish();
    }

    /**
     * Method will set all day text views to the same drawable if they
     * are not already containing that drawable
     * @param drawable
     */
    private void setDrawableForAllDays(Drawable drawable, boolean selected) {
        for(TextView textView : days) {
            textView.setBackground(drawable);
            daySelectedMap.put(textView.getId(), selected);
        }
    }

    private List<Integer> getDays() {
        List<Integer> list = new ArrayList<>();
        for(TextView textView : days) if (daySelectedMap.get(textView.getId())) list.add(presenter.getDay(textView.getText().toString()));
        return list;
    }

    private void cancelAlarms(Event event, AlarmManager alarmManager) {
        List<Integer> requestCodes = presenter.getEventRequestCodes(event);
        for(int i = 0; i < requestCodes.size(); i += 2) {
            PendingIntent start = AppConstants.createPendingIntentForDeletingAlarms(SetRingerService.class, context, requestCodes.get(i));
            PendingIntent end = AppConstants.createPendingIntentForDeletingAlarms(SetNormalService.class, context, requestCodes.get(i + 1));
            alarmManager.cancel(start);
            alarmManager.cancel(end);
        }
        presenter.updateEvent(event);
        presenter.deleteAndRemoveShowingNotificationIfActive(event);
        presenter.deleteRequestCodes(event);
    }

}
