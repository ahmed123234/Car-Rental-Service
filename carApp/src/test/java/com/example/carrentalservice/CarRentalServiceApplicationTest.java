package com.example.carrentalservice;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.Car;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.repositories.*;
import com.example.carrentalservice.services.car.CarServiceImpl;
import com.example.carrentalservice.services.order.RentOrderServiceImpl;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CarRentalServiceApplication.class})
@ExtendWith(SpringExtension.class)
class CarRentalServiceApplicationTest {
    @Autowired
    private CarRentalServiceApplication carRentalServiceApplication;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    /**
     * Method under test: {@link CarRentalServiceApplication#run(AppUserServiceImpl)}
     */
    @Test
    void testRun() throws Exception {

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();

        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("Name");

        UserRole userRole1 = new UserRole();
        userRole1.setId(123L);
        userRole1.setName("Name");
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        when(userRoleRepository.findByName((String) any())).thenReturn(userRole1);
        when(userRoleRepository.save((UserRole) any())).thenReturn(userRole);

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("janedoe");

        AppUser appUser1 = new AppUser();
        appUser1.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setLastName("Doe");
        appUser1.setLocked(true);
        appUser1.setPassword("iloveyou");
        appUser1.setRoles(new ArrayList<>());
        appUser1.setUserId(123L);
        appUser1.setUsername("janedoe");
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findByEmail((String) any())).thenReturn(appUser);
        when(appUserRepository.save((AppUser) any())).thenReturn(appUser1);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        carRentalServiceApplication.run(new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)))).run("foo");
        verify(userRoleRepository, atLeast(1)).findByName((String) any());
        verify(userRoleRepository, atLeast(1)).save((UserRole) any());
        verify(appUserRepository, atLeast(1)).findByEmail((String) any());
        verify(appUserRepository, atLeast(1)).save((AppUser) any());
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#run(AppUserServiceImpl)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRun2() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by run(AppUserServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.saveUser(AppUserServiceImpl.java:366)
        //       at com.example.carrentalservice.CarRentalServiceApplication.lambda$run$0(CarRentalServiceApplication.java:42)
        //   In order to prevent run(AppUserServiceImpl)
        //   from throwing NullPointerException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   run(AppUserServiceImpl).
        //   See https://diff.blue/R013 to resolve this issue.

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();

        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("Name");

        UserRole userRole1 = new UserRole();
        userRole1.setId(123L);
        userRole1.setName("Name");
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        when(userRoleRepository.findByName((String) any())).thenReturn(userRole1);
        when(userRoleRepository.save((UserRole) any())).thenReturn(userRole);

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("janedoe");

        AppUser appUser1 = new AppUser();
        appUser1.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setLastName("Doe");
        appUser1.setLocked(true);
        appUser1.setPassword("iloveyou");
        appUser1.setRoles(new ArrayList<>());
        appUser1.setUserId(123L);
        appUser1.setUsername("janedoe");
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findByEmail((String) any())).thenReturn(appUser);
        when(appUserRepository.save((AppUser) any())).thenReturn(appUser1);
        carRentalServiceApplication.run(new AppUserServiceImpl(userRoleRepository, appUserRepository, null,
                new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)))).run("foo");
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#run(AppUserServiceImpl)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRun3() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by run(AppUserServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.example.carrentalservice.CarRentalServiceApplication.lambda$run$0(CarRentalServiceApplication.java:36)
        //   In order to prevent run(AppUserServiceImpl)
        //   from throwing NullPointerException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   run(AppUserServiceImpl).
        //   See https://diff.blue/R013 to resolve this issue.

        (new CarRentalServiceApplication()).run(null).run("foo");
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#runner(CarServiceImpl)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRunner() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by runner(CarServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.carrentalservice.exception.ApiRequestException: car Mark already taken
        //       at com.example.carrentalservice.services.car.CarServiceImpl.addCar(CarServiceImpl.java:27)
        //       at com.example.carrentalservice.CarRentalServiceApplication.lambda$runner$1(CarRentalServiceApplication.java:70)
        //   In order to prevent runner(CarServiceImpl)
        //   from throwing ApiRequestException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   runner(CarServiceImpl).
        //   See https://diff.blue/R013 to resolve this issue.

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");

        Car car1 = new Car();
        car1.setCarClass("Car Class");
        car1.setCarCost(1L);
        car1.setCarId(123L);
        car1.setCarMark("Car Mark");
        car1.setCarModel("Car Model");
        car1.setCarStatus("Car Status");
        Optional<Car> ofResult = Optional.of(car1);
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.save((Car) any())).thenReturn(car);
        when(carRepository.findByCarMark((String) any())).thenReturn(ofResult);
        carRentalServiceApplication.runner(new CarServiceImpl(carRepository)).run("foo");
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#runner(CarServiceImpl)}
     */
    @Test
    void testRunner2() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by runner(CarServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.save((Car) any())).thenReturn(car);
        when(carRepository.findByCarMark((String) any())).thenReturn(Optional.empty());
        carRentalServiceApplication.runner(new CarServiceImpl(carRepository)).run("foo");
        verify(carRepository, atLeast(1)).save((Car) any());
        verify(carRepository, atLeast(1)).findByCarMark((String) any());
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#home()}
     */
    @Test
    void testHome() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        MockMvcBuilders.standaloneSetup(carRentalServiceApplication)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Hello...."));
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#lineRunner(RentOrderServiceImpl)}
     */
    @Test
    void testLineRunner() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by lineRunner(RentOrderServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("janedoe");
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findByUsername((String) any())).thenReturn(Optional.of(appUser));
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl(userRoleRepository, appUserRepository,
                passwordEncoder, new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)));

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.findByCarId((Long) any())).thenReturn(car);
        carRentalServiceApplication.lineRunner(new RentOrderServiceImpl(appUserServiceImpl,
                        new CarServiceImpl(carRepository), mock(RentOrderRepository.class), mock(RentOrderItemRepository.class)))
                .run("foo");
        verify(appUserRepository, atLeast(1)).findByUsername((String) any());
        verify(carRepository, atLeast(1)).findByCarId((Long) any());
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#lineRunner(RentOrderServiceImpl)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testLineRunner2() throws Exception {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R004 No meaningful assertions found.
        //   Diffblue Cover was unable to create an assertion.
        //   Make sure that fields modified by lineRunner(RentOrderServiceImpl)
        //   have package-private, protected, or public getters.
        //   See https://diff.blue/R004 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.carrentalservice.exception.ApiRequestException: no such user with the given username: ahmad12
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.getUserId(AppUserServiceImpl.java:312)
        //       at com.example.carrentalservice.services.order.RentOrderServiceImpl.createOrder(RentOrderServiceImpl.java:59)
        //       at com.example.carrentalservice.CarRentalServiceApplication.lambda$lineRunner$2(CarRentalServiceApplication.java:95)
        //   In order to prevent lineRunner(RentOrderServiceImpl)
        //   from throwing ApiRequestException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   lineRunner(RentOrderServiceImpl).
        //   See https://diff.blue/R013 to resolve this issue.

        CarRentalServiceApplication carRentalServiceApplication = new CarRentalServiceApplication();
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findByUsername((String) any())).thenReturn(Optional.empty());
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        AppUserServiceImpl appUserServiceImpl = new AppUserServiceImpl(userRoleRepository, appUserRepository,
                passwordEncoder, new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class)));

        Car car = new Car();
        car.setCarClass("Car Class");
        car.setCarCost(1L);
        car.setCarId(123L);
        car.setCarMark("Car Mark");
        car.setCarModel("Car Model");
        car.setCarStatus("Car Status");
        CarRepository carRepository = mock(CarRepository.class);
        when(carRepository.findByCarId((Long) any())).thenReturn(car);
        carRentalServiceApplication.lineRunner(new RentOrderServiceImpl(appUserServiceImpl,
                        new CarServiceImpl(carRepository), mock(RentOrderRepository.class), mock(RentOrderItemRepository.class)))
                .run("foo");
    }

    /**
     * Method under test: {@link CarRentalServiceApplication#home()}
     */
    @Test
    void testHome2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/", "Uri Variables");
        MockMvcBuilders.standaloneSetup(carRentalServiceApplication)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Hello...."));
    }
}

