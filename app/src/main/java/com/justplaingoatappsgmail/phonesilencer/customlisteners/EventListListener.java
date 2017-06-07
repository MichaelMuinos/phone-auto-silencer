package com.justplaingoatappsgmail.phonesilencer.customlisteners;

import android.view.View;

public interface EventListListener {
    void onEventListCardViewClick(int position);
    void onEventListSwitchCheckedChanged(int position, View view, boolean isChecked);
    void onEventListDeleteClickListener(int position);
}
