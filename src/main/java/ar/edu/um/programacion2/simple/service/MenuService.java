package ar.edu.um.programacion2.simple.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import ar.edu.um.programacion2.simple.exception.MenuNotFoundException;
import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.repository.MenuRepository;


/**
 * @author Martin
 *
 */
@Service
public class MenuService {

	@Value("${environments.loggin.user}")
    private String user;
    @Value("${environments.loggin.pass}")
    private String pass;
    @Value("${environments.loggin.id_tocken}")
    private String id_tocken;
    @Value("${environments.loggin.franquiciaId}")
    private String franquiciaId;
	
	@Autowired
	private MenuRepository repository;

	public Menu add(Menu menu) {
		return repository.save(menu);
	}

	public List<Menu> findAll() {
		return repository.findAll();
	}

	public Menu findByMenuId(Integer menuId) {
		return repository.findByMenuId(menuId).orElseThrow(() -> new MenuNotFoundException(menuId));
	}

	@Transactional
	public Boolean deleteByMenuId(Integer menuId) {
		if (repository.findByMenuId(menuId).isEmpty()) {
			return false;
		}
		repository.deleteByMenuId(menuId);
		return !repository.findByMenuId(menuId).isPresent();
	}

	public Menu update(Menu newMenu, Integer menuId) {
		return repository.findByMenuId(menuId).map(menu -> {
			menu = new Menu(
				menuId,
				newMenu.getNombre(),
				newMenu.getDescripcion(),
				newMenu.getPrecio(),
				newMenu.getUrlImagen(),
				newMenu.getActivo(),
				newMenu.getCreado(),
				newMenu.getActualizado()
				);
			return repository.save(menu);
		}).orElseThrow(() -> new MenuNotFoundException(menuId));
	}

}

