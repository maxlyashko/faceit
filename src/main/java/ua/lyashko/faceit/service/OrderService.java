package ua.lyashko.faceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.entity.MealEntity;
import ua.lyashko.faceit.entity.OrderEntity;
import ua.lyashko.faceit.pojo.DrinkPOJO;
import ua.lyashko.faceit.pojo.LunchPOJO;
import ua.lyashko.faceit.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderEntity> getAll() {
        return (List<OrderEntity>) orderRepository.findAll();
    }

    public OrderEntity create(OrderEntity order) {
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
            if (updatedOrder.isIncludeCookie()) {
                existingOrder.setIncludeCookie(updatedOrder.isIncludeCookie());
            }
            return orderRepository.save(existingOrder);
        });
    }

    public boolean isDrinkUsedInAnyOrder(DrinkEntity drink) {
        List<OrderEntity> orders = getAll();
        for (OrderEntity order : orders) {
            List<DrinkPOJO> drinksInOrder = order.getDrinks();
            if (drinksInOrder!=null && drinksInOrder.contains(drink)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFoodItemUsedInAnyOrder(MealEntity foodItem) {
        List<OrderEntity> orders = getAll();
        for (OrderEntity order : orders) {
            List<LunchPOJO> foodItemInOrder = order.getLunches();
            if (foodItemInOrder!=null && foodItemInOrder.contains(foodItem)) {
                return true;
            }
        }
        return false;
    }

    public void addLunchToOrder(OrderEntity order, LunchPOJO lunch) {
        order.getLunches().add(lunch);
    }

    public void addDrinkToOrder(OrderEntity order, DrinkPOJO drink) {
        order.getDrinks().add(drink);
    }

    public void deleteDrinkFromOrder(OrderEntity order, Long id) {
        List<DrinkPOJO> drinks = new ArrayList<>(order.getDrinks());
        drinks.remove(id.intValue());
        order.setDrinks(drinks);
    }

    public void deleteFoodItemFromOrder(OrderEntity order, Long id) {
        List<LunchPOJO> meals = new ArrayList<>(order.getLunches());
        meals.remove(id.intValue());
        order.setLunches(meals);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
