/**
 * 
 */
package ar.edu.um.programacion2.simple.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.um.programacion2.simple.exception.MenuNotFoundException;
import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.repository.MenuRepository;

/**
 * @author Martin
 *
 */
@Service
public class MenuService {

	@Autowired
	private MenuRepository repository;

	public Menu add(Menu menu) {
		return repository.save(menu);
	}

	public List<Menu> findAll() {
		return repository.findAll();
	}

	public Menu findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new MenuNotFoundException(id));
	}

	@Transactional
	public Boolean deleteById(Long id) {
		if (repository.findById(id).isEmpty()) {
			return false;
		}
		repository.deleteById(id);
		return !repository.findById(id).isPresent();
	}

	public Menu update(Menu newMenu, Long id) {
		return repository.findById(id).map(menu -> {
			menu = new Menu(
				id,
				newMenu.getNombre(),
				newMenu.getDescripcion(),
				newMenu.getPrecio(),
				newMenu.getUrlImagen(),
				newMenu.getActivo(),
				newMenu.getCreado(),
				newMenu.getActualizado()
				);
			return repository.save(menu);
		}).orElseThrow(() -> new MenuNotFoundException(id));
	}

}
