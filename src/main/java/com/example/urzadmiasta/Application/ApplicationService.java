package com.example.urzadmiasta.Application;

import com.example.urzadmiasta.ApplicationSaved.ApplicationSaved;
import com.example.urzadmiasta.ApplicationSaved.ApplicationSavedField;
import com.example.urzadmiasta.ApplicationSaved.ApplicationsSavedFieldsRepository;
import com.example.urzadmiasta.ApplicationSaved.ApplicationsSavedRepository;
import com.example.urzadmiasta.Forms.*;
import com.example.urzadmiasta.FormsAvailable.Field;
import com.example.urzadmiasta.FormsAvailable.FieldsRepository;
import com.example.urzadmiasta.FormsAvailable.FormAvailable;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Serwis obsługi składania wniosków
 */
@Service
public class ApplicationService {

    FormsAvailableRepository formsAvailableRepository;
    FieldsRepository fieldsRepository;
    UserRepository userRepository;
    ApplicationsSavedRepository applicationsSavedRepository;
    ApplicationsSavedFieldsRepository applicationsSavedFieldsRepository;

    /**
     * Konstruktor
     * @param formsAvailableRepository
     * @param fieldsRepository
     * @param userRepository
     * @param applicationsSavedRepository
     * @param applicationsSavedFieldsRepository
     */
    public ApplicationService(FormsAvailableRepository formsAvailableRepository,
                              FieldsRepository fieldsRepository,
                              UserRepository userRepository,
                              ApplicationsSavedRepository applicationsSavedRepository,
                              ApplicationsSavedFieldsRepository applicationsSavedFieldsRepository) {
        this.formsAvailableRepository = formsAvailableRepository;
        this.fieldsRepository = fieldsRepository;
        this.userRepository = userRepository;
        this.applicationsSavedRepository = applicationsSavedRepository;
        this.applicationsSavedFieldsRepository = applicationsSavedFieldsRepository;
    }

    /**
     * Sprawdza czy tekst znajduje się w tablicy tekstów
     * @param value szukany tekst
     * @param array tablica tekstów
     * @return true jeśli szukany tekst znajduje się w tablicy
     */
    static boolean checkIfValueIsInTheArray(String value, String[] array) {
        for (String s : array) {
            if (value != null && value.toLowerCase().strip().contains(s.toLowerCase().strip())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Wypełnia formularz danymi użytkownika. Na podstawie tablic prawdopodobnych nazw typu pola określa typ pola we wniosku,
     * jeśli podane pole pasuje do schematu wypełnia je danymi użykownika.
     * @param form formularz wniosku
     * @param authentication dane autentykacyjne zalogowanego użytkownika
     */
    public void fillUserDetails(Form form, Authentication authentication) {
        if (authentication != null) {
            String userName = authentication.getName();
            User user = userRepository.findByUserName(userName).orElse(new User());
            String[] firstName = {"imię", "imie"};
            String[] lastName = {"nazwisko"};
            String[] name = {"nazwa"};
            String[] pesel = {"pesel"};
            String[] state = {"województwo"};
            String[] city = {"miasto"};
            String[] email = {"email"};
            String[] phone = {"telefon"};

            for (FieldInForm fieldInForm : form.getFieldsInForm()) {
                if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), firstName)) {
                    fieldInForm.setValue(user.getFirstName());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), lastName)) {
                    fieldInForm.setValue(user.getLastName());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), name)) {
                    fieldInForm.setValue(user.getFirstName() + " " + user.getLastName());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), pesel)) {
                    fieldInForm.setValue(user.getPesel());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), state)) {
                    fieldInForm.setValue(user.getState());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), city)) {
                    fieldInForm.setValue(user.getCity());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), email)) {
                    fieldInForm.setValue(user.getEmail());
                } else if (checkIfValueIsInTheArray(fieldInForm.getField().getName(), phone)) {
                    fieldInForm.setValue(user.getPhone());
                }
            }
        }
    }

    /**
     * Buduje formularz wniosku na podstawie id zapisanego wniosku.
     * @param applicationId id zapisanego wniosku
     * @return zbudowany formularz wniosku
     */
    public Form buildFormFromApplicationId(int applicationId) {
        ApplicationSaved applicationSaved = applicationsSavedRepository.getOne(applicationId);
        int formId = applicationSaved.getFormId();
        FormAvailable formAvailable = formsAvailableRepository.getOne(formId);
        ArrayList<Field> fields = fieldsRepository.findFieldsOfForm(formId);
        ArrayList<ApplicationSavedField> savedFields = applicationsSavedFieldsRepository.findFieldsOfApplication(applicationId);

        return Form.formFromApplicationSaved(formAvailable,fields,applicationSaved,savedFields);
    }

    /**
     * Buduje formularz na podstawie zapisanego dostępnego formularza i pól powiązanych z nim
     * @param id id formularza
     * @return gotowy formularz
     */
    public Form getFormById(int id) {
        return new Form(formsAvailableRepository.getOne(id), fieldsRepository.findFieldsOfForm(id));
    }

    /**
     * Zapisuje wniosek. Tworzy na podstawie formularza nowy wniosek, któy zapisuje do bazy danych.
     * Następnie tworzy pola wniosku, powiązuje je ze złożonym wnioskiem na podstawie id i zapisuje dane do bazy danych.
     * @param form formularz wniosku
     * @param authentication dane uwierzytelniające użytkownika
     * @return id zapisanego wniosku
     */
    public int saveApplication(Form form, Authentication authentication) {
        ApplicationSaved applicationSaved = new ApplicationSaved();
        if (authentication != null) {
             int userId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
            applicationSaved.setUserId(userId);
            applicationSaved.setFormId(form.getId());
            applicationSaved.setTimestamp(new Timestamp(System.currentTimeMillis()));
            applicationsSavedRepository.saveAndFlush(applicationSaved);

            for (FieldInForm fieldInForm : form.getFieldsInForm()) {
                ApplicationSavedField applicationSavedField = new ApplicationSavedField();
                applicationSavedField.setApplicationSavedId(applicationSaved.getId());
                applicationSavedField.setValue(fieldInForm.getValue());
                applicationSavedField.setFieldId(fieldInForm.getField().getId());
                applicationsSavedFieldsRepository.saveAndFlush(applicationSavedField);
            }
        }
        return applicationSaved.getId();

    }

    /**
     * Generuje odpowiedź zawierającą plik pdf.
     * @param form formularz do wygenerowania pliku pdf
     * @return odpowiedź
     */
    public ResponseEntity<InputStreamResource> downloadPdf(Form form) {
        ByteArrayInputStream bis = PdfGenerator.createPdf(form);
        HttpHeaders headers = new HttpHeaders();
        String filename = form.getName().replaceAll(" ", "_") + ".pdf";
        headers.add("Content-Disposition", "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
