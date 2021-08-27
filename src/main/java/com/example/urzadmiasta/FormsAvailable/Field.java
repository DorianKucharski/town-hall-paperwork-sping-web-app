package com.example.urzadmiasta.FormsAvailable;

import javax.persistence.*;

/**
 * Pole formularza
 */
@Entity
@Table(name = "forms_fields", schema = "public")
public class Field {
    /**
     * Id pola
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    /**
     * Id formularza pola
     */
    public int formId;
    /**
     * Nazwa pola
     */
    public String name;
    /**
     * Opis pola
     */
    public String description;
    /**
     * Typ pola
     */
    public String type;

    public Field() {
    }

    public Field(String name, String description, String type){
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id=" + id +
                ", formId=" + formId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
