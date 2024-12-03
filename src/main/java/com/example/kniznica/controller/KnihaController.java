package com.example.kniznica.controller;


import com.example.kniznica.entity.Kniha;
import com.example.kniznica.entity.KnihaDto;
import com.example.kniznica.repositar.KnihaRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

@Controller
@RequestMapping("/knihy")
public class KnihaController {
    @Autowired
    private KnihaRepo repo;

    @GetMapping({"/", ""})
    public String showProductList(Model model) {
        List<Kniha> knihy = repo.findAll(Sort.by(Sort.Direction.DESC , "id"));
        model.addAttribute("knihy", knihy);
        return "knihy/index";
    }

    @GetMapping("/pridaj")
    public String showCreatePage(Model model){
        KnihaDto knihaDto = new KnihaDto();
        model.addAttribute("knihaDto" , knihaDto);
        return "knihy/pridaj";
    }

   @PostMapping("/pridaj")
   public String createProduct(@Valid @ModelAttribute KnihaDto productDto, BindingResult bindingResult) {

       if (productDto.getImageFile().isEmpty()) {
           bindingResult.addError(new FieldError("knihaDto", "imageFile", "The image file is required"));
       }
       if (bindingResult.hasErrors()) {
           return "knihy/pridaj";
       }


       MultipartFile image = productDto.getImageFile();
       Date date = new Date();
       String storageFileName = date.getTime() + "_" + image.getOriginalFilename();

       try {

           String uploadDir = "public/images";
           Path uploadPath = Paths.get(uploadDir);
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }


           try (InputStream inputStream = image.getInputStream()) {
               Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
           }
       } catch (Exception e) {
           System.out.println("File upload error: " + e.getMessage());
           bindingResult.addError(new FieldError("knihaDto", "imageFile", "Failed to upload image file"));
           return "knihy/pridaj";
       }


       Kniha product = new Kniha();
       product.setNazov(productDto.getNazov());
       product.setAutor(productDto.getAutor());
       product.setJeVypozicana(productDto.getJeVypozicanaDto());
       product.setImageFileName(storageFileName);


       repo.save(product);

       return "redirect:/knihy";
   }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam long id) {
        try {
            Kniha kniha = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
            model.addAttribute("kniha", kniha);

            KnihaDto knihaDto = new KnihaDto();
            knihaDto.setNazov(kniha.getNazov());
            knihaDto.setAutor(kniha.getAutor());
            knihaDto.setJeVypozicanaDto(kniha.getJeVypozicana());
            model.addAttribute("knihaDto", knihaDto);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            return "redirect:/knihy";
        }
        return "knihy/edit"; // Ensure this matches the form's template name
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam long id, @Valid @ModelAttribute KnihaDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "knihy/edit";
        }

        try {
            Kniha kniha= repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));
            model.addAttribute("kniha", kniha);


            if (!productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir, kniha.getImageFileName());

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (IOException e) {
                    System.err.println("Error deleting old image file: " + e.getMessage());
                }

                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.err.println("Error saving new image file: " + e.getMessage());
                }

                kniha.setImageFileName(storageFileName); // Set new file name to product
            }

            // Update other fields
            kniha.setNazov(productDto.getNazov());
            kniha.setAutor(productDto.getAutor());
            kniha.setJeVypozicana(productDto.getJeVypozicanaDto());

            // Save updated product
            repo.save(kniha);

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            return "knihy/edit"; // If an error occurs, stay on the edit page
        }

        return "redirect:/knihy";
    }

    @GetMapping("/delete")
   public String deleteProduct(@RequestParam long id){
        try {
           Kniha product = repo.findById(id).get();
           Path imagePath = Paths.get("public/images/" + product.getImageFileName());
           try {
               Files.delete(imagePath);

           } catch (Exception e){
                System.out.println("Exception" +  e.getMessage());
            }
           repo.delete(product);

        }catch (Exception e){
           System.out.println("Exception" +  e.getMessage());
      }


       return "redirect:/knihy";
   }

}
