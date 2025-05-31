package co.edu.unimagdalena.cbenavides.gateway.controller;

import co.edu.unimagdalena.cbenavides.gateway.dto.KeycloakUserDTO;
import co.edu.unimagdalena.cbenavides.gateway.dto.LoginRequest;
import co.edu.unimagdalena.cbenavides.gateway.dto.RegistrationRequest;
import co.edu.unimagdalena.cbenavides.gateway.services.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserLoginRegisterController {

    private final KeycloakService keycloakService;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest loginRequest) {
        return keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword())
            .map(ResponseEntity::ok)
            .onErrorResume(throwable -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody KeycloakUserDTO registrationRequest) {
        return keycloakService.registerUser(registrationRequest)
            .map(success -> ResponseEntity.ok().<Void>build())
            .onErrorResume(throwable -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
