package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;

    public RestaurantValue get(Integer id) {

        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + id));

        if (restaurant.getDeleted()) {
            throw new IllegalArgumentException("No restaurant restaurant id " + restaurant.getId());
        }
        //any validations

        return new RestaurantValue(restaurant);
    }

    public List<RestaurantValue> getAll() {
        //any validations

        return restaurantRepository.getAll().stream()
            .filter(restaurant -> !restaurant.getDeleted())
            .map(RestaurantValue::new)
            .collect(Collectors.toList());
    }

    public RestaurantValue create(RestaurantCreate create) {

        validateOpeningHours(create.getOpeningHours());

        //any other validations

        return new RestaurantValue(
            restaurantRepository.save(
                new Restaurant(
                    null,
                    create.getName(),
                    create.getContactInfo(),
                    create.getInfo(),
                    create.getAddress(),
                    create.getOpeningHours(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    false
                )
            )
        );
    }

    private void validateOpeningHours(List<Restaurant.OpeningHours> openingHours) {
        if (
            openingHours.stream()
                .collect(Collectors.groupingBy(Restaurant.OpeningHours::getDayOfWeek, Collectors.counting())).values()
                .stream()
                .anyMatch(value -> value > 1)
        ) {
            throw new IllegalArgumentException("Few opening hours for same day");
        }

        if (
            openingHours.stream()
                .anyMatch(hours -> hours.getFrom().isAfter(hours.getTo()))
        ) {
            throw new IllegalArgumentException("Wrong opening hours");
        }
    }

    public RestaurantValue update(RestaurantUpdate update) {
        Restaurant restaurant = restaurantRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + update.getId()));

        if (restaurant.getDeleted()) {
            throw new IllegalArgumentException("No restaurant restaurant id " + restaurant.getId());
        }

        validateOpeningHours(update.getOpeningHours());
        //any validations

        restaurant.update(
            update.getName(),
            update.getInfo(),
            update.getContactInfo(),
            update.getAddress(),
            update.getOpeningHours()
        );

        return new RestaurantValue(restaurantRepository.save(restaurant));
    }

    @Transactional
    public void delete(Integer id) {
        //any validations

        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + id));

        List<Meal> meals = mealRepository.findByRestaurantId(id);
        meals.forEach(Meal::markDeleted);
        meals.forEach(mealRepository::save);

        restaurant.markDeleted();
        restaurantRepository.save(restaurant);
    }
}
