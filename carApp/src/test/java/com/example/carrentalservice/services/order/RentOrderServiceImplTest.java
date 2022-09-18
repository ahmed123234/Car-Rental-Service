package com.example.carrentalservice.services.order;

import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.entities.RentOrderItem;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.repositories.RentOrderItemRepository;
import com.example.carrentalservice.repositories.RentOrderRepository;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RentOrderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class RentOrderServiceImplTest {
    @MockBean
    private AppUserServiceImpl appUserServiceImpl;

    @MockBean
    private CarServiceImpl carServiceImpl;

    @MockBean
    private RentOrderItemRepository rentOrderItemRepository;

    @MockBean
    private RentOrderRepository rentOrderRepository;

    @Autowired
    private RentOrderServiceImpl rentOrderServiceImpl;

    /**
     * Method under test: {@link RentOrderServiceImpl#addOrder(RentOrder, List)}
     */
    @Test
    void testAddOrder() {
        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(mock(Date.class));
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(mock(Date.class));
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderRepository.save(any())).thenReturn(rentOrder);

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("Order Driver");
        rentOrder1.setOrderFinishDate(mock(Date.class));
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(mock(Date.class));
        rentOrder1.setOrderStatus("Order Status");
        rentOrder1.setUserId(123L);
        rentOrderServiceImpl.addOrder(rentOrder1, new ArrayList<>());
        verify(rentOrderRepository).save(any());
        assertEquals(1, rentOrder1.getOrderBill());
        assertEquals(123L, rentOrder1.getUserId().longValue());
        assertEquals("Order Status", rentOrder1.getOrderStatus());
        assertTrue(rentOrder1.getOrderItems().isEmpty());
        assertEquals(123L, rentOrder1.getOrderId().longValue());
        assertEquals("Order Driver", rentOrder1.getOrderDriver());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#addOrder(RentOrder, List)}
     */
    @Test
    void testAddOrder2() {
        Long carId  =1L;
        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("available");

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(mock(Date.class));
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(mock(Date.class));
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderRepository.save(any())).thenReturn(rentOrder);

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("Order Driver");
        rentOrder1.setOrderFinishDate(mock(Date.class));
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(mock(Date.class));
        rentOrder1.setOrderStatus("Order Status");
        rentOrder1.setUserId(123L);

        when(carServiceImpl.getCar(any())).thenReturn(car);
        if(car.getCarStatus().equalsIgnoreCase("available")) {
            when(carServiceImpl.updateCarStatus(carId, "rented")).thenReturn("status updated successfully");
            RentOrderItem item = new RentOrderItem(1L, carId);
            when(rentOrderItemRepository.save(item)).thenReturn(item);
        }

        ArrayList<Long> resultLongList = new ArrayList<>();
        resultLongList.add(1L);
        rentOrderServiceImpl.addOrder(rentOrder1, resultLongList);
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder() {
        when(appUserServiceImpl.getUserId(any())).thenReturn(123L);
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        assertEquals("Invalid rent duration", rentOrderServiceImpl.createOrder("jane_doe",
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L}));
        verify(appUserServiceImpl).getUserId(any());
        verify(date).toLocalDate();
        verify(date1).toLocalDate();
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder2() {
        when(appUserServiceImpl.getUserId(any())).thenReturn(123L);
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenThrow(new IllegalArgumentException("requested"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.createOrder("jane_doe",
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L}));
        verify(appUserServiceImpl).getUserId(any());
        verify(date).toLocalDate();
        verify(date1).toLocalDate();
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder3() {
        when(appUserServiceImpl.getUserId(any())).thenReturn(123L);

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        when(carServiceImpl.getCar(any())).thenReturn(car);
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(-1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        assertEquals("The selected Cars is not available right now, please try again later",
                rentOrderServiceImpl.createOrder("jane_doe",
                        new RentOrderRequest("Order Driver", date, date1), new Long[]{123L}));
        verify(appUserServiceImpl).getUserId(any());
        verify(carServiceImpl).getCar(any());
        verify(date).toLocalDate();
        verify(date1).toLocalDate();
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder4() {
        //given
        String username = "ahmad_1323";
        Date date = new Date(1L);
        Date date1 = new Date(3L);
        RentOrderRequest orderRequest  = new RentOrderRequest("No", date, date1);
        Long[] carId = new Long[]{123L};
//        doNothing().when(orderRequest).getOrderDriver();
//        doNothing().when(orderRequest).getOrderStartDate();
//        doNothing().when(orderRequest).getOrderFinishDate();

        when(appUserServiceImpl.getUserId(username)).thenReturn(123L);
        RentOrder rentOrder  = new RentOrder();
        rentOrder.setOrderBill(0);
        rentOrder.setOrderDriver(orderRequest.getOrderDriver());
        rentOrder.setOrderFinishDate(orderRequest.getOrderFinishDate());
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(orderRequest.getOrderStartDate());
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);


        Car car = mock(Car.class);
        when(carServiceImpl.getCar(any())).thenReturn(car);
        doNothing().when(car).setCarClass(any());
        doNothing().when(car).setCarCost(any());
        doNothing().when(car).setCarId(any());
        doNothing().when(car).setCarMark(any());
        doNothing().when(car).setCarModel(any());
        doNothing().when(car).setCarStatus("available");
        car.setCarClass("car class");
        car.setCarCost(100L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("available");


        long days = ChronoUnit.DAYS.between(date1.toLocalDate(), date.toLocalDate());
        if ( days >= 1) {
            long bill = 0;
            when(car.getCarStatus()).thenReturn("available");
            when(carServiceImpl.getCar(car.getCarId()).getCarCost()).thenReturn(car.getCarCost());
            bill = bill + car.getCarCost() * days;

            doNothing().when(rentOrder).setOrderBill((int) bill);
            rentOrder.setOrderBill((int) bill);
            ArrayList<Long> resultLongList = new ArrayList<>();
            resultLongList.add(1L);
            doNothing().when(rentOrderServiceImpl).addOrder(rentOrder,resultLongList);

            assertEquals("Order created successfully", rentOrderServiceImpl.
                    createOrder(username, orderRequest, carId));
        }
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder5() {
        when(appUserServiceImpl.getUserId(any())).thenReturn(123L);
        Car car = mock(Car.class);
        when(car.getCarStatus()).thenReturn("Car Status");
        doNothing().when(car).setCarClass(any());
        doNothing().when(car).setCarCost(any());
        doNothing().when(car).setCarId(any());
        doNothing().when(car).setCarMark(any());
        doNothing().when(car).setCarModel(any());
        doNothing().when(car).setCarStatus(any());
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        when(carServiceImpl.getCar(any())).thenReturn(car);
        assertThrows(IllegalArgumentException.class,
                () -> rentOrderServiceImpl.createOrder("jane_doe", null, new Long[]{123L}));
        verify(car).setCarClass(any());
        verify(car).setCarCost(any());
        verify(car).setCarId(any());
        verify(car).setCarMark(any());
        verify(car).setCarModel(any());
        verify(car).setCarStatus(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#createOrder(String, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder6() {
        when(appUserServiceImpl.getUserId(any())).thenReturn(123L);
        Car car = mock(Car.class);
        when(car.getCarStatus()).thenReturn("Car Status");
        doNothing().when(car).setCarClass(any());
        doNothing().when(car).setCarCost(any());
        doNothing().when(car).setCarId(any());
        doNothing().when(car).setCarMark(any());
        doNothing().when(car).setCarModel(any());
        doNothing().when(car).setCarStatus(any());
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        when(carServiceImpl.getCar(any())).thenReturn(car);
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenThrow(new IllegalArgumentException("foo"));
        RentOrderRequest rentOrderRequest = mock(RentOrderRequest.class);
        when(rentOrderRequest.getOrderDriver()).thenReturn("Order Driver");
        when(rentOrderRequest.getOrderFinishDate()).thenReturn(mock(Date.class));
        when(rentOrderRequest.getOrderStartDate()).thenReturn(date);
        assertThrows(IllegalArgumentException.class,
                () -> rentOrderServiceImpl.createOrder("jane_doe", rentOrderRequest, new Long[]{123L}));
        verify(appUserServiceImpl).getUserId(any());
        verify(car).setCarClass(any());
        verify(car).setCarCost(any());
        verify(car).setCarId(any());
        verify(car).setCarMark(any());
        verify(car).setCarModel(any());
        verify(car).setCarStatus(any());
        verify(rentOrderRequest).getOrderDriver();
        verify(rentOrderRequest).getOrderFinishDate();
        verify(rentOrderRequest).getOrderStartDate();
        verify(date).toLocalDate();
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getUserOrders(Long)}
     */
    @Test
    void testGetUserOrders() {
        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        when(rentOrderRepository.findAllByUserId(any())).thenReturn(rentOrderList);
        List<RentOrder> actualUserOrders = rentOrderServiceImpl.getUserOrders(123L);
        assertSame(rentOrderList, actualUserOrders);
        assertTrue(actualUserOrders.isEmpty());
        verify(rentOrderRepository).findAllByUserId(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getUserOrders(Long)}
     */
    @Test
    void testGetUserOrders2() {
        when(rentOrderRepository.findAllByUserId(any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.getUserOrders(123L));
        verify(rentOrderRepository).findAllByUserId(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getUserOrders(Long)}
     */
    @Test
    void testGetUserOrders3() {
        //given
        Long userId = 1L;
        //when
        rentOrderServiceImpl.getUserOrders(userId);
        //then
        verify(rentOrderRepository).findAllByUserId(userId);
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderById(Long)}
     */
    @Test
    void testGetOrderById() {
        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(mock(Date.class));
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(mock(Date.class));
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderRepository.findByOrderId(any())).thenReturn(rentOrder);
        assertSame(rentOrder, rentOrderServiceImpl.getOrderById(123L));
        verify(rentOrderRepository).findByOrderId(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderById(Long)}
     */
    @Test
    void testGetOrderById2() {
        when(rentOrderRepository.findByOrderId(any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.getOrderById(123L));
        verify(rentOrderRepository).findByOrderId(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderById(Long)}
     */
    @Test
    void testGetOrderById3() {
        //given
        Long orderId = 1L;
        //when
        rentOrderServiceImpl.getOrderById(orderId);
        //then
        verify(rentOrderRepository).findByOrderId(orderId);
    }


    /**
     * Method under test: {@link RentOrderServiceImpl#getAllOrders()}
     */
    @Test
    void testGetAllOrders() {
        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        when(rentOrderRepository.findAllOrders()).thenReturn(rentOrderList);
        List<RentOrder> actualAllOrders = rentOrderServiceImpl.getAllOrders();
        assertSame(rentOrderList, actualAllOrders);
        assertTrue(actualAllOrders.isEmpty());
        verify(rentOrderRepository).findAllOrders();
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getAllOrders()}
     */
    @Test
    void testGetAllOrders2() {
        when(rentOrderRepository.findAllOrders()).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.getAllOrders());
        verify(rentOrderRepository).findAllOrders();
    }


    /**
     * Method under test: {@link RentOrderServiceImpl#getAllOrders()}
     */
    @Test
    void testGetAllOrders3() {
        //when
        rentOrderServiceImpl.getAllOrders();
        //then
        verify(rentOrderRepository).findAllOrders();
    }



    /**
     * Method under test: {@link RentOrderServiceImpl#updateOrderStatus(Long, String)}
     */
    @Test
    void testUpdateOrderStatus() {
        when(rentOrderRepository.updateOrderStatus(any(), any())).thenReturn(1);
        assertEquals("the selected order updated successfully.", rentOrderServiceImpl.updateOrderStatus(123L, "Status"));
        verify(rentOrderRepository).updateOrderStatus(any(),any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#updateOrderStatus(Long, String)}
     */
    @Test
    void testUpdateOrderStatus2() {
        when(rentOrderRepository.updateOrderStatus(any(), any()))
                .thenThrow(new IllegalArgumentException("the selected order updated successfully."));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.updateOrderStatus(123L, "Status"));
        verify(rentOrderRepository).updateOrderStatus(any(), any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#updateOrderStatus(Long, String)}
     */
    @Test
    void testUpdateOrderStatus3() {
        //given
        Long orderId = 1L;
        String status = "requested";
        //when
        rentOrderServiceImpl.updateOrderStatus(orderId, status);
        //then
        verify(rentOrderRepository).updateOrderStatus(orderId, status);
    }


    /**
     * Method under test: {@link RentOrderServiceImpl#getOrdersByStatus(String)}
     */
    @Test
    void testGetOrdersByStatus() {
        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        when(rentOrderRepository.findByOrderStatus(any())).thenReturn(rentOrderList);
        List<RentOrder> actualOrdersByStatus = rentOrderServiceImpl.getOrdersByStatus("Status");
        assertSame(rentOrderList, actualOrdersByStatus);
        assertTrue(actualOrdersByStatus.isEmpty());
        verify(rentOrderRepository).findByOrderStatus(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getOrdersByStatus(String)}
     */
    @Test
    void testGetOrdersByStatus2() {
        when(rentOrderRepository.findByOrderStatus(any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.getOrdersByStatus("Status"));
        verify(rentOrderRepository).findByOrderStatus(any());
    }

    @Test
    void testGetOrdersByStatus3() {
        //given
        String status = "requested";
        //when
        rentOrderServiceImpl.getOrdersByStatus(status);
        //then
        verify(rentOrderRepository).findByOrderStatus(status);
    }


    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderItems(Long)}
     */
    @Test
    void testGetOrderItems() {
        ArrayList<Car> carList = new ArrayList<>();
        when(rentOrderRepository.getOrderItems(any())).thenReturn(carList);
        List<Car> actualOrderItems = rentOrderServiceImpl.getOrderItems(123L);
        assertSame(carList, actualOrderItems);
        assertTrue(actualOrderItems.isEmpty());
        verify(rentOrderRepository).getOrderItems(any());
    }

    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderItems(Long)}
     */
    @Test
    void testGetOrderItems2() {
        when(rentOrderRepository.getOrderItems(any())).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> rentOrderServiceImpl.getOrderItems(123L));
        verify(rentOrderRepository).getOrderItems(any());
    }


    /**
     * Method under test: {@link RentOrderServiceImpl#getOrderItems(Long)}
     */
    @Test
    void testGetOrderItems3() {
        //given
        Long orderId = 1L;

        //when
        rentOrderServiceImpl.getOrderItems(orderId);

        //then
        verify(rentOrderRepository).getOrderItems(orderId);
    }
}

