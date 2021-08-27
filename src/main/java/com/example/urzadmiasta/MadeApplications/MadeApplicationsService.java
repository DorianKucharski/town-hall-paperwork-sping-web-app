package com.example.urzadmiasta.MadeApplications;

import com.example.urzadmiasta.Application.ApplicationService;
import com.example.urzadmiasta.ApplicationMade.ApplicationMade;
import com.example.urzadmiasta.ApplicationMade.ApplicationMadeField;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeFieldsRepository;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Serwis złożonych wniosków
 */
@Service
public class MadeApplicationsService {

    /**
     * Serwis składania wniosków
     */
    ApplicationService applicationService;
    /**
     * Repozytorium dostępnych wniosków
     */
    FormsAvailableRepository formsAvailableRepository;
    /**
     * Repozytorium pól wniosków
     */
    FieldsRepository fieldsRepository;
    /**
     * Repozytorium użytkowników
     */
    UserRepository userRepository;
    /**
     * Repozytorium zapisanych wniosków
     */
    ApplicationsSavedRepository applicationsSavedRepository;
    /**
     * Repozytorium pól zapisanych wniosków
     */
    ApplicationsSavedFieldsRepository applicationsSavedFieldsRepository;
    /**
     * Repozytorium złożonych wniosków
     */
    ApplicationsMadeRepository applicationsMadeRepository;
    /**
     * Repozytorium pól złożonych wniosków
     */
    ApplicationsMadeFieldsRepository applicationsMadeFieldsRepository;

    public MadeApplicationsService(ApplicationService applicationService,
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
     * Buduje obiekt klasy Form na podstawie id złożonego wniosku. Sprawdza czy wniosek został sprawdzony jeśli tak
     * pobiera z bazy nazwę urzędnika, który sprawdzał wniosek.
     * @param applicationId id złożonego wniosku
     * @return formularz zbudowany na podstawie złożonego wniosku
     */
    public Form buildFormFromApplicationId(int applicationId) {
        ApplicationMade applicationMade = applicationsMadeRepository.getOne(applicationId);
        int formId = applicationMade.getFormId();
        FormAvailable formAvailable = formsAvailableRepository.getOne(formId);
        ArrayList<Field> fields = fieldsRepository.findFieldsOfForm(formId);

        ArrayList<ApplicationMadeField> savedFields = applicationsMadeFieldsRepository.findFieldsOfApplication(applicationId);
        Form form = Form.formFromApplicationMade(formAvailable, fields, applicationMade, savedFields);
        if (form.isChecked()) {
            User official = userRepository.getOne(applicationMade.getClerkId());
            form.setClerkName(official.getFirstName() + " " + official.getLastName());
        }
        return form;
    }

    /**
     * Zwraca listę formularzy zbudowanych na podstawie wszystkich złożonych wniosków użytkownika
     * @param authentication dane uwierzytelniające użytkownika
     * @return lista formularzy
     */
    ArrayList<Form> getMadeApplicationsForms(Authentication authentication) {
        int userId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
        ArrayList<ApplicationMade> applicationsMade = applicationsMadeRepository.findAllByUserId(userId);
        ArrayList<Form> forms = new ArrayList<>();

        for (ApplicationMade applicationMade : applicationsMade) {
            forms.add(buildFormFromApplicationId(applicationMade.getId()));
        }
        return forms;
    }

    /**
     * Sprawdza czy użytkownik odwołujący się do wniosku to użytkownik składający wniosek.
     * Zabezpiecza przed oglądaniem wniosków innych użytkowników.
     * @param authentication dane uwierzytelniające użytkownika
     * @param form formularz wniosku
     * @return true jeśli złożony wniosek należy do danego użytkownika
     */
    boolean checkUser(Authentication authentication, Form form){
        int userId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
        return userId == form.getUserId();
    }
}
