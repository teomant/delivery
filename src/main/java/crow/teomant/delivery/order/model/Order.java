package crow.teomant.delivery.order.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class Order {
    private final Integer id;
    private final Integer restaurantId;
    private final Integer userId;
    private LocalDate created;
    private LocalDate approved;
    private LocalDate delivered;
    private Status status;
    private List<Item> items;
    private State state;

    public Order(Integer restaurantId, Integer userId, List<Item> items, State state) {
        this.id = null;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.items = items;
        this.state = state;
        this.status = Status.DRAFT;
        this.created = LocalDate.now();
    }

    public void update(List<Item> items, State state) {
        if (this.status != Status.DRAFT) {
            throw new IllegalStateException("You can modify only draft order");
        }
        this.items = items;
        this.state = state;
    }

    public void approved() {
        if (this.status != Status.DRAFT) {
            throw new IllegalStateException("You can approve only draft order");
        }
        this.status = Status.PROCESSING;
        this.approved = LocalDate.now();
    }

    public void delivered() {
        if (this.status != Status.PROCESSING) {
            throw new IllegalStateException("You can deliver only approved order");
        }
        this.status = Status.DELIVERED;
        this.delivered = LocalDate.now();
    }

    public enum Status {
        DRAFT,
        PROCESSING,
        DELIVERED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        Integer id;
        List<String> addons;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class State {
        private Integer restaurantId;
        private String restaurantName;
        private String restaurantAddress;

        private Integer userId;
        private String userName;
        private String userAddress;

        private List<ItemState> items;
        private LocalDate date;

        public BigDecimal getTotal() {
            return items.stream().map(ItemState::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemState {
        Integer id;
        String name;
        BigDecimal price;
        List<AddonState> addons;
        private LocalDate date;

        public BigDecimal getTotal() {
            return addons.stream().map(AddonState::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).add(price);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddonState {
        String name;
        String label;
        BigDecimal price;
        private LocalDate date;
    }

}
