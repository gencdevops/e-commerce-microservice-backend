package com.fmss.productservice.repository;

import com.fmss.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, String > {
    @Query(value = "select p from Product p where p.status=true")
    List<Product> getAllProducts();
}
