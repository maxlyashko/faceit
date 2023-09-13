package ua.lyashko.faceit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ua.lyashko.faceit.enums.KitchenType;

@Entity
@Getter
@Setter
public class MealEntity extends Product {
    @Enumerated(EnumType.STRING)
    @NonNull
    private KitchenType kitchenType;
}
