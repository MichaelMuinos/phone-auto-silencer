package com.justplaingoatappsgmail.phonesilencer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AppConstants {

    public static final int START_ACTIVITY_FOR_RESULT_CODE = 10000;
    public static final String EVENT_OBJECT = "EVENT_OBJECT";
    public static final String EVENT_KEY_ID = "EVENT_KEY_ID";
    public static final String CALENDAR_KEY = "CALENDAR";
    public static final String REQUEST_CODE_KEY = "REQUEST_CODE";
    public static Map<String,Long> REPEAT_MAP = createMap();
    public static long WEEK_IN_MILLISECONDS = 604800000L;

    private static Map<String, Long> createMap() {
        Map<String, Long> result = new HashMap<>();
        result.put("Weekly", 604800000L);
        result.put("Bi-Weekly", 1209600000L);
        result.put("Monthly", 2592000000L);
        return Collections.unmodifiableMap(result);
    }

    public static void showSnackBarMessage(CoordinatorLayout coordinatorLayout, String str, Context context, int color) {
        Snackbar snackBar = Snackbar.make(coordinatorLayout, str, Snackbar.LENGTH_LONG);
        TextView textView = (TextView) snackBar.getView().findViewById(android.support.design.R.id.snackbar_text);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else textView.setGravity(Gravity.CENTER_HORIZONTAL);
        // set text color
        textView.setTextColor(ContextCompat.getColor(context, color));
        snackBar.show();
    }

    public static String convertTimeToString(int hourOfDay, int minute) {
        String partOfDay = hourOfDay < 12 ? "AM" : "PM";
        int hour = hourOfDay % 12;
        String hourToString = hour == 0 ? "12" : (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour));
        String minuteToString = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        return hourToString + ":" + minuteToString + " " + partOfDay;
    }

    public static PendingIntent createPendingIntentForSettingAlarms(Class service, Context context, Event event, Calendar calendar, int requestCode) {
        Intent intent = new Intent(context, service);
        intent.putExtra(AppConstants.EVENT_KEY_ID, event.getId());
        intent.putExtra(AppConstants.CALENDAR_KEY, calendar);
        intent.putExtra(AppConstants.REQUEST_CODE_KEY, requestCode);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent createPendingIntentForDeletingAlarms(Class service, Context context, int requestCode) {
        return PendingIntent.getService(context, requestCode, new Intent(context, service), PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
