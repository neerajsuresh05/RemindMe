package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import java.util.List;

public class RemindersFragment extends Fragment {

    private ReminderAdapter adapter;
    private ReminderDatabaseHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.reminder_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new ReminderDatabaseHelper(getContext());
        List<Reminder> reminders = db.getAllReminders();
        adapter = new ReminderAdapter(reminders);
        recyclerView.setAdapter(adapter);

        ImageView addBtn = view.findViewById(R.id.btn_add_reminder);
        addBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddReminderActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.updateData(db.getAllReminders());
    }
}
