package crow.teomant.delivery.order.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SavedCurrent<T> {
    private final T saved;
    private final T current;
    private final Boolean actual;
}
