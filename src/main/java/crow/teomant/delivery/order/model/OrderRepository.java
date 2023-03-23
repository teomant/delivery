package crow.teomant.delivery.order.model;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Integer id);

    List<Order> findByRestaurantId(Integer id);

    List<Order> getAll();

    Order save(Order order);

}
