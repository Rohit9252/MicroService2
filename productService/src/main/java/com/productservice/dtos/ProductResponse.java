package com.productservice.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Integer id;

    private String nane;

    private BigDecimal price;

    private  String description;

}
