package com.example.urzadmiasta.SavedApplications;

import com.example.urzadmiasta.Application.ApplicationService;
import com.example.urzadmiasta.Forms.Form;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Servlet zapisanych wniosków
 */
@Controller
public class SavedApplicationsServlet {

    /**
     * Serwis wniosków
     */
    ApplicationService applicationService;
    /**
     * Serwis zapisanych wniosków
     */
    SavedApplicationsService savedApplicationsService;

    /**
     * Konstruktor
     * @param applicationService Serwis wniosków
     * @param savedApplicationsService Serwis zapisanych wniosków
     */
    SavedApplicationsServlet(ApplicationService applicationService, SavedApplicationsService savedApplicationsService){
        this.applicationService = applicationService;
        this.savedApplicationsService = savedApplicationsService;
    }

    /**
     * Przeglądanie zapisanych wniosków. Wywołuje metodę serwisu zapisanych wniosków, która zwraca listę formularzy
     * wszystkich zapisanych wniosków użytkownika pobranego z danych uwierzytelniających
     * @param authentication dane uwierzytelniające
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/savedApplications")
    public String showSavedApplications(Authentication authentication, Model model) {
        model.addAttribute("forms", savedApplicationsService.getApplicationsForms(authentication));
        return "savedApplications";
    }

    /**
     * Podgląd wybranego zapisanego wniosku. Wywołuje metodę serwisu, która generuje formularz wniosku ze zwłożonego
     * wniosku o danym id. Dodaje wygenerowany formularz do modelu strony odpowiedzi wraz z id wniosku.
     * @param id id wniosku
     * @param model model strony
     * @param authentication dane uwierzytelniające
     * @return odpowiedź
     */
    @PostMapping("/applicationPreview")
    public String applicationPreview(@RequestParam(name = "id") Integer id, Model model, Authentication authentication) {
        Form form = applicationService.buildFormFromApplicationId(id);
        model.addAttribute("applicationId", id);
        model.addAttribute("form", form);

        return "applicationSaved";
    }

    /**
     * Edycja zapisanego wniosku. Na podstawie id przesłanego jako parametr, generuje wypełniony formularz wniosku
     * posługując się metodą serwisu. Dodaje do modelu strony formularz wraz z id wniosku.
     * @param id id wniosku
     * @param model model strony
     * @param authentication dane uwierzytelniające
     * @return odpowiedź
     */
    @PostMapping("/editApplication")
    public String applicationEdit(@RequestParam(name = "id") String id, Model model, Authentication authentication) {
        Form form = applicationService.buildFormFromApplicationId(Integer.parseInt(id));
        model.addAttribute("applicationId", id);
        model.addAttribute("form", form);
        return "applicationEdit";
    }

    /**
     * Zapisanie zmian we wniosku o id przesłanym jako parametr. Do metody trafia także atrybut modelu w postaci formularza,
     * na podstawie którego wygenerowany będzie model z zastosowanymi zmianami.
     * @param id id wniosku do aktualizacji
     * @param form formularz zmienionego wniosku
     * @param model model strony
     * @param authentication dane uwierzytelniające
     * @return odpowiedź
     */
    @PostMapping("/updateApplication")
    public String updateApplication(@RequestParam(name = "applicationId") Integer id, @ModelAttribute Form form, Model model, Authentication authentication) {
        savedApplicationsService.updateApplication(form, authentication, id);
        model.addAttribute("form", form);
        return "applicationSaved";
    }

    /**
     * Usuwanie zapisanego wniosku o id przesłanym jako parametr. Posługując się metodą serwisu usuwa dany wniosek,
     * następnie zwraca metodę wyświetlającą wszystkie złożone wnioski.
     * @param id id wniosku do usunięcia
     * @param model model strony
     * @param authentication dane uwierzytelniające
     * @return odpowiedź
     */
    @PostMapping("/deleteApplication")
    public String deleteApplication(@RequestParam(name = "id") Integer id, Model model, Authentication authentication) {
        savedApplicationsService.deleteApplication(id, authentication);
        return showSavedApplications(authentication, model);
    }

    /**
     * Zatwierdza wniosek o danym id przesłanym jako parametr metodą post i przesyła go do złożenia.
     * Wywołując metodę serwisu, która odpowiada za utworzenie złożonego wniosku na podstawie zapisanego wniosku, następnie
     * usuwa zapisany wniosek.
     * @param id id zapisanego wniosku
     * @param model model strony
     * @param authentication dane uwierzytelniające
     * @return odpowiedź
     */
    @PostMapping("/sendApplication")
    public String sendApplication(@RequestParam(name = "id") Integer id, Model model, Authentication authentication) {
        Form form = savedApplicationsService.saveApplication(id, authentication);
        model.addAttribute("form", form);
        return "applicationMadePreview";
    }
}
