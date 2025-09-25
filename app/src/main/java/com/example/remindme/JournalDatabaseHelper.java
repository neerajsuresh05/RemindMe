package com.example.remindme;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;
import java.util.List;

public class JournalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "remindme.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";

    private static final String COL_ID = "id";
    private static final String COL_CONTENT = "content";
    private static final String COL_TIMESTAMP = "timestamp";

    public JournalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Notes table creation
        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONTENT + " TEXT, " +
                COL_TIMESTAMP + " LONG)";
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now drop & recreate notes table only
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long addNote(String content, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CONTENT, content);
        cv.put(COL_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_NOTES, null, cv);
        db.close();
        return id;
    }

    public List<Note> getAllNotes() {
        List<Note> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " ORDER BY " + COL_TIMESTAMP + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP));
                list.add(new Note(id, content, timestamp));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
