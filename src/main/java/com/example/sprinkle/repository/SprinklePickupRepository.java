package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinklePickup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinklePickupRepository extends JpaRepository<SprinklePickup, Long>, SprinklePickupRepositoryCustom {
}
