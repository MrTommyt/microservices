package co.edu.unimagdalena.cbenavides.gateway.configuration;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(authorize -> authorize
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/user/**").hasRole("USER")
              .anyRequest().authenticated())
          .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }
}
