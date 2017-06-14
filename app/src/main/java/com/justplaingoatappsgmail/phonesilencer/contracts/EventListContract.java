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
        void showEventEnabledMessage(String eventName);
        void showEventDisabledMessage(String eventName);
    }

    interface Presenter extends BasePresenter<View> {
        List<Event> getEvents();
        List<Integer> getEventRequestCodes(Event event);
        Calendar setCalendar(int day, int hour, int minute);
        int getIncrementedRequestCode();
        void addRequestCodes(String eventName, List<Integer> requestCodes);
        void deleteEvent(Event event);
        void deleteRequestCodes(Event event);
    }

}
