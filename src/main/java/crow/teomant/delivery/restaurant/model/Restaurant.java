package crow.teomant.delivery.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Restaurant {
    private final Integer id;
    private String name;
    private String contactInfo;
    private String info;
    private String address;
    private List<OpeningHours> openingHours;
    private List<State> states;
    private LocalDateTime version;
    private Boolean deleted;

    public void update(
        String name,
        String info,
        String contactInfo,
        String address,
        List<OpeningHours> openingHours
    ) {
        saveState();
        this.name = name;
        this.info = info;
        this.contactInfo = contactInfo;
        this.address = address;
        this.openingHours = openingHours;
    }

    public State getCurrentState() {
        return deleted ? null : new State(id, name, contactInfo, info, address, openingHours, version);
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
    public static class OpeningHours {
        @NonNull
        DayOfWeek dayOfWeek;
        //don't do so in production. Use separate dto for web
        @Schema(type = "string", pattern = "HH:MM", example = "10:00")
        @NonNull
        LocalTime from;
        @Schema(type = "string", pattern = "HH:MM", example = "10:00")
        @NonNull
        LocalTime to;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private Integer id;
        private String name;
        private String contactInfo;
        private String info;
        private String address;
        private List<OpeningHours> openingHours;
        private LocalDateTime version;
    }

    public void markDeleted() {
        saveState();
        this.deleted = true;
    }

    private void saveState() {
        states.add(
            new State(
                this.id,
                this.name,
                this.contactInfo,
                this.info,
                this.address,
                this.openingHours,
                this.version
            )
        );
        this.version = LocalDateTime.now();
    }

}
