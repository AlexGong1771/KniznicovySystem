package com.example.kniznica.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class KnihaDto {

    @NotEmpty(message = "This field is required")
    private String nazov;

    @NotEmpty(message = "This field is required")
    private String autor;

    @NotEmpty(message = "Image file is required")
    private MultipartFile imageFile;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "ANO/NIE is required")
    private StavVypozicky jeVypozicanaDto = StavVypozicky.NIE;
}
