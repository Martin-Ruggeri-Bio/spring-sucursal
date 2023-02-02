package ar.edu.um.programacion2.simple.model;

import java.util.Date;

import javax.persistence.*;

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
public class Venta {
    @Id
	private Integer id;
    @Column(columnDefinition = "DATE")
    private Date date;
    // a que usuario pertenece
    @ManyToOne(optional=false, cascade=CascadeType.DETACH, fetch=FetchType.EAGER)
    private User cliente;
}
