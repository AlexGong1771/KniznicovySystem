package com.example.kniznica.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Getter
@Setter
@Table(name = "citatel")
public class Citatel {
        @Id
        @Min(8)

        private String cisloOP;


        @NotEmpty()
        private String meno;
        @NotEmpty()
        private String priezvisko;
        @NotEmpty()
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date datumNarodenia;

    }


