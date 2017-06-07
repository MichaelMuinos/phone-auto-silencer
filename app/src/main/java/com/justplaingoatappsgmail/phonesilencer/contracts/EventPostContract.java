package com.justplaingoatappsgmail.phonesilencer.contracts;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;

import java.util.List;

public interface EventPostContract {

    interface View extends BaseView {
        void showEventNameError();
        void showEventNameDuplicateError();
        void showStartEndTimeConflictError();
        void showNoDaysSelectedError();
        void returnToEventListActivity();
    }

    interface Presenter extends BasePresenter<View> {
        void saveEvent(String title, String startTime, String endTime, List<TextView> days, Drawable drawable);
        String convertTimeToString(int hourOfDay, int minute);
    }

}
