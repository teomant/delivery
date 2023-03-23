package crow.teomant.delivery.meal.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.delivery.meal.model.Meal;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealStateConverter implements AttributeConverter<List<Meal.State>, String> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(List<Meal.State> attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public List<Meal.State> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<List<Meal.State>>() {
        });
    }
}
