package ar.edu.um.programacion2.simple.dtos;

import ar.edu.um.programacion2.simple.model.Sale;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    private List<Sale> sales;
}
