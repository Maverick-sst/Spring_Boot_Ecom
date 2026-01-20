package com.example.ecommerce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}