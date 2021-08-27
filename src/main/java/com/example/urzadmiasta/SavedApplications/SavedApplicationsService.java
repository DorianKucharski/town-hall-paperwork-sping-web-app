package com.example.urzadmiasta.SavedApplications;

import com.example.urzadmiasta.Application.ApplicationService;
import com.example.urzadmiasta.ApplicationMade.ApplicationMade;
import com.example.urzadmiasta.ApplicationMade.ApplicationMadeField;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeFieldsRepository;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Serwis zapisanych wniosków
 */
@Service
public class SavedApplicationsService {
    /**
     * Serwis wniosków
     */
    private ApplicationService applicationService;
    /**
     * Repozytorium dostępnych wniosków
     */
    private FormsAvailableRepository formsAvailableRepository;
    /**
     * Repozytorium pól dostępnych wniosków
     */
    private FieldsRepository fieldsRepository;
    /**
     * Repozytorium użytkowników
     */
    private UserRepository userRepository;
    /**
     * Repozytorium zapisanych wniosków
     */
    private ApplicationsSavedRepository applicationsSavedRepository;
    /**
     * Repozytorium pól zapisanych wniosków
     */
    private ApplicationsSavedFieldsRepository applicationsSavedFieldsRepository;
    /**
     * Repozytorium złożonych wniosków
     */
    private ApplicationsMadeRepository applicationsMadeRepository;
    /**
     * Repozytorium pól zapisanych wniosków
     */
    private ApplicationsMadeFieldsRepository applicationsMadeFieldsRepository;

    /**
     * Konstruktor
     * @param applicationService Serwis wniosków
     * @param formsAvailableRepository Repozytorium dostępnych wniosków
     * @param fieldsRepository Repozytorium pól dostępnych wniosków
     * @param userRepository Repozytorium użytkowników
     * @param applicationsSavedRepository Repozytorium zapisanych wniosków
     * @param applicationsSavedFieldsRepository Repozytorium pól zapisanych wniosków
     * @param applicationsMadeRepository Repozytorium złożonych wniosków
     * @param applicationsMadeFieldsRepository Repozytorium pól zapisanych wniosków
     */
    public SavedApplicationsService(ApplicationService applicationService,
                                    FormsAvailableRepository formsAvailableRepository,
                                    FieldsRepository fieldsRepository,
                                    UserRepository userRepository,
                                    ApplicationsSavedRepository applicationsSavedRepository,
                                    ApplicationsSavedFieldsRepository applicationsSavedFieldsRepository,
                                    ApplicationsMadeRepository applicationsMadeRepository,
                                    ApplicationsMadeFieldsRepository applicationsMadeFieldsRepository) {
        this.applicationService = applicationService;
        this.formsAvailableRepository = formsAvailableRepository;
        this.fieldsRepository = fieldsRepository;
        this.userRepository = userRepository;
        this.applicationsSavedRepository = applicationsSavedRepository;
        this.applicationsSavedFieldsRepository = applicationsSavedFieldsRepository;
        this.applicationsMadeRepository = applicationsMadeRepository;
        this.applicationsMadeFieldsRepository = applicationsMadeFieldsRepository;
    }

    /**
     * Buduje formularz na podstawie zapisanego wniosku, który wypełnia wartościami z zapisanego wniosku.
     * @param applicationId id wniosku
     * @return formularz zbudowany z zapisanego wniosku o danym id
     */
    Form getFormOfApplication(int applicationId) {
        ApplicationSaved applicationSaved = applicationsSavedRepository.getOne(applicationId);
        int formId = applicationSaved.getFormId();
        FormAvailable formAvailable = formsAvailableRepository.getOne(formId);
        ArrayList<Field> fields = fieldsRepository.findFieldsOfForm(formId);
        ArrayList<ApplicationSavedField> savedFields = applicationsSavedFieldsRepository.findFieldsOfApplication(applicationId);

        return Form.formFromApplicationSaved(formAvailable, fields, applicationSaved, savedFields);
    }

