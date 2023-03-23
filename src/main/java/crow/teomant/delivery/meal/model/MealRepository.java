package crow.teomant.delivery.meal.model;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    Optional<Meal> findById(Integer id);

    List<Meal> findByRestaurantId(Integer id);

    List<Meal> findByIdIn(List<Integer> ids);

    List<Meal> getAll();

    Meal save(Meal meal);
}
