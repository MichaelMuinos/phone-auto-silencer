package com.justplaingoatappsgmail.phonesilencer.model.services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;
import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import java.util.Calendar;

public class SetNormalService extends IntentService {

    private Calendar calendar;
    private int ringerMode;
    private int requestCode;
    private String repeat;

    public SetNormalService() {
        super(null);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        calendar = (Calendar) intent.getExtras().get(AppConstants.CALENDAR_KEY);
        ringerMode = (int) intent.getExtras().get(AppConstants.RINGER_MODE_KEY);
        requestCode = (int) intent.getExtras().get(AppConstants.REQUEST_CODE_KEY);
        repeat = (String) intent.getExtras().get(AppConstants.REPEAT_KEY);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // set the phone ringer to normal
        AudioManager audioManager =(AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        // set repeating alarm depending on build version and repeat option
        if(Build.VERSION.SDK_INT >= 19 && !repeat.equals("Once")) setAlarm();

        Log.d("Test", "Back to normal");

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setAlarm() {
        // create intent and put extras
        Intent intent = new Intent(getApplicationContext(), SetNormalService.class);
        intent.putExtra(AppConstants.RINGER_MODE_KEY, ringerMode);
        intent.putExtra(AppConstants.CALENDAR_KEY, calendar);
        intent.putExtra(AppConstants.REQUEST_CODE_KEY, requestCode);
        // create our pending intent
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // get alarm manager
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + AppConstants.REPEAT_MAP.get(repeat), pendingIntent);
    }

}
