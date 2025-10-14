package com.example.remindme;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    EditText editNoteContent;
    Button btnSaveNote;
    JournalDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Link views with XML
        editNoteContent = findViewById(R.id.editNoteContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        db = new JournalDatabaseHelper(this);

        btnSaveNote.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String content = editNoteContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please write some note", Toast.LENGTH_SHORT).show();
            return;
        }

        long timestamp = System.currentTimeMillis();

        // Save only content and timestamp
        db.addNote(content, timestamp);

        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
