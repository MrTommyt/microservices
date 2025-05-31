package co.edu.unimagdalena.cbenavides.gateway.configuration;

import co.edu.unimagdalena.cbenavides.gateway.filters.CachingFilter;
import co.edu.unimagdalena.cbenavides.gateway.filters.CorrelationIdGatewayFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {
    private final CorrelationIdGatewayFilterFactory correlationIdGatewayFilterFactory;
    private final CachingFilter cachingFilter;

    public RoutesConfig(CorrelationIdGatewayFilterFactory correlationIdGatewayFilterFactory, CachingFilter cachingFilter) {
        this.correlationIdGatewayFilterFactory = correlationIdGatewayFilterFactory;
        this.cachingFilter = cachingFilter;
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("payment_route", r ->
                r.path("/api/v1/payment/**").filters(f -> f
                        .filter(correlationIdGatewayFilterFactory.apply(new CorrelationIdGatewayFilterFactory.Config())))
                    .uri("lb://payment-service"))
            .route("inventory_route", r ->
                r.path("/api/v1/inventory/**").filters(f -> f
                        .filter(correlationIdGatewayFilterFactory.apply(new CorrelationIdGatewayFilterFactory.Config())))
                    .uri("lb://inventory-service"))
            .route("product_route", r ->
                r.path("/api/v1/product/**").filters(f -> f
                        .filter(correlationIdGatewayFilterFactory.apply(new CorrelationIdGatewayFilterFactory.Config()))
                        .filter(cachingFilter))
                    .uri("lb://product-service"))
            .route("order_route", r ->
                r.path("/api/v1/order/**").filters(f -> f
                        .filter(correlationIdGatewayFilterFactory.apply(new CorrelationIdGatewayFilterFactory.Config())))
                    .uri("lb://order-service"))
            .build();
    }
}
