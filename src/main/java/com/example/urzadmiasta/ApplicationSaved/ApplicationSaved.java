package com.example.urzadmiasta.ApplicationSaved;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Zapisany wniosek
 */
@Entity
@Table(name = "aplications_saved", schema = "public")
public class ApplicationSaved {

    /**
     * Id zapisanego wniosku
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
     * Data zapisania wniosku
     */
    private Timestamp timestamp;

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
}



