package crow.teomant.delivery.meal.service;

import crow.teomant.delivery.meal.model.Meal;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class MealUpdate {
    private Integer id;
    private String name;
    private String info;
    private BigDecimal price;
    private List<Meal.Addon> addons;
}
