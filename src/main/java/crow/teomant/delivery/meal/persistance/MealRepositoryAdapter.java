package crow.teomant.delivery.meal.persistance;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.persistance.OrderEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MealRepositoryAdapter implements MealRepository {
    private final MealJpaRepository jpaRepository;

    @Override
    public Optional<Meal> findById(Integer id) {
        return jpaRepository.findById(id).map(MealEntity::toModel);
    }

    @Override
    public List<Meal> findByRestaurantId(Integer id) {
        return jpaRepository.findByRestaurantId(id).stream().map(MealEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Meal> findByIdIn(List<Integer> ids) {
        return jpaRepository.findAllById(ids).stream().map(MealEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll() {
        return jpaRepository.findAll().stream().map(MealEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Meal save(Meal meal) {
        MealEntity mealEntity = Optional.ofNullable(meal.getId()).map(jpaRepository::getReferenceById)
            .orElse(new MealEntity(null, meal.getName(), meal.getInfo(), meal.getRestaurantId(), meal.getPrice(),
                meal.getAddons()));

        mealEntity.setName(meal.getName());
        mealEntity.setInfo(meal.getInfo());
        mealEntity.setPrice(meal.getPrice());
        mealEntity.setAddons(meal.getAddons());

        return jpaRepository.save(mealEntity).toModel();
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByRestaurantId(Integer id) {
        jpaRepository.deleteAllByRestaurantId(id);
    }
}
