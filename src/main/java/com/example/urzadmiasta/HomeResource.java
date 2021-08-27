package com.example.urzadmiasta;

import com.example.urzadmiasta.FormsAvailable.FormAvailable;
import com.example.urzadmiasta.FormsAvailable.FormsAvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Servlet głównej strony
 */
@Controller
public class HomeResource {

    /**
     * Repozytorium dostępnych formularzy
     */
    FormsAvailableRepository formsAvailableRepository;
    /**
     * Serwis obsługi pierwszego uruchomienia na nowej bazie danych
     */
    FirstRunService firstRunService;

    /**
     * Konstruktor
     * @param formsAvailableRepository Repozytorium dostępnych formularzy
     * @param firstRunService Serwis obsługi pierwszego uruchomienia na nowej bazie danych
     */
    HomeResource(FormsAvailableRepository formsAvailableRepository, FirstRunService firstRunService){
        this.formsAvailableRepository = formsAvailableRepository;
        this.firstRunService = firstRunService;
    }

    /**
     * Logowanie
     * @return strona logowania
     */
    @GetMapping("/login")
    public String login() {
        return ("login");
    }

    /**
     * Główna strona. Na podastwie danych uwierzytelniających zwraca odpowiednią stronę. Jeżeli użytkownik jest zalogowany
     * przenosi go do strony paneluu użytkownika, jeżeli użytkownik posiada uprawnienia administratora, dodaje
     * do modelu strony atrybut admin z wartością true, co skutkuje pojwieniem się dodatkowego kontekstu urzędnika.
     * Jeżeli uzytkownik jest niezalogowany wyświetlana jest domyślna strona z opcjami logowania, rejestracji
     * i wnioskami w formacie pdf możliwymi do ściągnięcia przez niezalogowanych użytkowników.
     * Sprawdza też czy jest to pierwsze uruchomienia na nowej bazie danych. Jeżeli tak dodaje do modelu, atrybut
     * odpowiadający za wyświetlenie stosownej informacji.
     * @param authentication dane uwierzytelniające
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        if (authentication != null) {
            boolean admin = false;
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()){
                if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                    admin = true;
                }
            }
            model.addAttribute("admin", admin);
            return ("panel");
        }

        model.addAttribute("firstRun", firstRunService.firstRun());
        List<FormAvailable> formsAvailable = formsAvailableRepository.getAllAvailable();
        model.addAttribute("formsAvailable", formsAvailable);

        return ("index");
    }
}

