package ua.lyashko.faceit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Entity
@Getter
@Setter
public class DrinkEntity extends Product {
    @NonNull
    private boolean ice;
    @NonNull
    private boolean lemon;
}
