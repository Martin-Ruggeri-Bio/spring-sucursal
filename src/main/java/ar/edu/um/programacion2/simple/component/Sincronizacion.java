/* package ar.edu.um.programacion2.simple.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import ar.edu.um.programacion2.simple.model.Consulta;
import ar.edu.um.programacion2.simple.service.ConsultaService;
import reactor.core.publisher.Mono;
import lombok.Data;

@Data
@Component
public class Sincronizacion {
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
	private ConsultaService consultaService;
    
    @Scheduled(cron = "${environments.cron.expression}")
	public Consulta check() {
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
        //System.out.println(consulta.block());
        //consultaService.verificar_accion(consulta.block());
        return consulta.block();
    } */
/* } */
/*     Flux<Menu> response = webClient
        .post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken + "\"")
        .body(BodyInserters.fromValue(accion_consulta))
        .retrieve()
        .bodyToFlux(Menu.class);
    return response.toStream().toList(); */