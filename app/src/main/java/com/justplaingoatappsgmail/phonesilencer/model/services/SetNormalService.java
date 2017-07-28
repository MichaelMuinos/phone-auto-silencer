package com.justplaingoatappsgmail.phonesilencer.model.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.Nullable;

import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.presenter.SetNormalServicePresenter;
import java.util.Calendar;
import javax.inject.Inject;

public class SetNormalService extends IntentService {

    private String eventId;
    private Event event;
    private Calendar calendar;
    private int requestCode;

    @Inject SetNormalServicePresenter presenter;

    public SetNormalService() {
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
        // grab our event by id from our database
        event = presenter.getEventById(eventId);
        // set the phone ringer to normal
        AudioManager audioManager =(AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        // clear notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(presenter.retrieveNotificationIdAndDelete(event));
        // set repeating alarm depending on build version and repeat option
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !event.getRepeat().equals("Once")) setAlarm();
        // if our repeat is once, then we need to set our event to disabled
        if(event.getRepeat().equals("Once")) presenter.updateEvent(event);
        // close realm instance
        // https://github.com/realm/realm-java/issues/1910
        presenter.closeRealm();
    }

    private void setAlarm() {
        // create intent and put extras
        Intent intent = new Intent(getApplicationContext(), SetNormalService.class);
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
