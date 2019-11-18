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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.broodcamp.web.application.AbstractBaseController;
import com.terawarehouse.business.domain.inventory.ProductStockDto;
import com.terawarehouse.data.entity.inventory.ProductStock;
import com.terawarehouse.data.repository.inventory.ProductStockRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
@RestController
@RequestMapping(path = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
public class ProductStockQueryController extends AbstractBaseController<ProductStock, ProductStockDto, UUID> {

    @GetMapping
    public CollectionModel<EntityModel<ProductStockDto>> findAll(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page)
            throws NotSupportedException {

        Pageable pageable = initPage(page, size);

        List<EntityModel<ProductStockDto>> entities = repository.findAll(pageable).stream().map(e -> modelAssembler.toModel(genericMapper.toDto(e))).collect(Collectors.toList());

        return new CollectionModel<>(entities, linkTo(methodOn(ProductStockQueryController.class).findAll(size, page)).withSelfRel());
    }

    @GetMapping(path = "/{productId}")
    public CollectionModel<EntityModel<ProductStockDto>> findByProductId(@PathVariable @NotNull UUID productId, @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer page) throws NotSupportedException {

        log.debug("GET /stocks producId={}", productId);

        Pageable pageable = initPage(page, size);

        List<EntityModel<ProductStockDto>> entities = ((ProductStockRepository) repository).findAll(pageable).stream().map(e -> modelAssembler.toModel(genericMapper.toDto(e)))
                .collect(Collectors.toList());

        return new CollectionModel<>(entities, linkTo(methodOn(ProductStockQueryController.class).findAll(size, page)).withSelfRel());
    }

}
