package crow.teomant.delivery.order.web;

import crow.teomant.delivery.order.service.OrderCreate;
import crow.teomant.delivery.order.service.OrderService;
import crow.teomant.delivery.order.service.OrderUpdate;
import crow.teomant.delivery.order.service.OrderValue;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderValue> getAll(@RequestParam(required = false) Integer restaurantId) {
        if (Objects.nonNull(restaurantId)) {
            return orderService.getByRestaurantId(restaurantId);
        }
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public OrderValue get(@PathVariable Integer id) {
        return orderService.get(id);
    }

    @GetMapping("/{id}/approved")
    public OrderValue approved(@PathVariable Integer id) {
        return orderService.approve(id);
    }

    @GetMapping("/{id}/delivered")
    public OrderValue delivered(@PathVariable Integer id) {
        return orderService.delivered(id);
    }

    @PostMapping
    public OrderValue create(@RequestBody OrderCreate create) {
        return orderService.create(create);
    }

    @PutMapping
    public OrderValue update(@RequestBody OrderUpdate update) {
        return orderService.update(update);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        orderService.delete(id);
    }
}
