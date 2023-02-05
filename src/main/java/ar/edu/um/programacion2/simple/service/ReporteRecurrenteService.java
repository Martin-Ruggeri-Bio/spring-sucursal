package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.um.programacion2.simple.model.ReporteRecurrente;
import ar.edu.um.programacion2.simple.repository.ReporteRecurrenteRepository;

@Service
public class ReporteRecurrenteService {
    @Autowired
	private ReporteRecurrenteRepository repository;

	public ReporteRecurrente add(ReporteRecurrente reporteRecurrente) {
		return repository.save(reporteRecurrente);
	}
}
