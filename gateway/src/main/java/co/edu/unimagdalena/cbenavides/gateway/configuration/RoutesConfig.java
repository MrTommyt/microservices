package co.edu.unimagdalena.cbenavides.gateway.configuration;

import co.edu.unimagdalena.cbenavides.gateway.filters.CorrelationIdFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {
    private final CorrelationIdFilter correlationIdFilter;

    public RoutesConfig(CorrelationIdFilter correlationIdFilter) {
        this.correlationIdFilter = correlationIdFilter;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("payment_route", r ->
                r.path("/api/v1/payment/**").filters(f -> f.filter(correlationIdFilter)).uri("lb://payment-service"))
            .route("inventory_route", r ->
                r.path("/api/v1/inventory/**").filters(f -> f.filter(correlationIdFilter)).uri("lb://inventory-service"))
            .route("product_route", r ->
                r.path("/api/v1/product/**").filters(f -> f.filter(correlationIdFilter)).uri("lb://product-service"))
            .route("order_route", r ->
                r.path("/api/v1/order/**").filters(f -> f.filter(correlationIdFilter)).uri("lb://order-service"))
            .build();
    }
}
