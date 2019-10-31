package com.terawarehouse.web.application.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broodcamp.data.mapper.GenericMapper;
import com.terawarehouse.business.domain.inventory.ProductStockAddCommandDto;
import com.terawarehouse.business.domain.inventory.ProductStockDto;
import com.terawarehouse.business.service.inventory.ProductStockCommandService;
import com.terawarehouse.data.entity.inventory.ProductStock;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
@RestController
@RequestMapping(path = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProductStockCommandController {

    @Autowired
    private ProductStockCommandService productStockCommandService;

    @Autowired
    private GenericMapper<ProductStock, ProductStockDto> genericMapper;

    @PostMapping
    public void restock(@RequestBody ProductStockAddCommandDto productStockAddCommand) {

        List<ProductStock> newProductStocks = genericMapper.toModel(productStockAddCommand.getProductStocks());
        
        productStockCommandService.restock(newProductStocks);
    }
}
