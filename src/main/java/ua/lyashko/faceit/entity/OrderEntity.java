package ua.lyashko.faceit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "order_lunch",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "lunch_id")
    )
    private List<LunchEntity> lunches;

    @ManyToMany
    @JoinTable(
            name = "order_drink",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "drink_id")
    )
    private List<DrinkEntity> drinks;

    private boolean includeCookie;
}
