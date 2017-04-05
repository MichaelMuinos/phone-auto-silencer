package com.justplaingoatappsgmail.phonesilencer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // configure realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("timers.realm")
                .build();

        // Make this realm the default
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
