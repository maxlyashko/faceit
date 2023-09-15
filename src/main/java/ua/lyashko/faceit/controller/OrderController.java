package ua.lyashko.faceit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.entity.LunchEntity;
import ua.lyashko.faceit.entity.OrderEntity;
import ua.lyashko.faceit.service.OrderService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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

    @PostMapping("/{orderId}/drinks")
    public ResponseEntity<OrderEntity> addDrinkToOrder(
            @PathVariable Long orderId,
            @RequestBody DrinkEntity drink) {
        Optional<OrderEntity> orderOptional = orderService.getById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderEntity order = orderOptional.get();
        orderService.addDrinkToOrder(order, drink);
        return ResponseEntity.ok(orderService.create(order));
    }

    @PostMapping("/{orderId}/lunches")
    public ResponseEntity<OrderEntity> addLunchToOrder(
            @PathVariable Long orderId,
            @RequestBody LunchEntity lunch) {
        Optional<OrderEntity> orderOptional = orderService.getById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderEntity order = orderOptional.get();
        orderService.addLunchToOrder(order, lunch);
        return ResponseEntity.ok(orderService.create(order));
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

    @DeleteMapping("/{orderId}/lunches/{lunchId}")
    public ResponseEntity<OrderEntity> deleteLunchFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long lunchId) {
        Optional<OrderEntity> orderOptional = orderService.getById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        OrderEntity order = orderOptional.get();
        orderService.deleteLunchFromOrder(order, lunchId);
        return ResponseEntity.ok(orderService.create(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}