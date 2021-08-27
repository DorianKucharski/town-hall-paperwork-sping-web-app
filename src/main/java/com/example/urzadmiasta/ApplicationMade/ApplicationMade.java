package com.example.urzadmiasta.ApplicationMade;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Złożony wniosek.
 */
@Entity
@Table(name = "aplications_made", schema = "public")
public class ApplicationMade {

    /**
     * Id wniosku
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * Id użytkownika
     */
    private int userId;
    /**
     * Id formularza
     */
    private int formId;
    /**
     * Data złożenia wniosku
     */
    private Timestamp timestamp;
    /**
     * Id urzędnika sprawdzającego wniosek
     */
    private int clerkId;
    /**
     * Czy wniosek jest sprawdzony
     */
    private boolean checked;
    /**
     * Czy wniosek jest zatwierdzony
     */
    private boolean approved;
    /**
     * Komentarz urzędnika
     */
    private String comment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
