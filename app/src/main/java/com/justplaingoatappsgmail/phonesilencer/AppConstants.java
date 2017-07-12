package com.justplaingoatappsgmail.phonesilencer;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class AppConstants {

    public static final String RINGER_MODE_KEY = "RINGER_MODE";
    public static final String CALENDAR_KEY = "CALENDAR";
    public static final String REQUEST_CODE_KEY = "REQUEST_CODE";

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

}
