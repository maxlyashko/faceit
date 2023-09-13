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

    @ElementCollection
    @CollectionTable(name = "lunches")
    @Column(columnDefinition = "TEXT") // Указываем тип JDBC, например, JSON
    private List<LunchPOJO> lunches;

    @ElementCollection
    @CollectionTable(name = "drinks")
    private List<DrinkPOJO> drinks;

    private boolean includeCookie;
}
