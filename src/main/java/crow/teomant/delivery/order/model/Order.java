package crow.teomant.delivery.order.model;

import crow.teomant.delivery.order.service.Item;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
    private LocalDateTime created;
    private LocalDateTime approved;
    private LocalDateTime delivered;
    private Status status;
    private List<Item> items;
    private List<State> states;
    private LocalDateTime version;

    public Order(Integer restaurantId, Integer userId, List<Item> items) {
        this.id = null;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.items = items;
        this.states = Collections.emptyList();
        this.status = Status.DRAFT;
        this.created = LocalDateTime.now();
        this.version = LocalDateTime.now();
    }

    public void update(List<Item> items) {
        if (this.status != Status.DRAFT) {
            throw new IllegalStateException("You can modify only draft order");
        }
        this.states.add(new State(this.id, this.created, this.approved, this.delivered, this.status, this.getItems(),
            this.version));
        this.items = items;
        this.version = LocalDateTime.now();
    }

    public void approved() {
        if (this.status != Status.DRAFT) {
            throw new IllegalStateException("You can approve only draft order");
        }
        this.states.add(new State(this.id, this.created, this.approved, this.delivered, this.status, this.getItems(),
            this.version));
        this.status = Status.PROCESSING;
        this.approved = LocalDateTime.now();
        this.version = LocalDateTime.now();
    }

    public void delivered() {
        if (this.status != Status.PROCESSING) {
            throw new IllegalStateException("You can deliver only approved order");
        }
        this.states.add(new State(this.id, this.created, this.approved, this.delivered, this.status, this.getItems(),
            this.version));
        this.status = Status.DELIVERED;
        this.delivered = LocalDateTime.now();
        this.version = LocalDateTime.now();
    }

    public enum Status {
        DRAFT,
        PROCESSING,
        DELIVERED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class State {
        private Integer id;
        private LocalDateTime created;
        private LocalDateTime approved;
        private LocalDateTime delivered;
        private Order.Status status;
        private List<Item> items;
        private LocalDateTime version;
    }

    public State getCurrentState() {
        return new State(id, created, approved, delivered, status, items, version);
    }

    public State getStateAt(LocalDateTime at) {
        return at.isAfter(version) ? getCurrentState() : states.stream()
            .filter(state -> state.getVersion().isBefore(at))
            .max(Comparator.comparing(State::getVersion))
            .orElseThrow();
    }
}
