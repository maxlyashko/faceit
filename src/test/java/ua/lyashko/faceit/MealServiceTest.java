package ua.lyashko.faceit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.lyashko.faceit.entity.MealEntity;
import ua.lyashko.faceit.enums.KitchenType;
import ua.lyashko.faceit.repository.MealRepository;
import ua.lyashko.faceit.service.MealService;
import ua.lyashko.faceit.service.OrderService;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private MealService mealService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<MealEntity> foodItemList = new ArrayList<>();
        foodItemList.add(new MealEntity());
        foodItemList.add(new MealEntity());

        when(mealRepository.findAll()).thenReturn(foodItemList);

        List<MealEntity> result = mealService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreate() {
        MealEntity foodItem = new MealEntity();
        foodItem.setName("Test Food Item");
        foodItem.setPrice(8.0);
        foodItem.setKitchenType(KitchenType.ITALIAN);

        when(mealRepository.save(any(MealEntity.class))).thenReturn(foodItem);

        MealEntity result = mealService.create(foodItem);

        assertNotNull(result);
        assertEquals("Test Food Item", result.getName());
        assertEquals(8.0, result.getPrice());
        assertEquals(KitchenType.ITALIAN, result.getKitchenType());
    }

    @Test
    public void testGetById_ExistingFoodItem() {
        MealEntity foodItem = new MealEntity();
        foodItem.setId(1L);
        foodItem.setName("Test Food Item");
        foodItem.setPrice(8.0);
        foodItem.setKitchenType(KitchenType.ITALIAN);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        Optional<MealEntity> result = mealService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Food Item", result.get().getName());
        assertEquals(8.0, result.get().getPrice());
        assertEquals(KitchenType.ITALIAN, result.get().getKitchenType());
    }

    @Test
    public void testGetById_NonExistingFoodItem() {
        when(mealRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<MealEntity> result = mealService.getById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdate_ExistingFoodItem() {
        MealEntity existingFoodItem = new MealEntity();
        existingFoodItem.setId(1L);
        existingFoodItem.setName("Existing Food Item");
        existingFoodItem.setPrice(12.0);
        existingFoodItem.setKitchenType(KitchenType.MEXICAN);

        MealEntity updatedFoodItem = new MealEntity();
        updatedFoodItem.setName("Updated Food Item");
        updatedFoodItem.setPrice(15.0);
        updatedFoodItem.setKitchenType(KitchenType.ITALIAN);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(existingFoodItem));
        when(mealRepository.save(existingFoodItem)).thenReturn(updatedFoodItem);

        Optional<MealEntity> result = mealService.update(1L, updatedFoodItem);

        assertTrue(result.isPresent());
        assertEquals("Updated Food Item", result.get().getName());
        assertEquals(15.0, result.get().getPrice());
        assertEquals(KitchenType.ITALIAN, result.get().getKitchenType());
    }

    @Test
    public void testDelete_FoodItemNotFound() {
        when(mealRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            mealService.delete(1L);
        });

        verify(mealRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDelete_FoodItemUsedInOrder() {
        MealEntity foodItem = new MealEntity();
        foodItem.setId(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        when(orderService.isFoodItemUsedInAnyOrder(foodItem)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            mealService.delete(1L);
        });

        verify(mealRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDelete_SuccessfulDeletion() {
        MealEntity foodItem = new MealEntity();
        foodItem.setId(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        when(orderService.isFoodItemUsedInAnyOrder(foodItem)).thenReturn(false);

        mealService.delete(1L);

        verify(mealRepository, times(1)).deleteById(1L);
    }
}
