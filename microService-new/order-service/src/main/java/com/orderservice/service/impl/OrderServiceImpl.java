package com.orderservice.service.impl;

import com.orderservice.dtos.InventoryResponse;
import com.orderservice.dtos.OrderLineItemsDto;
import com.orderservice.dtos.OrderRequest;
import com.orderservice.events.OrderPlacedEvent;
import com.orderservice.model.Order;
import com.orderservice.model.OrderLineItems;
import com.orderservice.repository.OrderRepository;
import com.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());


        List<OrderLineItems> collect = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto).collect(Collectors.toList());

        order.setOrderLineItemsList(collect);
        // before placing order check whether Stock is present or  not  using call of inventory Rest Api

            List<String> skuCodes     = order.getOrderLineItemsList()
                                                    .stream()
                                                    .map(OrderLineItems::getSkuCode)
                                                    .collect(Collectors.toList());

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
            InventoryResponse[] inventoryResponses =  webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build() ) // passing the Request Parameter with this Method
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class) // for which response you will Get after calling the Api
                    .block(); // for synchronized call
            // before  getting the result this will not go Ahead from this line


            boolean  result =  Arrays.stream(inventoryResponses).
                    allMatch(InventoryResponse::getIsInStock);

            if(result){
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order Placed";
            }else{
                throw new RuntimeException("Stock is Not present, Please Try Again After Some Time");
            }

        }finally {
            inventoryServiceLookup.end();
        }



    }


    public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){

        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;

    }


}
