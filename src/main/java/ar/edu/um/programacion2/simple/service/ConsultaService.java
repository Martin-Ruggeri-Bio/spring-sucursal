package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import ar.edu.um.programacion2.simple.model.Consulta;
import ar.edu.um.programacion2.simple.model.Menu;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
     * Luego filtra la respuesta recibida para verificar que su acción sea "menu".
     * Finalmente, devuelve la lista de menús obtenida de la respuesta.
     */
    public Mono<List<Menu>> consultaMenus()  {
        WebClient webClient = WebClient
            .builder()
            .baseUrl("http://10.101.102.1:8080/api/accion")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        // Realiza la llamada POST a la API y almacena el resultado en un Mono de tipo Consulta
        Mono<Consulta> respuestaServidor = webClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken + "\"")
            .body(BodyInserters.fromValue(this.jsonHead))
            .retrieve()
            .bodyToMono(Consulta.class);
        
        // Filtra la respuesta recibida y solo retorna la lista de menús si la acción es "menu"
        return respuestaServidor.filter(rs -> rs.getAccion().equals("menu"))
            .map(Consulta::getMenus);
    }

    /**
     * @author Martin
     * Funcion que verifica si la accion que devuelve la consulta es menu
     * si es, sicroniza los menus nuevos con los menus que se encuentran en la base de datos
     */
    public boolean verificarMenus() {
        Mono<List<Menu>> menuMono = consultaMenus();
        if (menuMono.blockOptional().isPresent()) {
            System.out.println("Hay menus nuevos.");
            return true;
        } else {
            System.out.println("No hay menus nuevos.");
            return false;
        }
    }       

    public List<Menu> obtenerMenus() throws ExecutionException, InterruptedException {
        Mono<List<Menu>> menuList = consultaMenus();
        return menuList.block();
    }

    /**
     * @author Martin
     * Funcion que actua sobre la base de datos de menus segun la comparacion de ambos contenidos
     */
	public void sincronizar_menus(List<Menu> menus){
        //recorro los menus
        for(Menu menu : menus) {
            //consulto si ese menu existe en la base de datos
            Menu menu_guardado = menuService.findByMenuId(menu.getMenuId());
            if (menu_guardado == null) {
                //si no existe lo agrego
                menuService.add(menu);
            } else {
                // si existe comparo ambos menus
                if (menu_guardado != menu){
                    // si son distintos lo actualizo
                    menuService.update(menu, menu.getMenuId());
                }
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
            List<Menu> menus = this.obtenerMenus();
            boolean hayMenus = this.verificarMenus();
            if (hayMenus){
                for (Menu menu : menus) {
                    System.out.println(menu.getNombre());
                }
                this.sincronizar_menus(menus);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
