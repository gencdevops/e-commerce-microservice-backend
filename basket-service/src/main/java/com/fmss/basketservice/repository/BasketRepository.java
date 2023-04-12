package com.fmss.basketservice.repository;

import com.fmss.basketservice.model.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, UUID> {

    List<Basket> findByUserId(UUID userId);

    @Query(value = "select b from Basket b where b.basketStatus = com.fmss.basketservice.model.enums.BasketStatus.ACTIVE and b.userId=:userId")
    Optional<Basket> findActiveBasketByUserId(UUID userId);

}
