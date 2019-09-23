package com.milind.reactivemicroservice.controller;

import com.milind.reactivemicroservice.entity.Order;
import com.milind.reactivemicroservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public Mono<Order> createOrder(@Valid @RequestBody Order order) {
        logger.info(" Order Received: "+order);
        logger.info(" Thread In Controller: " +Thread.currentThread().getName());
        Mono<Order> mOrder = orderService.createOrder(order);
        logger.info(" Order Submitted: "+Thread.currentThread().getName());
        return mOrder;
    }

    @PostMapping("/reactiveorders")
    public Mono<Order> createReactiveOrder(@Valid @RequestBody Order order) {
        logger.info(" Reactive Mode:: Order Received : "+order);
        logger.info(" Reactive Mode :: Thread In Controller: " +Thread.currentThread().getName());
        Mono<Order> mOrder =orderService.createReactiveOrder(order).subscribeOn(Schedulers.parallel());
        logger.info(" Reactive Mode :: Order Submitted: "+Thread.currentThread().getName());
        return mOrder;
    }

    @GetMapping("/orders/{id}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable(value = "id") Integer orderId) {
        return orderService.getOrder(orderId)
                .map(order -> ResponseEntity.ok(order))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/getallitems")
    public Flux<String> getAllItems(){
        return orderService.getAllItems();
    }
}
