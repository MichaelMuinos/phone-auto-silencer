package com.justplaingoatappsgmail.phonesilencer.model.database;

import android.content.Context;

import com.justplaingoatappsgmail.phonesilencer.enums.Repeat;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.RealmInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
        Event event = realm.where(Event.class).equalTo(Event.EVENT_NAME, eventName).findFirst();
        return event == null ? false : true;
    }

    public void addEvent(final String title, final int startTimeHour, final int startTimeMinute,
                         final int startTimeAmOrPm, final int endTimeHour, final int endTimeMinute,
                         final int endTimeAmOrPm, final int ringerMode, final List<Integer> days,
                         final String repeat) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event event = realm.createObject(Event.class, UUID.randomUUID().toString());
                event.setEventName(title);
                event.setStartTimeHour(startTimeHour);
                event.setStartTimeMinute(startTimeMinute);
                event.setStartTimeAmOrPm(startTimeAmOrPm);
                event.setEndTimeHour(endTimeHour);
                event.setEndTimeMinute(endTimeMinute);
                event.setEndTimeAmOrPm(endTimeAmOrPm);
                event.setRingerMode(ringerMode);
                event.setRepeat(repeat);
                // generate RealmList of RealmIntegers for the days. Need to create RealmInteger objects first
                // before adding them to the RealmList
                RealmList<RealmInteger> dayList = new RealmList<>();
                for(int i : days) {
                    RealmInteger realmInteger = realm.createObject(RealmInteger.class);
                    realmInteger.setRealmInt(i);
                    dayList.add(realmInteger);
                }
                event.setDays(dayList);
            }
        });
    }

    public void deleteEvent(final Event event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Event> deleteEvent = realm.where(Event.class).equalTo(Event.ID, event.getId()).findAll();
                deleteEvent.deleteAllFromRealm();
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
