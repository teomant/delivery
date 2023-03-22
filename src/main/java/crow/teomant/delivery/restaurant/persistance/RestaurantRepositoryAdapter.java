package crow.teomant.delivery.restaurant.persistance;

import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepository {
    private final RestaurantJpaRepository jpaRepository;

    @Override
    public Optional<Restaurant> findById(Integer id) {
        return jpaRepository.findById(id).map(RestaurantEntity::toModel);
    }

    @Override
    public List<Restaurant> getAll() {
        return jpaRepository.findAll().stream().map(RestaurantEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = Optional.ofNullable(restaurant.getId()).map(jpaRepository::getReferenceById)
            .orElse(new RestaurantEntity(null, restaurant.getContactInfo(), restaurant.getName(), restaurant.getInfo(),
                restaurant.getAddress(), restaurant.getOpeningHours()));

        restaurantEntity.setName(restaurant.getName());
        restaurantEntity.setInfo(restaurant.getInfo());
        restaurantEntity.setContactInfo(restaurant.getContactInfo());
        restaurantEntity.setAddress(restaurant.getAddress());
        restaurantEntity.setOpeningHours(restaurant.getOpeningHours());

        return jpaRepository.save(restaurantEntity).toModel();
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }
}
