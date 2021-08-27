package com.example.urzadmiasta.FormsAvailable;

import javax.persistence.*;

/**
 * Dostępny formularz
 */
@Entity
@Table(name = "forms_available", schema = "public")
public class FormAvailable {
    /**
     * Id formularza
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * Nazwa formularza
     */
    private String name;
    /**
     * Opis formularza
     */
    private String description;
    /**
     * Czy formularz jest dostępny. Używane do usuwania formularzy, ustawienie pola na false, sprawia iż formualrza nie
     * można już wypełnić, lecz wypełnione uprzednio wniosku z użyciem tego formularza nie znikają.
     */
    private boolean available;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
