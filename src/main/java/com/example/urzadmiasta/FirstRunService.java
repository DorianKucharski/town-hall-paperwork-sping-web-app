package com.example.urzadmiasta;

import com.example.urzadmiasta.Forms.Form;
import com.example.urzadmiasta.FormsAvailable.Field;
import com.example.urzadmiasta.FormsAvailable.FieldsRepository;
import com.example.urzadmiasta.FormsAvailable.FormAvailable;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący pierwsze uruchomienie aplikacji na nowej bazie danych.
 */
@Service
public class FirstRunService {

    /**
     * Repozytorium użytkowników
     */
    UserRepository userRepository;
    /**
     * Repozytorium formularzy
     */
    FormsAvailableRepository formsAvailableRepository;
    /**
     * Repozytorium pól formualrzy
     */
    FieldsRepository fieldsRepository;

    /**
     * Konstruktor
     * @param userRepository repozytorium użytkowników
     */
    FirstRunService(UserRepository userRepository,
                    FormsAvailableRepository formsAvailableRepository,
                    FieldsRepository fieldsRepository) {
        this.userRepository = userRepository;
        this.formsAvailableRepository = formsAvailableRepository;
        this.fieldsRepository = fieldsRepository;
    }

    /**
     * Sprawdza czy nastąpiło pierwsze uruchomienie, sprawdzając liczbę użytkowników. Jeżeli liczba ta równa jest zeru,
     * dodaje dwóch testowych użytkowników: urzędnika i użytkownika, oraz tworzy i dodaje jeden testowy formularz
     * @return true jeśli jest to pierwsze uruchomienie, false jeśli nie
     */
    boolean firstRun() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setRoles("ROLE_USER");
            user.setActive(true);
            user.setUserName("user");
            user.setPassword("user");
            user.setCity("Kielce");
            user.setStreet("aleja Tysiąclecia Państwa Polskiego 7");
            user.setFirstName("Użytkownik");
            user.setLastName("Testowy");
            user.setEmail("user@user.com");
            user.setPesel("12345678901");
            user.setPhone("+48123456789");
            user.setState("Świętokrzyskie");
            userRepository.saveAndFlush(user);
            User admin = new User();
            admin.setRoles("ROLE_ADMIN");
            admin.setActive(true);
            admin.setUserName("admin");
            admin.setPassword("admin");
            admin.setCity("Kielce");
            admin.setStreet("aleja Tysiąclecia Państwa Polskiego 7");
            admin.setFirstName("Urzędnik");
            admin.setLastName("Testowy");
            admin.setEmail("user@user.com");
            admin.setPesel("12345678901");
            admin.setPhone("+48123456789");
            admin.setState("Świętokrzyskie");
            userRepository.saveAndFlush(admin);

            FormAvailable formAvailable = new FormAvailable();
            formAvailable.setAvailable(true);
            formAvailable.setName("Testowy formularz");
            formAvailable.setDescription("Testowy opis testowego formularza do testowania wypełniania formularza w ramach testów");
            formsAvailableRepository.saveAndFlush(formAvailable);
            int formId = formAvailable.getId();
            Field field1 = new Field();
            field1.setFormId(formId);
            field1.setName("Imię osoby składającej testowy wniosek");
            field1.setDescription("pierwsze pole testowe - automatycznie wypełniane");
            field1.setType("integer");
            fieldsRepository.saveAndFlush(field1);

            Field field2 = new Field();
            field2.setFormId(formId);
            field2.setName("jakieś testowe pole");
            field2.setDescription("drugie testowe pole - nie wypełniane automatycznie");
            field2.setType("integer");
            fieldsRepository.saveAndFlush(field2);
            return true;
        }
        return false;
    }
}
