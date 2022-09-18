package com.example.carrentalservice.services.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.configuration.security.PasswordEncoder;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import com.example.carrentalservice.models.entities.UserRole;
import com.example.carrentalservice.models.handelers.AppUserRequest;
import com.example.carrentalservice.repositories.AppUserRepository;
import com.example.carrentalservice.repositories.UserRoleRepository;
import com.example.carrentalservice.services.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AppUserServiceImpl.class,
        PasswordEncoder.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Autowired
    private AppUserServiceImpl appUserServiceImpl;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    private AppUserServiceImpl userService;
    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    ConfirmationTokenService tokenService;

    @Mock
    PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found!";
    private final static String CREDENTIALS_ERROR_MESSAGE = "No such user with the given credentials %s";


    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoder();
        userService = new AppUserServiceImpl(userRoleRepository, appUserRepository, passwordEncoder, tokenService);

        List<AppUser> users = new ArrayList<>();
        users.add(new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        ));
        users.add(new AppUser(
                        "akram",
                        "ali",
                        "akram@gmail.com",
                        "akram1",
                        "201712@Asg"
                )
        );
        appUserRepository.saveAll(users);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Method under test: {@link AppUserServiceImpl#loadUserByUsername(String)}
     */
    @Test
    void testIfCanLoadUserByUsernameIsFalse() {
        //given
        String username = "ahmad@gmial.com";

        //when
        //then
        given(appUserRepository.findByEmail(username)).willReturn(null);
        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format(USER_NOT_FOUND_MESSAGE, username), HttpStatus.NOT_FOUND);

    }

    /**
     * Method under test: {@link AppUserServiceImpl#loadUserByUsername(String)}
     */
    @Test
    void testIfCanLoadUserByUsernameIsAvailable() {
        //given
        String username = "ahmad@gmial.com";

        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        //given
        given(appUserRepository.findByEmail(username)).willReturn(actualUser);

        // when
        UserDetails user = userService.loadUserByUsername(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        actualUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        //then
        assertThat(user).isEqualTo(new org.springframework.security.core.userdetails.User(actualUser.getUsername(),
                actualUser.getPassword(), authorities));

    }

    /**
     * Method under test: {@link AppUserServiceImpl#checkEmail(String)}
     */
    @Test
    void checkIfEmailIsNotUnique() {
        //given
        String email = "ahmad@gmial.com";

        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        //given
        given(appUserRepository.findByEmail(email)).willReturn(actualUser);

        assertThatThrownBy(() -> userService.checkEmail(email))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("email already taken", HttpStatus.BAD_REQUEST);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#checkEmail(String)}
     */
    @Test
    void checkIfEmailIsUnique() {
        //given
        String email = "ahmad@gmail.com";

        //when
        userService.checkEmail(email);

        //then
        assertThat(email).isEqualTo("ahmad@gmail.com");
    }

    /**
     * Method under test: {@link AppUserServiceImpl#checkUsername(String)}
     */
    @Test
    void checkIfUsernameIsNotUnique() {
        //given
        String username = "ahmad12";

        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        Optional<AppUser> optional = Optional.of(actualUser);

        //given
        given(appUserRepository.findByUsername(username)).willReturn(optional);


        assertThatThrownBy(() -> userService.checkUsername(username))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("username already taken");
    }

    /**
     * Method under test: {@link AppUserServiceImpl#checkUsername(String)}
     */
    @Test
    void checkIfUsernameIsUnique() {
        //given
        String username = "ahmad12";

        //when
        userService.checkUsername(username);

        //then
        assertThat(username).isEqualTo("ahmad12");
    }

    /**
     * Method under test: {@link AppUserServiceImpl#signUpUser(AppUser, String[])}
     */
    @Test
    void signUpUser() {
        //given
        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad_1234",
                "201712@Asg"
        );
        String [] roles = new String[1];
        roles[0] = "ROLE_ADMIN";

        checkIfEmailIsUnique();
        checkIfUsernameIsUnique();
        assertThat(actualUser.getEmail()).isEqualTo("ahmad@gmail.com");
        assertThat(actualUser.getUsername()).isEqualTo("ahmad_1234");
        given(appUserRepository.save(actualUser)).willReturn(actualUser);
        assertThat(appUserRepository.save(actualUser)).isEqualTo(actualUser);

        String token = "token";
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), actualUser);
//        confirmationTokenService.saveConfirmationToken(confirmationToken);

        tokenService.saveConfirmationToken(confirmationToken);
        assertThat(appUserServiceImpl.signUpUser(actualUser,roles)).isEqualTo(token);

        assertThat(token).isEqualTo("token");


    }

    /**
     * Method under test: {@link AppUserServiceImpl#enableAppUser(String)}
     */
    @Test
    void canEnableAppUser() {

        //given
        String email = "ahmad@gmail.com";

        // when
        when(appUserRepository.enableAppUser(email)).thenReturn(1);

        assertThat(userService.enableAppUser(email)).isEqualTo("user enabled successfully");

    }

    /**
     * Method under test: {@link AppUserServiceImpl#enableAppUser(String)}
     */
    @Test
    void canNotEnableAppUser() {

        //given
        String email = "ahmad@gmail.com";

        // when
        when(appUserRepository.enableAppUser(email)).thenReturn(0);

        assertThat(userService.enableAppUser(email)).isEqualTo("Error enabling Email");

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getByUserRole(String)}
     */
    @Test
    void canGetByUserRole() {
        //given
        String userRole = "ROLE_ADMIN";
        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        UserRole role = new UserRole(1L, "ROLE_ADMIN");

        given(userRoleRepository.findByName(userRole)).willReturn(role);

        List<AppUser> users = new ArrayList<>();
        users.add(actualUser);
        Optional<List<AppUser>> optional = Optional.of(users);

        given(appUserRepository.findByRoles(role)).willReturn(optional);
        assertThat(userService.getByUserRole(userRole)).isEqualTo(optional);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#getByUserRole(String)}
     */
    @Test
    void canNotGetByUserRole() {
        //given
        String userRole = "ROLE_CLIENT";

        UserRole role = new UserRole(1L, "ROLE_CLIENT");

        given(userRoleRepository.findByName(userRole)).willReturn(role);

        Optional<List<AppUser>> optional = Optional.empty();

        when(appUserRepository.findByRoles(role)).thenReturn(optional);

        assertThatThrownBy(() -> userService.getByUserRole(userRole))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("No such users with the role " + userRole);

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUser(String, String)}
     */
    @Test
    void canGetUser() {
        //given
        String username = "ahmad1";
        String password = "201712@Asg";
        //when
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(password);

        given(appUserRepository.getEncodedPassword(username)).willReturn(encodedPassword);

        assertThat(passwordEncoder.bCryptPasswordEncoder().matches(password, encodedPassword)).isEqualTo(true);

        assertThat(userService.getUser(username, password))
                .isEqualTo("The user with username :" + username + " and password: " + password + " is valid");

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUser(String, String)}
     */
    @Test
    void canNotGetUser() {
        //given
        String username = "ahmad1";
        String password = "201712@Asg";
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode("1234");
        given(appUserRepository.getEncodedPassword(username)).willReturn(encodedPassword);

        assertThat(passwordEncoder.bCryptPasswordEncoder().matches(password, encodedPassword)).isEqualTo(false);

        assertThatThrownBy(() -> userService.getUser(username, password))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format(CREDENTIALS_ERROR_MESSAGE, username));

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUserOrderCount(String)}
     */
    @Test
    void canGetUserOrderCount() {
        //given
        String username = "ahmad1";

        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        Optional<AppUser> optional = Optional.of(actualUser);
        given(appUserRepository.findByUsername(username)).willReturn(optional);
        int expected = 0;

        //when
        userService.getUserOrderCount(username);

        //then
        verify(appUserRepository).getUserOrdersCount(username);

        //then
        assertThat(appUserRepository.getUserOrdersCount(username)).isEqualTo(expected);
        assertThat(userService.getUserOrderCount(username))
                .isEqualTo(0);
    }


    /**
     * Method under test: {@link AppUserServiceImpl#getUserOrderCount(String)}
     */
    @Test
    void canNotGetUserOrderCount() {
        //given
        String username = "ahmad1";

        //when
        Optional<AppUser> optional = Optional.empty();
        given(appUserRepository.findByUsername(username)).willReturn(optional);

        //then
        assertThatThrownBy(() -> userService.getUserOrderCount(username))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format("no such user with name: " + username + " found"));
    }


    /**
     * Method under test: {@link AppUserServiceImpl#createNewUserAfterOAuthLoginSuccess
     * (String, String, String, AuthenticationProvider)}
     */
    @Test
    void testCreateNewUserAfterOAuthLoginSuccess() {
        //given
        String firstName = "ahmad";
        String lastName = "ali";
        String email = "ahmadghnnam60@gmail.com";
        AuthenticationProvider provider = AuthenticationProvider.GOOGLe;

        //when
        AppUser user = new AppUser(
                firstName,
                lastName,
                email,
                firstName + lastName,
                "201712@Asg"
        );

        user.setEnabled(true);
        user.setAuthenticationProvider(provider);

        appUserRepository.save(user);

        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        given(appUserRepository.save(appUserArgumentCaptor.capture())).willReturn(user);
//
//        userService.addRoleToUser(user.getEmail(), "ROLE_CUSTOMER");
        canAddRoleToUser();

        userService.createNewUserAfterOAuthLoginSuccess(email, firstName, lastName, provider);

        //then
//        assertThat(appUserRepository.save(appUserArgumentCaptor.capture())).isEqualTo(user);

        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedUser = appUserArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }


    /**
     * Method under test: {@link AppUserServiceImpl#updateUserInfo(String, AuthenticationProvider)}
     */
    @Test
    void canUpdateUserInfo() {
        //given
        String email = "ahmad@gmail.com";
        AuthenticationProvider provider = AuthenticationProvider.GOOGLe;

        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        given(appUserRepository.findByEmail(email)).willReturn(user);
        //when
        userService.updateUserInfo(email, provider);

        //then
        verify(appUserRepository).updateAppUserInfo(email, provider);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#updateUserInfo(String, AuthenticationProvider)}
     */
    @Test
    void canNotUpdateUserInfo() {
        //given
        String email = "ahmad@gmail.com";
        AuthenticationProvider provider = AuthenticationProvider.GOOGLe;


        //when
        if (appUserRepository.findByEmail(email) == null)
            //then
            assertThatThrownBy(() -> userService.updateUserInfo(email, provider))
                    .isInstanceOf(ApiRequestException.class)
                    .hasMessageContaining(String.format("No such user with email: " + email));
    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUsers()} )}
     */
    @Test
    void canGetUsers() {
        //when
        userService.getUsers();

        //then
        verify(appUserRepository).findAll();
    }

    /**
     * Method under test: {@link AppUserServiceImpl#changeStatus(String, String)}
     */
    @Test
    void canChangeStatusToEnabled() {
        //given
        String username = "ahmad1";
        String status = "enabled";

        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        given(appUserRepository.findAppUserByUsername(username)).willReturn(user);
        //when
        userService.changeStatus(username, status);

        //then
        verify(appUserRepository).updateStatus(username, true);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#changeStatus(String, String)}
     */
    @Test
    void canChangeStatusToDisabled() {
        //given
        String username = "ahmad1";
        String status = "disabled";

        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        given(appUserRepository.findAppUserByUsername(username)).willReturn(user);
        //when
        userService.changeStatus(username, status);

        //then
        verify(appUserRepository).updateStatus(username, false);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#changeStatus(String, String)}
     */
    @Test
    void canNotChangeStatus() {
        //given
        String username = "ahmad1";
        String status = "enabled";

        //when
        when(appUserRepository.findAppUserByUsername(username)).thenReturn(null);

        //then
        assertThatThrownBy(() -> userService.changeStatus(username, status))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format("no such user with username: " + username +
                        " the changing status process is failed"));

    }

    /**
     * Method under test: {@link AppUserServiceImpl#deleteUser(String)}
     */
    @Test
    void canDeleteUser() {
        //given
        String username = "ahmad1";

        given(appUserRepository.deleteUser(username)).willReturn(1);
        //when
        String expected = "The user with username: " + username + " is successfully deleted";
        //then
        assertThat(userService.deleteUser(username)).isEqualTo(expected);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#deleteUser(String)}
     */
    @Test
    void canNotDeleteUser() {
        //given
        String username = "ahmad1";

        //when
        given(appUserRepository.deleteUser(username)).willReturn(0);

        //then
        assertThatThrownBy(() -> userService.deleteUser(username))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format(" The user with username: " + username + "not found in the database.\n" +
                        "deletion is ignored"));
    }

    /**
     * Method under test: {@link AppUserServiceImpl#deleteUser(Long)}
     */
    @Test
    void testCanDeleteUserById() {
        //given
        Long userId = 1L;

        //when
        given(appUserRepository.deleteUserByID(userId)).willReturn(1);

        String expected = "The user with Id " + userId + "is successfully deleted";

        assertThat(userService.deleteUser(userId)).isEqualTo(expected);
    }


    /**
     * Method under test: {@link AppUserServiceImpl#deleteUser(Long)}
     */
    @Test
    void canNotDeleteUserById() {
        //given
        Long userId = 1L;

        //when
        given(appUserRepository.deleteUserByID(userId)).willReturn(0);

        //then
        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format(" The user with id: " + userId + "not found in the database.\n" +
                        "deletion is ignored"));
    }

    /**
     * Method under test: {@link AppUserServiceImpl#addUser(AppUserRequest)}
     */
    @Test
    void testAddUser() {
        //given
        String[] roles = new String[1];
        roles[0] = "ROLE_ADMIN";
        AppUserRequest request = new AppUserRequest(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg",
                roles
        );

        given(appUserRepository.findByEmail(request.getEmail())).willReturn(null);
        given(appUserRepository.findAppUserByUsername(request.getUsername())).willReturn(null);

        AppUser appUser = new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );

        appUser.setEnabled(true);

        appUserRepository.save(appUser);

        String expected = "User added successfully";

        assertThat(userService.addUser(request)).isEqualTo(expected);
    }


    /**
     * Method under test: {@link AppUserServiceImpl#addUser(AppUserRequest)}
     */
    @Test
    void testAddUser2() {
        //given
        String[] roles = new String[1];
        roles[0] = "ROLE_ADMIN";
        AppUserRequest request = new AppUserRequest(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg",
                roles
        );
        AppUser user = new AppUser("ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad_1234",
                "2017!2Asw");

        //when
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(user);
//        given(appUserRepository.findAppUserByUsername(request.getUsername())).willReturn(user);

        //then
        assertThatThrownBy(() -> userService.addUser(request))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("User with given email or username is already found." +
                        " Try again with new email/username", HttpStatus.BAD_REQUEST);
    }


    /**
     * Method under test: {@link AppUserServiceImpl#getUserId(String)}
     */
    @Test
    void canGetUserId() {
        //given
        String username = "ahmad1";


        //when
        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        actualUser.setUserId(1L);
        Optional<AppUser> optional = Optional.of(actualUser);

        given(appUserRepository.findByUsername(username)).willReturn(optional);
        assertThat(userService.getUserId(username)).isEqualTo(actualUser.getUserId());

//        userService.getUserId(username);
//        verify(appUserRepository).findByUsername(username).get().getUserId();

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUserId(String)}
     */
    @Test
    void cantNotGetUserId() {
        //given
        String username = "ahmad1";


        //when
        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        actualUser.setUserId(1L);
        Optional<AppUser> optional = Optional.empty();
        given(appUserRepository.findByUsername(username)).willReturn(optional);

//        boolean isFound = appUserRepository.findByUsername(username).isPresent();

//        if (!isFound)
        //then
        assertThatThrownBy(() -> userService.getUserId(username))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(String.format("no such user with the given username: " + username));
    }

    /**
     * Method under test: {@link AppUserServiceImpl#updateUserPassword(String, String)}
     */
    @Test
    void canNotUpdateUserPassword() {
        //given
        String username = "ahmad1";
        String password = "201712@aSd";

        String expected = "Password updating failed";
        //when
        when(appUserRepository.updateUserPassword(username, password)).thenReturn(0);

        //then
        assertThat(userService.updateUserPassword(username, password)).isEqualTo(expected);

    }

    /**
     * Method under test: {@link AppUserServiceImpl#updateUserPassword(String, String)}
     */
    @Test
    void canUpdateUserPassword() {
        //given
        String username = "ahmad1";
        String password = "201712@aSd";
        //when
        when(appUserRepository.updateUserPassword(username, password)).thenReturn(1);

        String expected = "Password updated successfully";
        //then
        assertThat(userService.updateUserPassword(username, password)).isEqualTo(expected);

    }

    /**
     * Method under test: {@link AppUserServiceImpl#getUserRole(String)}
     */
    @Test
    void getUserRole() {
        //given
        String username = "ahmad1";

        //when
        AppUser actualUser = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        actualUser.addRole(new UserRole(null, "ROLE_ADMIN"));
        given(appUserRepository.findAppUserByUsername(username)).willReturn(actualUser);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        actualUser.getAuthorities().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getAuthority())));

        String[] roles = new String[authorities.size()];

        for (int i = 0; i < authorities.size(); i++) {

            roles[i] = authorities.get(i).getAuthority();
        }

        assertThat(userService.getUserRole(username)).isEqualTo(roles);

    }

    /**
     * Method under test: {@link AppUserServiceImpl#saveRole(UserRole)}
     */
    @Test
    void saveRole() {
        //given
        UserRole role = new UserRole(null, "ROLE_ADMIN");

        //when
        userService.saveRole(role);

        //then
        verify(userRoleRepository).save(role);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#saveUser(AppUser)}
     */
    @Test
    void saveUser() {
        // given
        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));

        //when
        userService.saveUser(user);

        //then
        verify(appUserRepository).save(user);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#addRoleToUser(String, String)}
     */
    @Test
    void canAddRoleToUser() {
        //given
        String email = "ahmad@gmail.com";
        String roleName = "ROLE_ADMIN";

        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );

        //when
        given(appUserRepository.findByEmail(email)).willReturn(user);
        UserRole role = new UserRole(null, "ROLE_ADMIN");

        given(userRoleRepository.findByName(roleName)).willReturn(role);
        user.addRole(role);

        //then
        assertThat(userService.addRoleToUser(email, roleName)).isEqualTo(role);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#addRoleToUser(String, String)}
     */
    @Test
    void canNotAddRoleToUser() {
        //given
        String email = "ahmad@gmail.com";
        String roleName = "ROLE_ADMIN";

        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );
        //when
        given(appUserRepository.findByEmail(email)).willReturn(user);
        given(userRoleRepository.findByName(roleName)).willReturn(null);

        //then
        assertThatThrownBy(() -> userService.addRoleToUser(email, roleName))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("No such role named: " + roleName);
    }

    /**
     * Method under test: {@link AppUserServiceImpl#addRoleToUser(String, String)}
     */
    @Test
    void canNotAddRoleToUserTest() {
        //given
        String email = "ahmad@gmail.com";
        String roleName = "ROLE_ADMIN";


        //when
        given(appUserRepository.findByEmail(email)).willReturn(null);

        //then
        assertThatThrownBy(() -> userService.addRoleToUser(email, roleName))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("No such user with email: " + email + ". Process failed");

    }

    /**
     * Method under test: {@link AppUserServiceImpl#handleAuthorizationHeader(String)}
     */
    @Test
    void handleAuthorizationHeader() {
        //given
        String authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiJha3JhbTEyIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpc3MiOiIvYXBp" +
                "L2xvZ2luIiwiZXhwIjoxNjYyOTk2OTAyfQ.r8WZOAZrE1t9B53RJg275yrPQIQLTVdDGb-j_Ytizlo";

        String accessToken = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String username = decodedJWT.getSubject();

        assertThat(userService.handleAuthorizationHeader(authorizationHeader)).isEqualTo(any());

    }


}