package com.orderservice.service;

import com.orderservice.dtos.OrderRequest;

public interface OrderService {

    public String placeOrder(OrderRequest orderRequest);

}
