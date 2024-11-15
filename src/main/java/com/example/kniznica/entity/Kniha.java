package com.example.kniznica.entity;

import jakarta.persistence.*;
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

    private String nazov;
    private String autor;
    private String imageFileName;


    @Enumerated(EnumType.STRING)
    private StavVypozicky jeVypozicana = StavVypozicky.NIE;




}
