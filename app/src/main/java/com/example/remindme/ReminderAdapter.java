package com.example.remindme;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.*;
import java.util.*;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Reminder> reminders;

    public ReminderAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void updateData(List<Reminder> newData) {
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

        Date date = new Date(r.timeMillis);
        DateFormat df = DateFormat.getDateTimeInstance();
        holder.datetime.setText(df.format(date));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, datetime, repeat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reminder_title);
            datetime = itemView.findViewById(R.id.reminder_datetime);
            repeat = itemView.findViewById(R.id.reminder_repeat);
        }
    }
}
