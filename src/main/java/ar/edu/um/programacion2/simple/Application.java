package ar.edu.um.programacion2.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.service.MenuService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private MenuService menuService;

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
		
	}
}


/* "Super Pancho", "Pancho con jamon, queso, palta y lluvia de papas", 750.00,
			"https://cdn.cienradios.com/wp-content/uploads/sites/13/2020/04/Panchos-argentinos-nota.jpg",
			true, "2022-07-31T12:00:00Z", "2022-07-31T12:00:00Z" */