package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.restaurant.model.Restaurant;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantUpdate {
    private Integer id;
    private String name;
    private String contactInfo;
    private String info;
    private String address;
    private List<Restaurant.OpeningHours> openingHours;
}
