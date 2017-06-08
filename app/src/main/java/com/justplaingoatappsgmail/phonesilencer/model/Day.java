package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * This is a wrapper class for our days. This is needed to be done because realm
 * does not support storing primitives inside of the RealmList.
 */
@RealmClass
public class Day extends RealmObject {

    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

}

