package ua.lyashko.faceit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lyashko.faceit.entity.MealEntity;
import ua.lyashko.faceit.service.MealService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody MealEntity foodItem) {
        return ResponseEntity.created(
                URI.create("/api/meals/" + mealService.create(foodItem).getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<MealEntity>> getAll() {
        return ResponseEntity.ok(mealService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealEntity> getById(@PathVariable Long id) {
        return mealService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealEntity> update(@PathVariable Long id, @RequestBody MealEntity foodItem) {
        return mealService.update(id, foodItem)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mealService.delete(id);
        return ResponseEntity.noContent().build();
    }
}