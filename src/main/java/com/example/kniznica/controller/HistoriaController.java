package com.example.kniznica.controller;


import com.example.kniznica.entity.*;
import com.example.kniznica.repositar.CitatelRepo;
import com.example.kniznica.repositar.HistoryRepo;
import com.example.kniznica.repositar.KnihaRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/history")
public class HistoriaController {

    @Autowired
    private HistoryRepo historiaRepo;

    @Autowired
    private CitatelRepo citatelRepo;

    @Autowired
    private KnihaRepo knihaRepo;

    // Show all borrowing history
    @GetMapping({"/", ""})
    public String showHistory(Model model) {
        List<Historia> historyList = historiaRepo.findAll();
        model.addAttribute("historyList", historyList);

        return "history/index";
    }



    // forma pre rezervaciu khihy
    @GetMapping("/borrow")
    public String borrowBookForm(Model model) {
        List<Citatel> citatelList = citatelRepo.findAll();
        List<Kniha> knihaList = knihaRepo.findAll();

        model.addAttribute("citatelList", citatelList);
        model.addAttribute("knihaList", knihaList);
        return "history/borrow";
    }

    // postmaping
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam String cisloOP, @RequestParam Long knihaId, Model model) {
        Optional<Citatel> citatelOpt = citatelRepo.findById(cisloOP);
        Optional<Kniha> knihaOpt = knihaRepo.findById(knihaId);

        if (citatelOpt.isPresent() && knihaOpt.isPresent()) {
            Citatel citatel = citatelOpt.get();
            Kniha kniha = knihaOpt.get();

            // vytvaranie novej historie
            Historia historia = new Historia();
            historia.setCitatel(citatel);
            historia.setKniha(kniha);
            historia.setBorrowDate(new Date());
            historia.setReturnDate(calculateReturnDate());

            historiaRepo.save(historia);


            kniha.setJeVypozicana(StavVypozicky.ANO);
            knihaRepo.save(kniha);


            model.addAttribute("message", "Kniha je uspesne rezervovana");
        } else {
            model.addAttribute("message", "Citatel alebo kniha nie je najdena");
        }

        return "history/borrow";
    }




    @GetMapping("/edit")
    public String showEditPage(@RequestParam Long id, Model model) {
        try {
            // najdi historiu po id
            Historia historia = historiaRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Nespravne ID: " + id));


            HistoriaDto historyDto = new HistoriaDto();
            historyDto.setId(historia.getId());
            historyDto.setReturnDate(historia.getReturnDate());
            historyDto.setActualReturnDate(historia.getActualReturnDate());


            model.addAttribute("historiyyy", historia);
            model.addAttribute("historyDto", historyDto);

        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            return "redirect:/history";
        }


        return "history/edit";
    }


    @PostMapping("/edit")
    public String updateHistory(@Valid @ModelAttribute("historyDto") HistoriaDto historyDto,
                                BindingResult bindingResult, Model model, @RequestParam Long id) {

        if (bindingResult.hasErrors()) {
            return "history/edit"; // ak je chyba tak ostan na stranke
        }

        try {

            Historia historia = historiaRepo.findById(historyDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid history ID: " + id));


            historia.setReturnDate(historyDto.getReturnDate());
            historia.setActualReturnDate(historyDto.getActualReturnDate());


            historiaRepo.save(historia);

        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            return "history/edit";
        }


        return "redirect:/history";
    }



    @GetMapping("/delete")
    public String deleteProduct(@RequestParam long id){
        try {
            Optional<Historia> citatelOptional = historiaRepo.findById(id);

            if (citatelOptional.isPresent()) {
                Historia citatel = citatelOptional.get();
                historiaRepo.delete(citatel);
                System.out.println("Zmazanie citatela s cisloOP: " + id);
            } else {
                System.out.println("Citatel s cisloOP " + id + " nie je najdeny.");
            }
        } catch (Exception e) {
            System.err.println("Chyba operacie: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/history";
    }


    private Date calculateReturnDate() {

       long time = System.currentTimeMillis();
      return new Date(time + (14 * 24 * 60 * 60 * 1000));  // +14 dni + sysdate
   }
}
