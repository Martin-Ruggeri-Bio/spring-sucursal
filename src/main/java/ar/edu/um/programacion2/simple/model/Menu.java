package ar.edu.um.programacion2.simple.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Martin
 *
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
	@Id
	private Integer menuId;
	private String nombre;
	private String descripcion;
	private Float precio;
	private String urlImagen;
	private Boolean activo;
	private String creado;
	private String actualizado;
}
