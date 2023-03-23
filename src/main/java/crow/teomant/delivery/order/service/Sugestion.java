package crow.teomant.delivery.order.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Sugestion<T> {
    private final T current;
    private final T sugestion;
    private final Boolean actual;
}
