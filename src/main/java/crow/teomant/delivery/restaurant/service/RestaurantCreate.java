package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.restaurant.model.Restaurant;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantCreate {
    private String name;
    private String contactInfo;
    private String info;
    private List<Restaurant.OpeningHours> openingHours;
}
