package com.example.campus_buddy;

public class Offers {
    private String title;
    private String skill;
    private String details;
    private String documentId;
    private String status;
    private String accepted_by;
    private String created_by;

    // Firestore requires an empty constructor
    public Offers() {}

    public Offers(String title, String skill, String details, String documentId, String status, String accepted_by, String created_by) {
        this.title = title;
        this.skill = skill;
        this.details = details;
        this.documentId = documentId;
        this.status = status;
        this.accepted_by = accepted_by;
        this.created_by = created_by;
    }

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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAccepted_by() {
        return accepted_by;
    }

    public void setAccepted_by(String accepted_by) {
        this.accepted_by = accepted_by;
    }

    public String getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(String created_by) {
        this.created_by = created_by;
    }
}
