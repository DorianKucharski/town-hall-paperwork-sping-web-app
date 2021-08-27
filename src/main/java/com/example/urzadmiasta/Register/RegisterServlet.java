package com.example.urzadmiasta.Register;

import com.example.urzadmiasta.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Servlet rejestracji
 */
@Controller
public class RegisterServlet {

    /**
     * Repozytorium użytkowników
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Formualrz rejestracji. Zwraca model strony z dodanymi atrybutami obiektów klas Register i RegisterError
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/register")
    public String greetingForm(Model model) {
        model.addAttribute("register", new Register());
        model.addAttribute("registerError", new RegisterError());
        return "register";
    }

    /**
     * Rejestracja nowego użytkownika. Przesłany formularz rejestracji przesyła do serwisu rejestracji, który weryfikuje
     * podane dane, oraz tworzy nowego użytkownika i dodaje go do repozytorium.
     * @param model model strony
     * @param register obiekt klasy Register zawierający dane rejestracyjne nowego użytkownika
     * @return odpowiedź
     */
    @PostMapping("/register")
    public String greetingSubmit(Model model, @ModelAttribute Register register) {
        RegisterError registerError = RegisterService.RegisterCheck(userRepository, register);
        model.addAttribute("register", register);
        model.addAttribute("registerError", registerError);
        return "register";
    }
}
