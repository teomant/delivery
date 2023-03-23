package crow.teomant.delivery.order.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.model.OrderRepository;
import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import crow.teomant.delivery.user.model.User;
import crow.teomant.delivery.user.model.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;


    public OrderValue get(Integer id) {
        //any validations

        Order saved = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No order with id " + id));

        return mapValue(saved);
    }

    private OrderValue mapValue(Order saved) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(saved.getRestaurantId());
        Optional<User> user = userRepository.findById(saved.getUserId());
        List<Meal> mealsInOrder = mealRepository.findByIdIn(
            saved.getCurrentState().getItems().stream().map(Item::getId).collect(Collectors.toList())
        );

        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCurrentState(),
            new Sugestion<>(
                restaurant.map(r -> r.getStateAt(saved.getCurrentState().getVersion())).orElse(null),
                restaurant.map(Restaurant::getCurrentState).orElse(null),
                restaurant.map(Restaurant::getCurrentState)
                    .map(state -> state.getVersion().isBefore(saved.getCurrentState().getVersion()))
                    .orElse(false)
            ),
            new Sugestion<>(
                user.map(r -> r.getStateAt(saved.getCurrentState().getVersion())).orElse(null),
                user.map(User::getCurrentState).orElse(null),
                user.map(User::getCurrentState)
                    .map(state -> state.getVersion().isBefore(saved.getCurrentState().getVersion()))
                    .orElse(false)
            ),
            mealsInOrder.stream()
                .map(meal -> new Sugestion<>(
                    meal.getStateAt(saved.getCurrentState().getVersion()),
                    meal.getCurrentState(),
                    Optional.ofNullable(meal.getCurrentState())
                        .map(state -> state.getVersion().isBefore(saved.getCurrentState().getVersion()))
                        .orElse(false)
                ))
                .collect(Collectors.toList())
        );
    }

    public List<OrderValue> getByRestaurantId(Integer id) {
        //any validations

        return orderRepository.findByRestaurantId(id).stream().map(this::mapValue).collect(Collectors.toList());
    }

    public List<OrderValue> getAll() {
        //any validations

        return orderRepository.getAll().stream().map(this::mapValue).collect(Collectors.toList());
    }

    public OrderValue create(OrderCreate create) {

        Restaurant restaurant = restaurantRepository.findById(create.getRestaurantId())
            .filter(it -> !it.getDeleted())
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + create.getRestaurantId()));

        User user = userRepository.findById(create.getUserId())
            .filter(it -> !it.getDeleted())
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + create.getUserId()));

        List<Meal> mealsInOrder = mealRepository.findByIdIn(
            create.getItems().stream().map(Item::getId).collect(Collectors.toList())
        );

        validateItems(mealsInOrder, create.getItems(), restaurant.getId());

        //any other validations

        Order saved = orderRepository.save(new Order(create.getRestaurantId(), create.getUserId(), create.getItems()));

        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCurrentState(),
            new Sugestion<>(restaurant.getCurrentState(),
                restaurant.getCurrentState(),
                restaurant.getCurrentState().getVersion().isBefore(saved.getCurrentState().getVersion())),
            new Sugestion<>(user.getCurrentState(),
                user.getCurrentState(),
                user.getCurrentState().getVersion().isBefore(saved.getCurrentState().getVersion())),
            mealsInOrder.stream().map(meal -> new Sugestion<>(meal.getCurrentState(), meal.getCurrentState(), true))
                .collect(Collectors.toList())
        );
    }

    private void validateItems(List<Meal> mealsInOrder, List<Item> items, Integer id) {
        if (mealsInOrder.stream().anyMatch(meal -> !meal.getRestaurantId().equals(id))) {
            throw new IllegalArgumentException("Wrong restaurant in one of meals");
        }

        items.forEach(item -> {
            Meal meal = mealsInOrder.stream().filter(it -> it.getId().equals(item.getId())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("No meal with id " + item.getId()));

            item.getAddons().forEach(addon -> {
                if (meal.getAddons().stream().noneMatch(it -> it.getName().equals(addon))) {
                    throw new IllegalArgumentException("No addon with name " + addon);
                }
            });
        });
    }

    public OrderValue update(OrderUpdate update) {
        Order order = orderRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No order with id " + update.getId()));

        List<Meal> mealsInOrder = mealRepository.findByIdIn(
            update.getItems().stream().map(Item::getId).collect(Collectors.toList())
        );

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId())
            .filter(it -> !it.getDeleted())
            .orElseThrow(() -> new IllegalArgumentException("No more restaurant with id " + order.getRestaurantId()));

        User user = userRepository.findById(order.getUserId())
            .filter(it -> !it.getDeleted())
            .orElseThrow(() -> new IllegalArgumentException("No more user with id " + order.getUserId()));


        if (order.getStatus() != Order.Status.DRAFT) {
            throw new IllegalStateException("Wrong status");
        }

        validateItems(mealsInOrder, update.getItems(), restaurant.getId());
        //any validations

        order.update(
            update.getItems()
        );

        Order saved = orderRepository.save(order);
        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCurrentState(),
            new Sugestion<>(restaurant.getCurrentState(),
                restaurant.getCurrentState(),
                restaurant.getCurrentState().getVersion().isBefore(saved.getCurrentState().getVersion())),
            new Sugestion<>(user.getCurrentState(),
                user.getCurrentState(),
                user.getCurrentState().getVersion().isBefore(saved.getCurrentState().getVersion())),
            mealsInOrder.stream().map(meal -> new Sugestion<>(meal.getCurrentState(), meal.getCurrentState(), true))
                .collect(Collectors.toList())
        );
    }

    public OrderValue approve(Integer id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No order with id " + id));

        OrderValue value = mapValue(order);

        if (order.getStatus() != Order.Status.DRAFT) {
            throw new IllegalStateException("Wrong status");
        }

        if (!value.isActual()) {
            throw new IllegalStateException("Order outdated");
        }
        //any validations

        order.approved();

        return mapValue(orderRepository.save(order));
    }

    public OrderValue delivered(Integer id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No order with id " + id));

        if (order.getStatus() != Order.Status.PROCESSING) {
            throw new IllegalStateException("Wrong status");
        }
        //any validations

        order.delivered();

        return mapValue(orderRepository.save(order));
    }
}
