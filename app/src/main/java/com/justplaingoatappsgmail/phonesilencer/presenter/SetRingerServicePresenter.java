package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import java.util.concurrent.atomic.AtomicInteger;

public class SetRingerServicePresenter {

    private RealmService realmService;
    private AtomicInteger atomicInteger;

    public SetRingerServicePresenter(RealmService realmService, AtomicInteger atomicInteger) {
        this.realmService = realmService;
        this.atomicInteger = atomicInteger;
    }

    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    public int incrementAndGetInteger() {
        if(atomicInteger.get() == Integer.MAX_VALUE) atomicInteger.set(0);
        return atomicInteger.incrementAndGet();
    }

    public void saveNotificationId(String eventId, int notificationId) {
        realmService.saveNotification(eventId, notificationId);
    }

    public Event getEventById(String id) {
        return realmService.getEventById(id);
    }

}
