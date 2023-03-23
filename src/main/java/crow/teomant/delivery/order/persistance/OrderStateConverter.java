package crow.teomant.delivery.order.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.delivery.order.model.Order;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStateConverter implements AttributeConverter<List<Order.State>, String> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(List<Order.State> attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public List<Order.State> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<List<Order.State>>() {
        });
    }
}
