package ar.edu.um.programacion2.simple.controller;

import ar.edu.um.programacion2.simple.dtos.Menus;
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

        String clientId = user.getId();
        return new ResponseEntity<>(this.shoppingCartService.getListByClient(clientId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByClient(@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.replace("Bearer ", "");
        Optional<User> userOptional = userService.getByToken(token);
        User user = userOptional.orElse(null);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String clientId = user.getId();
        return new ResponseEntity<>(this.shoppingCartService.getCountByClient(clientId),HttpStatus.OK);
    }

    @PostMapping("/addMenu")
    public ResponseEntity<Message> addMenu(@RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody Menus menus, BindingResult bindingResult){
            String token = tokenHeader.replace("Bearer ", "");
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<User> userOptional = userService.getByToken(token);
            User user = userOptional.orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
            ShoppingCart shoppingCartOld = this.shoppingCartService.getByClientAndMenu(user.getId(), menus.getMenu().getId());
            if (shoppingCartOld != null) {
                // si el menu ya esta en carrito del cliente aumenta su cantidad
                ShoppingCart shoppingCartNew = new ShoppingCart();
                shoppingCartNew.setMenu(menus.getMenu());
                shoppingCartNew.setClient(user);
                shoppingCartNew.setAmount(shoppingCartOld.getAmount() + menus.getAmount());
                this.shoppingCartService.updateProduct(shoppingCartOld.getId(), shoppingCartNew);
            } else {
                //sino lo agrega
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setMenu(menus.getMenu());
                shoppingCart.setClient(user);
                shoppingCart.setAmount(menus.getAmount());
                this.shoppingCartService.addProduct(shoppingCart);
            }
            return new ResponseEntity<>(new Message("Producto agregado"),HttpStatus.OK);
    }


    @DeleteMapping("/clean/{item_id}")
    public ResponseEntity<Message> removeProduct(@PathVariable("item_id")String id){
        this.shoppingCartService.removeProduct(id);
        return new ResponseEntity<>(new Message("Eliminado"),HttpStatus.OK);
    }

    @PutMapping("/update/{item_id}")
    public ResponseEntity<Message> updateProduct(@PathVariable("item_id")String id, @RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody Menus menus, BindingResult bindingResult){
            String token = tokenHeader.replace("Bearer ", "");
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<User> userOptional = userService.getByToken(token);
            User user = userOptional.orElse(null);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (bindingResult.hasErrors())
                return new ResponseEntity<>(new Message("Revise los campos"),HttpStatus.BAD_REQUEST);
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setMenu(menus.getMenu());
            shoppingCart.setClient(user);
            shoppingCart.setAmount(menus.getAmount());
            this.shoppingCartService.updateProduct(id, shoppingCart);
            return new ResponseEntity<>(new Message("Actualizado correctamente"),HttpStatus.OK);
    }
}
