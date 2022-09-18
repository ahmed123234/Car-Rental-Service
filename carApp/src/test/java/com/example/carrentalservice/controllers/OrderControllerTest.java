package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.RentOrder;
import com.example.carrentalservice.models.entities.RentOrderItem;
import com.example.carrentalservice.models.handelers.AppUserRequest;
import com.example.carrentalservice.models.handelers.RentOrderRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.*;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.example.carrentalservice.services.order.RentOrderServiceImpl;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest
class OrderControllerTest {
    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private AppUserServiceImpl appUserServiceImpl;

    @Autowired
    private OrderController orderController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RentOrderServiceImpl rentOrderServiceImpl;

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder() {
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl(userRoleRepository, appUserRepository,
                passwordEncoder, new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)));

        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        UserRoleRepository userRoleRepository1 = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository1 = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder1 = new PasswordEncoder();
        OrderController orderController = new OrderController(rentOrderServiceImpl,
                new AppUserServiceImpl(userRoleRepository1, appUserRepository1, passwordEncoder1,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThrows(ApiRequestException.class, () -> orderController.createOrder(request,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{123L}));
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder2() {
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl(userRoleRepository, appUserRepository,
                passwordEncoder, new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)));

        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        UserRoleRepository userRoleRepository1 = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository1 = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder1 = new PasswordEncoder();
        OrderController orderController = new OrderController(rentOrderServiceImpl,
                new AppUserServiceImpl(userRoleRepository1, appUserRepository1, passwordEncoder1,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        assertThrows(IllegalArgumentException.class, () -> orderController.createOrder(null,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{1L}));
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder3() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        OrderController orderController = new OrderController(rentOrderServiceImpl,
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        MockHttpServletRequest request = new MockHttpServletRequest();
        assertThrows(ApiRequestException.class, () -> orderController.createOrder(request,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{123L}));
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder5() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        OrderController orderController = new OrderController(rentOrderServiceImpl,
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("https://example.org/example");
        assertThrows(ApiRequestException.class, () -> orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{123L}));
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder6() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        OrderController orderController = new OrderController(rentOrderServiceImpl,
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        assertEquals(0, actualCreateOrderResult.getData().size());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder7() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        OrderController orderController = new OrderController(
                new RentOrderServiceImpl(appUserServiceImpl, new CarServiceImpl(mock(CarRepository.class)),
                        mock(RentOrderRepository.class), mock(RentOrderItemRepository.class)),
                null, mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class)), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        assertEquals(0, actualCreateOrderResult.getData().size());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder8() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.getUserId((String) any())).thenReturn(123L);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        AppUserServiceImpl appUserServiceImpl1 = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl1.handleAuthorizationHeader((String) any())).thenReturn("JaneDoe");
        OrderController orderController = new OrderController(rentOrderServiceImpl, appUserServiceImpl1,
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        ObjectNode data = actualCreateOrderResult.getData();
        assertEquals("{\r\n  \"message\" : \"Invalid rent duration\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserServiceImpl).getUserId((String) any());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(appUserServiceImpl1).handleAuthorizationHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
        verify(date).toLocalDate();
        verify(date1).toLocalDate();
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder9() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.getUserId((String) any())).thenReturn(123L);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = new RentOrderServiceImpl(appUserServiceImpl,
                new CarServiceImpl(mock(CarRepository.class)), mock(RentOrderRepository.class),
                mock(RentOrderItemRepository.class));

        AppUserServiceImpl appUserServiceImpl1 = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl1.handleAuthorizationHeader((String) any())).thenReturn("JaneDoe");
        OrderController orderController = new OrderController(rentOrderServiceImpl, appUserServiceImpl1,
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenThrow(new ApiRequestException("An error occurred"));
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        assertEquals(0, actualCreateOrderResult.getData().size());
        verify(appUserServiceImpl).getUserId((String) any());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(appUserServiceImpl1).handleAuthorizationHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
        verify(date).toLocalDate();
        verify(date1).toLocalDate();
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder10() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.getUserId((String) any())).thenReturn(123L);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        AppUserServiceImpl appUserServiceImpl1 = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl1.handleAuthorizationHeader((String) any())).thenReturn("JaneDoe");
        OrderController orderController = new OrderController(null, appUserServiceImpl1, mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        assertEquals(0, actualCreateOrderResult.getData().size());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(appUserServiceImpl1).handleAuthorizationHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder11() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.getUserId((String) any())).thenReturn(123L);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = mock(RentOrderServiceImpl.class);
        when(rentOrderServiceImpl.createOrder((String) any(), (RentOrderRequest) any(), (Long[]) any()))
                .thenReturn("Create Order");
        AppUserServiceImpl appUserServiceImpl1 = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl1.handleAuthorizationHeader((String) any())).thenReturn("JaneDoe");
        OrderController orderController = new OrderController(rentOrderServiceImpl, appUserServiceImpl1,
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        ObjectNode data = actualCreateOrderResult.getData();
        assertEquals("{\r\n  \"message\" : \"Create Order\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(rentOrderServiceImpl).createOrder((String) any(), (RentOrderRequest) any(), (Long[]) any());
        verify(appUserServiceImpl1).handleAuthorizationHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#createOrder(HttpServletRequest, RentOrderRequest, Long[])}
     */
    @Test
    void testCreateOrder12() {
        AppUserServiceImpl appUserServiceImpl = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl.getUserId((String) any())).thenReturn(123L);
        when(appUserServiceImpl.addUser((AppUserRequest) any())).thenReturn("Add User");
        appUserServiceImpl.addUser(new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                new String[]{"Authorization"}));
        RentOrderServiceImpl rentOrderServiceImpl = mock(RentOrderServiceImpl.class);
        when(rentOrderServiceImpl.createOrder((String) any(), (RentOrderRequest) any(), (Long[]) any())).thenReturn(null);
        AppUserServiceImpl appUserServiceImpl1 = mock(AppUserServiceImpl.class);
        when(appUserServiceImpl1.handleAuthorizationHeader((String) any())).thenReturn("JaneDoe");
        OrderController orderController = new OrderController(rentOrderServiceImpl, appUserServiceImpl1,
                mock(AppUserRepository.class));
        DefaultMultipartHttpServletRequest defaultMultipartHttpServletRequest = mock(
                DefaultMultipartHttpServletRequest.class);
        when(defaultMultipartHttpServletRequest.getHeader((String) any())).thenReturn("Bearer ");
        Date date = mock(Date.class);
        when(date.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        Date date1 = mock(Date.class);
        when(date1.toLocalDate()).thenReturn(LocalDate.ofEpochDay(1L));
        RestResponse actualCreateOrderResult = orderController.createOrder(defaultMultipartHttpServletRequest,
                new RentOrderRequest("Order Driver", date, date1), new Long[]{123L});
        assertEquals(HttpStatus.OK, actualCreateOrderResult.getStatus());
        assertEquals(1, actualCreateOrderResult.getData().size());
        verify(appUserServiceImpl).addUser((AppUserRequest) any());
        verify(rentOrderServiceImpl).createOrder((String) any(), (RentOrderRequest) any(), (Long[]) any());
        verify(appUserServiceImpl1).handleAuthorizationHeader((String) any());
        verify(defaultMultipartHttpServletRequest).getHeader((String) any());
    }

    /**
     * Method under test: {@link OrderController#getByOrderStatus(String)}
     */
    @Test
    void testGetByOrderStatus() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getOrdersByStatus((String) any())).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/status")
                .param("status", "foo");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\":10,\"orderFinishDate\":10}]"));
    }

    /**
     * Method under test: {@link OrderController#getByOrderStatus(String)}
     */
    @Test
    void testGetByOrderStatus2() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenThrow(new ApiRequestException("An error occurred"));

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getOrdersByStatus((String) any())).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/status")
                .param("status", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\"}]"));
    }

    /**
     * Method under test: {@link OrderController#getByOrderStatus(String)}
     */
    @Test
    void testGetByOrderStatus3() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);
        Date date2 = mock(Date.class);
        when(date2.getTime()).thenReturn(10L);
        Date date3 = mock(Date.class);
        when(date3.getTime()).thenReturn(10L);

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("?");
        rentOrder1.setOrderFinishDate(date2);
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(date3);
        rentOrder1.setOrderStatus("?");
        rentOrder1.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder1);
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getOrdersByStatus((String) any())).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/status")
                .param("status", "foo");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\":10,\"orderFinishDate\":10},{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,"
                                        + "\"orderStatus\":\"?\",\"orderDriver\":\"?\",\"orderStartDate\":10,\"orderFinishDate\":10}]"));
    }

    /**
     * Method under test: {@link OrderController#getByOrderStatus(String)}
     */
    @Test
    void testGetByOrderStatus4() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);
        Date date2 = mock(Date.class);
        when(date2.getTime()).thenReturn(10L);
        Date date3 = mock(Date.class);
        when(date3.getTime()).thenThrow(new ApiRequestException(""));

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("?");
        rentOrder1.setOrderFinishDate(date2);
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(date3);
        rentOrder1.setOrderStatus("?");
        rentOrder1.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder1);
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getOrdersByStatus((String) any())).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/status")
                .param("status", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\"}]"));
    }

    /**
     * Method under test: {@link OrderController#getOrder(Long)}
     */
    @Test
    void testGetOrder() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderServiceImpl.getOrderById((Long) any())).thenReturn(rentOrder);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/admin/get{id}", 123L);
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"Order Status\",\"orderDriver\":\"Order"
                                        + " Driver\",\"orderStartDate\":10,\"orderFinishDate\":10}"));
    }

    /**
     * Method under test: {@link OrderController#getOrder(Long)}
     */
    @Test
    void testGetOrder2() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenThrow(new ApiRequestException("An error occurred"));

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderServiceImpl.getOrderById((Long) any())).thenReturn(rentOrder);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/admin/get{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"Order Status\",\"orderDriver\":\"Order"
                                        + " Driver\",\"orderStartDate\"}"));
    }

    /**
     * Method under test: {@link OrderController#getOrder(Long)}
     */
    @Test
    void testGetOrder3() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);

        RentOrderItem rentOrderItem = new RentOrderItem();
        rentOrderItem.setCarId(123L);
        rentOrderItem.setOrderId(123L);
        rentOrderItem.setOrderItemId(123L);

        HashSet<RentOrderItem> rentOrderItemSet = new HashSet<>();
        rentOrderItemSet.add(rentOrderItem);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(rentOrderItemSet);
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderServiceImpl.getOrderById((Long) any())).thenReturn(rentOrder);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/admin/get{id}", 123L);
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderId\":123,\"userId\":123,\"orderItems\":[{\"carId\":123,\"orderId\":123,\"orderItemId\":123}],\"orderBill"
                                        + "\":1,\"orderStatus\":\"Order Status\",\"orderDriver\":\"Order Driver\",\"orderStartDate\":10,\"orderFinishDate"
                                        + "\":10}"));
    }

    /**
     * Method under test: {@link OrderController#getOrder(Long)}
     */
    @Test
    void testGetOrder4() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenThrow(new ApiRequestException(null));

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderServiceImpl.getOrderById((Long) any())).thenReturn(rentOrder);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/admin/get{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"Order Status\",\"orderDriver\":\"Order"
                                        + " Driver\",\"orderStartDate\"}"));
    }

    /**
     * Method under test: {@link OrderController#getOrder(Long)}
     */
    @Test
    void testGetOrder5() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenThrow(new ApiRequestException(""));

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("Order Driver");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("Order Status");
        rentOrder.setUserId(123L);
        when(rentOrderServiceImpl.getOrderById((Long) any())).thenReturn(rentOrder);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/admin/get{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"Order Status\",\"orderDriver\":\"Order"
                                        + " Driver\",\"orderStartDate\"}"));
    }


    /**
     * Method under test: {@link OrderController#getOrders()}
     */
    @Test
    void testGetOrders1() throws Exception {
        List<RentOrder> orders = new ArrayList<>();
        Mockito.when(rentOrderServiceImpl.getAllOrders()).thenReturn(orders);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/orders").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", "There is no orders yet." );
        ResponseEntity response = ResponseEntity.ok().body(new RestResponse(
                objectNode,
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        ));
        when(orderController.getOrders()).thenReturn(response);

        assertEquals(response,orderController.getOrders());
    }



    @Test
    void testGetOrders() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getAllOrders()).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\":10,\"orderFinishDate\":10}]"));
    }

    /**
     * Method under test: {@link OrderController#getOrders()}
     */
    @Test
    void testGetOrders2() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenThrow(new ApiRequestException("An error occurred"));

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getAllOrders()).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\"}]"));
    }

    /**
     * Method under test: {@link OrderController#getOrders()}
     */
    @Test
    void testGetOrders3() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);
        Date date2 = mock(Date.class);
        when(date2.getTime()).thenReturn(10L);
        Date date3 = mock(Date.class);
        when(date3.getTime()).thenReturn(10L);

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("?");
        rentOrder1.setOrderFinishDate(date2);
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(date3);
        rentOrder1.setOrderStatus("?");
        rentOrder1.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder1);
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getAllOrders()).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\":10,\"orderFinishDate\":10},{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,"
                                        + "\"orderStatus\":\"?\",\"orderDriver\":\"?\",\"orderStartDate\":10,\"orderFinishDate\":10}]"));
    }

    /**
     * Method under test: {@link OrderController#getOrders()}
     */
    @Test
    void testGetOrders4() throws Exception {
        Date date = mock(Date.class);
        when(date.getTime()).thenReturn(10L);
        Date date1 = mock(Date.class);
        when(date1.getTime()).thenReturn(10L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.setOrderBill(1);
        rentOrder.setOrderDriver("?");
        rentOrder.setOrderFinishDate(date);
        rentOrder.setOrderId(123L);
        rentOrder.setOrderItems(new HashSet<>());
        rentOrder.setOrderStartDate(date1);
        rentOrder.setOrderStatus("?");
        rentOrder.setUserId(123L);
        Date date2 = mock(Date.class);
        when(date2.getTime()).thenReturn(10L);
        Date date3 = mock(Date.class);
        when(date3.getTime()).thenThrow(new ApiRequestException(""));

        RentOrder rentOrder1 = new RentOrder();
        rentOrder1.setOrderBill(1);
        rentOrder1.setOrderDriver("?");
        rentOrder1.setOrderFinishDate(date2);
        rentOrder1.setOrderId(123L);
        rentOrder1.setOrderItems(new HashSet<>());
        rentOrder1.setOrderStartDate(date3);
        rentOrder1.setOrderStatus("?");
        rentOrder1.setUserId(123L);

        ArrayList<RentOrder> rentOrderList = new ArrayList<>();
        rentOrderList.add(rentOrder1);
        rentOrderList.add(rentOrder);
        when(rentOrderServiceImpl.getAllOrders()).thenReturn(rentOrderList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"orderId\":123,\"userId\":123,\"orderItems\":[],\"orderBill\":1,\"orderStatus\":\"?\",\"orderDriver\":\"?\","
                                        + "\"orderStartDate\"}]"));
    }

    /**
     * Method under test: {@link OrderController#getUserOrder(HttpServletRequest, Long)}
     */
    @Test
    void testGetUserOrder() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/orders/customer/get");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("orderId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testGetUserOrder1() throws Exception {
        when(appUserServiceImpl.handleAuthorizationHeader(anyString())).thenReturn("ahmad");
        String username= "ahmad";
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(mock(AppUser.class));
        Long userId = 1L;
        Mockito.when(rentOrderServiceImpl.getUserOrders(userId))
                .thenReturn(new ArrayList<>());
        RentOrder order = mock(RentOrder.class);
        Mockito.when(rentOrderServiceImpl.getOrderById(anyLong())).thenReturn(order);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/orders/customer/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        HttpServletRequest request = mock(HttpServletRequest.class);
        orderController.getUserOrder(request, anyLong());
        Mockito.verify(rentOrderServiceImpl).getOrderById(anyLong());

    }

    /**
     * Method under test: {@link OrderController#getUserOrders(HttpServletRequest)}
     */
    @Test
    void testGetUserOrders() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/orders/user");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link OrderController#getUserOrders(HttpServletRequest)}
     */
    @Test
    void testGetUserOrders2() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/orders/user");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }




    /**
     * Method under test: {@link OrderController#updateOrderStatus(Long, String)}
     */
    @Test
    void testUpdateOrderStatus() throws Exception {
        Mockito.when(rentOrderServiceImpl.updateOrderStatus(anyLong(), anyString()))
                .thenReturn("the selected order updated successfully.");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/orders/update").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
       orderController.updateOrderStatus(anyLong(), anyString());
        Mockito.verify(rentOrderServiceImpl).updateOrderStatus(anyLong(), anyString());

    }

    /**
     * Method under test: {@link OrderController#getOrderItems(Long)}
     */
    @Test
    void testGetOrderItems1() throws Exception {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());

        Mockito.when(rentOrderServiceImpl.getOrderItems(anyLong())).thenReturn(cars);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/orders/items/get").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        orderController.getOrderItems(anyLong());
        Mockito.verify(rentOrderServiceImpl).getOrderItems(anyLong());

    }
}

