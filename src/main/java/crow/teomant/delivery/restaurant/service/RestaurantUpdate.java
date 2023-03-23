package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.restaurant.model.Restaurant;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class RestaurantUpdate {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String contactInfo;
    @NonNull
    private String info;
    @NonNull
    private String address;
    @NonNull
    private List<Restaurant.OpeningHours> openingHours;
}
