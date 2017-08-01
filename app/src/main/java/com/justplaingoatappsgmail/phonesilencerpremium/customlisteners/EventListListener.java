package com.justplaingoatappsgmail.phonesilencerpremium.customlisteners;

import android.view.View;

import com.justplaingoatappsgmail.phonesilencerpremium.model.Event;

public interface EventListListener {
    void onEventListCardViewClick(int position);
    void onEventListSwitchCheckedChanged(Event event, View switchView, View positionTagView);
    void onEventListDeleteClickListener(Event event);
}
