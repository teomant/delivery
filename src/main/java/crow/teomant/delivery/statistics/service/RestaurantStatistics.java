package crow.teomant.delivery.statistics.service;

import crow.teomant.delivery.order.service.OrderValue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RestaurantStatistics(
    List<OrderValue> orders,
    List<TopByNumber> topUsersByNumberOfOrders,
    List<TopByNumber> topMealsByNumberOfOrders,
    List<TopByPrice> mostExpensiveMealsInOrders,
    List<TopBySpend> spendMostMoney,
    BigDecimal totalForPeriod
) {

    public record TopByNumber(Integer id, Long number) {
    }

    public record TopBySpend(Integer id, BigDecimal summ) {
    }

    public record TopByPrice(Integer id, BigDecimal summ, LocalDate date) {
    }
}

