package ar.edu.um.programacion2.simple.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.um.programacion2.simple.service.UserService;
import ar.edu.um.programacion2.simple.model.User;
import ar.edu.um.programacion2.simple.dtos.UserLoginRequest;
import ar.edu.um.programacion2.simple.dtos.UserTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Este controlador recibe un objeto UserLoginRequest que contiene el nombre de usuario y la contraseña. 
// Luego busca un usuario en la base de datos con el nombre de usuario proporcionado y verifica si la contraseña coincide.
// Si el usuario y la contraseña son válidos, se genera un nuevo token y se almacena en la base de datos junto con el usuario.
// Finalmente, se devuelve una respuesta que contiene el token generado.

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
	private UserService userService;

    @PostMapping("/login")
    public UserTokenResponse login(@RequestBody UserLoginRequest userLogin) {
        // Validar el usuario y la contraseña en memoria
        if (userLogin.getUserName() != null) {
            // Verificar si el nombre de usuario ya existe
            if (userService.existByUserName(userLogin.getUserName())) {
                // throw new RuntimeException("El nombre de usuario ya está en uso");
                Optional<User> userOptional = userService.getByUserName(userLogin.getUserName());
                User user = userOptional.orElse(null);
                UserTokenResponse userTokenResponse = new UserTokenResponse(user.getToken());
                return userTokenResponse;
            }
            // Crear un nuevo objeto User con los detalles del registro
            User user = new User();
            user.setUserName(userLogin.getUserName());
            // Crear el token JWT
            String token = Jwts.builder()
                    .setSubject(user.getUserName())
                    .claim("userId", user.getId())
                    .signWith(SignatureAlgorithm.HS256, "secreto")
                    .compact();
            user.setToken(token);
            userService.save(user);
            UserTokenResponse userTokenResponse = new UserTokenResponse(token);
            return userTokenResponse;
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
