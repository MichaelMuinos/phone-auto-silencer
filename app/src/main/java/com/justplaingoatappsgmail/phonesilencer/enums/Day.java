package com.justplaingoatappsgmail.phonesilencer.enums;

public enum Day {

    MONDAY("M"),
    TUESDAY("T"),
    WEDNESDAY("W"),
    THURSDAY("TH"),
    FRIDAY("F"),
    SATURDAY("SA"),
    SUNDAY("SU");

    private final String str;

    Day(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
