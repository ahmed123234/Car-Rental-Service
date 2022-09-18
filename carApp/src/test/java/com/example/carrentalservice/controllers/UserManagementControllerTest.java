package com.example.carrentalservice.controllers;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.models.handelers.AppUserRequest;
import com.example.carrentalservice.models.handelers.RestResponse;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.repositories.ConfirmationTokenRepository;
import com.example.carrentalservice.repositories.UserRoleRepository;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import com.example.carrentalservice.services.user.AppUserService;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ContextConfiguration(classes = {UserManagementController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest
class UserManagementControllerTest {
    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private AppUserService appUserService;

    @Autowired
    private UserManagementController userManagementController;


    @Autowired
    MockMvc mockMvc;

    /**
     * Method under test: {@link UserManagementController#getUser(String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUser() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.carrentalservice.exception.ApiRequestException: No such user with the given credentials janedoe
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.getUser(AppUserServiceImpl.java:168)
        //       at com.example.carrentalservice.controllers.UserManagementController.getUser(UserManagementController.java:63)
        //   In order to prevent getUser(String, String)
        //   from throwing ApiRequestException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   getUser(String, String).
        //   See https://diff.blue/R013 to resolve this issue.

        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.getEncodedPassword((String) any())).thenReturn("secret");
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class))).getUser("janedoe", "iloveyou");
    }

    /**
     * Method under test: {@link UserManagementController#getUser(String, String)}
     */
    @Test
    void testGetUser2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getUser((String) any(), (String) any())).thenReturn("User");
        RestResponse actualUser = (new UserManagementController(appUserService, mock(AppUserRepository.class)))
                .getUser("janedoe", "iloveyou");
        assertEquals(HttpStatus.OK, actualUser.getStatus());
        ObjectNode data = actualUser.getData();
        assertEquals("{\r\n  \"message\" : \"User\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserService).getUser((String) any(), (String) any());
    }

    /**
     * Method under test: {@link UserManagementController#getByUserRole(String)}
     */
    @Test
    void testGetByUserRole() throws Exception {
        when(appUserService.getByUserRole((String) any())).thenReturn(Optional.of(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/role/{userRole}",
                "User Role");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserManagementController#getByUserRole(String)}
     */
    @Test
    void testGetByUserRole2() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        Optional<List<AppUser>> ofResult = Optional.of(appUserList);
        when(appUserService.getByUserRole((String) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/role/{userRole}",
                "User Role");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#getByUserRole(String)}
     */
    @Test
    void testGetByUserRole3() throws Exception {
        when(appUserService.getByUserRole((String) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/role/{userRole}",
                "User Role");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("null"));
    }

    /**
     * Method under test: {@link UserManagementController#getUsers()}
     */
    @Test
    void testGetUsers() throws Exception {
        when(appUserService.getUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserManagementController#getUsers()}
     */
//    @Test
//    void testGetUsers1() throws Exception {
//        Mockito.when(appUserService.getUsers()).thenReturn(null);
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .post("/api/v1/users").accept(MediaType.APPLICATION_JSON)).andReturn();
//
//        System.out.println(mvcResult.getResponse());
//        ObjectNode objectNode = new ObjectMapper().createObjectNode();
//        objectNode.put("message", "no one");
//
//        ResponseEntity response = ResponseEntity.ok().body(new RestResponse(
//                objectNode,
//                HttpStatus.OK,
//                now(ZoneId.of("Z"))
//        ));
////        assertEquals("no one", mvcResult.getResponse().getContentAsString());
//        when(userManagementController.getUsers()).thenReturn(response);
//        assertEquals(response,userManagementController.getUsers());
////        Mockito.verify(appUserService).getUsers();
//    }



    @Test
    void testGetUsers2() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        when(appUserService.getUsers()).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#getUsers()}
     */
    @Test
    void testGetUsers3() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser1);
        appUserList.add(appUser);
        when(appUserService.getUsers()).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true},{"
                                        + "\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe\""
                                        + ",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#updateUserStatus(String, String)}
     */
    @Test
    void testUpdateUserStatus() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

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
        when(appUserRepository.updateStatus((String) any(), anyBoolean())).thenReturn(1);
        when(appUserRepository.findAppUserByUsername((String) any())).thenReturn(appUser);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        RestResponse actualUpdateUserStatusResult = (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class))).updateUserStatus("janedoe", "Status");
        assertEquals(HttpStatus.OK, actualUpdateUserStatusResult.getStatus());
        ObjectNode data = actualUpdateUserStatusResult.getData();
        assertEquals("{\r\n  \"message\" : \"The user with username janedoe is successfully Disabled\"\r\n}",
                data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserRepository).findAppUserByUsername((String) any());
        verify(appUserRepository).updateStatus((String) any(), anyBoolean());
    }

    /**
     * Method under test: {@link UserManagementController#updateUserStatus(String, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserStatus2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.carrentalservice.exception.ApiRequestException: An error occurred
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.changeStatus(AppUserServiceImpl.java:235)
        //       at com.example.carrentalservice.controllers.UserManagementController.updateUserStatus(UserManagementController.java:98)
        //   In order to prevent updateUserStatus(String, String)
        //   from throwing ApiRequestException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   updateUserStatus(String, String).
        //   See https://diff.blue/R013 to resolve this issue.

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
        when(appUserRepository.updateStatus((String) any(), anyBoolean()))
                .thenThrow(new ApiRequestException("An error occurred"));
        when(appUserRepository.findAppUserByUsername((String) any())).thenReturn(appUser);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class))).updateUserStatus("janedoe", "Status");
    }

    /**
     * Method under test: {@link UserManagementController#addAppUser(AppUserRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddAppUser() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class com.example.carrentalservice.models.handelers.AppUserRequest]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.example.carrentalservice.models.handelers.AppUserRequest` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
        //    at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 1, column: 2]
        //       at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:388)
        //       at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.read(AbstractJackson2HttpMessageConverter.java:343)
        //       at org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters(AbstractMessageConverterMethodArgumentResolver.java:185)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.readWithMessageConverters(RequestResponseBodyMethodProcessor.java:160)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.resolveArgument(RequestResponseBodyMethodProcessor.java:133)
        //       at org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)
        //       at org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:179)
        //       at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:146)
        //       at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
        //       at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
        //       at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1070)
        //       at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)
        //       at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
        //       at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:681)
        //       at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
        //       at org.springframework.test.web.servlet.TestDispatcherServlet.service(TestDispatcherServlet.java:72)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
        //       at org.springframework.mock.web.MockFilterChain$ServletFilterProxy.doFilter(MockFilterChain.java:167)
        //       at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java:134)
        //       at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java:199)
        //   In order to prevent addAppUser(AppUserRequest)
        //   from throwing HttpMessageConversionException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   addAppUser(AppUserRequest).
        //   See https://diff.blue/R013 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.carrentalservice.exception.ApiRequestException: User with given email or username is already found. Try again with new email/username
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.addUser(AppUserServiceImpl.java:301)
        //       at com.example.carrentalservice.controllers.UserManagementController.addAppUser(UserManagementController.java:156)
        //   In order to prevent addAppUser(AppUserRequest)
        //   from throwing ApiRequestException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   addAppUser(AppUserRequest).
        //   See https://diff.blue/R013 to resolve this issue.

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

        AppUser appUser2 = new AppUser();
        appUser2.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser2.setEmail("jane.doe@example.org");
        appUser2.setEnabled(true);
        appUser2.setFirstName("Jane");
        appUser2.setLastName("Doe");
        appUser2.setLocked(true);
        appUser2.setPassword("iloveyou");
        appUser2.setRoles(new ArrayList<>());
        appUser2.setUserId(123L);
        appUser2.setUsername("janedoe");
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findAppUserByUsername((String) any())).thenReturn(appUser);
        when(appUserRepository.findByEmail((String) any())).thenReturn(appUser1);
        when(appUserRepository.save((AppUser) any())).thenReturn(appUser2);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        UserManagementController userManagementController = new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class));
        userManagementController.addAppUser(
                new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", new String[]{"Roles"}));
    }

    /**
     * Method under test: {@link UserManagementController#addAppUser(AppUserRequest)}
     */
    @Test
    void testAddAppUser2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class com.example.carrentalservice.models.handelers.AppUserRequest]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.example.carrentalservice.models.handelers.AppUserRequest` (no Creators, like default constructor, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
        //    at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 1, column: 2]
        //       at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:388)
        //       at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.read(AbstractJackson2HttpMessageConverter.java:343)
        //       at org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters(AbstractMessageConverterMethodArgumentResolver.java:185)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.readWithMessageConverters(RequestResponseBodyMethodProcessor.java:160)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.resolveArgument(RequestResponseBodyMethodProcessor.java:133)
        //       at org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)
        //       at org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:179)
        //       at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:146)
        //       at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)
        //       at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
        //       at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
        //       at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1070)
        //       at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963)
        //       at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
        //       at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:681)
        //       at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
        //       at org.springframework.test.web.servlet.TestDispatcherServlet.service(TestDispatcherServlet.java:72)
        //       at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
        //       at org.springframework.mock.web.MockFilterChain$ServletFilterProxy.doFilter(MockFilterChain.java:167)
        //       at org.springframework.mock.web.MockFilterChain.doFilter(MockFilterChain.java:134)
        //       at org.springframework.test.web.servlet.MockMvc.perform(MockMvc.java:199)
        //   In order to prevent addAppUser(AppUserRequest)
        //   from throwing HttpMessageConversionException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   addAppUser(AppUserRequest).
        //   See https://diff.blue/R013 to resolve this issue.

        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.addUser((AppUserRequest) any())).thenReturn("Add User");
        UserManagementController userManagementController = new UserManagementController(appUserService,
                mock(AppUserRepository.class));
        RestResponse actualAddAppUserResult = userManagementController.addAppUser(
                new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", new String[]{"Roles"}));
        assertEquals(HttpStatus.OK, actualAddAppUserResult.getStatus());
        ObjectNode data = actualAddAppUserResult.getData();
        assertEquals("{\r\n  \"message\" : \"Add User\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserService).addUser((AppUserRequest) any());
    }

    /**
     * Method under test: {@link UserManagementController#findAllActiveUsers()}
     */
    @Test
    void testFindAllActiveUsers() throws Exception {
        when(appUserRepository.findAllActiveUsers((Sort) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/active/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllActiveUsers()}
     */
    @Test
    void testFindAllActiveUsers2() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        when(appUserRepository.findAllActiveUsers((Sort) any())).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/active/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllActiveUsers()}
     */
    @Test
    void testFindAllActiveUsers3() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser1);
        appUserList.add(appUser);
        when(appUserRepository.findAllActiveUsers((Sort) any())).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/active/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true},{"
                                        + "\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe\""
                                        + ",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllDisabledUsers()}
     */
    @Test
    void testFindAllDisabledUsers() throws Exception {
        when(appUserRepository.findAllDisabledUsers((Sort) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/disabled/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllDisabledUsers()}
     */
    @Test
    void testFindAllDisabledUsers2() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        when(appUserRepository.findAllDisabledUsers((Sort) any())).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/disabled/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllDisabledUsers()}
     */
    @Test
    void testFindAllDisabledUsers3() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser1);
        appUserList.add(appUser);
        when(appUserRepository.findAllDisabledUsers((Sort) any())).thenReturn(appUserList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/disabled/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe"
                                        + "\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true},{"
                                        + "\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username\":\"janedoe\""
                                        + ",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled\":true,"
                                        + "\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true}]"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllByPagination()}
     */
    @Test
    void testFindAllByPagination() throws Exception {
        when(appUserRepository.findAllByPagination((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/page/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllByPagination()}
     */
    @Test
    void testFindAllByPagination2() throws Exception {
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

        ArrayList<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        PageImpl<AppUser> pageImpl = new PageImpl<>(appUserList);
        when(appUserRepository.findAllByPagination((Pageable) any())).thenReturn(pageImpl);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/page/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[{\"userId\":123,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"username"
                                        + "\":\"janedoe\",\"password\":\"iloveyou\",\"roles\":[],\"authenticationProvider\":\"LOCAL\",\"locked\":true,\"enabled"
                                        + "\":true,\"authorities\":[],\"accountNonLocked\":false,\"accountNonExpired\":true,\"credentialsNonExpired\":true"
                                        + "}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":1,\"size\":1,\"number\":0,\"sort\":{"
                                        + "\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}"));
    }

    /**
     * Method under test: {@link UserManagementController#findAllByPagination()}
     */
    @Test
    void testFindAllByPagination3() throws Exception {
        when(appUserRepository.findAllByPagination((Pageable) any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/page/find");
        MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link UserManagementController#getUserRoles(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUserRoles() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        //       at jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //       at jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //       at jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //       at java.util.Objects.checkIndex(Objects.java:372)
        //       at java.util.ArrayList.get(ArrayList.java:459)
        //       at com.example.carrentalservice.services.user.AppUserServiceImpl.getUserRole(AppUserServiceImpl.java:342)
        //       at com.example.carrentalservice.controllers.UserManagementController.getUserRoles(UserManagementController.java:254)
        //   In order to prevent getUserRoles(String)
        //   from throwing IndexOutOfBoundsException, add constructors or factory
        //   methods that make it easier to construct fully initialized objects used in
        //   getUserRoles(String).
        //   See https://diff.blue/R013 to resolve this issue.

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
        when(appUserRepository.findAppUserByUsername((String) any())).thenReturn(appUser);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class))).getUserRoles("janedoe");
    }

    /**
     * Method under test: {@link UserManagementController#getUserRoles(String)}
     */
    @Test
    void testGetUserRoles2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("Name");

        ArrayList<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        appUser.setRoles(userRoleList);
        appUser.setUserId(123L);
        appUser.setUsername("janedoe");
        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.findAppUserByUsername((String) any())).thenReturn(appUser);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        RestResponse actualUserRoles = (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                mock(AppUserRepository.class))).getUserRoles("janedoe");
        assertEquals(HttpStatus.OK, actualUserRoles.getStatus());
        ObjectNode data = actualUserRoles.getData();
        assertEquals("{\r\n  \"role # 1\" : \"Name\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserRepository).findAppUserByUsername((String) any());
    }

    /**
     * Method under test: {@link UserManagementController#getUserOrderCount(String)}
     */
    @Test
    void testGetUserOrderCount() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.getUserOrdersCount((String) any())).thenReturn(3);
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository1 = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        RestResponse actualUserOrderCount = (new UserManagementController(
                new AppUserServiceImpl(userRoleRepository, appUserRepository1, passwordEncoder,
                        new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                appUserRepository)).getUserOrderCount("janedoe");
        assertEquals(HttpStatus.OK, actualUserOrderCount.getStatus());
        ObjectNode data = actualUserOrderCount.getData();
        assertEquals("{\r\n  \"message\" : \"user: janedoe has 3 orders till present\"\r\n}", data.toPrettyString());
        assertEquals(1, data.size());
        verify(appUserRepository).getUserOrdersCount((String) any());
    }

    /**
     * Method under test: {@link UserManagementController#getUserOrderCount(String)}
     */
    @Test
    void testGetUserOrderCount2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R015 Method may be nondeterministic.
        //   The execution of the created test may depend on the runtime environment.
        //   See https://diff.blue/R015 to resolve this issue.

        AppUserRepository appUserRepository = mock(AppUserRepository.class);
        when(appUserRepository.getUserOrdersCount((String) any()))
                .thenThrow(new ApiRequestException("An error occurred"));
        UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
        AppUserRepository appUserRepository1 = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        assertThrows(ApiRequestException.class,
                () -> (new UserManagementController(new AppUserServiceImpl(userRoleRepository, appUserRepository1,
                        passwordEncoder, new ConfirmationTokenServiceImpl(mock(ConfirmationTokenRepository.class))),
                        appUserRepository)).getUserOrderCount("janedoe"));
        verify(appUserRepository).getUserOrdersCount((String) any());
    }

    /**
     * Method under test: {@link UserManagementController#deleteAppUser(Long)}
     */
    @Test
    void testDeleteAppUser1() throws Exception {
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/v1/users/delete");
        MockHttpServletRequestBuilder requestBuilder = deleteResult.param("userId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testDeleteAppUser() throws Exception {
        Mockito.when(appUserService.deleteUser(1L)).thenReturn("The user with Id " + 1L + "is successfully deleted");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/delete").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        userManagementController.deleteAppUser(anyLong());
        Mockito.verify(appUserService).deleteUser(anyLong());
    }


    /**
     * Method under test: {@link UserManagementController#deleteUser(HttpServletRequest)}
     */
    @Test
    void testDeleteUser() throws Exception {
        String username = "ahmad_1234";
//        HttpServletRequest request = null;
        HttpServletRequest request1 = mock(HttpServletRequest.class);
//        when(request1.getHeaders(AUTHORIZATION)).thenReturn(any());
        when(appUserService.handleAuthorizationHeader(any())).thenReturn(anyString());
        Mockito.when(appUserService.deleteUser(username))
                .thenReturn("The user with username: " + username + " is successfully deleted");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/users/account/delete").accept(MediaType.APPLICATION_JSON)).andReturn();

        System.out.println(mvcResult.getResponse());
        userManagementController.deleteUser(request1);
        Mockito.verify(appUserService).deleteUser(username);
    }


    /**
     * Method under test: {@link UserManagementController#deleteUser(HttpServletRequest)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        String username = "ahmad_1234";
//        HttpServletRequest request = null;
        HttpServletRequest request1 = mock(HttpServletRequest.class);
        Mockito.when(request1).thenReturn(null);
        assertThatThrownBy(()-> userManagementController.deleteUser(request1))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Access token is missing", BAD_REQUEST);
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/v1/users/account/delete").accept(MediaType.APPLICATION_JSON)).andReturn();

//        System.out.println(mvcResult.getResponse());
//        userManagementController.deleteUser(request1);
//        Mockito.verify(appUserService).deleteUser(username);
    }


    @Test
    void testDeleteUser1() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserManagementController#updatePassword(HttpServletRequest, String)}
     */
    @Test
    void testUpdatePassword() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/users/password/update")
                .param("password", null);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userManagementController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

