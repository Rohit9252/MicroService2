package com.orderservice.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemsDto {
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}
