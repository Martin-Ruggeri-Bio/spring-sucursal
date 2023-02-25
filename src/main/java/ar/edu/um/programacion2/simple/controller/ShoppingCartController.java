package ar.edu.um.programacion2.simple.controller;

import ar.edu.um.programacion2.simple.dtos.Message;
import ar.edu.um.programacion2.simple.model.ShoppingCart;
import ar.edu.um.programacion2.simple.model.User;
import ar.edu.um.programacion2.simple.service.ShoppingCartService;
import ar.edu.um.programacion2.simple.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoppingList")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
	private UserService userService;

    @GetMapping()
    public ResponseEntity<List<ShoppingCart>> getListByClient(@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userName = user.getUserName();
        return new ResponseEntity<>(this.shoppingCartService.getListByClient(userName), HttpStatus.OK);
    }
    @GetMapping("/count/{client_id}")
    public ResponseEntity<Long> countByClient(@PathVariable("client_id")String id){
        return new ResponseEntity<>(this.shoppingCartService.getCountByClient(id),HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Message> addProduct(@Valid @RequestBody ShoppingCart shoppingCart,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
        this.shoppingCartService.addProduct(shoppingCart);
        return new ResponseEntity<>(new Message("Producto agregado"),HttpStatus.OK);
    }
    @DeleteMapping("/clean/{item_id}")
    public ResponseEntity<Message> removeProduct(@PathVariable("item_id")String id){
        this.shoppingCartService.removeProduct(id);
        return new ResponseEntity<>(new Message("Eliminado"),HttpStatus.OK);
    }

}
