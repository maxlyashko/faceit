package ua.lyashko.faceit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.lyashko.faceit.entity.DrinkEntity;
import ua.lyashko.faceit.repository.DrinkRepository;
import ua.lyashko.faceit.service.DrinkService;
import ua.lyashko.faceit.service.OrderService;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DrinkServiceTest {

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private DrinkService drinkService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() {
        List<DrinkEntity> drinkList = new ArrayList<>();
        drinkList.add(new DrinkEntity());
        drinkList.add(new DrinkEntity());

        when(drinkRepository.findAll()).thenReturn(drinkList);

        List<DrinkEntity> result = drinkService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreate() {
        DrinkEntity drink = new DrinkEntity();
        drink.setName("Test Drink");
        drink.setPrice(5.0);

        when(drinkRepository.save(any(DrinkEntity.class))).thenReturn(drink);

        DrinkEntity result = drinkService.create(drink);

        assertNotNull(result);
        assertEquals("Test Drink", result.getName());
        assertEquals(5.0, result.getPrice());
    }

    @Test
    public void testGetById_ExistingDrink() {
        DrinkEntity drink = new DrinkEntity();
        drink.setId(1L);
        drink.setName("Test Drink");
        drink.setPrice(5.0);

        when(drinkRepository.findById(1L)).thenReturn(Optional.of(drink));

        Optional<DrinkEntity> result = drinkService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Drink", result.get().getName());
        assertEquals(5.0, result.get().getPrice());
    }

    @Test
    public void testGetById_NonExistingDrink() {
        when(drinkRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<DrinkEntity> result = drinkService.getById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdate_ExistingDrink() {
        DrinkEntity existingDrink = new DrinkEntity();
        existingDrink.setId(1L);
        existingDrink.setName("Existing Drink");
        existingDrink.setPrice(10.0);

        DrinkEntity updatedDrink = new DrinkEntity();
        updatedDrink.setName("Updated Drink");
        updatedDrink.setPrice(15.0);

        when(drinkRepository.findById(1L)).thenReturn(Optional.of(existingDrink));
        when(drinkRepository.save(existingDrink)).thenReturn(updatedDrink);

        Optional<DrinkEntity> result = drinkService.update(1L, updatedDrink);

        assertTrue(result.isPresent());
        assertEquals("Updated Drink", result.get().getName());
        assertEquals(15.0, result.get().getPrice());
    }

    @Test
    public void testDelete_DrinkNotFound() {
        when(drinkRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            drinkService.delete(1L);
        });

        verify(drinkRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDelete_DrinkUsedInOrder() {
        DrinkEntity drink = new DrinkEntity();
        drink.setId(1L);

        when(drinkRepository.findById(1L)).thenReturn(Optional.of(drink));

        when(orderService.isDrinkUsedInAnyOrder(drink)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            drinkService.delete(1L);
        });

        verify(drinkRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDelete_SuccessfulDeletion() {
        DrinkEntity drink = new DrinkEntity();
        drink.setId(1L);

        when(drinkRepository.findById(1L)).thenReturn(Optional.of(drink));

        when(orderService.isDrinkUsedInAnyOrder(drink)).thenReturn(false);

        drinkService.delete(1L);

        verify(drinkRepository, times(1)).deleteById(1L);
    }

}
