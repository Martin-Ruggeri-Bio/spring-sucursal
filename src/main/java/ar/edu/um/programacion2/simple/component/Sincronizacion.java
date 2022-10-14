package ar.edu.um.programacion2.simple.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import ar.edu.um.programacion2.simple.model.Menu;
import reactor.core.publisher.Flux;
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
    
    WebClient webClient = WebClient
        .builder()
        .baseUrl("http://10.101.102.1:8080/api/accion")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
    Flux<Menu> response = webClient
        .post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.id_tocken + "\"")
        .body(BodyInserters.fromValue(accion_consulta))
        .retrieve()
        .bodyToFlux(Menu.class);
    return response.toStream().toList();
}
