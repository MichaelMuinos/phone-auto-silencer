package com.justplaingoatappsgmail.phonesilencer.view.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.AppConstants;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.veinhorn.tagview.TagView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private List<Event> eventList;
    private EventListListener listener;
    private Context context;
    private static Map<Integer,String> dayMap;

    static {
        dayMap = new HashMap<>();
        dayMap.put(Calendar.SUNDAY, "SU");
        dayMap.put(Calendar.MONDAY, "M");
        dayMap.put(Calendar.TUESDAY, "T");
        dayMap.put(Calendar.WEDNESDAY, "W");
        dayMap.put(Calendar.THURSDAY, "TH");
        dayMap.put(Calendar.FRIDAY, "F");
        dayMap.put(Calendar.SATURDAY, "SA");
    }

    public EventListAdapter(EventListListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        eventList = new ArrayList<>();
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_post_item, parent, false);
        return new EventListViewHolder(view, listener, eventList);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        // grab event object from list
        Event event = eventList.get(position);
        // set event name
        holder.eventName.setText(event.getEventName());
        // create days string
        StringBuilder sb = new StringBuilder();
        // if our list is of size 7, that means all days have been selected
        if(event.getDays().size() == 7) {
            sb.append("All Days");
        } else {
            for (int i = 0; i < event.getDays().size(); i++) {
                sb.append(dayMap.get(event.getDays().get(i).getRealmInt()));
                if (i != event.getDays().size() - 1) sb.append("-");
            }
        }
        // set days tag
        holder.daysTag.setText(sb.toString());
        // set our switch and tag based on if it was enabled or not
        if(event.isEnabled()) {
            holder.switchEvent.setChecked(true);
            holder.switchEvent.setText("Enabled\t");
            holder.positionTag.setText("Enabled");
        } else {
            holder.switchEvent.setChecked(false);
            holder.switchEvent.setText("Disabled\t");
            holder.positionTag.setText("Disabled");
        }
        // create start time string
        String startTime = AppConstants.convertTimeToString(event.getStartTimeHour(), event.getStartTimeMinute());
        // create end time string
        String endTime = AppConstants.convertTimeToString(event.getEndTimeHour(), event.getEndTimeMinute());
        // set time tag
        holder.timeTag.setText(startTime + " to " + endTime);
        // set ringer tag
        holder.ringerTag.setText(event.getRingerMode() == AudioManager.RINGER_MODE_SILENT ? "Silent" : "Vibrate");
        // set repeat tag
        holder.repeatTag.setText(event.getRepeat());
        // set position tag
        holder.positionTag.setText(holder.switchEvent.getText());
        // set close button color
        holder.closeButton.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventList(List<Event> eventList) {
        this.eventList.clear();
        this.eventList.addAll(eventList);
    }

    public static class EventListViewHolder extends RecyclerView.ViewHolder {

        private EventListListener listener;
        private List<Event> eventList;

        @BindView(R.id.event_post_item_title) TextView eventName;
        @BindView(R.id.event_post_item_days_tag) TagView daysTag;
        @BindView(R.id.event_post_item_time_tag) TagView timeTag;
        @BindView(R.id.event_post_item_ringer_tag) TagView ringerTag;
        @BindView(R.id.event_post_item_repeat_tag) TagView repeatTag;
        @BindView(R.id.event_post_item_position_tag) TagView positionTag;
        @BindView(R.id.event_post_item_switch) Switch switchEvent;
        @BindView(R.id.event_post_item_close_button) ImageView closeButton;

        public EventListViewHolder(View itemView, EventListListener listener, List<Event> eventList) {
            super(itemView);
            this.listener = listener;
            this.eventList = eventList;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.event_card_view)
        public void onEventCardViewClick() {
            listener.onEventListCardViewClick(this.getLayoutPosition());
        }

        @OnCheckedChanged(R.id.event_post_item_switch)
        public void onSwitchCheckedChanged() {
            listener.onEventListSwitchCheckedChanged(eventList.get(this.getLayoutPosition()), switchEvent, positionTag);
        }

        @OnClick(R.id.event_post_item_close_button)
        public void onCloseButtonClick() {
            listener.onEventListDeleteClickListener(eventList.get(this.getLayoutPosition()));
        }

    }

}