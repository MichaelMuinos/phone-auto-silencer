package com.justplaingoatappsgmail.phonesilencer.contracts;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;

import java.util.List;

public interface EventPostContract {

    interface View extends BaseView {
        void showEventNameError();
        void showEventNameDuplicateError();
        void showStartEndTimeConflictError();
        void showStartEndTimeNotSetError();
        void showNoDaysSelectedError();
        void returnToEventListActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void saveEvent(String title, String startTime, String endTime, int ringerMode, List<Integer> days, String repeat);
        String convertTimeToString(int hourOfDay, int minute);
        int getDay(String day);
    }

}
