package com.example.kniznica.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class KnihaDto {
    @NotEmpty(message = "This field is required")
    private String nazov,autor ;
    private MultipartFile imageFile;

    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "ANO/NIE")
    private StavVypozicky jeVypozicanaDto = StavVypozicky.NIE;



}
