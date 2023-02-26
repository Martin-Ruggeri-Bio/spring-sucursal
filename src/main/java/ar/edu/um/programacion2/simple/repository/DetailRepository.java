package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, String> {
    List<Detail> findBySale_Id(String saleId);
    List<Detail> findByClient_Id(String clientId);
}
