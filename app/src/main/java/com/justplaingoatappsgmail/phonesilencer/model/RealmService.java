package com.justplaingoatappsgmail.phonesilencer.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

public class RealmService {

    private Realm realm;
    private Context context;

    public RealmService(Realm realm, Context context) {
        this.realm = realm;
        this.context = context;
        init();
    }

    private void init() {
        Realm.init(context);
    }

    /**
     * Returns true or not depending on if the realm instance contains the event name
     * in one of its entries
     * @param eventName
     * @return
     */
    public boolean containsName(String eventName) {
        Event event = realm.where(Event.class).equalTo("eventName", eventName).findFirst();
        return event == null ? false : true;
    }

    public void addEvent(final String title) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event event = realm.createObject(Event.class);
                event.setId(UUID.randomUUID().toString());
                event.setEventName(title);
            }
        });
    }

    public List<Event> getAllEvents() {
        return realm.isEmpty() ? new ArrayList<Event>() : new ArrayList<>(realm.where(Event.class).findAll());
    }

    public void closeRealmInstance() {
        if(!realm.isClosed()) realm.close();
    }

}
