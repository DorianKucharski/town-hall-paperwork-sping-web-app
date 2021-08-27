package com.example.urzadmiasta.Panel;

import com.example.urzadmiasta.Register.Register;
import com.example.urzadmiasta.User.User;
import com.example.urzadmiasta.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Servlet panelu użytkownika
 */
@Controller
public class ProfileServlet {

    /**
     * Repozytorium użytkowników
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Formularz profilu użytkownika. Adres posiada dwa mappingi get i post. Metoda powiązana z get, pobiera
     * dane uwierzytelniające zalogowanego użytkownika, na podstawie tych danych znajduje użytkownika w repozytorium,
     * a następnie profil użytkownika dodaje do modelu strony i zwraca template profil
     * @param authentication dane uwierzytelniające
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/profile")
    public String greetingForm(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName).orElse(new User());
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Formularz profilu użytkownika. Adres posiada dwa mappingi get i post. Metoda powiązana z metodą post odpowiada
     * za uaktualnienie danych użytkownika na podstawie edytowaych danych przesłanych w postaci atrybutu modelu.
     * Metoda sprawdza na podstawie danych uwierzytelniających sprawdza czy przesłane dane formularza dotyczą
     * zalogowanego użytkownika, jeśli nie zwraca stronę obsługi błędów.
     * @param model model strony
     * @param user przesłąne dane użytkownika do zaktualizowania
     * @param authentication dane uwierzytelniające
     * @return
     */
    @PostMapping("/profile")
    public String greetingSubmit(Model model, @ModelAttribute User user, Authentication authentication) {
        String userName = authentication.getName();
        User userAuth = userRepository.findByUserName(userName).orElse(new User());
        if (userAuth.getId() != user.getId()){
            return "error";
        }
        userRepository.updateUserDetails(user.getFirstName(), user.getLastName(), user.getPesel(), user.getState(),
                user.getCity(), user.getStreet(), user.getEmail(), user.getPhone(), user.getUserName(), user.getId());
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Formularz zmiany hasła. Dodaje do modelu strony obiekt klasy Register, który zostanie wypełniony wartościami z formularza.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("register", new Register());
        return "password";
    }

    /**
     * Zmiana hasła użytkownika. Pobiera z repozytorium użytkownika na podstawie danych autoryzujących, następnie poprzez
     * serwis użytktownika sprawdza wpisane hasła, czy hasło obecne zgadza się z wpisanym w formularzu, czy nowe hasła
     * są takie same oraz czy są odpowiednio trudne. Następnie dodaje te informacje do modelu strony w postaci atrybutów.
     * Jeżeli wszystko się zgadza następuje zmiana hasła.
     * @param authentication dane uwierzytelniające
     * @param model model strony
     * @param register obiekt klasy register zawierający dane z fromualrza
     * @return odpowiedź
     */
    @PostMapping("/password")
    public String changePasswordSubmit(Authentication authentication, Model model, @ModelAttribute Register register) {
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName).orElse(new User());

        Boolean wrongPassword = !ProfileService.checkOldPassword(userRepository, user.getId(), register.getOldPassword());
        Boolean passwordDontMatch = !ProfileService.checkIfPasswordsMatch(register.getPassword(), register.getRepeatPassword());
        Boolean passwordToEasy = ProfileService.checkIfPasswordTooEasy(register.getPassword());
        Boolean success = !wrongPassword && !passwordDontMatch && !passwordToEasy;
        model.addAttribute("wrongPassword", wrongPassword);
        model.addAttribute("passwordDontMatch", passwordDontMatch);
        model.addAttribute("passwordToEasy", passwordToEasy);
        model.addAttribute("success", success);
        if (success){
            userRepository.changeUserPassword(register.getPassword(), user.getId());
        }
        return "password";
    }

}
