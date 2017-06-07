package com.justplaingoatappsgmail.phonesilencer.presenter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.RealmService;
import java.util.List;

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
    public void saveEvent(String title, String startTime, String endTime, List<TextView> days, Drawable drawable) {
        if(isValidName(title) && isValidTimeInterval(startTime, endTime) && hasAtLeastOneDaySelected(days, drawable)) {
            realmService.addEvent(title);
            view.returnToEventListActivity();
        }
    }

    @Override
    public String convertTimeToString(int hourOfDay, int minute) {
        String partOfDay = hourOfDay < 12 ? "AM" : "PM";
        int hour = hourOfDay % 12;
        String hourToString = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String minuteToString = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        return hourToString + ":" + minuteToString + " " + partOfDay;
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

    /**
     * Method checks to see if the time interval is the exact same. If so, it is invalid.
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean isValidTimeInterval(String startTime, String endTime) {
        if(startTime.equals(endTime)) {
            view.showStartEndTimeConflictError();
            return false;
        }
        return true;
    }

    /**
     * Method checks to see if there is at least one day in the week that is marked.
     * It loops through all the text view days to see if the drawable background is
     * of the type circle_red.
     * @param days
     * @param drawable
     * @return
     */
    private boolean hasAtLeastOneDaySelected(List<TextView> days, Drawable drawable) {
        for(TextView textView : days) {
            if(textView.getBackground().getConstantState().equals(drawable)) {
                return true;
            }
        }
        view.showNoDaysSelectedError();
        return false;
    }

}
