package com.example.remindme;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class AddReminderActivity extends AppCompatActivity {

    EditText editTitle;
    TextView txtDate, txtTime;
    Spinner spinnerRepeat;
    Button btnSave;

    Calendar calendar;
    String selectedRepeat = "Once";

    ReminderDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        editTitle = findViewById(R.id.editTitle);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        spinnerRepeat = findViewById(R.id.spinnerRepeat);
        btnSave = findViewById(R.id.btnSaveReminder);

        db = new ReminderDatabaseHelper(this);
        calendar = Calendar.getInstance();

        txtDate.setOnClickListener(v -> showDatePicker());
        txtTime.setOnClickListener(v -> showTimePicker());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(adapter);
        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRepeat = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(v -> saveReminder());
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            txtDate.setText(day + "/" + (month + 1) + "/" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void saveReminder() {
        String title = editTitle.getText().toString().trim();

        if (title.isEmpty() || txtDate.getText().toString().isEmpty() || txtTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long timeMillis = calendar.getTimeInMillis();

        // Save to DB
        int id = db.addReminder(title, timeMillis, selectedRepeat);

        // Set Alarm
        setAlarm(id, title, timeMillis, selectedRepeat);

        Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show();
        finish(); // go back to list
    }

    private void setAlarm(int id, String title, long timeMillis, String repeat) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long repeatInterval = 0;
        switch (repeat) {
            case "Daily": repeatInterval = AlarmManager.INTERVAL_DAY; break;
            case "Weekly": repeatInterval = AlarmManager.INTERVAL_DAY * 7; break;
            case "Monthly": repeatInterval = AlarmManager.INTERVAL_DAY * 30; break;
        }

        if (repeat.equals("Once")) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeMillis, repeatInterval, pendingIntent);
        }
    }
}
