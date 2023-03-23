package crow.teomant.delivery.order.service;

import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderCreate {
    @NonNull
    private final Integer restaurantId;
    @NonNull
    private final Integer userId;
    @NonNull
    private List<Item> items;
}
