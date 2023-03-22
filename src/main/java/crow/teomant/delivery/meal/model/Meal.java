package crow.teomant.delivery.meal.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class Meal {
    private final Integer id;
    private final Integer restaurantId;
    private String name;
    private String info;
    private BigDecimal price;
    private List<Addon> addons;

    public void update(
        String name,
        String info,
        BigDecimal price,
        List<Addon> addons
    ) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.addons = addons;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Addon {
        String name;  //inner name for restaurant
        String label; //label for client
        BigDecimal price;
    }

}
