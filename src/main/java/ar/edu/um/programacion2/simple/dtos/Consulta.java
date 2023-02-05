package ar.edu.um.programacion2.simple.dtos;

import java.util.List;

import ar.edu.um.programacion2.simple.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
    private String accion;
    private List<Menu> menus;
    private Reporte reporte;
}
