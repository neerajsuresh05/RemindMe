package com.example.remindme;

public class Reminder {
    public int id;
    public String title;
    public long timeMillis;
    public String repeat;

    public Reminder(int id, String title, long timeMillis, String repeat) {
        this.id = id;
        this.title = title;
        this.timeMillis = timeMillis;
        this.repeat = repeat;
    }
}
