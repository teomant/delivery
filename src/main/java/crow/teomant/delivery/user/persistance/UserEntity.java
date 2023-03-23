package crow.teomant.delivery.user.persistance;

import crow.teomant.delivery.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "states")
    @Convert(converter = UserStateConverter.class)
    private List<User.State> states;

    @Column(name = "version")
    private LocalDateTime version;

    @Column(name = "deleted")
    private Boolean deleted;

    public User toModel() {
        return new User(
            id,
            username,
            name,
            contactInfo,
            email,
            address,
            birthDate,
            new ArrayList<>(states),
            version,
            deleted
        );
    }

}
