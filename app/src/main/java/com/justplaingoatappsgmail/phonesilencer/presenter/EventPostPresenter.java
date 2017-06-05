package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.RealmService;

public class EventPostPresenter implements EventPostContract.Presenter {

    private EventPostContract.View view;
    private RealmService realmService;

    public EventPostPresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    @Override
    public void setView(EventPostContract.View view) {
        this.view = view;
    }

    @Override
    public void saveEvent(String title) {
        if(isValidName(title)) {
            realmService.addEvent(title);
            view.returnToEventListActivity();
        }
    }

    @Override
    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    private boolean isValidName(String title) {
        // if the name is not valid, show our name error message
        if(title.isEmpty() || title.trim().length() == 0) {
            view.showEventNameError();
            return false;
            // if the name is a duplicate, show our name dup error message
        } else if(realmService.containsName(title)) {
            view.showEventNameDuplicateError();
            return false;
            // otherwise, it must be a valid name
        } else {
            return true;
        }
    }

}
