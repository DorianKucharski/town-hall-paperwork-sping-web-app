package com.example.urzadmiasta.FormsAvailable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FormsAvailableServlet {

    @Autowired
    FormsAvailableRepository formsAvailableRepository;

    @GetMapping("/forms")
    public String formsAvailableGet( Model model) {
        List<FormAvailable> forms = formsAvailableRepository.getAllAvailable();
        model.addAttribute("forms", forms);
        return "forms";
    }
}
