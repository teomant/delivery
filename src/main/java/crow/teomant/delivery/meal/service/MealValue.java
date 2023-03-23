package crow.teomant.delivery.meal.service;

import crow.teomant.delivery.meal.model.Meal;
import lombok.Value;

@Value
public class MealValue {
    Integer id;
    Integer restaurantId;
    Meal.State state;

    public MealValue(Meal meal) {
        this.id = meal.getId();
        this.restaurantId = meal.getRestaurantId();
        this.state = meal.getCurrentState();
    }
}
