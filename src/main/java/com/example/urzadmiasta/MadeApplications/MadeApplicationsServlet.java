package com.example.urzadmiasta.MadeApplications;

import com.example.urzadmiasta.Forms.Form;
import com.example.urzadmiasta.SavedApplications.SavedApplicationsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

/**
 * Servlet złożonych wniosków
 */
@Controller
public class MadeApplicationsServlet {

    /**
     * Serwis złożonych wniosków
     */
    MadeApplicationsService madeApplicationsService;

    /**
     * Konstruktor
     * @param madeApplicationsService serwis złożonych wniosków
     */
    MadeApplicationsServlet(MadeApplicationsService madeApplicationsService){
        this.madeApplicationsService = madeApplicationsService;
    }

    /**
     * Zwraca w odpowiedzi stronę wyświetlającą wszystkie złożone wniosku użytkownika.
     * Template strony jest taki sam dla użytkownika jak i admina, dlatego do modelu dodawany jest atrybut admin,
     * w zależności od jego wartości przycisk odpowiadający za podgląd wniosku odnosi się do odpowiego mappingu
     * @param authentication dane uwierzytelniające użytkownika, potrzebne do uzyskania id użytkownika
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/madeApplications")
    public String showMadeApplications(Authentication authentication, Model model) {
        ArrayList<Form> forms = madeApplicationsService.getMadeApplicationsForms(authentication);
        model.addAttribute("forms", forms);
        model.addAttribute("admin", false);
        return "madeApplications";
    }

    /**
     * Podgląd złożonego wniosku. Wyświetla wniosek na podstawie przesłanego jako parametr id wniosku.
     * Używa metody post, dodatkowo sprawdza czy użytkownik próbuje uzyskać podgląd do swojego wniosku, jeśli
     * próbuje uzyskać dostęp do wniosku innego użytkownika zwraca stronę błędu.
     * @param id id złożonego wniosku
     * @param authentication dane uwierzytelniające użytkownika
     * @param model model strony
     * @return odpowiedź
     */
    @PostMapping("/applicationMadePreview")
    public String applicationMadePreview(@RequestParam(name = "id") Integer id, Authentication authentication, Model model) {
        Form form = madeApplicationsService.buildFormFromApplicationId(id);
        model.addAttribute("form", form);
        if (!madeApplicationsService.checkUser(authentication, form)){
            return "error";
        }
        return "applicationMadePreview";
    }

}
