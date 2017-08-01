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
        void showEventEnabledMessage(String eventName);
        void showEventDisabledMessage(String eventName);
        void removeNotification(int notificationId);
    }

    interface Presenter extends BasePresenter<View> {
        List<Event> getEvents();
        List<Integer> getEventRequestCodes(Event event);
        Calendar setCalendar(int day, int hour, int minute);
        int getIncrementedRequestCode();
        int getNumberOfEvents();
        void addRequestCodes(Event event, List<Integer> requestCodes);
        void updateEvent(Event event, boolean update);
        void deleteEvent(Event event);
        void deleteRequestCodes(Event event, boolean isDeleted);
    }

}
