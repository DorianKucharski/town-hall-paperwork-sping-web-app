package com.example.urzadmiasta.Application;

import com.example.urzadmiasta.Forms.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Servlet składania wniosków. Obsługuje wypełnianie wniosku, jego zapisanie, oraz ściąganie wniosku jak i formularzy.
 */
@Controller
public class ApplicationServlet {

    /**
     * Service obsługujący wnioski
     */
    ApplicationService applicationService;

    /**
     * Konstruktor
     * @param applicationService serwis wniosków
     */
    ApplicationServlet(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Tworzenie nowego wniosku. Na podstawie id formularza przesłanego jako parametr, generuje formularz wniosku,
     * następnie wypełnia formularz danymi użytkownika i dołącza formularz do modelu odpowiedzi.
     * @param id id formularza wniosku
     * @param authentication dane uwierzytelniajace użytkownika
     * @param model model strony
     * @return odpowiedź
     */
    @PostMapping("/application")
    public String applicationGet(@RequestParam(name = "id") Integer id, Authentication authentication, Model model) {
        Form form = applicationService.getFormById(id);
        applicationService.fillUserDetails(form, authentication);
        model.addAttribute("form", form);
        return "application";
    }

    /**
     * Zapisuje wniosek przesłąny jako atrybut modelu strony, który następnie przesyła do strony podglądu wniosku.
     * @param model model strony
     * @param form formularz wniosku
     * @param authentication dane uwierzytelniające użytkownika
     * @return odpowiedź
     */
    @PostMapping("/applicationSaved")
    public String applicationPost(Model model, @ModelAttribute Form form, Authentication authentication) {
        int applicationId = applicationService.saveApplication(form, authentication);
        model.addAttribute("applicationId", applicationId);
        model.addAttribute("form", form);
        return "applicationSaved";
    }


    /**
     * Przesyłą formularz wniosku w postaci pdf. Tworzy formularz na podstawie id formularza, następnie odwołując się do
     * metody tworzącej odpowiedź zawierającą załącznik w postaci pliku pdf.
     * @param id id wniosku
     * @return odpowiedź z plikiem pdf
     */
    @RequestMapping(value = "/downloadForm", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadForm(@RequestParam(name = "id") int id) {
        Form form = applicationService.getFormById(id);
        return applicationService.downloadPdf(form);
    }

    /**
     * Przesyła wniosek w postaci pliku pdf. Na podstawie id wniosku tworzy formularz wniosku następnie za pomocą serwisu
     * wniosków przesyła plik jako załącznik w odpowiedzi.
     * @param id id wniosku
     * @return odpowiedź z plikiem pdf w załączniku
     */
    @RequestMapping(value = "/downloadApplication", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadApplication(@RequestParam(name = "id") int id) {
        Form form = applicationService.buildFormFromApplicationId(id);
        return applicationService.downloadPdf(form);
    }



}
