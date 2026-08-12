package com.jter.jterstaffers.managers;

public class Note {

    private final int id;
    private final String text;
    private final String author;
    private final long timestamp;

    public Note(int id, String text, String author, long timestamp) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
