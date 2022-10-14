package ar.edu.um.programacion2.simple.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ar.edu.um.programacion2.simple.exception.MenuNotFoundException;
import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.service.MenuService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Martin
 *
 */
@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {

	@Autowired
	private MenuService service;

	@GetMapping("/")
	public ResponseEntity<List<Menu>> findAll() {
		return new ResponseEntity<List<Menu>>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Menu> findById(@PathVariable Long id) {
		try {
			return new ResponseEntity<Menu>(service.findById(id), HttpStatus.OK);
		} catch (MenuNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/")
	public ResponseEntity<Menu> add(@RequestBody Menu menu) {
		return new ResponseEntity<Menu>(service.add(menu), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
		log.debug("Eliminando cliente {}", id);
		return new ResponseEntity<Boolean>(service.deleteById(id), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Menu> update(@RequestBody Menu menu, @PathVariable Long id) {
		return new ResponseEntity<Menu>(service.update(menu, id), HttpStatus.OK);
	}

}
