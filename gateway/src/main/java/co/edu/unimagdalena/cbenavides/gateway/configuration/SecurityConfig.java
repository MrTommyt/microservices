package co.edu.unimagdalena.cbenavides.gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    // 🔐 Cadena de seguridad para rutas protegidas (/api/**, /admin/**)
    @Bean
    public SecurityWebFilterChain protectedSecurityFilterChain(ServerHttpSecurity http) {
        log.info("🔐 Cargando SecurityWebFilterChain protegida");
        return http
            .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**", "/admin/**"))
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/api/**").authenticated()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
            )
            .build();
    }

    // 🆓 Cadena de seguridad para rutas públicas (/user/**)
    @Bean
    public SecurityWebFilterChain publicSecurityFilterChain(ServerHttpSecurity http) {
        log.info("🆓 Cargando SecurityWebFilterChain pública");
        return http
            .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/user/**"))
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll()
            )
            .build();
    }

    @Bean
    public SecurityWebFilterChain fallbackSecurityFilterChain(ServerHttpSecurity http) {
        log.info("🔁 Cargando fallback SecurityWebFilterChain para rutas no manejadas");
        return http
            .securityMatcher(ServerWebExchangeMatchers.anyExchange())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(ex -> ex.anyExchange().permitAll())
            .build();
    }

    // 🔁 Convertidor de roles de Keycloak
    private ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesExtractor() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        delegate.setAuthorityPrefix("ROLE_");
        delegate.setAuthoritiesClaimName("realm_access.roles");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(delegate);

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
