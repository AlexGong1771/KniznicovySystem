package com.example.kniznica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kniha")
@Getter
@Setter
public class Kniha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty()
    private String nazov;
    @NotEmpty()
    private String autor;
    @NotEmpty()
    private String imageFileName;


    @Enumerated(EnumType.STRING)
    @NotEmpty()
    private StavVypozicky jeVypozicana = StavVypozicky.NIE;




}
