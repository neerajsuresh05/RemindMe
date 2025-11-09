package com.example.remindme;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "remindme.db";
    private static final int DATABASE_VERSION = 3;

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // Add reminder
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

    // Get all reminders
    public List<Reminder> getAllReminders() {
        List<Reminder> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REMINDERS + " ORDER BY " + COL_TIME + " ASC", null);

        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIME));
                String repeat = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPEAT));
                list.add(new Reminder(id, title, timeMillis, repeat));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Get single reminder
    public Reminder getReminderById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDERS, null, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
            long timeMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIME));
            String repeat = cursor.getString(cursor.getColumnIndexOrThrow(COL_REPEAT));
            cursor.close();
            db.close();
            return new Reminder(id, title, timeMillis, repeat);
        }
        db.close();
        return null;
    }

    // Update reminder
    public int updateReminder(int id, String title, long timeMillis, String repeat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_TIME, timeMillis);
        cv.put(COL_REPEAT, repeat);
        int rows = db.update(TABLE_REMINDERS, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // Delete reminder
    public void deleteReminder(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
