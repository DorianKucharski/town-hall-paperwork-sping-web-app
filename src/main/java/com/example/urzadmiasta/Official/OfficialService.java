package com.example.urzadmiasta.Official;

import com.example.urzadmiasta.ApplicationMade.ApplicationMade;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeRepository;
import com.example.urzadmiasta.Forms.Form;
import com.example.urzadmiasta.FormsAvailable.FormAvailable;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import com.example.urzadmiasta.MadeApplications.MadeApplicationsService;
import com.example.urzadmiasta.Register.Register;
import com.example.urzadmiasta.Register.RegisterError;
import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serwis kontekstu urzędnika
 */
@Service
public class OfficialService {

    /**
     * Repozytorium złożonych wniosków
     */
    ApplicationsMadeRepository applicationsMadeRepository;
    /**
     * Repozytorium dostępnych formularzy
     */
    FormsAvailableRepository formsAvailableRepository;
    /**
     * Serwis obsługi złożonych wniosków
     */
    MadeApplicationsService madeApplicationsService;
    /**
     * Repozytorium użytkowników
     */
    UserRepository userRepository;

    /**
     * Konstruktor
     * @param applicationsMadeRepository Repozytorium złożonych wniosków
     * @param formsAvailableRepository Repozytorium dostępnych formularzy
     * @param madeApplicationsService Serwis obsługi złożonych wniosków
     * @param userRepository Repozytorium użytkowników
     */
    OfficialService(ApplicationsMadeRepository applicationsMadeRepository,
                    FormsAvailableRepository formsAvailableRepository,
                    MadeApplicationsService madeApplicationsService,
                    UserRepository userRepository){
        this.applicationsMadeRepository = applicationsMadeRepository;
        this.formsAvailableRepository = formsAvailableRepository;
        this.madeApplicationsService = madeApplicationsService;
        this.userRepository =userRepository;
    }

    /**
     * Pobiera z repozytorium listę wszystkich złożonych wniosków
     * @return lista wniosków
     */
    public List<ApplicationMade> getApplicationsTotalList(){
        return applicationsMadeRepository.findAll();
    }

    /**
     * Zwraca liczbę wszystkich złożonych wniosków
     * @return liczba wniosków
     */
    public int getApplicationsTotal(){
        return getApplicationsTotalList().size();
    }

