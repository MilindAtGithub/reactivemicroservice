package com.milind.reactivemicroservice.repository;

import com.milind.reactivemicroservice.entity.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, Integer> {
}
