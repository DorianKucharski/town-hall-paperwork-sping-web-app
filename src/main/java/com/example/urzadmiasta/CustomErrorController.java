package com.example.urzadmiasta;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller obsługi błędów implementuje interfejs ErrorController
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Mapping agresu /error. Tworzy odpowiedź w postaci strony błędu
     * @return odpowiedź
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }

    /**
     * Przesłonięta metoda implementu ErrorControllera. Zwraca adres strony obsługi błędów.
     * @return adres przekierowania.
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
