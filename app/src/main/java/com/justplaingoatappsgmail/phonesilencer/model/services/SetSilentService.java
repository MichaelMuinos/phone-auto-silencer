package com.justplaingoatappsgmail.phonesilencer.model.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.model.Event;

public class SetSilentService extends IntentService {

    private int ringerMode;

    public SetSilentService() {
        super(null);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SetSilentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        ringerMode = intent.getExtras().getInt(Event.RINGER_MODE);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // set phone silent
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(ringerMode);
        Log.d("Test", "Silenced");
    }

}
