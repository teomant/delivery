package crow.teomant.delivery.meal.persistance;

import crow.teomant.delivery.meal.model.Meal;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "addons")
    @Convert(converter = AddonsConverter.class)
    private List<Meal.Addon> addons;

    @Column(name = "states")
    @Convert(converter = MealStateConverter.class)
    private List<Meal.State> states;

    @Column(name = "version")
    private LocalDateTime version;

    @Column(name = "deleted")
    private Boolean deleted;

    public Meal toModel() {
        return new Meal(
            id,
            restaurantId,
            name,
            info,
            price,
            addons,
            new ArrayList<>(states),
            version,
            deleted
        );
    }

}
