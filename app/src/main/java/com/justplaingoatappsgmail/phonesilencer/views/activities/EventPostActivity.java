package com.justplaingoatappsgmail.phonesilencer.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.models.Event;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class EventPostActivity extends AppCompatActivity {

    @BindView(R.id.timer_name) EditText timerName;

    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_post);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        Log.d("click", "post on create");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String name = extras.getString("objectName");
            timerName.setText(name);
        }
    }

    // save button on click listener
    @OnClick(R.id.save_button)
    public void saveOnClick() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // create a event object
                Event event = realm.createObject(Event.class, UUID.randomUUID().toString());
                event.setTimerName(timerName.getText().toString());

                RealmResults<Event> events = realm.where(Event.class).findAll();
                for(Event t : events) {
                    Log.d("Realm", t.getTimerName());
                }
            }
        });

        Intent returnIntent = new Intent();
//        returnIntent.putExtra("choice", EventActivity.CHOICE_GIVEN);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
