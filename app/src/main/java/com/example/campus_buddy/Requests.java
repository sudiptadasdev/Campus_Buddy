package com.example.campus_buddy;

public class Requests {
    private String title;
    private String skill;
    private String details;

    // Firestore requires an empty constructor
    public Requests() {}

    public Requests(String title, String skill, String details) {
        this.title = title;
        this.skill = skill;
        this.details = details;
    }

    public String getTitle() { return title; }
    public String getSkill() { return skill; }
    public String getDetails() { return details; }

    @Override
    public String toString() {
        return "Offer: " + title + "\nSkill: " + skill + "\nDetails: " + details;
    }
}
