package ar.edu.um.programacion2.simple.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import ar.edu.um.programacion2.simple.model.Menu;

/**
 * @author Martin
 * JpaRepository trae todas las operraciones basicas de un crud
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

	public Optional<Menu> findById(Integer id);

	@Modifying
	public void deleteById(Integer id);

}
