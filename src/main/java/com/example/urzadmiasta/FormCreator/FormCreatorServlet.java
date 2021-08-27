package com.example.urzadmiasta.FormCreator;

import com.example.urzadmiasta.Forms.Form;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

/**
 * Servlet kretora formularzy
 */
@Controller
public class FormCreatorServlet {

    /**
     * Serwis kreatora formularzy
     */
    FormCreatorService formCreatorService;

    /**
     * Konstruktor servletu
     * @param formCreatorService
     */
    FormCreatorServlet(FormCreatorService formCreatorService){
        this.formCreatorService = formCreatorService;
    }

    /**
     * Zwraca model strony kreatora formularza. Metody get i post odnoszą się do tego samego mappingu stąd też,
     * do modelu dodawany jest atrybut isCompleted który odpowiada za obsługę modelu, jeżeli ma on wartość false,
     * na stronie wyświetlany jest kreator, jeżeli true, wyświetlany jest komunikat o pomyślnym dodaniu formularza
     * @param model model strony
     * @return odpowiedź
     */
    @GetMapping("/official/formCreator")
    public String createProposalGet( Model model) {
        model.addAttribute("isCompleted", Boolean.FALSE);
        return "formCreator";
    }


    /**
     * Pobiera przesłane parametry i na ich podstawie odnosząc się do specjalnego do tego celu konstruktora przyjmującego
     * listę parametrów w postaci mapy tworzony jest formularz, który następnie za pomocą metody serwisu jest dodawany do bazy danych.
     * Z racji tego iż kreator napisany jest w javascript, nie użyto tutaj biblioteki thymeleaf, dlatego też parametry nie są
     * przesyłane jako atrybut modelu a jako mapa parametrów.
     * @param model model strony
     * @param allParams przesłane parametry formularza kretora formularzy
     * @return odpowiedź
     */
    @PostMapping("/official/formCreator")
    public String createProposalPost(Model model, @RequestParam Map<String, String> allParams) {
        Form form = new Form(allParams);
        formCreatorService.createForm(form);
        model.addAttribute("isCompleted", Boolean.TRUE);
        return "formCreator";
    }
}
