package ua.lyashko.faceit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ua.lyashko.faceit.pojo.DrinkPOJO;
import ua.lyashko.faceit.pojo.LunchPOJO;

import java.util.List;

@Entity
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<LunchPOJO> lunches;

    private List<DrinkPOJO> drinks;

    private boolean includeCookie;
}
