package com.example.carrentalservice.repositories;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long>{

    AppUser findByEmail(String email);
    Optional<AppUser> findByUsername(String userName);

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

    Optional<List<AppUser>> findByRoles(UserRole userRole);
    Optional <AppUser> findByUsernameAndPassword(String username, String password);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled =?2 WHERE a.userId = ?1")
    int UpdateStatus(Long userId, boolean status);

    @Transactional
    @Modifying
    @Query("DELETE FROM AppUser a " +
            "WHERE a.userId = ?1")
    int deleteUser(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a SET  a.password = ?2 " +
            "WHERE a.username = ?1")
    int updateUserPassword(String username, String password);
}