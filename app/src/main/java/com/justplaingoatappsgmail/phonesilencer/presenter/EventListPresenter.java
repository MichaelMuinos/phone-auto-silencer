package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.RealmService;
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
    public void deleteEvent(Event event) {
        realmService.deleteEvent(event);
    }

    @Override
    public void closeRealm() {
        realmService.closeRealmInstance();
    }

}