    /**
     * Zwraca listę formularzy zbudowanych na podstawie zapisanych wniosków użytkownika. Z danych uwierzytelniających
     * pobiera nazwę użytkownika, wyszukuje użytkownika za jej pomocą w repozytorium użytkowników,
     * następnie za pomocą id użytkownika z repozytorium zapisanych wniosków pobiera wszystkie zapisane wnioski użytkownika
     * Następnie za pomocą metody budującej formularz z zapisanego wniosku, buduje wniosek, który dodaje do listy formularzy.
     * @param authentication dane uwierzytelniające użytkownika
     * @return lista formularzy zapisanych wniosków
     */
    ArrayList<Form> getApplicationsForms(Authentication authentication) {
        int userId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
        ArrayList<ApplicationSaved> applicationsSaved = applicationsSavedRepository.findAllByUserId(userId);
        ArrayList<Form> forms = new ArrayList<>();

        for (ApplicationSaved applicationSaved : applicationsSaved) {
            forms.add(getFormOfApplication(applicationSaved.getId()));
        }

        return forms;
    }

    /**
     * Usuwa zapisany wniosek o danym id wraz z jego zapisanymi polami.
     * @param id id wniosku do usunięcia
     * @param authentication dane autoryzujące użytkownika
     */
    void deleteApplication(int id, Authentication authentication) {
        ArrayList<ApplicationSavedField> fields = applicationsSavedFieldsRepository.findFieldsOfApplication(id);
        applicationsSavedFieldsRepository.deleteAll(fields);
        applicationsSavedFieldsRepository.flush();
        applicationsSavedRepository.deleteById(id);
        applicationsSavedRepository.flush();
    }

    /**
     * Składanie wniosku z zapisanego wniosku. Buduje formualrz wniosku na podstawie wniosku o podanym id.
     * Tworzy nowy obiekt klasy ApplicationMade czyli złożonego wniosku. Następnie ustawia jego pola na podstawie
     * pól zbudowanego formularza. W ten sposób translatuje wniosek zapisany na wniosek złożony. Zapisuje
     * nowo utworzony złożony wniosek, następnie tworzy jego pola, ustawia jego wartości na podstawie formularza,
     * po czym zapisuje w repozytorium utworzone pola. Na koniec zwraca formularz.
     * @param id id zapisanego wniosku
     * @param authentication dane uwierzytelniające użytkownika
     * @return formularz zapisanego wniosku.
     */
    Form saveApplication(int id, Authentication authentication) {
        Form form = applicationService.buildFormFromApplicationId(id);
        ApplicationMade applicationMade = new ApplicationMade();
        if (authentication != null) {
            int userId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
            applicationMade.setUserId(userId);
            applicationMade.setFormId(form.getId());
            applicationMade.setTimestamp(new Timestamp(System.currentTimeMillis()));
            applicationMade.setClerkId(0);
            applicationMade.setChecked(false);
            applicationMade.setApproved(false);
            applicationMade.setComment("");
            applicationsMadeRepository.saveAndFlush(applicationMade);

            for (FieldInForm fieldInForm : form.getFieldsInForm()) {
                ApplicationMadeField applicationMadeField = new ApplicationMadeField();
                applicationMadeField.setApplicationMadeId(applicationMade.getId());
                applicationMadeField.setValue(fieldInForm.getValue());
                applicationMadeField.setFieldId(fieldInForm.getField().getId());
                applicationsMadeFieldsRepository.saveAndFlush(applicationMadeField);
            }
        }
        return form;
    }

    /**
     * Zmiana wartości pól zapisanego wniosku. Na podstawie przesłanego formularza wniosku uaktualnia pola wniosku o
     * danym id, wywołując metodę repozytorium.
     * @param form formularz ze uaktualnionymi polami
     * @param authentication dane uwierzytelniajace użytkownika
     * @param id id zapisanego wniosku do uaktualnienia
     */
    void updateApplication(Form form, Authentication authentication, int id) {
        for (FieldInForm fieldInForm : form.getFieldsInForm()) {
            applicationsSavedFieldsRepository.setSavedFieldValue(fieldInForm.getValue(), id, fieldInForm.getField().getId());
        }
    }
}
