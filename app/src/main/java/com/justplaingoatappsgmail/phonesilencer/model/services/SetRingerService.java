package com.justplaingoatappsgmail.phonesilencer.model.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.presenter.SetRingerServicePresenter;
import com.justplaingoatappsgmail.phonesilencer.view.activities.EventListActivity;
import java.util.Calendar;
import javax.inject.Inject;

public class SetRingerService extends IntentService {

    private String eventId;
    private Event event;
    private Calendar calendar;
    private int requestCode;

    @Inject SetRingerServicePresenter presenter;

    public SetRingerService() {
        super(null);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        eventId = (String) intent.getExtras().get(AppConstants.EVENT_KEY_ID);
        calendar = (Calendar) intent.getExtras().get(AppConstants.CALENDAR_KEY);
        requestCode = (int) intent.getExtras().get(AppConstants.REQUEST_CODE_KEY);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // setup injection target here because we need to create the Realm instance in the same thread
        // as where we retrieve data
        ((PhoneSilencerApplication) getApplication()).getComponent().inject(this);
        // grab event by id from database
        event = presenter.getEventById(eventId);
        // set phone silent
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(event.getRingerMode());
        // set notification
        Intent notificationIntent = new Intent(this, EventListActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(event.getEventName() + " is enabled")
                .setContentText(AppConstants.convertTimeToString(event.getStartTimeHour(), event.getStartTimeMinute()) + " to " + AppConstants.convertTimeToString(event.getEndTimeHour(), event.getEndTimeMinute()))
                .setSmallIcon(R.drawable.silence)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        // get our unique notification id
        int notificationId = presenter.incrementAndGetInteger();
        notificationManager.notify(notificationId, notification);
        // save our notification id
        presenter.saveNotificationId(event.getId(), notificationId);
        // set repeating alarm depending on build version and repeat option
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !event.getRepeat().equals("Once")) setAlarm();
        // close realm instance
        // https://github.com/realm/realm-java/issues/1910
        presenter.closeRealm();
    }

    private void setAlarm() {
        // create intent and put extras
        Intent intent = new Intent(getApplicationContext(), SetRingerService.class);
        intent.putExtra(AppConstants.EVENT_KEY_ID, event.getId());
        intent.putExtra(AppConstants.CALENDAR_KEY, calendar);
        intent.putExtra(AppConstants.REQUEST_CODE_KEY, requestCode);
        // create our pending intent
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // get alarm manager
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + AppConstants.REPEAT_MAP.get(event.getRepeat()), pendingIntent);
    }

}
