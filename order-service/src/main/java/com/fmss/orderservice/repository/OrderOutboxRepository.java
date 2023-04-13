package com.fmss.orderservice.repository;


import com.fmss.orderservice.model.OrderOutbox;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.UUID;


public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<OrderOutbox> findAll(Pageable pageable);

    void deleteByOrderId(UUID orderId);



}

