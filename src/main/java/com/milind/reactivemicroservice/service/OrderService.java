package com.milind.reactivemicroservice.service;

import com.milind.reactivemicroservice.entity.Order;
import com.milind.reactivemicroservice.repository.OrderRepository;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Component
public class OrderService {

    Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderRepository orderRepository;

    public Mono<Order> createOrder(Order order) {

        logger.info("Order Service Thread: "+ Thread.currentThread().getName());
        return orderRepository.save(order);
    }

    public Mono<Order> createReactiveOrder(Order order) {

        logger.info(" Reactive Mode :: Order Service Thread: "+ Thread.currentThread().getName());
//        Callable<Mono<Order>> callable = () -> {
//            logger.info(" Reactive Mode :: Callable thread id for Order Placement: " + Thread.currentThread().getName());
//            return orderRepository.save(order).subscribeOn(Schedulers.parallel());
//        };
          return orderRepository.save(order).flatMap( m -> { return Mono.just(processOrder(m));}).subscribeOn(Schedulers.parallel());
          //return orderRepository.save(order);
    }

    private Order processOrder(Order o){
        logger.info(" Reactive Mode :: Processing Order "+ Thread.currentThread().getName());
        o.setOrderId(o.getOrderId()+1000);
        return o;
    }

    public Mono<Order> getOrder(Integer id) {

        return orderRepository.findById(id);
    }


    public Flux<String>getAllItems(){
        logger.info(" Reactive Mode :: Getting All  Items "+ Thread.currentThread().getName());
        Flux<Order> orderFlux = orderRepository.findAll();
        return Flux.fromIterable(getItemsFromAllOrders(orderFlux));
        //return getItemsFromAllOrders(orderFlux);
    }


    private List<String> getItemsFromAllOrders(Flux<Order> fluxOrder){

        List <String> itemList = new ArrayList<>();
        fluxOrder.subscribe(new Subscriber<Order>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(Long.MAX_VALUE); // <-- here
            }

            @Override
            public void onNext(Order order) {
                //subscription.request(1);
                logger.info(" Reactive Mode :: Getting Items from  Orders: "+order.getOrderId()+" Thread:" + Thread.currentThread().getName());
                itemList.addAll(order.getItems());
            }

            @Override
            public void onError(Throwable t) {
                logger.error(t.getMessage(),t);
            }

            @Override
            public void onComplete() {
                logger.info(" Reactive Mode :: Getting Items from  Orders "+ Thread.currentThread().getName());
            }
        });
        fluxOrder.blockFirst();
        //fluxOrder.blockLast();
        return itemList;
    }

}
