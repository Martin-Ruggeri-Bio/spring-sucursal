package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,String> {
    List<Sale> findByClient_UserName(String userName);
}