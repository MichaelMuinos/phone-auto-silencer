package com.justplaingoatappsgmail.phonesilencer.presenter;

import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventListPresenter implements EventListContract.Presenter {

    private EventListContract.View view;
    private RealmService realmService;
    private AtomicInteger atomicInteger;

    public EventListPresenter(RealmService realmService, AtomicInteger atomicInteger) {
        this.realmService = realmService;
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void setView(EventListContract.View view) {
        this.view = view;
        // call getEvents from our view and set the list to our adapter
        // in the activity
        view.showEvents();
    }

    @Override
    public List<Event> getEvents() {
        List<Event> results = realmService.getAllEvents();
        // if we don't have any events, show message to user to create some
        if(results.size() == 0) view.showSnackBarNoEventsMessage();
        return results;
    }

    @Override
    public void addPendingIntentRequestCodes(String eventName, List<Integer> requestCodes) {
        realmService.addPendingIntentRequestCodes(eventName, requestCodes);
        view.showEventEnabledMessage(eventName);
    }

    @Override
    public int getIncrementedRequestCode() {
        if(atomicInteger.get() == Integer.MAX_VALUE) atomicInteger.set(0);
        return atomicInteger.incrementAndGet();
    }

    @Override
    public Calendar setCalendar(int day, int hour, int minute, int aMOrPm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.AM_PM, aMOrPm);
        return calendar;
    }

    @Override
    public void deleteEvent(Event event) {
        realmService.deleteEvent(event);
    }

    @Override
    public void closeRealm() {
        realmService.closeRealmInstance();
    }

}
