package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.um.programacion2.simple.model.ReporteHistorico;
import ar.edu.um.programacion2.simple.repository.ReporteHistoricoRepository;


@Service
public class ReporteHistoricoService {
    @Autowired
	private ReporteHistoricoRepository repository;

	public ReporteHistorico add(ReporteHistorico reporteHistorico) {
		return repository.save(reporteHistorico);
	}
}
