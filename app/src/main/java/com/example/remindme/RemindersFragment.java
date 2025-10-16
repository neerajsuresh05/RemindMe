package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import java.util.List;

public class RemindersFragment extends Fragment {

    private ReminderAdapter adapter;
    private ReminderDatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.reminder_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new ReminderDatabaseHelper(getContext());
        adapter = new ReminderAdapter(db.getAllReminders());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Reminder reminder) {
                Intent intent = new Intent(getActivity(), AddReminderActivity.class);
                intent.putExtra("reminderId", reminder.id); // edit mode
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Reminder reminder) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Reminder")
                        .setMessage("Are you sure you want to delete this reminder?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.deleteReminder(reminder.id);
                            adapter.updateData(db.getAllReminders());
                            Toast.makeText(getContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        ImageView addBtn = view.findViewById(R.id.btn_add_reminder);
        addBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddReminderActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(db.getAllReminders());
    }
}
