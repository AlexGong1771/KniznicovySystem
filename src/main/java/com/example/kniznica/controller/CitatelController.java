package com.example.kniznica.controller;

import com.example.kniznica.entity.Citatel;
import com.example.kniznica.entity.CitatelDto;
import com.example.kniznica.repositar.CitatelRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/citatelia")
public class CitatelController {
    @Autowired
    private CitatelRepo repo;

    @GetMapping({"/", ""})
    public String showProductList(Model model) {
        List<Citatel> citatelia = repo.findAll();
        model.addAttribute("citatelia", citatelia);
        return "citatelia/dajZoznamCitatelov";
    }
    @GetMapping("/pridajCitatela")
    public String showCreatePage(Model model){
        CitatelDto citatelDto = new CitatelDto();
        model.addAttribute("citatelDto" , citatelDto);
        return "citatelia/pridajCitatela";
    }

    @PostMapping("/pridajCitatela")
    public String createProduct(@Valid @ModelAttribute CitatelDto citatelDto, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "citatelia/pridajCitatela";
        }




        // Create a new product and set its properties
        Citatel citatel = new Citatel();
        citatel.setCisloOP(citatelDto.getCisloOP());
        citatel.setMeno(citatelDto.getMeno());
        citatel.setPriezvisko(citatelDto.getPriezvisko());
        citatel.setDatumNarodenia(citatelDto.getDatumNarodenia());


        // Save the product to the repository
        repo.save(citatel);

        return "redirect:/citatelia";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam String cisloOP) {
        try {
            Citatel citatel = repo.findById(cisloOP).orElseThrow(() -> new IllegalArgumentException("Nesparvne cislo OP: " + cisloOP));
            model.addAttribute("citatel", citatel);

            CitatelDto citatelDto = new CitatelDto();
            citatelDto.setMeno(citatel.getMeno());
            citatelDto.setPriezvisko(citatel.getPriezvisko());
            citatelDto.setDatumNarodenia(citatel.getDatumNarodenia());
            model.addAttribute("citatelDto", citatelDto);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            return "redirect:/citatelia";
        }
        return "citatelia/edit"; // Ensure this matches the form's template name
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam String cisloOP, @Valid @ModelAttribute CitatelDto citatelDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "citatelia/edit"; // Ensure this matches the template name used in the GET method
        }

        try {
            Citatel citatel= repo.findById(cisloOP).orElseThrow(() -> new IllegalArgumentException("Nespavne cislo OP: " + cisloOP));
            model.addAttribute("citatel", citatel);



            // Update other fields

            citatel.setMeno(citatelDto.getMeno());
            citatel.setPriezvisko(citatelDto.getPriezvisko());
            citatel.setDatumNarodenia(citatelDto.getDatumNarodenia());

            // Save updated product
            repo.save(citatel);

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            return "citatelia/edit"; // If an error occurs, stay on the edit page
        }

        return "redirect:/citatelia";
    }


    @GetMapping("/delete")
    public String deleteProduct(@RequestParam String cisloOP) {
        try {
            Optional<Citatel> citatelOptional = repo.findById(cisloOP);

            if (citatelOptional.isPresent()) {
                Citatel citatel = citatelOptional.get();
                repo.delete(citatel);
                System.out.println("Deleted citatel with cisloOP: " + cisloOP);
            } else {
                System.out.println("Citatel with cisloOP " + cisloOP + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error during delete operation: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/citatelia";
    }

}
