package crow.teomant.delivery.order.persistance;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByRestaurantId(Integer id);
}
