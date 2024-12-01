package com.example.campus_buddy;

public class Requests {
    private String title;
    private String skill;
    private String details;
    private String documentId; // Add this field
    private String status;
    private String accepted_by;

    public Requests(String title, String skill, String details, String documentId, String status, String accepted_by) {
        this.title = title;
        this.skill = skill;
        this.details = details;
        this.documentId = documentId;
        this.accepted_by = accepted_by;
        this.status = status;
    }

    // Default constructor
    public Requests() {}

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String documentId) {
        this.status = status;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }
}
