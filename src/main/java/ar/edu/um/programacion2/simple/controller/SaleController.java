package ar.edu.um.programacion2.simple.controller;

import ar.edu.um.programacion2.simple.dtos.DateRange;
import ar.edu.um.programacion2.simple.dtos.Message;
import ar.edu.um.programacion2.simple.model.Sale;
import ar.edu.um.programacion2.simple.model.User;
import ar.edu.um.programacion2.simple.service.SaleService;
import ar.edu.um.programacion2.simple.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
	private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAll(@RequestHeader(value="Authorization", required=true) String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(this.saleService.getSalesAll(), HttpStatus.OK);
    }

    @GetMapping("/date-between")
    public ResponseEntity<List<Sale>> findByDateBetween(@RequestHeader(value="Authorization", required=true) String tokenHeader,
                                                        @Valid @RequestBody DateRange dateRange, BindingResult bindingResult) {
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(this.saleService.findByDateBetween(dateRange.getFechaInicio(), dateRange.getFechaFin()), HttpStatus.OK);
    }


    // ventas realizadas a un cliente especifico
    @GetMapping("/client")
    public ResponseEntity<List<Sale>> getByClient(@RequestHeader(value="Authorization", required=true) String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        return new ResponseEntity<>(this.saleService.getSalesByClient(clientId), HttpStatus.OK);
    }
    @PostMapping(path = "/create")
    public ResponseEntity<Message> createSale(@RequestHeader(value="Authorization", required=true) String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        this.saleService.createSale(clientId);
        return new ResponseEntity<>(new Message("Compra exitosa"), HttpStatus.OK);
    }
}
