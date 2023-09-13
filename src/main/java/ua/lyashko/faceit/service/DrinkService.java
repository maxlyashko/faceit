package ua.lyashko.faceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.repository.DrinkRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkService {
    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private OrderService orderService;


    public List<DrinkEntity> getAll() {
        return (List<DrinkEntity>) drinkRepository.findAll();
    }

    public DrinkEntity create(DrinkEntity drinkEntity) {
        return drinkRepository.save(drinkEntity);
    }

    public Optional<DrinkEntity> getById(Long id) {
        return drinkRepository.findById(id);
    }

    public Optional<DrinkEntity> update(Long id, DrinkEntity updatedDrink) {
        return getById(id).map(existingDrink -> {
            if (updatedDrink.getName()!=null) {
                existingDrink.setName(updatedDrink.getName());
            }
            if (updatedDrink.getPrice()!=0.0) {
                existingDrink.setPrice(updatedDrink.getPrice());
            }
            return drinkRepository.save(existingDrink);
        });
    }

    public void delete(Long id) {
        Optional<DrinkEntity> drinkOptional = getById(id);
        if (drinkOptional.isEmpty()) {
            throw new IllegalArgumentException("Drink with ID " + id + " not found");
        }
        DrinkEntity drink = drinkOptional.get();
        if (orderService.isDrinkUsedInAnyOrder(drink)) {
            throw new IllegalStateException("Drink with ID " + id + " is already used in an order");
        }
        drinkRepository.deleteById(id);
    }
}
