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

import com.google.gson.Gson;
import com.justplaingoatappsgmail.phonesilencer.CustomListeners.TimerRecyclerViewClickListener;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.adapters.TimerAdapter;
import com.justplaingoatappsgmail.phonesilencer.models.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class TimerActivity extends AppCompatActivity implements TimerRecyclerViewClickListener {

    public static final int CHOICE_GIVEN = 9000;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.timer_recycler_view) RecyclerView recyclerView;

    private TimerAdapter timerAdapter;
    private RealmResults<Timer> timerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // bind views
        ButterKnife.bind(this);

        // set action bar
        setSupportActionBar(toolbar);

        Realm.init(this);

        timerList = Realm.getDefaultInstance().where(Timer.class).findAll();
        timerAdapter = new TimerAdapter(timerList, this);
        recyclerView.setAdapter(timerAdapter);
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
                timerAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(TimerActivity.this, TimerPostActivity.class);
        startActivityForResult(intent, CHOICE_GIVEN);
    }

    @Override
    public void timerRecyclerViewOnClick(int position) {
        Log.d("click", "in timer activity");
        Timer timer = timerList.get(position);
        Intent intent = new Intent(TimerActivity.this, TimerPostActivity.class);
        intent.putExtra("obj", new Gson().toJson(timer));
        startActivityForResult(intent, CHOICE_GIVEN);
    }

}
