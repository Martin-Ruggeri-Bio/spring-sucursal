package ar.edu.um.programacion2.simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ar.edu.um.programacion2.simple.exception.MenuNotFoundException;
import ar.edu.um.programacion2.simple.dtos.Consulta;
import ar.edu.um.programacion2.simple.dtos.Message;
import ar.edu.um.programacion2.simple.model.Menu;
import ar.edu.um.programacion2.simple.model.ReporteHistorico;
import ar.edu.um.programacion2.simple.model.ReporteRecurrente;
import ar.edu.um.programacion2.simple.dtos.Reporte;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ConsultaService {
    @Value("${logginSucursal.user}")
    private String user;
    @Value("${logginSucursal.pass}")
    private String pass;
    @Value("${logginSucursal.id_tocken}")
    private String id_tocken_sucursal;
    @Value("${logginSucursal.consultaJsonHead}")
    private String consultaJsonHead;
	@Autowired
	private MenuService menuService;
    @Autowired
	private ReporteRecurrenteService reporteRecurrenteService;

    /**
     * @author Martin
     * Funcion de web client que consulta a la sece central,
     * una llamada POST a la API especificada en API_URL, sobre si hay alguna novedad para sincronizarse
     * si hay menus nuevos el servidor le contesta con una lista de menus, o vacio
     */
    @Transactional
    public Consulta consulta()  {
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
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken_sucursal + "\"")
            .body(BodyInserters.fromValue(this.consultaJsonHead))
            .retrieve()
            .bodyToMono(Consulta.class);
        
        return consulta.block();

    }

    @Transactional
    public void enviar_reporte_historico(ReporteHistorico reporteHistorico)  {
        WebClient webClient = WebClient
            .builder()
            .baseUrl("http://localhost:8095/Reporte/Historico")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        // Realiza la llamada POST a la API del servicio de reporte y almacena el resultado en un Mono de tipo Message
        Mono<Message> response = webClient
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(reporteHistorico))
            .retrieve()
            .bodyToMono(Message.class);
        
        response.subscribe(
                message -> System.out.println("Respuesta del servicio de reporte: " + message.getInfoMessage()),
                error -> System.err.println("Error al llamar al servicio de reporte: " + error.getMessage())
            );
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
                System.out.println("El menu no existe en la base de datos, se agrega");
                this.menuService.add(menu);
                try {
                    //consulto si ese menu existe en la base de datos
                    Menu menu_guardado = this.menuService.findById(menu.getId());
                    System.out.println(menu_guardado);
                } catch (MenuNotFoundException e2) {
                    System.out.println("El menu no  se pudo guardar en la base de datos");
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error al consultar el menú en la base de datos.");
            }
        }
        System.out.println("Todos los menus se guardaron");
	}

    public void enviarReportes(Reporte reporte){
        //recorro los menus
        System.out.println("Evaluando reporte");
        if(reporte.getTipo().equals("historico")){
            System.out.println("Hay reporte historico.");
            ReporteHistorico reporteHistorico = new ReporteHistorico(
                reporte.getId(),
                reporte.getTipo(),
                reporte.getFechaInicio(),
                reporte.getFechaFin()
            );
            this.enviar_reporte_historico(reporteHistorico);
        }else if(reporte.getTipo().equals("recurrente")){
            System.out.println("Hay reporte recurrente.");
            ReporteRecurrente reporteRecurrente = new ReporteRecurrente(
                reporte.getId(),
                reporte.getTipo(),
                reporte.getFechaInicio(),
                reporte.getFechaFin(),
                reporte.getIntervalo()
            );
            this.reporteRecurrenteService.add(reporteRecurrente);
        }else{
            System.out.println("No hay reportes.");
        }		
	}

    /**
     * @author Martin
     * Funcion que se ejecuta periodicamente para checkear si hay nueva informacion en la sede central
     * si asi fuera disparar la accion de verificar el contenido
     */
    @Scheduled(cron = "${cron.expression}")
    public void check(){
        try {
            Consulta consulta = this.consulta();
            // Funcion que verifica si la accion que devuelve la consulta es menu
            // si es, sicroniza los menus nuevos con los menus que se encuentran en la base de datos
            if(consulta.getAccion().equals("menu")){
                System.out.println("Hay menus nuevos.");
                List<Menu> menus = consulta.getMenus();
                System.out.println(menus);
                this.sincronizarMenus(menus);
            }else if (consulta.getAccion().equals("reporte")){
                System.out.println("Hay reportes nuevos.");
                Reporte reporte = consulta.getReporte();
                this.enviarReportes(reporte);
            }else{
                System.out.println("No hay nada nuevo.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