    /**
     * Oblicza liczbę wszystkich oczekujących wniosków
     * @return liczba oczekujących wniosków
     */
    public int getApplicationsWaiting(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTotalList()){
            if (!applicationMade.isChecked()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Oblicza liczbę wszystkich wniosków zaakceptowanych
     * @return liczba zaakceptowanych wniosków
     */
    public int getApplicationsApproved(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTotalList()){
            if (applicationMade.isChecked() && applicationMade.isApproved()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Oblicza liczbę wszystkich wniosków odrzuconych
     * @return liczba wniosków odrzuconych
     */
    public int getApplicationsDenied(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTotalList()){
            if (applicationMade.isChecked() && !applicationMade.isApproved()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Filtruje listę wszystkich wniosków aby utworzyć listę wniosków złożonych dzisiaj
     * @return lista wszystkich wniosków złożonych dzisiaj
     */
    public List<ApplicationMade> getApplicationsTodayList(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin =  LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
        LocalDateTime end =  LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth()+1, 0, 0);
        Timestamp beginTimestamp = Timestamp.valueOf(begin);
        Timestamp endTimestamp = Timestamp.valueOf(end);

        List<ApplicationMade> applicationsMadeToday = new ArrayList<>();
        List<ApplicationMade> applicationsMade = applicationsMadeRepository.findAll();
        for (ApplicationMade applicationMade : applicationsMade){
            if (applicationMade.getTimestamp().after(beginTimestamp) && applicationMade.getTimestamp().before(endTimestamp)){
                applicationsMadeToday.add(applicationMade);
            }
        }

        return applicationsMadeToday;
    }

    /**
     * Oblicza liczbę wniosków złożonych dzisiaj
     * @return liczba wniosków złożonych dzisiaj
     */
    public int getApplicationsToday(){
        return getApplicationsTodayList().size();
    }

    /**
     * Oblicza liczbę oczekujących wniosków złożonych dzisiaj
     * @return liczba oczekujących wniosków złożonych dzisiaj
     */
    public int getApplicationsWaitingToday(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTodayList()){
            if (!applicationMade.isChecked()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Oblicza liczbę zaakceptowanych wniosków złożonych dzisiaj
     * @return liczba zaakceptowanych wniosków złożonych dzisiaj
     */
    public int getApplicationsApprovedToday(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTodayList()){
            if (applicationMade.isChecked() && applicationMade.isApproved()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Oblicza liczbę odrzuconych wniosków złożonych dzisiaj
     * @return liczba wniosków
     */
    public int getApplicationsDeniedToday(){
        int amount = 0;
        for(ApplicationMade applicationMade: getApplicationsTodayList()){
            if (applicationMade.isChecked() && !applicationMade.isApproved()){
                amount++;
            }
        }
        return amount;
    }

    /**
     * Filtruje listę wszystkich złożonych wniosków, zwracając listę wniosków oczekujących
     * @return lista wniosków oczekujących
     */
    public List<Form> getWaitingMadeApplications(){
        List<Form> waitingApplications = new ArrayList<>();
        for (ApplicationMade applicationMade : getApplicationsTotalList()){
            if(!applicationMade.isChecked()){
                waitingApplications.add(madeApplicationsService.buildFormFromApplicationId(applicationMade.getId()));
            }
        }
        return waitingApplications;
    }

    /**
     * Filtruje listę wszystkich złożonych wniosków, zwracając listę wszystkich wniosków sprawdzonych.
     * @return lista wniosków sprawdzonych
     */
    public List<Form> getCheckedMadeApplications(){
        List<Form> waitingApplications = new ArrayList<>();
        for (ApplicationMade applicationMade : getApplicationsTotalList()){
            if(applicationMade.isChecked()){
                waitingApplications.add(madeApplicationsService.buildFormFromApplicationId(applicationMade.getId()));
            }
        }
        return waitingApplications;
    }

    /**
     * Uaktualnia status złożonego wniosku.
     * @param applicationId id złożonego wniosku
     * @param approved true - zaakceptować wniosek, false - odrzucić
     * @param comment komentarz do wniosku
     * @param authentication dane autoryzujące urzędnika
     */
    public void updateApplicationStatus(int applicationId, boolean approved, String comment, Authentication authentication){
        int clerkId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
        applicationsMadeRepository.updateApplicationStatus(true, approved, comment, clerkId, applicationId);
    }

    /**
     * Sprawdza czy następuje konfilkt interesów, to jest czy urzędnik próbuje sprawdzić wniosek złożony przez samego siebie.
     * @param authentication dane uwierzytelniające urzędnika
     * @param form formularz
     * @return true - jeśli występuje konflikt, false - jeśli nie
     */
    public boolean checkConflictOfInterest(Authentication authentication, Form form){
        int clerkId = userRepository.findByUserName(authentication.getName()).orElse(new User()).getId();
        return clerkId == form.getUserId();
    }

    /**
     * Wyszukuje użytkownika na podstawie podanego id
     * @param id id użytkownika
     * @return User
     */
    public User getUserById(int id){
        return userRepository.findById(id).orElse(new User());
    }

    /**
     * Wyszukuje wszystkie dostępne formualrze
     * @return lista formularzy
     */
    public List<FormAvailable> getAllForms(){
        return formsAvailableRepository.getAllAvailable();
    }

    /**
     * Usuwa możliwość wypełnienia danego formularza, ustawiając jego pole available na false.
     * @param id id formualrza
     */
    void deleteForm(int id){
        formsAvailableRepository.deleteForm(id);
    }

    /**
     * Sprawdza dane rejestracji nowego użytkownika, sprawdza czy dany login nie jest zajęty, oraz czy hasła się zgadzają.
     * @param register obiekt rejestracji
     * @return obiekt klasy RegisterError, zaiwerający błędy pól rejestracji
     */
    RegisterError createOfficialCheck(Register register){
        RegisterError registerError = new RegisterError();
        registerError.setSuccess(true);
        if (userRepository.findByUserName(register.getUserName()).isPresent()) {
            registerError.setNameError(true);
            registerError.setSuccess(false);
        }
        if (!register.getPassword().equals(register.getRepeatPassword())) {
            registerError.setPasswordsNotMatch(true);
            registerError.setSuccess(false);
        }
        return registerError;
    }

    /**
     * Tworzy nowego użytkownika i zapisuje go w repozytorium użytkowników.
     * Nadaje mu role admina i ustawia na użytkownika aktywnego.
     * @param register obiekt rejestracji
     */
    void addNewOfficial(Register register){
        User user = new User();
        user.setActive(true);
        user.setRoles("ROLE_ADMIN");
        user.setUserName(register.getUserName());
        user.setPassword(register.getPassword());
        userRepository.saveAndFlush(user);
    }
}
