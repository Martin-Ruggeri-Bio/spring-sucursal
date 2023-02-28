package ar.edu.um.programacion2.simple.controller;

import ar.edu.um.programacion2.simple.dtos.DateRange;
import ar.edu.um.programacion2.simple.dtos.Details;
import ar.edu.um.programacion2.simple.model.Detail;
import ar.edu.um.programacion2.simple.model.User;
import ar.edu.um.programacion2.simple.service.DetailService;
import ar.edu.um.programacion2.simple.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/saleDetail")
public class DetailController {

    @Autowired
    private DetailService detailService;

    @Autowired
	private UserService userService;

    @GetMapping("/client")
    public ResponseEntity<List<Detail>> getDetailsBySale(@RequestHeader(value="Authorization", required=true) String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(this.detailService.getDetailsAll(), HttpStatus.OK);
    }

    // SaleControler
    @PostMapping("/date-between")
    public ResponseEntity<Details> findByDateBetween(@RequestHeader(value="Authorization", required=true) String tokenHeader,
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
        Details details = new Details(this.detailService.findByDateBetween(dateRange.getFechaInicio(), dateRange.getFechaFin()));
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
