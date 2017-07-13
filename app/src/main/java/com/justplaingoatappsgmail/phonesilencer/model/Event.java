package com.justplaingoatappsgmail.phonesilencer.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Event extends RealmObject implements Parcelable {

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

    public Event() {}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.eventName);
        dest.writeString(this.repeat);
        dest.writeInt(this.startTimeHour);
        dest.writeInt(this.startTimeMinute);
        dest.writeInt(this.endTimeHour);
        dest.writeInt(this.endTimeMinute);
        dest.writeInt(this.ringerMode);
        dest.writeTypedList(this.days);
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.eventName = in.readString();
        this.repeat = in.readString();
        this.startTimeHour = in.readInt();
        this.startTimeMinute = in.readInt();
        this.endTimeHour = in.readInt();
        this.endTimeMinute = in.readInt();
        this.ringerMode = in.readInt();
        this.days = new RealmList<>();
        this.days.addAll(in.createTypedArrayList(RealmInteger.CREATOR));
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}
