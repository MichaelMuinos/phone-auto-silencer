package com.justplaingoatappsgmail.phonesilencer.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventRecyclerViewClickListener;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.adapters.EventAdapter;
import com.justplaingoatappsgmail.phonesilencer.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EventActivity extends AppCompatActivity implements EventRecyclerViewClickListener {

    public static final int CHOICE_GIVEN = 9000;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.timer_recycler_view) RecyclerView recyclerView;

    private EventAdapter eventAdapter;
    private RealmResults<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // bind views
        ButterKnife.bind(this);

        // set action bar
        setSupportActionBar(toolbar);

        Realm.init(this);

        eventList = Realm.getDefaultInstance().where(Event.class).findAll();
        eventAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(eventAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHOICE_GIVEN) {
            if(resultCode == Activity.RESULT_OK) {
                eventAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // floating action button on click listener method
    @OnClick(R.id.fab)
    public void fabOnClick() {
        Intent intent = new Intent(EventActivity.this, EventPostActivity.class);
        startActivityForResult(intent, CHOICE_GIVEN);
    }

    @Override
    public void eventRecyclerViewOnClick(int position) {
        Event event = eventList.get(position);
        Intent intent = new Intent(EventActivity.this, EventPostActivity.class);
        intent.putExtra("objectName", event.getTimerName());
        startActivityForResult(intent, CHOICE_GIVEN);
    }

}
