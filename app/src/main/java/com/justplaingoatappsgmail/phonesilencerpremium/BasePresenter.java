package com.justplaingoatappsgmail.phonesilencerpremium;

import com.justplaingoatappsgmail.phonesilencerpremium.model.Event;

public interface BasePresenter<T> {
    void setView(T view);
    void closeRealm();
    void deleteAndRemoveShowingNotificationIfActive(Event event);
}
