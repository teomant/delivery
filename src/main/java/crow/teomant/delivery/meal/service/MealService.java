package crow.teomant.delivery.meal.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
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

    public MealValue get(Integer id) {

        Meal meal = mealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No meal with id " + id));

        if (meal.getDeleted()) {
            throw new IllegalArgumentException("No meal with id " + id);
        }
        //any validations

        return new MealValue(meal);
    }

    public List<MealValue> getByRestaurantId(Integer id) {
        //any validations

        return mealRepository.findByRestaurantId(id).stream()
            .filter(meal -> !meal.getDeleted())
            .map(MealValue::new)
            .collect(Collectors.toList());
    }

    public List<MealValue> getAll() {
        //any validations

        return mealRepository.getAll().stream()
            .filter(meal -> !meal.getDeleted())
            .map(MealValue::new)
            .collect(Collectors.toList());
    }

    public MealValue create(MealCreate create) {

        if (restaurantRepository.findById(create.getRestaurantId()).filter(it -> !it.getDeleted()).isEmpty()) {
            throw new IllegalArgumentException("No restaurant with id " + create.getRestaurantId());
        }

        if (
            create.getAddons().stream().collect(Collectors.groupingBy(Meal.Addon::getName, Collectors.counting()))
                .values().stream().anyMatch(value -> value > 1)
        ) {
            throw new IllegalArgumentException("Duplicates in addons");
        }

        if (create.getPrice().compareTo(BigDecimal.ZERO) < 0
            || create.getAddons().stream().anyMatch(addon -> addon.getPrice().compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException("Wrong price");
        }

        //any other validations

        return new MealValue(
            mealRepository.save(
                new Meal(
                    null,
                    create.getRestaurantId(),
                    create.getName(),
                    create.getInfo(),
                    create.getPrice(),
                    create.getAddons(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    false
                )
            )
        );
    }

    public MealValue update(MealUpdate update) {
        Meal meal = mealRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No meal with id " + update.getId()));

        if (update.getAddons().stream().collect(Collectors.groupingBy(Meal.Addon::getName, Collectors.counting()))
            .values().stream().anyMatch(value -> value > 1)) {
            throw new IllegalArgumentException("Duplicates in addons");
        }

        if (meal.getDeleted()) {
            throw new IllegalArgumentException("No meal with id " + update.getId());
        }

        if (update.getPrice().compareTo(BigDecimal.ZERO) < 0
            || update.getAddons().stream().anyMatch(addon -> addon.getPrice().compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException("Wrong price");
        }

        //any validations

        meal.update(
            update.getName(),
            update.getInfo(),
            update.getPrice(),
            update.getAddons()
        );

        return new MealValue(mealRepository.save(meal));
    }

    @Transactional
    public void delete(Integer id) {
        //any validations
        Meal meal = mealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No meal with id " + id));
        meal.markDeleted();
        mealRepository.save(meal);
    }
}
