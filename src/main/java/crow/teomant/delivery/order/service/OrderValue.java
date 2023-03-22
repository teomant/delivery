package crow.teomant.delivery.order.service;

import crow.teomant.delivery.order.model.Order;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderValue {
    private final Integer id;
    private final Integer restaurantId;
    private final Integer userId;
    private LocalDate created;
    private LocalDate approved;
    private LocalDate delivered;
    private Order.Status status;

    private IsActual<String> restaurantName;
    private IsActual<String> restaurantAddress;
    private IsActual<String> userUsername;
    private IsActual<String> userAddress;
    private List<IsActual<Order.ItemState>> items;

    public Boolean isActual() {
        return restaurantName.getActual() && restaurantAddress.getActual() && userUsername.getActual()
            && userAddress.getActual() && items.stream().allMatch(IsActual::getActual);
    }
}
