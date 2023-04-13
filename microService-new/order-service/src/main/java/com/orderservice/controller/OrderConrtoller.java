package com.orderservice.controller;

import com.orderservice.dtos.OrderRequest;
import com.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderConrtoller {

    private  final OrderService orderService;

    @PostMapping
    @CircuitBreaker(name="inventory", fallbackMethod = "fallBackMethod")
//    @TimeLimiter(name="inventory")
//    @Retry(name = "inventory")
    public ResponseEntity<CompletableFuture<String>> placeOrder(@RequestBody OrderRequest orderRequest){
        return new ResponseEntity<>(CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest)), HttpStatus.CREATED);
    }

    public ResponseEntity<CompletableFuture<String>> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException){

        return new ResponseEntity<>(CompletableFuture.supplyAsync(()-> "Oops ! Something went wrong, Please Order after Some Time"), HttpStatus.BAD_GATEWAY);
    }


}
