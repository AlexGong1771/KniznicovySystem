package com.example.kniznica.repositar;



import com.example.kniznica.entity.Citatel;
import com.example.kniznica.entity.Historia;
import com.example.kniznica.entity.Kniha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepo extends JpaRepository<Historia, Long> {

    List<Historia> findByCitatel(Optional<Citatel> citatel);

    List<Historia> findByKniha(Kniha kniha);

    List<Historia> findByCitatelAndKniha(Citatel citatel, Kniha kniha);


}
