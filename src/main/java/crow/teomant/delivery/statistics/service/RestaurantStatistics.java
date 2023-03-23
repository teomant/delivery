package crow.teomant.delivery.statistics.service;

import crow.teomant.delivery.order.service.OrderValue;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RestaurantStatistics(
    Long draftOrders,
    Long processingOrders,
    Long deliveredOrders,
    DeliveredStatistics delivered
) {

    public record DeliveredStatistics(
        List<OrderValue> orders,
        List<TopByNumber> topUsersByNumberOfOrders,
        List<TopByNumber> topMealsByNumberOfOrders,
        List<TopByPrice> mostExpensiveMealsInOrders,
        List<TopBySpend> spendMostMoney,
        BigDecimal totalForPeriod
    ) {
    }

    public record TopByNumber(Integer id, Long number) {
    }

    public record TopBySpend(Integer id, BigDecimal summ) {
    }

    public record TopByPrice(Integer id, BigDecimal summ, LocalDateTime version) {
    }
}

