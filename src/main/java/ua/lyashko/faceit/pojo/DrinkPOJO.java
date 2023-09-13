package ua.lyashko.faceit.pojo;

import lombok.Data;
import lombok.NonNull;
import ua.lyashko.faceit.entity.DrinkEntity;

@Data
public class DrinkPOJO {
    @NonNull
    private DrinkEntity drink;
    @NonNull
    private boolean ice;
    @NonNull
    private boolean lemon;
}
