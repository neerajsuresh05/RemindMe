package com.example.remindme;

public class Note {
    public int id;
    public String content;
    public long timestamp;

    public Note(int id, String content, long timestamp) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}
