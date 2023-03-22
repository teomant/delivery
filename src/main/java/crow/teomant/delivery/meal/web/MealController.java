package crow.teomant.delivery.meal.web;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.service.MealCreate;
import crow.teomant.delivery.meal.service.MealService;
import crow.teomant.delivery.meal.service.MealUpdate;
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
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping
    public List<Meal> getAll(@RequestParam(required = false) Integer restaurantId) {
        if (Objects.nonNull(restaurantId)) {
            return mealService.getByRestaurantId(restaurantId);
        }
        return mealService.getAll();
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable Integer id) {
        return mealService.get(id);
    }

    @PostMapping
    public Meal create(@RequestBody MealCreate create) {
        return mealService.create(create);
    }

    @PutMapping
    public Meal update(@RequestBody MealUpdate update) {
        return mealService.update(update);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        mealService.delete(id);
    }
}
