package com.justplaingoatappsgmail.phonesilencerpremium.contracts;

import com.justplaingoatappsgmail.phonesilencerpremium.BasePresenter;
import com.justplaingoatappsgmail.phonesilencerpremium.BaseView;
import com.justplaingoatappsgmail.phonesilencerpremium.model.Event;

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
