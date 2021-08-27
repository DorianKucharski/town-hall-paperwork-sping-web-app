package com.example.urzadmiasta.FormCreator;

import com.example.urzadmiasta.Forms.*;
import com.example.urzadmiasta.FormsAvailable.Field;
import com.example.urzadmiasta.FormsAvailable.FieldsRepository;
import com.example.urzadmiasta.FormsAvailable.FormAvailable;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import org.springframework.stereotype.Service;

/**
 * Serwis tworzenia nowych formularzy wniosków
 */
@Service
public class FormCreatorService {

    /**
     * Repozytorium dostępnych wniosków
     */
    FormsAvailableRepository formsAvailableRepository;
    /**
     * Repozytorium pól wniosków
     */
    FieldsRepository fieldsRepository;

    /**
     * Konstruktor
     * @param formsAvailableRepository Repozytorium dostępnych wniosków
     * @param fieldsRepository Repozytorium pól wniosków
     */
    FormCreatorService(FormsAvailableRepository formsAvailableRepository,
                       FieldsRepository fieldsRepository) {
        this.formsAvailableRepository = formsAvailableRepository;
        this.fieldsRepository = fieldsRepository;
    }

    /**
     * Zapisuje utworzony wniosek. Na podstawie obiektu klasy Form przesłanego jako parametr, tworzy obiekt klasy
     * FormAvailable który jest encją w bazie danych, następnie ten obiekt dodaje do bazy danych. Na podstawie listy pól
     * formularza znajdującej się w obiekcie form tworzy kolejne encje pól i je także dodaje do bazy danych.
     * Wiążac je z encją formularza.
     * @param form formularz
     */
    public void createForm(Form form) {
        FormAvailable formAvailable = new FormAvailable();
        formAvailable.setName(form.getName());
        formAvailable.setDescription(form.getDescription());
        formAvailable.setAvailable(true);
        formsAvailableRepository.save(formAvailable);
        formsAvailableRepository.flush();
        int formId = formAvailable.getId();
        for (FieldInForm fieldInForm : form.getFieldsInForm()) {
            fieldInForm.getField().formId = formId;
            fieldsRepository.saveAndFlush(fieldInForm.getField());
        }
    }

}
