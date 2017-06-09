package com.justplaingoatappsgmail.phonesilencer.contracts;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.Calendar;
import java.util.List;


public interface EventListContract {

    interface View extends BaseView {
        void showEvents();
        void showSnackBarNoEventsMessage();
        void showSnackBarUndoMessage();
    }

    interface Presenter extends BasePresenter<View> {
        List<Event> getEvents();
        Calendar setCalendar(int day, int hour, int minute, int aMOrPm);
        void deleteEvent(Event event);
    }

}
