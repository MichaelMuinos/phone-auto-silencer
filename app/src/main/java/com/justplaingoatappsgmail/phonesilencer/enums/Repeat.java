package com.justplaingoatappsgmail.phonesilencer.enums;

public enum Repeat {

    WEEKLY("Weekly"),
    ONCE("Once"),
    BIWEEKLY("Bi-Weekly"),
    MONTHLY("Monthly");

    private final String str;

    Repeat(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
