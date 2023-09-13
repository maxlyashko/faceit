package ua.lyashko.faceit.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lyashko.faceit.entity.MealEntity;

public interface MealRepository extends CrudRepository<MealEntity, Long> {
}
