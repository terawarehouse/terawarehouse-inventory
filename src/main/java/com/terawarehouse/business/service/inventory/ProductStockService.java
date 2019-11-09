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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broodcamp.util.StringUtils;
import com.terawarehouse.business.exception.SerialNoAlreadyExistsException;
import com.terawarehouse.business.exception.WarrantyCardNoAlreadyExistsException;
import com.terawarehouse.data.entity.inventory.ProductStock;
import com.terawarehouse.data.entity.inventory.ProductStockHistory;
import com.terawarehouse.data.entity.inventory.ProductStockStatusEnum;
import com.terawarehouse.data.repository.inventory.ProductStockHistoryRepository;
import com.terawarehouse.data.repository.inventory.ProductStockRepository;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @since 0.0.1
 * @version 0.0.1
 */
@Service
public class ProductStockService {

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductStockHistoryRepository productStockHistoryRepository;

    private void validateSerialNumber(String serialNo) {

        if (productStockRepository.findBySerialNo(serialNo).isPresent()) {
            throw new SerialNoAlreadyExistsException();
        }
    }

    private void validateWarrantyCardNo(String cardNo) {

        if (productStockRepository.findByWarrantyCardNo(cardNo).isPresent()) {
            throw new WarrantyCardNoAlreadyExistsException();
        }
    }

    public ProductStock save(ProductStock productStock) {

        validateSerialNumber(productStock.getSerialNo());

        if (!StringUtils.isBlank(productStock.getWarrantyCardNo())) {
            validateWarrantyCardNo(productStock.getWarrantyCardNo());
        }

        if (productStock.getTradingBranchId() != null && productStock.getPreviousTradingBranchId() != null
                && !productStock.getTradingBranchId().equals(productStock.getPreviousTradingBranchId())) {
            // create history
            ProductStockHistory productStockHistory = new ProductStockHistory();
            productStockHistory.setAction(ProductStockStatusEnum.TRANSFERRED);
            productStockHistory.setEventDate(new Date());
            productStockHistory.setProductStock(productStock);
            productStockHistory.setSourceTradingBranchId(productStock.getPreviousTradingBranchId());
            productStockHistory.setTargetTradingBranchId(productStock.getTradingBranchId());
            productStockHistoryRepository.save(productStockHistory);
        }

        return productStockRepository.save(productStock);
    }

}
