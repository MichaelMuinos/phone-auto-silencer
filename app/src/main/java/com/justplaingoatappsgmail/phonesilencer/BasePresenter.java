package com.justplaingoatappsgmail.phonesilencer;

public interface BasePresenter<T> {
    void setView(T view);
    void closeRealm();
}
