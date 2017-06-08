package com.justplaingoatappsgmail.phonesilencer.contracts;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;
import com.justplaingoatappsgmail.phonesilencer.enums.Repeat;

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
        void saveEvent(String title, String startTime, String endTime, List<TextView> days, Drawable drawable, int ringerMode, String repeat);
        String convertTimeToString(int hourOfDay, int minute);
    }

}
