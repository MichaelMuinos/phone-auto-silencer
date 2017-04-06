package com.justplaingoatappsgmail.phonesilencer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventRecyclerViewClickListener;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.TimerViewHolder> {

    public static EventRecyclerViewClickListener listener;
    
    private RealmResults<Event> eventList;

    public EventAdapter(RealmResults<Event> eventList, EventRecyclerViewClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_post_item, parent, false);
        TimerViewHolder timerViewHolder = new TimerViewHolder(view);
        return timerViewHolder;
    }

    @Override
    public void onBindViewHolder(TimerViewHolder holder, int position) {
        // grab timer object
        Event eventObject = eventList.get(position);
        // set timer name
        holder.timerPostName.setText(eventObject.getTimerName());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timer_post_name) TextView timerPostName;

        public TimerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void cardViewOnClick() {
            listener.eventRecyclerViewOnClick(this.getLayoutPosition());
        }

    }

}
