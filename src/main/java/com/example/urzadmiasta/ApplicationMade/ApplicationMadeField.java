package com.example.urzadmiasta.ApplicationMade;

import javax.persistence.*;

/**
 * Pole złożonego wniosku
 */
@Entity
@Table(name = "aplications_made_fields", schema = "public")
public class ApplicationMadeField  {
    /**
     * Id pola
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * Id złożonego wniosku
     */
    private int applicationMadeId;
    /**
     * Id pola w formularzu
     */
    private int fieldId;
    /**
     * Wartość pola
     */
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationMadeId() {
        return applicationMadeId;
    }

    public void setApplicationMadeId(int applicationMadeId) {
        this.applicationMadeId = applicationMadeId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
