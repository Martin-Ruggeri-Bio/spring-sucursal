package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.ReporteRecurrente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReporteRecurrenteRepository extends JpaRepository<ReporteRecurrente, Long>{
    public Optional<ReporteRecurrente> findById(Long id);
}
