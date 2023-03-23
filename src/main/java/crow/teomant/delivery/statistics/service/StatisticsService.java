package crow.teomant.delivery.statistics.service;

import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.service.IsActual;
import crow.teomant.delivery.order.service.OrderService;
import crow.teomant.delivery.order.service.OrderValue;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final OrderService orderService;

    public RestaurantStatistics getStatistics(StatisticsRequest request) {
        List<OrderValue> orders = orderService.getByRestaurantId(request.getId()).stream()
            .filter(order -> order.getDelivered().isAfter(request.getFrom())
                && order.getDelivered().isBefore(request.getTo()))
            .toList();

        return new RestaurantStatistics(
            orders.stream().filter(order -> order.getStatus() == Order.Status.DRAFT).count(),
            orders.stream().filter(order -> order.getStatus() == Order.Status.PROCESSING).count(),
            orders.stream().filter(order -> order.getStatus() == Order.Status.DELIVERED).count(),
            getDeliveredStatistics(orders.stream()
                .filter(order -> order.getStatus() == Order.Status.DELIVERED).toList())
        );
    }

    private RestaurantStatistics.DeliveredStatistics getDeliveredStatistics(List<OrderValue> delivered) {
        return new RestaurantStatistics.DeliveredStatistics(
            delivered,
            delivered.stream().collect(Collectors.groupingBy(OrderValue::getUserId, Collectors.counting()))
                .entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new RestaurantStatistics.TopByNumber(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()),
            delivered.stream().map(OrderValue::getItems)
                .flatMap(Collection::stream)
                .map(IsActual::getValue)
                .collect(Collectors.groupingBy(Order.ItemState::getId, Collectors.counting()))
                .entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new RestaurantStatistics.TopByNumber(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()),
            delivered.stream().map(OrderValue::getItems)
                .flatMap(Collection::stream)
                .map(IsActual::getValue)
                .sorted(Comparator.comparing(Order.ItemState::getPrice, Comparator.reverseOrder()))
                .map(item -> new RestaurantStatistics.TopByPrice(item.getId(), item.getPrice(), item.getDate()))
                .distinct()
                .limit(5)
                .collect(Collectors.toList()),
            delivered.stream().collect(Collectors.groupingBy(OrderValue::getUserId))
                .entrySet()
                .stream()
                .map(entry -> new RestaurantStatistics.TopBySpend(
                    entry.getKey(),
                    entry.getValue().stream()
                        .map(OrderValue::getItems)
                        .flatMap(Collection::stream)
                        .map(IsActual::getValue)
                        .map(Order.ItemState::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .sorted(Comparator.comparing(RestaurantStatistics.TopBySpend::summ, Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList()),
            delivered.stream().map(OrderValue::getItems)
                .flatMap(Collection::stream)
                .map(IsActual::getValue)
                .map(Order.ItemState::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
