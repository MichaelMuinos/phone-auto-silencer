package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmPendingIntent extends RealmObject {

    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";
    public static final String REQUEST_CODES = "requestCodes";

    @PrimaryKey
    private String id;
    private String eventName;
    private RealmList<RealmInteger> requestCodes;

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

    public void setRequestCodes(RealmList<RealmInteger> requestCodes) {
        this.requestCodes = requestCodes;
    }

    public RealmList<RealmInteger> getRequestCodes() {
        return requestCodes;
    }

}
