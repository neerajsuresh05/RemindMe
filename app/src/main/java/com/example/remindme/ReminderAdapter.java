package com.example.remindme;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.*;
import java.util.*;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Reminder> reminders;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Reminder reminder);
        void onItemLongClick(Reminder reminder);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReminderAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void updateData(List<Reminder> newData){
        this.reminders = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        holder.title.setText(r.title);
        holder.repeat.setText("Repeat: " + r.repeat);
        holder.datetime.setText(DateFormat.getDateTimeInstance().format(new Date(r.timeMillis)));

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) listener.onItemClick(r);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if(listener != null) listener.onItemLongClick(r);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, datetime, repeat;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.reminder_title);
            datetime = itemView.findViewById(R.id.reminder_datetime);
            repeat = itemView.findViewById(R.id.reminder_repeat);
        }
    }
}
