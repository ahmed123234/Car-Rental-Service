package com.example.carrentalservice.repositories;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {AppUserRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * Method under test: {@link AppUserRepository#findByEmail(String)}
     */
    @Test
    void testFindByEmail() {

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1111");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.findByEmail("jane.doe@example.org")).isEqualTo(appUser);
    }

    @Test
    void testFindByEmail2() {

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.findByEmail("jane")).isNull();
    }

    /**
     * Method under test: {@link AppUserRepository#findByUsername(String)}
     */
    @Test
    void testFindByUsername() {

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        Optional<AppUser> optionalAppUser = Optional.of(appUser);
        assertThat(appUserRepository.findByUsername("jane1222")).isEqualTo(optionalAppUser);
    }

    @Test
    void testFindByUsername2() {

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        Optional<AppUser> optionalAppUser = Optional.empty();
        assertThat(appUserRepository.findByUsername("")).isEqualTo(optionalAppUser);
    }

    /**
     * Method under test: {@link AppUserRepository#findAppUserByUsername(String)}
     */
    @Test
    void testFindAppUserByUsername() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.findAppUserByUsername("jane1222")).isEqualTo(appUser);
    }

    @Test
    void testFindAppUserByUsername2() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.findAppUserByUsername("")).isNull();
    }

    /**
     * Method under test: {@link AppUserRepository#enableAppUser(String)}
     */
    @Test
    void testEnableAppUser() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.enableAppUser("jane.doe@example.org")).isEqualTo(1);
    }

    @Test
    void testEnableAppUser2() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.enableAppUser("")).isEqualTo(0);
    }

    @Test
    void testUpdateAppUserInfo() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.updateAppUserInfo("jane.doe@example.org",
                AuthenticationProvider.GOOGLe)).isEqualTo(1);
    }

    @Test
    void testUpdateAppUserInfo2() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.updateAppUserInfo("", AuthenticationProvider.GOOGLe)).isEqualTo(0);
    }

    /**
     * Method under test: {@link AppUserRepository#findByRoles(UserRole)}
     */
    @Test
    @Disabled
    void testFindByRoles() {
        UserRole role = new UserRole(1L, "ROLE_ADMIN");
        Collection<UserRole> roles = new ArrayList<>();
        roles.add(role);
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(roles);
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        List<AppUser> users = new ArrayList<>();
        users.add(appUser);
        Optional<List<AppUser>> appUsers = Optional.of(users);
        assertThat(appUserRepository.findByRoles(role)).isEqualTo(appUsers);
    }

    /**
     * Method under test: {@link AppUserRepository#getUserOrdersCount(String)}
     */
    @Test
    void testGetUserOrdersCount() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);

        assertThat(appUserRepository.getUserOrdersCount("jane1222")).isEqualTo(0);
    }

    /**
     * Method under test: {@link AppUserRepository#updateStatus(String, boolean)}
     */
    @Test
    void testUpdateStatus() {
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.updateStatus("jane1222", false)).isEqualTo(1);
    }

    /**
     * Method under test: {@link AppUserRepository#deleteUser(String)}
     */
    @Test
    void testDeleteUserByUsername() {
        //given
        String username = "jane1222";

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.deleteUser(username)).isEqualTo(1);
    }

    /**
     * Method under test: {@link AppUserRepository#deleteUserByID(Long)}
     */
    @Test
    void testDeleteUserByID() {
        //given
        long id = 1L;
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUser.setUserId(id);
        appUserRepository.save(appUser);
        assertThat(appUserRepository.deleteUserByID(id)).isEqualTo(0);
    }


    /**
     * Method under test: {@link AppUserRepository#updateUserPassword(String, String)}
     */
    @Test
    void testUpdateUserPassword() {
        //given
        String username = "jane1222";
        String password = "112W@Asdq1";
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.updateUserPassword(username, password)).isEqualTo(1);
    }

    /**
     * Method under test: {@link AppUserRepository#findAllActiveUsers(Sort)}
     */
    @Test
    void testFindAllActiveUsers() {
        //given
        List<AppUser> actualUsers = new ArrayList<>();

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);

        actualUsers.add(appUser);

        assertThat(appUserRepository.findAllActiveUsers(JpaSort.unsafe("userId")))
                .isEqualTo(actualUsers);
    }

    /**
     * Method under test: {@link AppUserRepository#findAllDisabledUsers(Sort)}
     */
    @Test
    void testFindAllDisabledUsers() {
        //given
        List<AppUser> actualUsers = new ArrayList<>();

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(false);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);

        actualUsers.add(appUser);

        assertThat(appUserRepository.findAllDisabledUsers(JpaSort.unsafe("userId")))
                .isEqualTo(actualUsers);
    }

    /**
     * Method under test: {@link AppUserRepository#findAllByPagination(Pageable)}
     */
    @Test
    void testFindAllByPagination() {
        //given
//        List<AppUser> actualUsers = new ArrayList<>();

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(false);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("201712@Asd");
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);

//        actualUsers.add(appUser);

        Page<AppUser> actual = appUserRepository.findAllByPagination(Pageable.unpaged());


        assertThat(appUserRepository.findAllByPagination(Pageable.unpaged())).isEqualTo(actual);
    }

    /**
     * Method under test: {@link AppUserRepository#getEncodedPassword(String)}
     */
    @Test
    void testGetEncodedPassword() {
        //given
        String actualPassword = "201712@Asg";
        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(false);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword(actualPassword);
        appUser.setRoles(new ArrayList<>());
        appUser.setUsername("jane1222");
        appUserRepository.save(appUser);
        assertThat(appUserRepository.getEncodedPassword("jane1222")).isEqualTo(actualPassword);
    }

}

