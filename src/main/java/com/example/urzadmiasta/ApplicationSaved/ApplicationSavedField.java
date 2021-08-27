package com.example.urzadmiasta.ApplicationSaved;



import javax.persistence.*;

/**
 * Pole zapisanego wniosku.
 */
@Entity
@Table(name = "aplications_saved_fields", schema = "public")
public class ApplicationSavedField{
    /**
     * Id zapisanego pola
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * Id zapisanego wniosku powiązanego z polem
     */
    private int applicationSavedId;
    /**
     * Id pola w formualrzu
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

    public int getApplicationSavedId() {
        return applicationSavedId;
    }

    public void setApplicationSavedId(int applicationSavedId) {
        this.applicationSavedId = applicationSavedId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

}
