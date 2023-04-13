package com.fmss.basketservice.repository;

import com.fmss.basketservice.model.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasketItemRepository extends JpaRepository<BasketItem, UUID> {

    void deleteByBasket_BasketId(UUID basketId);
}
