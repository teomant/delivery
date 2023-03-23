package crow.teomant.delivery.order.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.user.model.User;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderValue {
    private final Integer id;
    private final Integer restaurantId;
    private final Integer userId;
    private final Order.State state;
    private final Metadata metadata;

    public Boolean isActual() {
        return metadata.restaurant.getActual()
            && metadata.user.getActual()
            && metadata.meals.stream().allMatch(Sugestion::getActual);
    }

    public BigDecimal getTotal() {
        return state.getItems().stream()
            .map(item -> {
                    Meal.State current = metadata.meals.stream()
                        .filter(meal -> meal.getCurrent().getId().equals(item.getId()))
                        .findAny().get().getCurrent();

                    return item.getAddons().stream()
                        .map(addon -> current.getAddons().stream()
                            .filter(a -> a.getName().equals(addon))
                            .findAny()
                            .map(Meal.Addon::getPrice).get())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(current.getPrice());
                }
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public record Metadata(Sugestion<Restaurant.State> restaurant, Sugestion<User.State> user,
                           List<Sugestion<Meal.State>> meals) {
    }
}
