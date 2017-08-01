package com.justplaingoatappsgmail.phonesilencerpremium.presenter;

import com.justplaingoatappsgmail.phonesilencerpremium.model.Event;
import com.justplaingoatappsgmail.phonesilencerpremium.model.database.RealmService;

public class SetNormalServicePresenter {

    private RealmService realmService;

    public SetNormalServicePresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    public int retrieveNotificationIdAndDelete(Event event) {
        return realmService.retrieveAndDeleteNotification(event);
    }

    public Event getEventById(String id) {
        return realmService.getEventById(id);
    }

    public void updateEvent(Event event) {
        realmService.updateEventEnabled(event.getId(), false);
    }

}
