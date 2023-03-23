package crow.teomant.delivery.meal.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class Meal {
    private final Integer id;
    private final Integer restaurantId;
    private String name;
    private String info;
    private BigDecimal price;
    private List<Addon> addons;
    private List<State> states;
    private LocalDateTime version;
    private Boolean deleted;

    public void update(
        String name,
        String info,
        BigDecimal price,
        List<Addon> addons
    ) {
        saveState();
        this.name = name;
        this.info = info;
        this.price = price;
        this.addons = addons;
    }

    private void saveState() {
        this.states.add(
            new State(
                this.id,
                this.name,
                this.info,
                this.price,
                this.addons,
                this.version
            )
        );
        this.version = LocalDateTime.now();
    }

    public State getCurrentState() {
        return deleted ? null : new State(id, name, info, price, addons, version);
    }

    public State getStateAt(LocalDateTime at) {
        return at.isAfter(version) ? getCurrentState() : states.stream()
            .filter(state -> state.getVersion().isBefore(at))
            .max(Comparator.comparing(State::getVersion))
            .orElseThrow();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Addon {
        String name;  //inner name for restaurant
        String label; //label for client
        BigDecimal price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private Integer id;
        private String name;
        private String info;
        private BigDecimal price;
        private List<Addon> addons;
        private LocalDateTime version;
    }

    public void markDeleted() {
        this.states.add(
            new State(
                this.id,
                this.name,
                this.info,
                this.price,
                this.addons,
                this.version
            )
        );
        this.version = LocalDateTime.now();
        this.deleted = true;
    }
}
