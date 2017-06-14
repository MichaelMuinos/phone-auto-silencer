package com.justplaingoatappsgmail.phonesilencer.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Event extends RealmObject implements Serializable {

    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";
    public static final String REPEAT = "repeat";
    public static final String START_TIME_HOUR = "startTimeHour";
    public static final String START_TIME_MINUTE = "startTimeMinute";
    public static final String END_TIME_HOUR = "endTimeHour";
    public static final String END_TIME_MINUTE = "endTimeMinute";
    public static final String RINGER_MODE = "ringerMode";
    public static final String DAYS = "days";

    @PrimaryKey
    private String id;
    private String eventName;
    private String repeat;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeHour;
    private int endTimeMinute;
    private int ringerMode;
    private RealmList<RealmInteger> days;

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

    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getStartTimeMinute() {
        return startTimeMinute;
    }

    public void setStartTimeMinute(int startTimeMinute) {
        this.startTimeMinute = startTimeMinute;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }

    public int getEndTimeMinute() {
        return endTimeMinute;
    }

    public void setEndTimeMinute(int endTimeMinute) {
        this.endTimeMinute = endTimeMinute;
    }

    public RealmList<RealmInteger> getDays() {
        return days;
    }

    public void setDays(RealmList<RealmInteger> days) {
        this.days = days;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getRingerMode() {
        return ringerMode;
    }

    public void setRingerMode(int ringerMode) {
        this.ringerMode = ringerMode;
    }

}
