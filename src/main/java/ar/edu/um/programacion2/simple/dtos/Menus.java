package ar.edu.um.programacion2.simple.dtos;
import ar.edu.um.programacion2.simple.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menus {
    private Menu menu;
    private int amount;
}
