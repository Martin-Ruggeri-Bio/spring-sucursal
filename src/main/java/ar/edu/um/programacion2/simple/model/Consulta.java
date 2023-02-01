package ar.edu.um.programacion2.simple.model;

import java.util.List;

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
    private String accion;
    private List<Menu> menus;

}
