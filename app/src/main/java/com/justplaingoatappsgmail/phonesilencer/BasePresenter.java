package com.justplaingoatappsgmail.phonesilencer;

import com.justplaingoatappsgmail.phonesilencer.model.Event;

public interface BasePresenter<T> {
    void setView(T view);
    void closeRealm();
    void deleteAndRemoveShowingNotificationIfActive(Event event);
}
