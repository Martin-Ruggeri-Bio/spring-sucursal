/**
 * 
 */
package ar.edu.um.programacion2.simple.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import ar.edu.um.programacion2.simple.model.Menu;

/**
 * @author Martin
 *
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

	public Optional<Menu> findByMenuId(Integer menuId);

	@Modifying
	public void deleteByMenuId(Integer menuId);

}
