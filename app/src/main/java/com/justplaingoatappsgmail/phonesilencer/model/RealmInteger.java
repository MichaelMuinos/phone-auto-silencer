package com.justplaingoatappsgmail.phonesilencer.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * This is a wrapper class for our days. This is needed to be done because realm
 * does not support storing primitives inside of the RealmList.
 */
@RealmClass
public class RealmInteger extends RealmObject {

    private int realmInt;

    public void setRealmInt(int realmInt) {
        this.realmInt = realmInt;
    }

    public int getRealmInt() {
        return realmInt;
    }

}