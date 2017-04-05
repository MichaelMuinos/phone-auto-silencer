package com.justplaingoatappsgmail.phonesilencer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.models.Timer;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> {

    private RealmResults<Timer> timerList;

    public TimerAdapter(RealmResults<Timer> timerList) {
        this.timerList = timerList;
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
        Timer timerObject = timerList.get(position);
        // set timer name
        holder.timerPostName.setText(timerObject.getTimerName());
    }

    @Override
    public int getItemCount() {
        return timerList.size();
    }

    static class TimerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timer_post_name) TextView timerPostName;

        public TimerViewHolder(View itemView) {
            super(itemView);
        }

    }

}
