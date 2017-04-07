package com.justplaingoatappsgmail.phonesilencer.presenters;

import android.content.Context;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.models.Event;

import java.util.UUID;

import io.realm.Realm;

public class EventPostPresenter implements EventPostContract.Presenter {

    private Realm realm;
    private Context context;
    private EventPostContract.View view;

    public EventPostPresenter(Realm realm, Context context, EventPostContract.View view) {
        this.realm = realm;
        this.context = context;
        this.view = view;
        init();
    }

    private void init() {
        realm.init(context);
    }

    @Override
    public boolean saveTimer(final String title) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // create a event object
                Event event = realm.createObject(Event.class, UUID.randomUUID().toString());
                event.setTimerName(title);
            }
        });
        view.returnToPreviousActivity();
        return true;
    }

    @Override
    public void closeRealmInstance() {
        realm.close();
    }

}
