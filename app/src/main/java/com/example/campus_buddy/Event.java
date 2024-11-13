package com.example.campus_buddy;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Event {
    private String title;
    private Timestamp date;
    private String description;
    private String location;

    // Firestore requires an empty constructor
    public Event() {}

    public Event(String title, Timestamp date, String description, String location) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.location = location;
    }

    public String getTitle() { return title; }
    public Timestamp getDate() { return date; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdf.format(date.toDate());
    }
}
