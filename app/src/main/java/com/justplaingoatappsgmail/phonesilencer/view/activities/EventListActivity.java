package com.justplaingoatappsgmail.phonesilencer.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.view.adapters.EventListAdapter;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListCardViewClickListener;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventListActivity extends AppCompatActivity implements EventListContract.View, EventListCardViewClickListener {

    private static final int CHOICE_GIVEN = 9000;
    private EventListAdapter eventListAdapter;

    @BindView(R.id.activity_event_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_event_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.activity_event_toolbar) Toolbar toolbar;

    @Inject EventListContract.Presenter presenter;
    @Inject Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        // bind views
        ButterKnife.bind(this);
        // allow injection
        ((PhoneSilencerApplication) getApplication()).getComponent().inject(this);
        // set action bar
        setSupportActionBar(toolbar);
        // create event list adapter
        eventListAdapter = new EventListAdapter(this);
        // set adapter and layout manager for the recycler view
        recyclerView.setAdapter(eventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // set view to null to avoid memory leak
        presenter.setView(null);
        // close our realm instance
        presenter.closeRealm();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOICE_GIVEN) {
            if (resultCode == Activity.RESULT_OK) {
                eventListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * On click method for our floating action button which will
     * start a new activity EventPostActivity
     */
    @OnClick(R.id.activity_event_fab)
    public void onFabClick() {
        Intent intent = new Intent(EventListActivity.this, EventPostActivity.class);
        startActivityForResult(intent, CHOICE_GIVEN);
    }

    /**
     * Method for interface EventListCardViewClickListener
     * When a card view is clicked, we enter the EventPostActivity with the details
     * @param position
     */
    @Override
    public void onEventListCardViewClick(int position) {

    }

    @Override
    public void showEvents() {
        eventListAdapter.setEventList(presenter.getEvents());
    }

    /**
     * Method for the EventListContract.View
     * Used to show our snack bar if we do not have any Events
     */
    @Override
    public void showSnackBarNoEventsMessage() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "You have no events created!", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    /**
     * Method for the EventListContract.View
     * Used to show our snack bar if we want to undo the deletion of an Event
     */
    @Override
    public void showSnackBarUndoMessage() {

    }

}
