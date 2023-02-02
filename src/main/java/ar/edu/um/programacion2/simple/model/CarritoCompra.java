package ar.edu.um.programacion2.simple.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.validation.constraints.NotNull;
/**
 * @author Martin
 *
 */
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
// agregamos los items de manera temporal los menus que usuario agregue a su carrito
public class CarritoCompra {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Menu menu;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User cliente;
    @NotNull
    private int amount;
}
