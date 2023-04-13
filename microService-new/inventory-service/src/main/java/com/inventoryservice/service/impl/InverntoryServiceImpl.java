package com.inventoryservice.service.impl;

import com.inventoryservice.dtos.InventoryResponse;
import com.inventoryservice.model.Inventory;
import com.inventoryservice.repository.InventoryRepository;
import com.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InverntoryServiceImpl implements InventoryService {

    private  final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @Override
    public List<InventoryResponse> isInStock(List<String> skuCode) {
//        log.info("wait Started");
//        Thread.sleep(10000);
//        log.info("wait Ended" );
         return inventoryRepository.findBySkuCodeIn(skuCode)
                 .stream().map(this::mapToObj)
                 .collect(Collectors.toList());



    }

    public InventoryResponse mapToObj(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }

}
