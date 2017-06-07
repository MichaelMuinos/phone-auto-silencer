package com.justplaingoatappsgmail.phonesilencer.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private List<Event> eventList;
    private EventListListener listener;

    public EventListAdapter(EventListListener listener) {
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

        private EventListListener listener;

        @BindView(R.id.event_post_item_title) TextView eventName;
        @BindView(R.id.event_post_item_switch) Switch switchEvent;

        public EventListViewHolder(View itemView, EventListListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.event_card_view)
        public void onEventCardViewClick() {
            listener.onEventListCardViewClick(this.getLayoutPosition());
        }

        @OnCheckedChanged(R.id.event_post_item_switch)
        public void onSwitchCheckedChanged(boolean isChecked) {
            listener.onEventListSwitchCheckedChanged(this.getLayoutPosition(), switchEvent, isChecked);
        }

        @OnClick(R.id.event_post_item_close_button)
        public void onCloseButtonClick() {
            listener.onEventListDeleteClickListener(this.getLayoutPosition());
        }

    }

}
