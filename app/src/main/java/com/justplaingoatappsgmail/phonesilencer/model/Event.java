package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;

public class Event implements RealmModel {

    @PrimaryKey
    private String id;
    private String eventName;

    public void setId(String id) {
        this.id = id;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

}
