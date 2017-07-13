package com.justplaingoatappsgmail.phonesilencer.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * This is a wrapper class for our days. This is needed to be done because realm
 * does not support storing primitives inside of the RealmList.
 */
@RealmClass
public class RealmInteger extends RealmObject implements Parcelable {

    private int realmInt;

    public RealmInteger() {}

    public void setRealmInt(int realmInt) {
        this.realmInt = realmInt;
    }

    public int getRealmInt() {
        return realmInt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.realmInt);
    }

    protected RealmInteger(Parcel in) {
        this.realmInt = in.readInt();
    }

    public static final Parcelable.Creator<RealmInteger> CREATOR = new Parcelable.Creator<RealmInteger>() {
        @Override
        public RealmInteger createFromParcel(Parcel source) {
            return new RealmInteger(source);
        }

        @Override
        public RealmInteger[] newArray(int size) {
            return new RealmInteger[size];
        }
    };

}