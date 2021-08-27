package com.example.urzadmiasta.Official;

import com.example.urzadmiasta.Application.ApplicationService;
import com.example.urzadmiasta.Forms.FieldInForm;
import com.example.urzadmiasta.Forms.Form;
import com.example.urzadmiasta.MadeApplications.MadeApplicationsService;
import com.example.urzadmiasta.Register.Register;
import com.example.urzadmiasta.Register.RegisterError;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Servlet kontekstu urzędnika
 */
@Controller
public class OfficialServlet {
    /**
     * Serwis kontekstu urzędnika
     */
    OfficialService officialService;
    /**
     * Serwis złożonych wniosków
     */
    MadeApplicationsService madeApplicationsService;
    /**
     * Serwis wniosków
     */
    ApplicationService applicationService;

    /**
     * Konstruktor
     * @param officialService Serwis kontekstu urzędnika
     * @param madeApplicationsService Serwis złożonych wniosków
     * @param applicationService Serwis wniosków
     */
    OfficialServlet(OfficialService officialService, MadeApplicationsService madeApplicationsService, ApplicationService applicationService) {
        this.officialService = officialService;
        this.madeApplicationsService = madeApplicationsService;
        this.applicationService = applicationService;
    }

    /**
     * Mapowanie głównej strony kontekstu urzędnika. Dodaje do modelu strony statystyki obliczone przez serwis urzędnika.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official")
    public String getOfficialPanel(Model model) {
        model.addAttribute("applicationsTotal", officialService.getApplicationsTotal());
        model.addAttribute("applicationsWaiting", officialService.getApplicationsWaiting());
        model.addAttribute("applicationsApproved", officialService.getApplicationsApproved());
        model.addAttribute("applicationsDenied", officialService.getApplicationsDenied());

        model.addAttribute("applicationsTotalToday", officialService.getApplicationsToday());
        model.addAttribute("applicationsWaitingToday", officialService.getApplicationsWaitingToday());
        model.addAttribute("applicationsApprovedToday", officialService.getApplicationsApprovedToday());
        model.addAttribute("applicationsDeniedToday", officialService.getApplicationsDeniedToday());
        return "official";
    }

    /**
     * Zwraca stronę przeglądania wszystkich oczekujących złożonych wniosków.
     * Używa takiego samego template'u jak strona złożonych wniosków zwykłego użytkownika, stąd też do modelu dodawany
     * jest atrybut admin o wartości true.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official/waitingMadeApplications")
    public String getOfficialWaitingMadeApplications(Model model) {
        model.addAttribute("forms", officialService.getWaitingMadeApplications());
        model.addAttribute("admin", true);
        return "madeApplications";
    }

    /**
     * Zwraca stronę przeglądania wszystkich sprawdzonych złożonych wniosków.
     * Używa takiego samego template'u jak strona złożonych wniosków zwykłego użytkownika, stąd też do modelu dodawany
     * jest atrybut admin o wartości true. Od wartości tego atrybutu zależny jest adres do którego odnosi się
     * przycisk przeglądania wniosku.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official/checkedMadeApplications")
    public String getOfficialCheckedMadeApplications(Model model) {
        model.addAttribute("forms", officialService.getCheckedMadeApplications());
        model.addAttribute("admin", true);
        return "madeApplications";
    }

    /**
     * Podgląd złożonego wniosku. Buduje formularz wniosku na podstawie przesłanego przez metodę post parametru id wniosku.
     * Dodaje formularz do modelu strony, sprawdza czy występuje konfilkt interesów, dodaje go jako atrybut do modelu,
     * gdzie jest obsługiwany poprzez odpowiedni komunikat. Pobiera informacje o użytkowniku składającym wniosek
     * i dodaje do modelu strony.
     * @param id id wniosku
     * @param model model strony
     * @param authentication dane uwierzytelniające urzędnika
     * @return odpowiedź
     */
    @PostMapping("/official/applicationMadePreview")
    public String postOfficialMadeApplicationPreview(@RequestParam(name = "id") Integer id, Model model, Authentication authentication) {
        Form form = madeApplicationsService.buildFormFromApplicationId(id);
        model.addAttribute("form", form);
        model.addAttribute("conflict", officialService.checkConflictOfInterest(authentication, form));
        model.addAttribute("user", officialService.getUserById(form.getUserId()));
        return "officialApplicationMadePreview";
    }

