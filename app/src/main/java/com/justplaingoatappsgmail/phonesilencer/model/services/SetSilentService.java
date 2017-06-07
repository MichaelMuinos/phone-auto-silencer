package com.justplaingoatappsgmail.phonesilencer.model.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SetSilentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SetSilentService(String name) {
        super(name);
    }

    public SetSilentService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

}
