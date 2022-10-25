package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import ar.edu.um.programacion2.simple.model.Consulta;
import ar.edu.um.programacion2.simple.model.Menu;
import reactor.core.publisher.Mono;

@Service
public class ConsultaService {
    @Value("${environments.loggin.user}")
    private String user;
    @Value("${environments.loggin.pass}")
    private String pass;
    @Value("${environments.loggin.id_tocken}")
    private String id_tocken;
    @Value("${environments.loggin.id_franquicia}")
    private String id_franquicia;
    private String accion_consulta = "{\"accion\": \"consulta\", \"franquiciaID\": \"" + id_franquicia + "\"}";
    @Autowired
	private MenuService menuService;
    
    /**
     * @author Martin
     * Funcion de web client que consulta a la sece central sobre si hay alguna novedad para sincronizarse
     * devuelve un objeto ¿de tipo consulta o de tipo mono?
     * la funcion block en teoria sirve para bloquear el proceso pra extraer el contenido, !preguntar!!!
     */
    public Consulta sincronizacioConsulta() {
        WebClient webClient = WebClient
            .builder()
            .baseUrl("http://10.101.102.1:8080/api/accion")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        Mono<Consulta> consulta = webClient
            .post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken + "\"")
            .body(BodyInserters.fromValue(accion_consulta))
            .retrieve()
            .bodyToMono(Consulta.class);
        return consulta.block();
    }

    /**
     * @author Martin
     * Funcion que verifica si la accion que devuelve la consulta es menu
     * si es, sicroniza los menus nuevos con los menus que se encuentran en la base de datos
     */
	public void verificar_accion(Consulta consulta){
        if(consulta.getAccion() == "menu"){
            this.sincronizar_menus(consulta);
        }		
	}

    /**
     * @author Martin
     * Funcion que actua sobre la base de datos de menus segun la comparacion de ambos contenidos
     */
	public void sincronizar_menus(Consulta consulta){
        //recorro los menus
        for(Menu menu : consulta.getMenus()) {
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
        Consulta consulta = this.sincronizacioConsulta();
        this.verificar_accion(consulta);
    }
}
