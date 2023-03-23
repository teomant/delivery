package crow.teomant.delivery.order.service;

import java.util.List;
import lombok.Data;

@Data
public class OrderUpdate {
    private Integer id;
    private List<Item> items;
}
