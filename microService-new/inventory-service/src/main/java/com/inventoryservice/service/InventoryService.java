package com.inventoryservice.service;

import com.inventoryservice.dtos.InventoryResponse;

import java.util.List;

public interface InventoryService {

    public List<InventoryResponse> isInStock(List<String> skuCode);

}
