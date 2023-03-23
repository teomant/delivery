package crow.teomant.delivery.user.persistance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.delivery.user.model.User;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStateConverter implements AttributeConverter<List<User.State>, String> {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(List<User.State> attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @Override
    @SneakyThrows
    public List<User.State> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<List<User.State>>() {
        });
    }
}
