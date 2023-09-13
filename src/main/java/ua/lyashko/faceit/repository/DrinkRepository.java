package ua.lyashko.faceit.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lyashko.faceit.entity.DrinkEntity;

public interface DrinkRepository extends CrudRepository<DrinkEntity, Long> {
}
