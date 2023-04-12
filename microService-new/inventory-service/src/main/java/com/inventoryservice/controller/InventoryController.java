package com.inventoryservice.controller;


import com.inventoryservice.dtos.InventoryResponse;
import com.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {



    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> skuCode){
        return new ResponseEntity<>(inventoryService.isInStock(skuCode), HttpStatus.OK);
    }

}
