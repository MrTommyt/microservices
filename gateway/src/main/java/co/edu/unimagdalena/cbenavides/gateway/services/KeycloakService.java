package co.edu.unimagdalena.cbenavides.gateway.services;

import co.edu.unimagdalena.cbenavides.gateway.dto.KeycloakUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KeycloakService {

    private final KeycloakAdminTokenService adminTokenService;
    private final WebClient webClient;
    private final String realm;
    private final String clientId;
    private final String clientSecret;

    public KeycloakService(
        KeycloakAdminTokenService adminTokenService,
        WebClient.Builder webClientBuilder,
        @Value("${keycloak.auth-server-url}") String keycloakUrl,
        @Value("${keycloak.realm}") String realm,
        @Value("${keycloak.client-id}") String clientId,
        @Value("${keycloak.client-secret}") String clientSecret) {
        this.adminTokenService = adminTokenService;
        this.webClient = webClientBuilder.baseUrl(keycloakUrl).build();
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Mono<String> login(String username, String password) {
        return adminTokenService.getAdminToken()
            .flatMap(adminToken -> webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                    .fromFormData("grant_type", "password")
                    .with("client_id", clientId)
                    .with("client_secret", clientSecret)
                    .with("username", username)
                    .with("password", password))
                .retrieve()
                .bodyToMono(String.class));
    }

    public Mono<String> registerUser(KeycloakUserDTO userDTO) {
        return adminTokenService.getAdminToken()
            .flatMap(adminToken -> webClient.post()
                .uri("/admin/realms/{realm}/users", realm)
                .headers(headers -> headers.setBearerAuth(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .onStatus(HttpStatus.CONFLICT::equals, response -> {
                    log.info("User with username {} already exists", userDTO.getUsername());
                    return Mono.error(new RuntimeException("User with username " + userDTO.getUsername() + " already exists"));
                })
                .bodyToMono(String.class)
                .log()
            ).log();
    }
}
