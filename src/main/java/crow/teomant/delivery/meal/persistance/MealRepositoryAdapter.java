package crow.teomant.delivery.meal.persistance;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                meal.getAddons(), meal.getStates(), meal.getVersion(), meal.getDeleted()));

        mealEntity.setName(meal.getName());
        mealEntity.setInfo(meal.getInfo());
        mealEntity.setPrice(meal.getPrice());
        mealEntity.setAddons(meal.getAddons());
        mealEntity.setStates(meal.getStates());
        mealEntity.setVersion(meal.getVersion());
        mealEntity.setDeleted(meal.getDeleted());

        return jpaRepository.save(mealEntity).toModel();
    }
}
