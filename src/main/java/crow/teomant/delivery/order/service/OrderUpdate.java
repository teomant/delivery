package crow.teomant.delivery.order.service;

import crow.teomant.delivery.order.model.Order;
import java.util.List;
import lombok.Data;

@Data
public class OrderUpdate {
    private Integer id;
    private List<Order.Item> items;
}