    /**
     * Aktualizacja statusu złożonego wniosku. Metodą post przesyłany jest atrybut modelu w postaci obiektu klasy Form,
     * z którego pobierane jest id wniosku z którego zbudowanego był formularz, oraz to czy został zaakceptowany czy odrzucony
     * oraz jaki komentarz został dodany do wniosku. Na podstawie danych uwierzytelniających urzędnika pobierany jest
     * jego id. Serwis urzędnika obsługuje aktualizacje stanu. Do modelu odpowiedzi dodawana jest lista wszystkich oczekujących
     * wniosków następnie jako odpowiedź przekazywana jest strona listy oczekujących wniosków.
     * @param model model strony odpowiedzi
     * @param form formularz sprawdzanego wniosku
     * @param authentication dane uwierzytelniające urzędnika
     * @return odpowiedź
     */
    @PostMapping("/official/applicationMadeUpdate")
    public String postOfficialMadeApplicationUpdate(Model model, @ModelAttribute Form form, Authentication authentication) {
        officialService.updateApplicationStatus(form.getApplicationId(), form.isApproved(), form.getComment(), authentication);
        model.addAttribute("forms", officialService.getWaitingMadeApplications());
        model.addAttribute("admin", true);
        return "madeApplications";
    }

    /**
     * Mapowanie strony wyświetlającej wszystkie aktywne formualrze.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official/forms")
    public String getOfficialForms(Model model) {
        model.addAttribute("forms", officialService.getAllForms());
        return "officialForms";
    }

    /**
     * Edycja aktywnego formularza. Na podstawie id przesłanego jako parametr metodą post, odwołując sie do metody
     * serwisu wniosków buduje formularz, który następnie dodaje jako atrybut do modelu strony.
     * @param id id wniosku
     * @param model model strony
     * @return odpowiedź
     */
    @PostMapping("/official/formEdit")
    public String postFormEdit(@RequestParam(name = "id") Integer id, Model model) {
        Form form = applicationService.getFormById(id);
        model.addAttribute("form", form);
        return "formEdit";
    }

    /**
     * Usuwanie edytowanego wniosku. Za pomocą serwisu urzędnika usuwa formularz o id formualrza przesłąnego jako
     * atrybut modelu strony. Następnie dodaje do modelu odpowiedzi listę wszystkich dostępnych formularzy i w
     * odpowiedzi zwraca stronę wyświetlającą wszystkie dostępne formularze
     * @param form formularz do usunięcia
     * @param model model strony
     * @return odpowiedź
     */
    @PostMapping("/official/formDelete")
    public String postFormDelete(@ModelAttribute Form form, Model model) {
        officialService.deleteForm(form.getId());
        model.addAttribute("forms", officialService.getAllForms());
        return "officialForms";
    }

    /**
     * Formularz tworzenia konta nowego urzędnika. Do modelu strony odpowiedzi dodawane są nowe obiekty klas
     * Register i RegisterError. Pierwszy obiekt przechowuje wpisane dane nowego urzędnika. Na podstawie drugiego
     * wyświetlany jest formularz jeśli pole registererror isSuccess ma wartość false.
     * Adres ma dwa mappingi post i get, obydwa używają jednego template'u strony, modelowane są inaczej w zależności
     * od atrybutu registerError.
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official/createOfficial")
    public String getCreateOfficial(Model model) {
        model.addAttribute("register", new Register());
        model.addAttribute("registerError", new RegisterError());
        return "officialCreateOfficial";
    }

    /**
     * Dodawanie nowego urzędnika. Przesyłany jest atrybut modelu w postaci obiektu register formularza dodawania urzędnika
     * na jego podstawie następuje sprawdzenie poprawności danych za pomocą serwisu. Na podstawie uzyskanego w ten sposób
     * obiektu registerError dodanego do modelu strony po stronie template'u strony nastepuje wyświetlenie stosownego
     * komunikatu. Jeśli pole isSuccess obiektu registerError ma wartość true, podane dane są poprawne i użytkownik
     * może zostać dodan, za pomocą serwisu urzędnika.
     * @param register obiekt klasy Register zawierający dane nowego urzędnika
     * @param model model strony
     * @return odpowiedź
     */
    @PostMapping("/official/createOfficial")
    public String postCreateOfficial(@ModelAttribute Register register, Model model) {
        RegisterError registerError = officialService.createOfficialCheck(register);
        model.addAttribute("register", register);
        model.addAttribute("registerError", registerError);
        if (registerError.isSuccess()) {
            officialService.addNewOfficial(register);
        }
        return "officialCreateOfficial";
    }

}
