package com.justplaingoatappsgmail.phonesilencer.model.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetNormalService;
import com.justplaingoatappsgmail.phonesilencer.model.services.SetRingerService;
import com.justplaingoatappsgmail.phonesilencer.presenter.BootBroadcastReceiverPresenter;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Inject BootBroadcastReceiverPresenter presenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((PhoneSilencerApplication) context.getApplicationContext()).getComponent().inject(this);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        List<Event> enabledEvents = presenter.getEnabledEvents();
        for(Event event : enabledEvents) setAlarm(event, alarmManager, context);
        presenter.closeRealm();
    }

    private void setAlarm(Event event, AlarmManager alarmManager, Context context) {
        // delete the notifications that were tied to the cancelled event
        presenter.deleteNotificationIfPresent(event);
        // get our request codes to re-enable the alarms
        List<Integer> requestCodes = presenter.getEventRequestCodes(event);
        // cycle through our days and set the calendars
        for(int i = 0; i < event.getDays().size(); i += 2) {
            // create calendars
            Calendar startCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getStartTimeHour(), event.getStartTimeMinute());
            Calendar endCalendar = presenter.setCalendar(event.getDays().get(i).getRealmInt(), event.getEndTimeHour(), event.getEndTimeMinute());
            // if we are attempting to set a time that has already passed, set both alarms forward by a week
            if(System.currentTimeMillis() > startCalendar.getTimeInMillis() && System.currentTimeMillis() > endCalendar.getTimeInMillis()) {
                startCalendar.setTimeInMillis(startCalendar.getTimeInMillis() + AppConstants.WEEK_IN_MILLISECONDS);
                endCalendar.setTimeInMillis(endCalendar.getTimeInMillis() + AppConstants.WEEK_IN_MILLISECONDS);
            }
            // create pending intent
            PendingIntent start = AppConstants.createPendingIntentForSettingAlarms(SetRingerService.class, context, event, startCalendar, requestCodes.get(i));
            PendingIntent end = AppConstants.createPendingIntentForSettingAlarms(SetNormalService.class, context, event, endCalendar, requestCodes.get(i + 1));
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
    }

}
