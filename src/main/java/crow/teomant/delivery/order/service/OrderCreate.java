package crow.teomant.delivery.order.service;

import crow.teomant.delivery.order.model.Order;
import java.util.List;
import lombok.Data;

@Data
public class OrderCreate {
    private final Integer restaurantId;
    private final Integer userId;
    private List<Order.Item> items;
}
