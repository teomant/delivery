package crow.teomant.delivery.order.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.delivery.order.model.Order;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateConverter implements AttributeConverter<Order.State, String> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Order.State attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public Order.State convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<Order.State>() {
        });
    }
}
