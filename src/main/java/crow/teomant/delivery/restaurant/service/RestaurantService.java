package crow.teomant.delivery.restaurant.service;

import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
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

    public Restaurant get(Integer id) {
        //any validations

        return restaurantRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + id));
    }

    public List<Restaurant> getAll() {
        //any validations

        return restaurantRepository.getAll();
    }

    public Restaurant create(RestaurantCreate create) {

        validateOpeningHours(create.getOpeningHours());

        //any other validations

        return restaurantRepository.save(
            new Restaurant(null, create.getName(), create.getContactInfo(), create.getInfo(), create.getOpeningHours())
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

    public Restaurant update(RestaurantUpdate update) {
        Restaurant restaurant = restaurantRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + update.getId()));

        validateOpeningHours(update.getOpeningHours());
        //any validations

        restaurant.update(
            update.getName(),
            update.getInfo(),
            update.getContactInfo(),
            update.getOpeningHours()
        );

        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void delete(Integer id) {
        //any validations
        mealRepository.deleteByRestaurantId(id);
        restaurantRepository.delete(id);
    }
}
