package com.example.kniznica.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
public class HistoriaDto {
private Long id;
        private String cisloOP;
        private Long knihaId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date borrowDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date returnDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date actualReturnDate;
}
