package crow.teomant.delivery.order.service;

import crow.teomant.delivery.meal.model.Meal;
import crow.teomant.delivery.meal.model.MealRepository;
import crow.teomant.delivery.order.model.Order;
import crow.teomant.delivery.order.model.OrderRepository;
import crow.teomant.delivery.restaurant.model.Restaurant;
import crow.teomant.delivery.restaurant.model.RestaurantRepository;
import crow.teomant.delivery.user.model.User;
import crow.teomant.delivery.user.model.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
            saved.getItems().stream().map(Order.Item::getId).collect(Collectors.toList())
        );

        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCreated(),
            saved.getApproved(),
            saved.getDelivered(),
            saved.getStatus(),
            new IsActual<>(
                saved.getState().getRestaurantName(),
                restaurant.map(Restaurant::getName)
                    .map(name -> Objects.equals(name, saved.getState().getRestaurantName()))
                    .orElse(false)
            ),
            new IsActual<>(
                saved.getState().getUserName(),
                user.map(User::getName)
                    .map(name -> Objects.equals(name, saved.getState().getUserName()))
                    .orElse(false)
            ),
            saved.getState().getItems().stream()
                .map(item -> new IsActual<>(
                        item,
                        isActualItem(mealsInOrder, item)
                    )
                )
                .collect(Collectors.toList())
        );
    }

    private boolean isActualItem(List<Meal> mealsInOrder, Order.ItemState item) {
        return mealsInOrder.stream().anyMatch(meal ->
            item.getId().equals(meal.getId())
                && item.getName().equals(meal.getName())
                && item.getPrice().compareTo(meal.getPrice()) == 0
                && item.getAddons().stream().allMatch(addon ->
                meal.getAddons().stream().anyMatch(existing ->
                    existing.getName().equals(addon.getName())
                        && existing.getLabel().equals(addon.getLabel())
                        && existing.getPrice().compareTo(addon.getPrice()) == 0
                )
            )
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
            .orElseThrow(() -> new IllegalArgumentException("No restaurant with id " + create.getRestaurantId()));

        User user = userRepository.findById(create.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("No user with id " + create.getUserId()));

        List<Meal> mealsInOrder = mealRepository.findByIdIn(
            create.getItems().stream().map(Order.Item::getId).collect(Collectors.toList())
        );

        validateItems(mealsInOrder, create.getItems(), restaurant.getId());

        //any other validations

        Order saved = orderRepository.save(
            new Order(
                create.getRestaurantId(),
                create.getUserId(),
                create.getItems(),
                new Order.State(
                    restaurant.getId(),
                    restaurant.getName(),
                    user.getId(),
                    user.getName(),
                    create.getItems().stream().map(item -> getItemState(mealsInOrder, item))
                        .collect(Collectors.toList()),
                    LocalDate.now()
                )
            )
        );

        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCreated(),
            saved.getApproved(),
            saved.getDelivered(),
            saved.getStatus(),
            new IsActual<>(saved.getState().getRestaurantName(), true),
            new IsActual<>(saved.getState().getUserName(), true),
            saved.getState().getItems().stream().map(item -> new IsActual<>(item, true))
                .collect(Collectors.toList())
        );
    }

    private void validateItems(List<Meal> mealsInOrder, List<Order.Item> items, Integer id) {
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

    private Order.ItemState getItemState(List<Meal> mealsInOrder, Order.Item item) {
        Meal meal = mealsInOrder.stream().filter(it -> it.getId().equals(item.getId())).findAny().get();

        return new Order.ItemState(
            meal.getId(),
            meal.getName(),
            meal.getPrice(),
            item.getAddons().stream().map(addon -> {
                Meal.Addon found =
                    meal.getAddons().stream().filter(it -> it.getName().equals(addon)).findAny().get();
                return new Order.AddonState(found.getName(), found.getLabel(), found.getPrice(), LocalDate.now());
            }).collect(Collectors.toList()),
            LocalDate.now()
        );
    }

    public OrderValue update(OrderUpdate update) {
        Order order = orderRepository.findById(update.getId())
            .orElseThrow(() -> new IllegalArgumentException("No order with id " + update.getId()));

        List<Meal> mealsInOrder = mealRepository.findByIdIn(
            update.getItems().stream().map(Order.Item::getId).collect(Collectors.toList())
        );

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId())
            .orElseThrow(() -> new IllegalArgumentException("No more restaurant with id " + order.getRestaurantId()));

        User user = userRepository.findById(order.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("No more user with id " + order.getUserId()));


        if (order.getStatus() != Order.Status.DRAFT) {
            throw new IllegalStateException("Wrong status");
        }

        validateItems(mealsInOrder, update.getItems(), restaurant.getId());
        //any validations

        order.update(
            update.getItems(),
            new Order.State(
                restaurant.getId(),
                restaurant.getName(),
                user.getId(),
                user.getName(),
                update.getItems().stream().map(item -> getItemState(mealsInOrder, item))
                    .collect(Collectors.toList()),
                LocalDate.now()
            )
        );

        Order saved = orderRepository.save(order);
        return new OrderValue(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getUserId(),
            saved.getCreated(),
            saved.getApproved(),
            saved.getDelivered(),
            saved.getStatus(),
            new IsActual<>(saved.getState().getRestaurantName(), true),
            new IsActual<>(saved.getState().getUserName(), true),
            saved.getState().getItems().stream().map(item -> new IsActual<>(item, true))
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

    @Transactional
    public void delete(Integer id) {
        //any validations

        mealRepository.delete(id);
    }
}
