package com.mk.pasajeqr.user;

import com.mk.pasajeqr.utils.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    long countByRole(RoleType role);
    long countByRoleAndCreatedAtBetween(RoleType role, LocalDateTime start, LocalDateTime end);

}
