package crow.teomant.delivery.meal.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    public Meal get(Integer id) {
        //any validations

        return mealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + id));
    }

    public List<Meal> getByRestaurantId(Integer id) {
        //any validations

        return mealRepository.findByRestaurantId(id);
    }

    public List<Meal> getAll() {
        //any validations

        return mealRepository.getAll();
    }

    public Meal create(MealCreate create) {

        if (restaurantRepository.findById(create.getRestaurantId()).isEmpty()) {
            throw new IllegalArgumentException("No restaurant with id " + create.getRestaurantId());
        }

        if (
            create.getAddons().stream().collect(Collectors.groupingBy(Meal.Addon::getName, Collectors.counting()))
                .values().stream().anyMatch(value -> value > 1)
        ) {
            throw new IllegalArgumentException("Duplicates in addons");
        }

        //any other validations

        return mealRepository.save(
            new Meal(null, create.getRestaurantId(), create.getName(), create.getInfo(), create.getPrice(),
                create.getAddons())
        );
    }

    public Meal update(MealUpdate update) {
        Meal meal = mealRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No meal with id " + update.getId()));

        if (
            update.getAddons().stream().collect(Collectors.groupingBy(Meal.Addon::getName, Collectors.counting()))
                .values().stream().anyMatch(value -> value > 1)
        ) {
            throw new IllegalArgumentException("Duplicates in addons");
        }

        //any validations

        meal.update(
            update.getName(),
            update.getInfo(),
            update.getPrice(),
            update.getAddons()
        );

        return mealRepository.save(meal);
    }

    @Transactional
    public void delete(Integer id) {
        //any validations

        mealRepository.delete(id);
    }
}
