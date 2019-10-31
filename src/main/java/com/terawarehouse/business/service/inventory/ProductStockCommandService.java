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
package com.terawarehouse.business.service.inventory;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terawarehouse.data.entity.inventory.ProductStock;
import com.terawarehouse.data.entity.inventory.ProductStockStatusEnum;
import com.terawarehouse.data.repository.inventory.ProductStockRepository;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
@Service
public class ProductStockCommandService {

    @Autowired
    private ProductStockRepository productStockRepository;

    public void restock(List<ProductStock> newProducts) {

        Date now = new Date();
        if (newProducts != null && !newProducts.isEmpty()) {
            newProducts.forEach(e -> {
                e.setCreated(now);
                e.setStatus(ProductStockStatusEnum.CREATED);
                productStockRepository.save(e);
            });
        }
    }
    
    public void sold(List<ProductStock> soldProducts ) {
        
    }
}
