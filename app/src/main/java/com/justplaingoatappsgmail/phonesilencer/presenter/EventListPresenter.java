package com.justplaingoatappsgmail.phonesilencer.presenter;

import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;

import java.util.Calendar;
import java.util.List;

public class EventListPresenter implements EventListContract.Presenter {

    private EventListContract.View view;
    private RealmService realmService;

    public EventListPresenter(RealmService realmService) {
        this.realmService = realmService;
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
    public Calendar setCalendar(int day, int hour, int minute, int aMOrPm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.AM_PM, aMOrPm);
        Log.d("Test", calendar.getTime().toString());
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
