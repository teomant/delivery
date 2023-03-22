package crow.teomant.delivery.restaurant.model;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findById(Integer id);

    List<Restaurant> getAll();

    Restaurant save(Restaurant restaurant);

    void delete(Integer id);
}
