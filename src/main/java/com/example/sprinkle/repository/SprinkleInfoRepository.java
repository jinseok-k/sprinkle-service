package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinkleInfoRepository extends JpaRepository<SprinkleInfo, Long>, SprinkleInfoRepositoryCustom {
}
