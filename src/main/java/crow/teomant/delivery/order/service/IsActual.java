package crow.teomant.delivery.order.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IsActual<T> {
    private final T value;
    private final Boolean actual;
}
