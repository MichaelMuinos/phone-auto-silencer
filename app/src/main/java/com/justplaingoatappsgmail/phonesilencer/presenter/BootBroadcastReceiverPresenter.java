package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import java.util.Calendar;
import java.util.List;

public class BootBroadcastReceiverPresenter {

    private RealmService realmService;

    public BootBroadcastReceiverPresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    public Calendar setCalendar(int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    public List<Integer> getEventRequestCodes(Event event) {
        return realmService.getRealmPendingIntentRequestCodes(event);
    }

    public List<Event> getEnabledEvents() {
        return realmService.getEnabledEvents();
    }

    public void deleteNotificationIfPresent(Event event) {
        if(realmService.notificationIsPresent(event)) {
            realmService.deleteNotification(event);
        }
    }

}
