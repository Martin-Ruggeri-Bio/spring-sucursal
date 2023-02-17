package ar.edu.um.programacion2.simple.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
// import java.util.List;

import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private Double total;

    @NotNull
    @Column(columnDefinition = "DATE")
    private Date date;

    // @OneToMany(mappedBy = "sale",  cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Detail> detalleVenta;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private User client;

    public Sale(double total, Date date, User client) {
        this.total = total;
        this.date = date;
        this.client = client;
    }
}
