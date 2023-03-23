package crow.teomant.delivery.restaurant.persistance;

import crow.teomant.delivery.restaurant.model.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restautants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "address")
    private String address;

    @Column(name = "opening_hours")
    @Convert(converter = OpeningHoursConverter.class)
    private List<Restaurant.OpeningHours> openingHours;

    @Column(name = "states")
    @Convert(converter = RestaurantStatesConverter.class)
    private List<Restaurant.State> states;

    @Column(name = "version")
    private LocalDateTime version;

    @Column(name = "deleted")
    private Boolean deleted;

    public Restaurant toModel() {
        return new Restaurant(
            id,
            name,
            contactInfo,
            info,
            address,
            openingHours,
            new ArrayList<>(states),
            version,
            deleted
        );
    }

}
