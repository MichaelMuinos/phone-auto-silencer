package com.justplaingoatappsgmail.phonesilencer.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.justplaingoatappsgmail.phonesilencer.R;

import butterknife.BindView;

public class TimerPostActivity extends AppCompatActivity {

    @BindView(R.id.timer_name) EditText timerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_post);
    }

    // save button on click listener
    public void saveOnClick() {

    }

}
