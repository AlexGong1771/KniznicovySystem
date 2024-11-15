package com.example.kniznica.repositar;

import com.example.kniznica.entity.Kniha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnihaRepo extends JpaRepository<Kniha , Long> {
}
