package com.example.urzadmiasta;

import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeFieldsRepository;
import com.example.urzadmiasta.ApplicationMade.ApplicationsMadeRepository;
import com.example.urzadmiasta.ApplicationSaved.ApplicationsSavedFieldsRepository;
import com.example.urzadmiasta.ApplicationSaved.ApplicationsSavedRepository;
import com.example.urzadmiasta.FormsAvailable.FieldsRepository;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import com.example.urzadmiasta.User.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Główna klasa
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, FormsAvailableRepository.class,
		FieldsRepository.class, ApplicationsSavedRepository.class, ApplicationsSavedFieldsRepository.class,
        ApplicationsMadeRepository.class, ApplicationsMadeFieldsRepository.class})
public class Main {

    /**
     * Główna metoda
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
