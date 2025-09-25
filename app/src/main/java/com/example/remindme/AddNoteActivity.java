package com.example.remindme;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    EditText editNote;
    Button btnSave;
    JournalDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editNote = findViewById(R.id.editNote);
        btnSave = findViewById(R.id.btnSaveNote);
        db = new JournalDatabaseHelper(this);

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String content = editNote.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "Please write some note", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();
        db.addNote(content, timestamp);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
