package com.example.carrentalservice.repositories;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long>{

    AppUser findByEmail(String email);
    Optional<AppUser> findByUsername(String username);

    AppUser findAppUserByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.authenticationProvider =?2 WHERE a.email = ?1")
    int updateAppUserInfo(String email, AuthenticationProvider provider);

//    @Query("SELECT u FROM AppUser u WHERE u.roles =?1")
    Optional<List<AppUser>> findByRoles(UserRole userRoles);

    @Query("SELECT count(o.orderId) FROM AppUser u  JOIN RentOrder o on u.userId = o.userId " +
            "WHERE u.username = :username")
    Integer getUserOrdersCount(@Param(value = "username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled =?2 WHERE a.username = ?1")
    int updateStatus(String username, boolean status);

    @Transactional
    @Modifying
    @Query("DELETE FROM AppUser a " +
            "WHERE a.username = :username")
    int deleteUser(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM AppUser a " +
            "WHERE a.userId = :userId")
    int deleteUserByID(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET  a.password = :password WHERE a.username = :username")
    int updateUserPassword(String username, String password);

    @Query("SELECT u FROM AppUser u WHERE u.enabled = true")
    List<AppUser> findAllActiveUsers(Sort sort);

    @Query("SELECT u FROM AppUser u WHERE u.enabled = false")
    List<AppUser> findAllDisabledUsers(Sort sort);

    @Query("SELECT u from AppUser u ORDER BY u.userId")
    Page<AppUser> findAllByPagination(Pageable pageable);

    @Query("SELECT u.password from AppUser u WHERE  u.username = :username")
    String getEncodedPassword(String username);

}