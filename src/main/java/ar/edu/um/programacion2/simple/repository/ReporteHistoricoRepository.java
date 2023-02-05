package ar.edu.um.programacion2.simple.repository;

import ar.edu.um.programacion2.simple.model.ReporteHistorico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReporteHistoricoRepository extends JpaRepository<ReporteHistorico, Long>{
    public Optional<ReporteHistorico> findById(Long id);
}
