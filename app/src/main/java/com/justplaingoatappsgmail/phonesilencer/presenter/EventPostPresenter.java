package com.justplaingoatappsgmail.phonesilencer.presenter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPostPresenter implements EventPostContract.Presenter {

    private EventPostContract.View view;
    private RealmService realmService;
    private static Map<String,Integer> dayMap;

    static {
        dayMap = new HashMap<>();
        dayMap.put("MO", Calendar.MONDAY);
        dayMap.put("TU", Calendar.TUESDAY);
        dayMap.put("WE", Calendar.WEDNESDAY);
        dayMap.put("TH", Calendar.THURSDAY);
        dayMap.put("FR", Calendar.FRIDAY);
        dayMap.put("SA", Calendar.SATURDAY);
        dayMap.put("SU", Calendar.SUNDAY);
    }

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
            int startTimeHour = Integer.parseInt(startTime.substring(0, 2));
            int startTimeMinute = Integer.parseInt(startTime.substring(3, 5));
            int endTimeHour = Integer.parseInt(endTime.substring(0, 2));
            int endTimeMinute = Integer.parseInt(endTime.substring(3, 5));
            List<Integer> dayList = getDays(days, drawable);
            realmService.addEvent(title, startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, dayList, );
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
        if(startTime.equals("Start Time") || endTime.equals("End Time")) {
            view.showStartEndTimeNotSetError();
            return false;
        } else if(startTime.equals(endTime)) {
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
            if(textView.getBackground().getConstantState().equals(drawable.getConstantState())) {
                return true;
            }
        }
        view.showNoDaysSelectedError();
        return false;
    }

    private List<Integer> getDays(List<TextView> days, Drawable drawable) {
        List<Integer> list = new ArrayList<>();
        for(TextView textView : days) {
            if(textView.getBackground().getConstantState().equals(drawable.getConstantState())) {
                list.add(dayMap.get(textView.getText().toString()));
            }
        }
        return list;
    }

}
