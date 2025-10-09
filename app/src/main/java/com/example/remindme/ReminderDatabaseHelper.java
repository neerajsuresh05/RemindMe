package com.example.remindme;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "remindme.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_REMINDERS = "reminders";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_TIME = "timeMillis";
    private static final String COL_REPEAT = "repeat";

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_REMINDERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_TIME + " LONG, " +
                COL_REPEAT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    public int addReminder(String title, long timeMillis, String repeat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_TIME, timeMillis);
        cv.put(COL_REPEAT, repeat);

        long id = db.insert(TABLE_REMINDERS, null, cv);
        db.close();
        return (int) id;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REMINDERS + " ORDER BY " + COL_TIME + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIME));
                String repeat = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPEAT));
                list.add(new Reminder(id, title, timeMillis, repeat));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
