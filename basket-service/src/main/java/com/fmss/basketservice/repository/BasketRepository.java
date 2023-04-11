package com.fmss.basketservice.repository;

import com.fmss.basketservice.model.enitity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, String> {

    List<Basket> findBasketsByUserId(String userId);

}
