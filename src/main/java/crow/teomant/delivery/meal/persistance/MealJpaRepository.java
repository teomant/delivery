package crow.teomant.delivery.meal.persistance;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealJpaRepository extends JpaRepository<MealEntity, Integer> {
    List<MealEntity> findByRestaurantId(Integer id);
    void deleteAllByRestaurantId(Integer id);
}
