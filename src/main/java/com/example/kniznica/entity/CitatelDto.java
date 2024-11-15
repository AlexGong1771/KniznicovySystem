package com.example.kniznica.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CitatelDto {
    @NotEmpty(message = "This field is required")
    @Min(8)
    private String cisloOP;
    @NotEmpty(message = "This field is required")
    private String meno,priezvisko  ;
    @NotEmpty(message = "This field is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumNarodenia;



}
