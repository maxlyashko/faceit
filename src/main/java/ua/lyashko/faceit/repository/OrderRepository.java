package ua.lyashko.faceit.repository;

import org.springframework.data.repository.CrudRepository;
import ua.lyashko.faceit.entity.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
