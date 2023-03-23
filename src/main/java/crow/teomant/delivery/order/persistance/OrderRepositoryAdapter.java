package crow.teomant.delivery.order.persistance;

import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.model.OrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Optional<Order> findById(Integer id) {
        return jpaRepository.findById(id).map(OrderEntity::toModel);
    }

    @Override
    public List<Order> findByRestaurantId(Integer id) {
        return jpaRepository.findAllByRestaurantId(id).stream().map(OrderEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Order> getAll() {
        return jpaRepository.findAll().stream().map(OrderEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = Optional.ofNullable(order.getId()).map(jpaRepository::getReferenceById)
            .orElse(
                new OrderEntity(
                    null,
                    order.getRestaurantId(),
                    order.getUserId(),
                    order.getCreated(),
                    order.getApproved(),
                    order.getDelivered(),
                    order.getStatus(),
                    order.getItems(),
                    order.getStates(),
                    order.getVersion()
                )
            );

        orderEntity.setApproved(order.getApproved());
        orderEntity.setDelivered(order.getDelivered());
        orderEntity.setStatus(order.getStatus());
        orderEntity.setItems(order.getItems());
        orderEntity.setStates(order.getStates());
        orderEntity.setVersion(order.getVersion());

        return jpaRepository.save(orderEntity).toModel();
    }

}
