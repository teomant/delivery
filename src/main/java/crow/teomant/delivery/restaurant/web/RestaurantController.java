package crow.teomant.delivery.restaurant.web;

import crow.teomant.delivery.restaurant.service.RestaurantCreate;
import crow.teomant.delivery.restaurant.service.RestaurantService;
import crow.teomant.delivery.restaurant.service.RestaurantUpdate;
import crow.teomant.delivery.restaurant.service.RestaurantValue;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantValue> getAll() {
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantValue get(@PathVariable Integer id) {
        return restaurantService.get(id);
    }

    @PostMapping
    public RestaurantValue create(@RequestBody RestaurantCreate create) {
        return restaurantService.create(create);
    }

    @PutMapping
    public RestaurantValue update(@RequestBody RestaurantUpdate update) {
        return restaurantService.update(update);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        restaurantService.delete(id);
    }
}
