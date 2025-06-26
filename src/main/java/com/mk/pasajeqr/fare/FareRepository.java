package com.mk.pasajeqr.fare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {
    boolean existsByCode(String code);
}
