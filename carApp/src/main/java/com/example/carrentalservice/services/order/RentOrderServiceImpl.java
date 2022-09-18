package com.example.carrentalservice.services.order;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.entities.RentOrderItem;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.repositories.RentOrderItemRepository;
import com.example.carrentalservice.repositories.RentOrderRepository;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RentOrderServiceImpl implements RentOrderService {

    private final AppUserServiceImpl appUserServiceImpl;
    private final CarServiceImpl carServiceImpl;
    private final RentOrderRepository rentOrderRepository;
    private final RentOrderItemRepository rentOrderItemRepository;
    @Override
    public void addOrder(RentOrder order, @NotNull List<Long> carsId) {

        Long orderId = rentOrderRepository.save(order).getOrderId();

        for (Long carId : carsId) {
            if(carServiceImpl.getCar(carId).getCarStatus().equalsIgnoreCase("available")) {
                carServiceImpl.updateCarStatus(carId, "rented");
                RentOrderItem item = new RentOrderItem(orderId, carId);
                rentOrderItemRepository.save(item);
            }
        }
    }

    @Override
    public String createOrder (String username, @NotNull RentOrderRequest rentOrderRequest, Long [] carId) {

        String message;
        List<Long> carsId = new ArrayList<>();
        Collections.addAll(carsId, carId);

                RentOrder rentOrder = new RentOrder();
                rentOrder.setOrderStatus("requested");
                rentOrder.setOrderDriver(rentOrderRequest.getOrderDriver());
                rentOrder.setOrderStartDate(rentOrderRequest.getOrderStartDate());
                rentOrder.setOrderFinishDate(rentOrderRequest.getOrderFinishDate());
                rentOrder.setUserId(appUserServiceImpl.getUserId(username));
                System.out.println(username + "username of the user want the order");

                long days = ChronoUnit.DAYS.between(rentOrder.getOrderStartDate().toLocalDate(),
                        rentOrder.getOrderFinishDate().toLocalDate());

                if (days >= 1) {
                    long bill = 0;

                    // calculate the price
                    for (Long car : carsId) {
                        if(carServiceImpl.getCar(car).getCarStatus().equalsIgnoreCase("available")) {
                            bill = bill + (carServiceImpl.getCar(car).getCarCost()) * days;
                        }
                    }
                    if (bill!=0 && rentOrder.getOrderDriver().equals("yes")) {
                        bill = bill + (days * 35);
                    }
                    if (bill == 0) {
                        log.info("Thanks {} for using the app. unlucky, the selected cars is rented recently. ",
                                username);
                        return "The selected Cars is not available right now, please try again later";
                    }
                    rentOrder.setOrderBill((int) bill);
                    addOrder(rentOrder, carsId);
                    log.info("Thanks {}, your order created successfully", username);
                    message = "Order created successfully";
                }else {
                    log.info("Thanks {}, unlucky, the entered duration is invalid", username);
                    message = "Invalid rent duration";
                }
        return message;
    }

    @Override
    public List<RentOrder> getUserOrders(Long userId) {

        return rentOrderRepository.findAllByUserId(userId);
    }

    @Override
    public RentOrder getOrderById(Long orderId) {
        return rentOrderRepository.findByOrderId(orderId);
    }

    @Override
    public List<RentOrder> getAllOrders() {
        return rentOrderRepository.findAllOrders();
    }

    //update order status requested or canceled
    @Override
    public String updateOrderStatus(Long orderId, String status) {

        rentOrderRepository.updateOrderStatus(orderId, status);
        return "the selected order updated successfully.";
    }
    @Override
    public List<RentOrder> getOrdersByStatus(String status) {
       return rentOrderRepository.findByOrderStatus(status);
    }

    @Override
    public List<Car> getOrderItems(Long orderId) {
        return rentOrderRepository.getOrderItems(orderId);
    }
}
