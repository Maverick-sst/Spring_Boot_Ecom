package com.example.ecommerce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.CartItem;
import java.util.List;


@Repository
public interface CartRepository extends MongoRepository<CartItem, String> {
    public List<CartItem> findByUserId(String userId);
}