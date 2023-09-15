package ua.lyashko.faceit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.service.DrinkService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody DrinkEntity drink) {
        return ResponseEntity.created(
                URI.create("/api/drinks/" + drinkService.create(drink).getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<DrinkEntity>> getAll() {
        return ResponseEntity.ok(drinkService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkEntity> getById(@PathVariable Long id) {
        return drinkService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrinkEntity> update(@PathVariable Long id, @RequestBody DrinkEntity drink) {
        return drinkService.update(id, drink)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        drinkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}