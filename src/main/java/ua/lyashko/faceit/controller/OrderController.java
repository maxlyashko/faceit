package ua.lyashko.faceit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lyashko.faceit.entity.OrderEntity;
import ua.lyashko.faceit.service.OrderService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrderEntity order) {
        return ResponseEntity.created(
                URI.create("/api/orders/" + orderService.create(order).getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getById(@PathVariable Long id) {
        return orderService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderEntity> update(@PathVariable Long id, @RequestBody OrderEntity order) {
        return orderService.update(id, order)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}/drinks/{drinkId}")
    public ResponseEntity<OrderEntity> deleteDrinkFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long drinkId) {
        Optional<OrderEntity> orderOptional = orderService.getById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderEntity order = orderOptional.get();
        orderService.deleteDrinkFromOrder(order, drinkId);
        return ResponseEntity.ok(orderService.create(order));
    }

    @DeleteMapping("/{orderId}/food-items/{foodItemId}")
    public ResponseEntity<OrderEntity> deleteFoodItemFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long foodItemId) {
        Optional<OrderEntity> orderOptional = orderService.getById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderEntity order = orderOptional.get();
        orderService.deleteFoodItemFromOrder(order, foodItemId);
        return ResponseEntity.ok(orderService.create(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
