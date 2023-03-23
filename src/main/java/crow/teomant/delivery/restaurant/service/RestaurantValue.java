package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.restaurant.model.Restaurant;
import lombok.Value;

@Value
public class RestaurantValue {
    Integer id;
    Restaurant.State state;

    public RestaurantValue(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.state = restaurant.getCurrentState();
    }
}
