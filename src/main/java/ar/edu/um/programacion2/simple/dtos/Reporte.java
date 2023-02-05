package ar.edu.um.programacion2.simple.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
	private Long id;
	private String tipo;
	private String fechaInicio;
    private String fechaFin;
    private String intervalo;

}
