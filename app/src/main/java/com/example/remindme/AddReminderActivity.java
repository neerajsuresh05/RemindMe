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
    int reminderId = -1;
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

        reminderId = getIntent().getIntExtra("reminderId", -1);
        if(reminderId != -1){
            Reminder r = db.getReminderById(reminderId);
            if(r != null){
                editTitle.setText(r.title);
                calendar.setTimeInMillis(r.timeMillis);
                txtDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d",
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)));
                txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                selectedRepeat = r.repeat;
                spinnerRepeat.setSelection(Arrays.asList(getResources().getStringArray(R.array.repeat_options))
                        .indexOf(selectedRepeat));
            }
        }

        btnSave.setOnClickListener(v -> saveReminder());
    }

    private void showDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            txtDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Prevent selecting past dates
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
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
        if(title.isEmpty() || txtDate.getText().toString().isEmpty() || txtTime.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long timeMillis = calendar.getTimeInMillis();
        if(timeMillis < System.currentTimeMillis()){
            Toast.makeText(this, "Cannot set reminder in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        if(reminderId != -1){
            db.updateReminder(reminderId, title, timeMillis, selectedRepeat);
            Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
        } else {
            db.addReminder(title, timeMillis, selectedRepeat);
            Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
