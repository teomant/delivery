package crow.teomant.delivery.meal.service;

import crow.teomant.delivery.meal.model.Meal;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class MealUpdate {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String info;
    @NonNull
    private BigDecimal price;
    @NonNull
    private List<Meal.Addon> addons;
}
