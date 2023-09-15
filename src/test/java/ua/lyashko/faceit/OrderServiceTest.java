package ua.lyashko.faceit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.lyashko.faceit.entity.*;
import ua.lyashko.faceit.repository.OrderRepository;
import ua.lyashko.faceit.service.LunchService;
import ua.lyashko.faceit.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private LunchService lunchService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<OrderEntity> orders = new ArrayList<>();
        orders.add(new OrderEntity());
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderEntity> result = orderService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    public void testCreate() {
        OrderEntity order = new OrderEntity();
        order.setLunches(new ArrayList<>());

        when(orderRepository.save(order)).thenReturn(order);

        OrderEntity result = orderService.create(order);

        assertNotNull(result);
        verify(lunchService, times(order.getLunches().size())).createLunch(any(LunchEntity.class));
    }


    @Test
    public void testGetById() {
        Long orderId = 1L;
        OrderEntity order = new OrderEntity();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<OrderEntity> result = orderService.getById(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    public void testUpdate() {
        Long orderId = 1L;
        OrderEntity existingOrder = new OrderEntity();
        existingOrder.setId(orderId);

        OrderEntity updatedOrder = new OrderEntity();
        updatedOrder.setId(orderId);
        updatedOrder.setIncludeCookie(true);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(updatedOrder);

        Optional<OrderEntity> result = orderService.update(orderId, updatedOrder);

        assertTrue(result.isPresent());
        assertEquals(updatedOrder.isIncludeCookie(), result.get().isIncludeCookie());
    }

    @Test
    public void testIsDrinkUsedInAnyOrder() {
        Long drinkId = 1L;
        DrinkEntity drink = new DrinkEntity();
        drink.setId(drinkId);

        OrderEntity order = new OrderEntity();
        List<DrinkEntity> drinks = new ArrayList<>();
        drinks.add(drink);
        order.setDrinks(drinks);

        List<OrderEntity> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAll()).thenReturn(orders);

        assertTrue(orderService.isDrinkUsedInAnyOrder(drink));
    }

    @Test
    public void testIsFoodItemUsedInAnyLunch() {
        Long foodItemId = 1L;
        MealEntity foodItem = new MealEntity();
        foodItem.setId(foodItemId);

        OrderEntity order = new OrderEntity();
        List<LunchEntity> lunches = new ArrayList<>();
        LunchEntity lunch = new LunchEntity();
        List<MealEntity> mainCourse = new ArrayList<>();
        mainCourse.add(foodItem);
        lunch.setMainCourse(mainCourse);
        lunches.add(lunch);
        order.setLunches(lunches);

        List<OrderEntity> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findAll()).thenReturn(orders);

        assertTrue(orderService.isFoodItemUsedInAnyLunch(foodItem));
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        OrderEntity order = new OrderEntity();
        order.setId(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
