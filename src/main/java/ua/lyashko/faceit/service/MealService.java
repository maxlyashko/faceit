package ua.lyashko.faceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lyashko.faceit.entity.MealEntity;
import ua.lyashko.faceit.repository.MealRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private OrderService orderService;

    public List<MealEntity> getAll() {
        return (List<MealEntity>) mealRepository.findAll();
    }

    public MealEntity create(MealEntity mealEntity) {
        return mealRepository.save(mealEntity);
    }

    public Optional<MealEntity> getById(Long id) {
        return mealRepository.findById(id);
    }

    public Optional<MealEntity> update(Long id, MealEntity updatedFoodItem) {
        return getById(id).map(existingFoodItem -> {
            if (updatedFoodItem.getName()!=null) {
                existingFoodItem.setName(updatedFoodItem.getName());
            }
            if (updatedFoodItem.getPrice()!=0.0) {
                existingFoodItem.setPrice(updatedFoodItem.getPrice());
            }
            if (updatedFoodItem.getKitchenType()!=null) {
                existingFoodItem.setKitchenType(updatedFoodItem.getKitchenType());
            }
            return mealRepository.save(existingFoodItem);
        });
    }

    public void delete(Long id) {
        Optional<MealEntity> foodItemOptional = getById(id);
        if (foodItemOptional.isEmpty()) {
            throw new IllegalArgumentException("Food item with ID " + id + " not found");
        }
        MealEntity foodItem = foodItemOptional.get();
        if (orderService.isFoodItemUsedInAnyOrder(foodItem)) {
            throw new IllegalStateException("Food item with ID " + id + " is already used in an order");
        }
        mealRepository.deleteById(id);
    }
}
