package co.edu.unimagdalena.cbenavides.gateway.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/product")
    public ResponseEntity<String> productFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de productos no está disponible en este momento.");
    }

    @GetMapping("/fallback/order")
    public ResponseEntity<String> orderFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de pedidos no está disponible temporalmente.");
    }

    @GetMapping("/fallback/inventory")
    public ResponseEntity<String> inventoryFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de inventario no está disponible temporalmente.");
    }

    @GetMapping("/fallback/payment")
    public ResponseEntity<String> paymentFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("El servicio de pagos no está disponible temporalmente.");
    }

}
