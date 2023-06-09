package crow.teomant.delivery.order.persistance;

import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.service.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "approved")
    private LocalDateTime approved;

    @Column(name = "delivered")
    private LocalDateTime delivered;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Order.Status status;

    @Column(name = "items")
    @Convert(converter = ItemsConverter.class)
    private List<Item> items;

    @Column(name = "states")
    @Convert(converter = OrderStateConverter.class)
    private List<Order.State> states;

    @Column(name = "version")
    private LocalDateTime version;

    public Order toModel() {
        return new Order(
            id,
            restaurantId,
            userId,
            created,
            approved,
            delivered,
            status,
            items,
            new ArrayList<>(states),
            version
        );
    }

}
