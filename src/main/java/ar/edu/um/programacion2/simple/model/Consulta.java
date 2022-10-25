package ar.edu.um.programacion2.simple.model;

import java.util.List;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Martin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    @Id
	private Integer consultaId;
    private String accion;
    private List<Menu> menus;

}
