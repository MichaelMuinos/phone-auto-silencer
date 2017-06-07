package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Event implements RealmModel {

    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";

    @PrimaryKey
    private String id;
    private String eventName;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

}
