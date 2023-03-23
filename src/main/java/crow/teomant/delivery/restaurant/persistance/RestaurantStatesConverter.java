package crow.teomant.delivery.restaurant.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.delivery.restaurant.model.Restaurant;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantStatesConverter implements AttributeConverter<List<Restaurant.State>, String> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(List<Restaurant.State> attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public List<Restaurant.State> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<List<Restaurant.State>>() {
        });
    }
}
