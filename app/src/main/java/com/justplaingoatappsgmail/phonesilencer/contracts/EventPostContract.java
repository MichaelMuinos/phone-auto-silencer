package com.justplaingoatappsgmail.phonesilencer.contracts;

public interface EventPostContract {

    interface View {
        void showTimerNameError();
        void showTimerNameConflictError();
        void showStartTimeError();
        void showEndTimeError();
        void showDaysSelectedError();
        void showRingerModeError();
        void showRepeatOptionError();
    }

    interface Presenter {
        // might need to pass all attributes through method
        boolean saveTimer();
    }

}
