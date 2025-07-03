package com.mk.pasajeqr.stop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
    boolean existsByName(String name);
    Page<Stop> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
