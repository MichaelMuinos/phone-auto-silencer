package com.justplaingoatappsgmail.phonesilencer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListCardViewClickListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private List<Event> eventList;
    private EventListCardViewClickListener listener;

    public EventListAdapter(EventListCardViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_post_item, parent, false);
        return new EventListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        // grab event object from list
        Event event = eventList.get(position);
        // set timer name
        holder.eventName.setText(event.getEventName());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public static class EventListViewHolder extends RecyclerView.ViewHolder {

        private EventListCardViewClickListener listener;

        @BindView(R.id.event_name) TextView eventName;

        public EventListViewHolder(View itemView, EventListCardViewClickListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.event_card_view)
        public void onEventCardViewClick() {
            listener.onEventListCardViewClick(this.getLayoutPosition());
        }

    }

}
