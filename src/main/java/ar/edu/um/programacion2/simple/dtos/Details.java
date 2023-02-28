package ar.edu.um.programacion2.simple.dtos;

import java.util.List;

import ar.edu.um.programacion2.simple.model.Detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    private List<Detail> detail;
}
