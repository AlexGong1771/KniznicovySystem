package com.example.kniznica.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "historia")
public class Historia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cisloOP")
    private Citatel citatel;

    @ManyToOne
    @JoinColumn(name = "kniha_id")
    private Kniha kniha;

    @Column(name = "data_vypozicania", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date borrowDate;

    @Column(name = "data_vracania", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;

    @Column(name = "aktualne vratenie")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualReturnDate;

}
