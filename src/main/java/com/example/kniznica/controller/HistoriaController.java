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



    // Form for borrowing a book
    @GetMapping("/borrow")
    public String borrowBookForm(Model model) {
        List<Citatel> citatelList = citatelRepo.findAll();
        List<Kniha> knihaList = knihaRepo.findAll();

        model.addAttribute("citatelList", citatelList);
        model.addAttribute("knihaList", knihaList);
        return "history/borrow";
    }

    // Process borrowing a book
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam String cisloOP, @RequestParam Long knihaId, Model model) {
        Optional<Citatel> citatelOpt = citatelRepo.findById(cisloOP);
        Optional<Kniha> knihaOpt = knihaRepo.findById(knihaId);

        if (citatelOpt.isPresent() && knihaOpt.isPresent()) {
            Citatel citatel = citatelOpt.get();
            Kniha kniha = knihaOpt.get();

            // Create a new borrowing history entry
            Historia historia = new Historia();
            historia.setCitatel(citatel);
            historia.setKniha(kniha);
            historia.setBorrowDate(new Date());
            historia.setReturnDate(calculateReturnDate());  // Calculate or set return date

            historiaRepo.save(historia);

            // Update book's status to borrowed (assuming `jeVypozicana` is an enum)
            kniha.setJeVypozicana(StavVypozicky.ANO);
            knihaRepo.save(kniha);

            model.addAttribute("message", "Book successfully borrowed!");
        } else {
            model.addAttribute("message", "User or Book not found");
        }

        return "history/borrow";
    }



    // Display the return book form
//    @GetMapping("/return/{id}")
//    public String returnBookForm(@PathVariable("id") Long id, Model model) {
//        if (id == null) {
//            model.addAttribute("message", "Invalid history ID provided.");
//            return "error"; // Redirect to a generic error page or display an error.
//        }
//
//        Optional<Historia> historiaOpt = historiaRepo.findById(id);
//        if (historiaOpt.isPresent()) {
//            model.addAttribute("historia", historiaOpt.get());
//        } else {
//            model.addAttribute("message", "History record not found.");
//        }
//        return "history/return";
//    }
//
//    // Process the book return
//    @PostMapping("/return")
//    public String returnBook(@RequestParam("id") Long id,
//                             @RequestParam("cisloOP") String cisloOP,
//                             @RequestParam("knihaId") Long knihaId,
//                             Model model) {
//        if (id == null || knihaId == null || cisloOP == null) {
//            model.addAttribute("message", "Both history ID and book ID are required.");
//            return "error"; // Redirect to an error page.
//        }
//
//        Optional<Historia> historiaOpt = historiaRepo.findById(id);
//        Optional<Kniha> knihaOpt = knihaRepo.findById(knihaId);
//        Optional<Citatel> citatelOpt = citatelRepo.findById(cisloOP);
//        if (historiaOpt.isPresent() && knihaOpt.isPresent() && citatelOpt.isPresent()) {
//            Historia historia = historiaOpt.get();
//            Kniha kniha = knihaOpt.get();
//            Citatel citatel = citatelOpt.get();
//
//            // Update the history record's actual return date
//            historia.setActualReturnDate(new Date());
//            historiaRepo.save(historia);
//
//            // Update the book's availability status
//            kniha.setJeVypozicana(StavVypozicky.NIE);
//            knihaRepo.save(kniha);
//
//            model.addAttribute("message", "Book successfully returned!");
//            return "redirect:/history"; // Redirect to the history list.
//        } else {
//            model.addAttribute("message", "History record or Book not found.");
//            return "error"; // Redirect to an error page.
//        }
//    }
    @GetMapping("/edit")
    public String showEditPage(@RequestParam Long id, Model model) {
        try {
            // Fetch Historia entity using ID
            Historia historia = historiaRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid history ID: " + id));

            // Create a DTO and populate it with values from the Historia entity
            HistoriaDto historyDto = new HistoriaDto();
            historyDto.setId(historia.getId());
            historyDto.setReturnDate(historia.getReturnDate());
            historyDto.setActualReturnDate(historia.getActualReturnDate());

            // Add both the Historia entity and the HistoriaDto to the model
            model.addAttribute("historiyyy", historia);
            model.addAttribute("historyDto", historyDto);

        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            return "redirect:/history"; // Redirect to the history list on error
        }

        // Return the form view
        return "history/edit";
    }

    // POST: Handle the form submission and update the Historia entity
    @PostMapping("/edit")
    public String updateHistory(@Valid @ModelAttribute("historyDto") HistoriaDto historyDto,
                                BindingResult bindingResult, Model model, @RequestParam Long id) {

        if (bindingResult.hasErrors()) {
            return "history/edit"; // Stay on the form page if validation fails
        }

        try {
            // Fetch the Historia entity by its ID
            Historia historia = historiaRepo.findById(historyDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid history ID: " + id));

            // Update the Historia entity with values from the DTO
            historia.setReturnDate(historyDto.getReturnDate());
            historia.setActualReturnDate(historyDto.getActualReturnDate());

            // Save the updated Historia entity back to the repository
            historiaRepo.save(historia);

        } catch (Exception e) {
            model.addAttribute("message", "Error: " + e.getMessage());
            return "history/edit"; // If an error occurs, stay on the edit page
        }

        // Redirect to the history list after successful update
        return "redirect:/history";
    }



    @GetMapping("/delete")
    public String deleteProduct(@RequestParam long id){
        try {
            Optional<Historia> citatelOptional = historiaRepo.findById(id);

            if (citatelOptional.isPresent()) {
                Historia citatel = citatelOptional.get();
                historiaRepo.delete(citatel);
                System.out.println("Deleted citatel with cisloOP: " + id);
            } else {
                System.out.println("Citatel with cisloOP " + id + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error during delete operation: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/history";
    }


    private Date calculateReturnDate() {
       // Define logic for calculating return date, e.g., add 14 days for the borrow period
       long time = System.currentTimeMillis();
      return new Date(time + (14 * 24 * 60 * 60 * 1000));  // Example: 14 days from now
   }
}
