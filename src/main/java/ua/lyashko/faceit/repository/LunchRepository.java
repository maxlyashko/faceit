package ua.lyashko.faceit.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lyashko.faceit.entity.LunchEntity;

public interface LunchRepository extends CrudRepository<LunchEntity, Long> {
}
