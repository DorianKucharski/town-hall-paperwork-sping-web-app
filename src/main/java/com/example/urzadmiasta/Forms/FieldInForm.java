package com.example.urzadmiasta.Forms;

import com.example.urzadmiasta.FormsAvailable.Field;

/**
 * Klasa pola w formularzu z klasy Form.
 */
public class FieldInForm {
    /**
     * Obiekt klasy encji Field.
     */
    Field field;
    /**
     * Wartość pola
     */
    String value;

    public FieldInForm(){

    }

    /**
     * Konstruktor. Przyjmuje wyłącznie jeden parametr ponieważ, wywoływany jest wyłącznie do tworzenia formularza,
     * w momencie w którym wartość pola jest jeszcze nie znana.
     * @param field
     */
    public FieldInForm(Field field){
        this.field = field;
        this.value = "";
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }



}
