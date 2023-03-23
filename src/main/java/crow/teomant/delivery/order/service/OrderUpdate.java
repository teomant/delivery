package crow.teomant.delivery.order.service;

import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderUpdate {
    @NonNull
    private Integer id;
    @NonNull
    private List<Item> items;
}
