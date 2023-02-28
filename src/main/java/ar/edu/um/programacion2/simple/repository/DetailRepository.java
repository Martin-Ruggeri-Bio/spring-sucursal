package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface DetailRepository extends JpaRepository<Detail, String> {
    List<Detail> findByVentaId(String saleId);
    List<Detail> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
