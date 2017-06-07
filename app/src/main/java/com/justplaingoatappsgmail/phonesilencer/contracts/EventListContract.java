package com.justplaingoatappsgmail.phonesilencer.contracts;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.List;


public interface EventListContract {

    interface View extends BaseView {
        void showEvents();
        void showSnackBarNoEventsMessage();
        void showSnackBarUndoMessage();
    }

    interface Presenter extends BasePresenter<View> {
        List<Event> getEvents();
        void deleteEvent(Event event);
    }

}
