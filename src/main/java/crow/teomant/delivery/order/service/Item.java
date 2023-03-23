package crow.teomant.delivery.order.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @NonNull
    Integer id;
    @NonNull
    List<String> addons;
}
