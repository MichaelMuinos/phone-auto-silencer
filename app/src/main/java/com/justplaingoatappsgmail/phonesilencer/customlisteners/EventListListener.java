package com.justplaingoatappsgmail.phonesilencer.customlisteners;

import android.view.View;

import com.justplaingoatappsgmail.phonesilencer.model.Event;

public interface EventListListener {
    void onEventListCardViewClick(int position);
    void onEventListSwitchCheckedChanged(Event event, View switchView, View positionTagView, boolean isChecked);
    void onEventListDeleteClickListener(Event event);
}
