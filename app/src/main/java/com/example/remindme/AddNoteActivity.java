package com.example.remindme;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class AddNoteActivity extends AppCompatActivity {

    EditText editNoteTitle, editNoteContent;
    Button btnSave;
    JournalDatabaseHelper db;
    int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editNoteTitle = findViewById(R.id.editNoteTitle);
        editNoteContent = findViewById(R.id.editNoteContent);
        btnSave = findViewById(R.id.btnSaveNote);

        db = new JournalDatabaseHelper(this);

        noteId = getIntent().getIntExtra("noteId", -1);
        if(noteId != -1){
            Note note = db.getNoteById(noteId);
            if(note != null){
                editNoteContent.setText(note.getContent());
            }
        }

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String content = editNoteContent.getText().toString().trim();
        if(content.isEmpty()){
            Toast.makeText(this, "Please write some note", Toast.LENGTH_SHORT).show();
            return;
        }
        long timestamp = System.currentTimeMillis();
        if(noteId != -1){
            db.updateNote(noteId, content, timestamp);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            db.addNote(content, timestamp);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
