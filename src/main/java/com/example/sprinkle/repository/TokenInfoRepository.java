package com.example.sprinkle.repository;

import com.example.sprinkle.entity.TokenInfo;
import com.example.sprinkle.entity.pk.TokenInfoPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenInfoRepository extends JpaRepository<TokenInfo, TokenInfoPk> {
}
