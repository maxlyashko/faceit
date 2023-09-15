package ua.lyashko.faceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.entity.LunchEntity;
import ua.lyashko.faceit.entity.MealEntity;
import ua.lyashko.faceit.entity.OrderEntity;
import ua.lyashko.faceit.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final LunchService lunchService;

    @Autowired
    public OrderService(OrderRepository orderRepository, LunchService lunchService) {
        this.orderRepository = orderRepository;
        this.lunchService = lunchService;
    }

    public List<OrderEntity> getAll() {
        return (List<OrderEntity>) orderRepository.findAll();
    }

    public OrderEntity create(OrderEntity order) {
        for (LunchEntity lunch : order.getLunches()) {
            lunchService.createLunch(lunch);
        }
        return orderRepository.save(order);
    }

    public Optional<OrderEntity> getById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<OrderEntity> update(Long id, OrderEntity updatedOrder) {
        return getById(id).map(existingOrder -> {
            if (updatedOrder.getDrinks()!=null) {
                existingOrder.setDrinks(updatedOrder.getDrinks());
            }
            if (updatedOrder.getLunches()!=null) {
                existingOrder.setLunches(updatedOrder.getLunches());
            }
            if (!updatedOrder.isIncludeCookie() || updatedOrder.isIncludeCookie()) {
                existingOrder.setIncludeCookie(updatedOrder.isIncludeCookie());
            }
            return orderRepository.save(existingOrder);
        });
    }

    public boolean isDrinkUsedInAnyOrder(DrinkEntity drink) {
        List<OrderEntity> orders = getAll();
        for (OrderEntity order : orders) {
            List<DrinkEntity> drinksInOrder = order.getDrinks();
            if (drinksInOrder!=null && drinksInOrder.contains(drink)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFoodItemUsedInAnyLunch(MealEntity foodItem) {
        List<OrderEntity> orders = getAll();
        for (OrderEntity order : orders) {
            List<LunchEntity> foodItemInOrder = order.getLunches();
            for (LunchEntity lunch : foodItemInOrder) {
                List<MealEntity> mainCourse = lunch.getMainCourse();
                List<MealEntity> dessert = lunch.getDessert();
                if ((mainCourse!=null && mainCourse.contains(foodItem)) ||
                        (dessert!=null && dessert.contains(foodItem))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addLunchToOrder(OrderEntity order, LunchEntity lunch) {
        lunchService.createLunch(lunch);
        order.getLunches().add(lunch);
    }

    public void addDrinkToOrder(OrderEntity order, DrinkEntity drink) {
        order.getDrinks().add(drink);
    }

    public void deleteDrinkFromOrder(OrderEntity order, Long id) {
        List<DrinkEntity> drinks = new ArrayList<>(order.getDrinks());
        drinks.remove(id.intValue());
        order.setDrinks(drinks);
    }

    public void deleteLunchFromOrder(OrderEntity order, Long id) {
        List<LunchEntity> lunches = new ArrayList<>(order.getLunches());
        lunches.remove(id.intValue());
        order.setLunches(lunches);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}