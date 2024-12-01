package com.example.campus_buddy;

public class StudentActivity {
    private String email;
    private int rating;

    // Firestore requires an empty constructor
    public StudentActivity() {}

    public StudentActivity(String email, int rating) {
        this.email = email;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
