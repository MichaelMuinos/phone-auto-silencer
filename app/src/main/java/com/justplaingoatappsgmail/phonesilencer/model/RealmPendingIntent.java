package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmPendingIntent extends RealmObject {

    public static final String EVENT_ID = "eventId";

    @PrimaryKey
    private String id;
    private String eventId;
    private RealmList<RealmInteger> requestCodes;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setRequestCodes(RealmList<RealmInteger> requestCodes) {
        this.requestCodes = requestCodes;
    }

    public RealmList<RealmInteger> getRequestCodes() {
        return requestCodes;
    }

}
