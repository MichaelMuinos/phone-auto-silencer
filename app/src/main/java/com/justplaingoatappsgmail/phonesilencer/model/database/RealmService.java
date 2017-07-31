package com.justplaingoatappsgmail.phonesilencer.model.database;

import android.content.Context;
import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.Notification;
import com.justplaingoatappsgmail.phonesilencer.model.RealmPendingIntent;
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

    public void addEvent(final String id, final String title, final int startTimeHour, final int startTimeMinute,
                         final int endTimeHour, final int endTimeMinute, final int ringerMode,
                         final List<Integer> days, final String repeat, final boolean isEnabled, final boolean update) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // event is set based on whether we are updating an event or creating a new one
                Event event = !update ? realm.createObject(Event.class, id) : realm.where(Event.class).equalTo(Event.ID, id).findFirst();
                event.setEventName(title);
                event.setStartTimeHour(startTimeHour);
                event.setStartTimeMinute(startTimeMinute);
                event.setEndTimeHour(endTimeHour);
                event.setEndTimeMinute(endTimeMinute);
                event.setRingerMode(ringerMode);
                event.setRepeat(repeat);
                event.setEnabled(isEnabled);
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

    public void updateEventEnabled(final String id, final boolean isEnabled) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event event = realm.where(Event.class).equalTo(Event.ID, id).findFirst();
                event.setEnabled(isEnabled);
            }
        });
    }

    public List<Event> getEnabledEvents() {
        return new ArrayList<>(realm.where(Event.class).equalTo(Event.IS_ENABLED, true).findAll());
    }

    public void deleteEvent(final Event event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event deleteEvent = realm.where(Event.class).equalTo(Event.ID, event.getId()).findFirst();
                deleteEvent.deleteFromRealm();
            }
        });
    }

    public Event getEventById(String id) {
        return realm.where(Event.class).equalTo(Event.ID, id).findFirst();
    }

    public void addPendingIntentRequestCodes(final String eventId, final List<Integer> requestCodes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmPendingIntent realmPendingIntent = realm.createObject(RealmPendingIntent.class, UUID.randomUUID().toString());
                realmPendingIntent.setEventId(eventId);
                RealmList<RealmInteger> realmListRequestCodes = new RealmList<>();
                for(int i : requestCodes) {
                    RealmInteger realmInteger = realm.createObject(RealmInteger.class);
                    realmInteger.setRealmInt(i);
                    realmListRequestCodes.add(realmInteger);
                }
                realmPendingIntent.setRequestCodes(realmListRequestCodes);
            }
        });
    }

    public void deleteRealmPendingIntent(final Event event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmPendingIntent pendingIntentCodes = realm.where(RealmPendingIntent.class).equalTo(RealmPendingIntent.EVENT_ID, event.getId()).findFirst();
                pendingIntentCodes.deleteFromRealm();
            }
        });
    }

    public void saveNotification(final String eventId, final int notificationId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notification notification = realm.createObject(Notification.class, UUID.randomUUID().toString());
                notification.setEventId(eventId);
                notification.setNotificationId(notificationId);
            }
        });
    }

    public int retrieveAndDeleteNotification(final Event event) {
        final Notification notification = realm.where(Notification.class).equalTo(Notification.EVENT_ID, event.getId()).findFirst();
        int notificationId = notification.getNotificationId();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                notification.deleteFromRealm();
            }
        });
        return notificationId;
    }

    public void deleteNotification(final Event event) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notification notification = realm.where(Notification.class).equalTo(Notification.EVENT_ID, event.getId()).findFirst();
                notification.deleteFromRealm();
            }
        });
    }

    public boolean notificationIsPresent(final Event event) {
        final Notification notification = realm.where(Notification.class).equalTo(Notification.EVENT_ID, event.getId()).findFirst();
        if(notification == null) return false;
        return true;
    }

    public List<Event> getAllEvents() {
        return realm.isEmpty() ? new ArrayList<Event>() : new ArrayList<>(realm.where(Event.class).findAll().sort(Event.EVENT_NAME));
    }

    public List<Integer> getRealmPendingIntentRequestCodes(Event event) {
        RealmPendingIntent realmPendingIntent = realm.where(RealmPendingIntent.class).equalTo(RealmPendingIntent.EVENT_ID, event.getId()).findFirst();
        List<Integer> list = new ArrayList<>();
        for(RealmInteger realmInteger : realmPendingIntent.getRequestCodes()) list.add(realmInteger.getRealmInt());
        return list;
    }

    public void closeRealmInstance() {
        if(!realm.isClosed()) realm.close();
    }

}
