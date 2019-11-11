/**
 * An Open Source Inventory and Sales Management System
 * Copyright (C) 2019 Edward P. Legaspi (https://github.com/czetsuya)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.terawarehouse.web.application.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broodcamp.data.mapper.GenericMapper;
import com.broodcamp.util.StringUtils;
import com.terawarehouse.business.domain.inventory.ProductStockAddCommandDto;
import com.terawarehouse.business.domain.inventory.ProductStockDto;
import com.terawarehouse.business.service.inventory.ProductStockCommandService;
import com.terawarehouse.data.entity.inventory.ProductStock;
import com.terawarehouse.web.assembler.inventory.ProductStockResourceAssembler;

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

    @Autowired
    private ProductStockResourceAssembler modelAssembler;

    @PostMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductStockDto>>> restock(@RequestBody ProductStockAddCommandDto productStockAddCommand) {

        List<ProductStock> newProductStocks = genericMapper.toModel(productStockAddCommand.getProductStocks());

        List<ProductStock> invalidProductStocks = productStockCommandService.save(newProductStocks);

        boolean isError = invalidProductStocks.stream().anyMatch(e -> !StringUtils.isBlank(e.getErrMessage()));

        List<EntityModel<ProductStockDto>> result = invalidProductStocks.stream().map(e -> modelAssembler.toModel(genericMapper.toDto(e))).collect(Collectors.toList());

        return new ResponseEntity<>(new CollectionModel<>(result), isError ? HttpStatus.CONFLICT : HttpStatus.CREATED);
    }
}
