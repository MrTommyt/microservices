package co.edu.unimagdalena.cbenavides.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class KeycloakAdminTokenService {

    private final AtomicReference<String> adminToken = new AtomicReference<>();
    private Instant lastChecked = Instant.now();
    private final long checkInterval = 1000 * 60 * 5;
    private final String tokenUri;
    private final String clientId;
    private final String secret;
    private final WebClient webClient;

    public KeycloakAdminTokenService(
        @Value("${keycloak.auth-server-url}") String authServerUrl,
        @Value("${keycloak.token-uri}") String tokenUri,
        @Value("${keycloak.client-id}") String clientId,
        @Value("${keycloak.username}") String username,
        @Value("${keycloak.password}") String password,
        @Value("${keycloak.client-secret}") String secret
    ) {
        this.tokenUri = tokenUri;
        this.clientId = clientId;
//        this.username = username;
//        this.password = password;
        this.secret = secret;
        this.webClient = WebClient.create(authServerUrl);
    }

    public Mono<String> getAdminToken() {
        if (adminToken.get() != null && lastChecked.plusMillis(checkInterval).isAfter(Instant.now())) {
            return Mono.just(adminToken.get());
        }

        return webClient.post()
            .uri(tokenUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .bodyValue("grant_type=password&client_id=" + clientId +
//                       "&username=" + username +
//                       "&password=" + password)
            .body(BodyInserters
                    .fromFormData("grant_type", "client_credentials")
                    .with("client_id", clientId)
                    .with("client_secret", secret)
//                    .with("username", username)
//                    .with("password", password)
            )
//            .bodyValue("grant_type=client_credentials" +
//                       "&client_id=" + clientId +
//                       "&client_secret=" + secret
//            )
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                String token = (String) response.get("access_token");
                adminToken.set(token);
                lastChecked = Instant.now();
                log.info("✅ Admin token retrieved successfully");
                return token;
            })
            .doOnError(error -> log.error("❌ Failed to retrieve admin token: {}", error.getMessage()));
    }
}
