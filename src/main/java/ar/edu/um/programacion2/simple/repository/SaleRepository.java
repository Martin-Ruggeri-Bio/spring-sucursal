package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,String> {
    List<Sale> findByClient_Id(String clientId);
    List<Sale> findByDateBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
