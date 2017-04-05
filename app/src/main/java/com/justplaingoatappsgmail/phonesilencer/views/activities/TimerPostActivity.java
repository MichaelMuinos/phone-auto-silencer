package com.justplaingoatappsgmail.phonesilencer.views.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.models.Timer;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class TimerPostActivity extends AppCompatActivity {

    @BindView(R.id.timer_name) EditText timerName;

    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_post);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
    }

    // save button on click listener
    @OnClick(R.id.save_button)
    public void saveOnClick() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // create a timer object
                Timer timer = realm.createObject(Timer.class, UUID.randomUUID().toString());
                timer.setTimerName(timerName.getText().toString());

                RealmResults<Timer> timers = realm.where(Timer.class).findAll();
                for(Timer t : timers) {
                    Log.d("Realm", t.getTimerName());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
