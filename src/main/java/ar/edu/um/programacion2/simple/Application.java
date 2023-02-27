package ar.edu.um.programacion2.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import ar.edu.um.programacion2.simple.model.User;
import ar.edu.um.programacion2.simple.service.UserService;
import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.service.MenuService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserService userService;

	@Override
	public void run(String... args) throws Exception {
		Menu menu = new Menu(
			1,
			"Super Pancho",
			"Pancho con jamon, queso, palta y lluvia de papas",
			750.00F,
			"https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg",
			true,
			"2022-07-31T12:00:00Z",
			"2022-07-31T12:00:00Z"
		);
		System.out.println("este es el menu por default");
		System.out.println(menu);
		menu = menuService.add(menu);

		User user = new User();
		user.setUserName("ServicioReporte");
		String token = Jwts.builder()
                    .setSubject(user.getUserName())
                    .claim("userId", user.getId())
                    .signWith(SignatureAlgorithm.HS256, "secreto")
                    .compact();
		user.setToken(token);
		userService.save(user);
	}
}
