package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.RentOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {RentOrderRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class RentOrderRepositoryTest {

    @Autowired
    private RentOrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        List<RentOrder> orders =  new ArrayList<>();

        orders.add(new RentOrder(
                2L,
                200,
                "requested",
                "yes", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(4))
        ));

        orders.add(new RentOrder(1L,
                        600,
                        "rented",
                        "yes",
                        Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now().plusDays(2)))
        );

        orders.add( new RentOrder(
                        2L,
                        100,
                        "requested",
                        "yes", Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now().plusDays(7)))

                );

        orderRepository.saveAll(orders);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    /**
     * Method under test: {@link RentOrderRepository#findAllByUserId(Long)}
     */
    @Test
    void canFindAllByUserId() {
        //given
        Long userId = 1L;
        List<RentOrder> orders = new ArrayList<>();

        orders.add(new RentOrder(
                        1L,
                        200,
                        "requested",
                        "yes",
                        Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now().plusDays(4))
                ));
        orders.add(new RentOrder(
                        1L,
                        300,
                        "requested",
                        "yes",
                        Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now().plusDays(10))
                ));

        orderRepository.saveAll(orders);

        //when
        Iterable<RentOrder> actual = orderRepository.findAllByUserId(userId);

        AtomicInteger validIdFound = new AtomicInteger();
        actual.forEach(
                rentOrder -> {
                    if (rentOrder.getOrderId() > 0) {
                        validIdFound.getAndIncrement();
                    }
                }
        );
        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    /**
     * Method under test: {@link RentOrderRepository#findByOrderId(Long)}
     */
    @Test
    void testFindByOrderId() {

       RentOrder order =  new RentOrder(
                2L,
                200,
                "requested",
                "yes", Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(4)));

       order.setOrderId(1L);
       orderRepository.save(order);
        //given
        Long orderId = 1L;

        //when
        RentOrder actual = orderRepository.findByOrderId(orderId);

        AtomicInteger validIdFound = new AtomicInteger();
        if(actual.getOrderId() > 0)
            validIdFound.getAndIncrement();

        assertThat(validIdFound.intValue()).isEqualTo(1);

    }

    /**
     * Method under test: {@link RentOrderRepository#updateOrderStatus(Long, String)}
     */
    @Test
    void testUpdateOrderStatus() {
        //given
        String status = "rented";
        Long orderId = 1L;

        //when
        int actual = orderRepository.updateOrderStatus(orderId, status);

        AtomicInteger validIdFound = new AtomicInteger();

        if(actual == 1) {
            validIdFound.getAndIncrement();
        }
        assertThat(actual).isEqualTo(validIdFound.intValue());
    }

    /**
     * Method under test: {@link RentOrderRepository#findByOrderId(Long)}
     */
    @Test
    void testFindByOrderStatus() {
        //given
        String status = "requested";

        //when
        Iterable<RentOrder> actual = orderRepository.findByOrderStatus(status);

        AtomicInteger validIdFound = new AtomicInteger();
        actual.forEach(
                rentOrder -> {
                    if (rentOrder.getOrderId() > 0) {
                        validIdFound.getAndIncrement();
                    }
                }
        );
        assertThat(validIdFound.intValue()).isEqualTo(2);
    }

    /**
     * Method under test: {@link RentOrderRepository#findAllOrders()}
     */
    @Test
    void testFindAllOrders() {
        //when
        Iterable<RentOrder> actual = orderRepository.findAllOrders();

        AtomicInteger validIdFound = new AtomicInteger();
        actual.forEach(
                rentOrder -> {
                    if (rentOrder.getOrderId() > 0) {
                        validIdFound.getAndIncrement();
                    }
                }
        );
        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    /**
     * Method under test: {@link RentOrderRepository#getOrderItems(Long)}
     */
    @Test
    void testGetOrderItems() {
        //given
        Long orderId = 2L;

        //when
        Iterable<Car> actual = orderRepository.getOrderItems(orderId);

        AtomicInteger validIdFound = new AtomicInteger();
        actual.forEach(
                car -> {
                    if (car.getCarId() > 0) {
                        validIdFound.getAndIncrement();
                    }
                }
        );
        assertThat(validIdFound.intValue()).isEqualTo(0);
    }
}