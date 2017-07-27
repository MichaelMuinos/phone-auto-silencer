package com.justplaingoatappsgmail.phonesilencer.contracts;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.List;

public interface EventPostContract {

    interface View extends BaseView {
        void showEventNameError();
        void showEventNameLengthError();
        void showEventNameDuplicateError();
        void showStartEndTimeConflictError();
        void showNoDaysSelectedError();
    }

    interface Presenter extends BasePresenter<View> {
        boolean isValidEvent(String previousTitle, String title, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, List<Integer> days, boolean update);
        void saveEvent(String id, String title, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, int ringerMode, List<Integer> days, String repeat, boolean update);
        List<Integer> getEventRequestCodes(Event event);
        void deleteRequestCodes(Event event);
        void updateEvent(Event event);
        int getDay(String day);
    }

}
