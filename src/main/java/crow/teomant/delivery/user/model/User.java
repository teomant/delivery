package crow.teomant.delivery.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class User {
    private final Integer id;
    private final String username;
    private String name;
    private String contactInfo;
    private String email;
    private String address;
    private LocalDate birthDate;
    private List<State> states;
    private LocalDateTime version;
    private Boolean deleted;

    public void update(
        String name,
        String email,
        String contactInfo,
        String address,
        LocalDate birthDate
    ) {
        saveState();
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
        this.address = address;
        this.birthDate = birthDate;
    }

    private void saveState() {
        this.states.add(
            new State(id, this.name, this.contactInfo, this.email, this.address, this.birthDate, this.version)
        );
        this.version = LocalDateTime.now();
    }

    public State getCurrentState() {
        return deleted ? null : new State(id, name, contactInfo, email, address, birthDate, version);
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
    public static class State {
        private Integer id;
        private String name;
        private String contactInfo;
        private String email;
        private String address;
        private LocalDate birthDate;
        private LocalDateTime version;
    }

    public void markDeleted() {
        saveState();
        this.deleted = true;
    }

}
