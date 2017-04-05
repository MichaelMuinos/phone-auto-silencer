package com.justplaingoatappsgmail.phonesilencer.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Timer extends RealmObject {

    @PrimaryKey
    private String id;
    private String timerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimerName() {
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

}
