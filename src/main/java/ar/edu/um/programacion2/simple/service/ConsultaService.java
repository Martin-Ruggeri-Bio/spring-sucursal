package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ar.edu.um.programacion2.simple.exception.MenuNotFoundException;
import ar.edu.um.programacion2.simple.model.Consulta;
import ar.edu.um.programacion2.simple.model.Menu;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ConsultaService {
    @Value("${environments.loggin.user}")
    private String user;
    @Value("${environments.loggin.pass}")
    private String pass;
    @Value("${environments.loggin.id_tocken}")
    private String id_tocken;
    private String jsonHead = "{\"accion\":\"consulta\",\"franquiciaID\":\"de0319e4-bde6-4898-8303-1307a3b9be56\"}";
	@Autowired
	private MenuService menuService;

    /**
     * @author Martin
     * Funcion de web client que consulta a la sece central,
     * una llamada POST a la API especificada en API_URL, sobre si hay alguna novedad para sincronizarse
     * si hay menus nuevos el servidor le contesta con una lista de menus, o vacio
     */
    public Consulta consultaMenus()  {
        WebClient webClient = WebClient
            .builder()
            .baseUrl("http://10.101.102.1:8080/api/accion")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        // Realiza la llamada POST a la API y almacena el resultado en un Mono de tipo Consulta
        Mono<Consulta> consulta = webClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken + "\"")
            .body(BodyInserters.fromValue(this.jsonHead))
            .retrieve()
            .bodyToMono(Consulta.class);
        
        return consulta.block();

    }

    /**
     * @author Martin
     * Funcion que actua sobre la base de datos de menus segun la comparacion de ambos contenidos
     */
	public void sincronizarMenus(List<Menu> menus){
        //recorro los menus
        System.out.println("sincronizando menus");
        for(Menu menu : menus) {
            try {
                //consulto si ese menu existe en la base de datos
                Menu menu_guardado = this.menuService.findById(menu.getId());
                if (menu.equals(menu_guardado)) {
                    System.out.println("Los menus son iguales, no se actualiza");
                } else {
                    System.out.println("Los menus no son iguales, si se actualiza");
                    this.menuService.update(menu, menu.getId());
                }
            } catch (MenuNotFoundException e) {
                System.out.println(e);
                System.out.println("El menu no existe en la base de datos, se agrega");
                this.menuService.add(menu);
            }
        }		
	}

    /**
     * @author Martin
     * Funcion que se ejecuta periodicamente para checkear si hay nueva informacion en la sede central
     * si asi fuera disparar la accion de verificar el contenido
     */
    @Scheduled(cron = "${environments.cron.expression}")
    public void check(){
        try {
            Consulta consulta = this.consultaMenus();
            // Funcion que verifica si la accion que devuelve la consulta es menu
            // si es, sicroniza los menus nuevos con los menus que se encuentran en la base de datos
            if(consulta.getAccion().equals("menu")){
                System.out.println("Hay menus nuevos.");
                List<Menu> menus = consulta.getMenus();
                System.out.println(menus);
                this.sincronizarMenus(menus);
            }else {
                System.out.println("No hay menus nuevos.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
