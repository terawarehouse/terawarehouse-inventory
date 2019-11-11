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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 */
@Service
public class ProductStockCommandService {

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private ProductStockHistoryRepository productStockHistoryRepository;

    /**
     * Validates an instance of {@linkplain ProductStock} before saving.
     * 
     * @param ps {@link ProductStock} instance
     * @throws SerialNoAlreadyExistsException       serialNo is already save in the
     *                                              database
     * @throws WarrantyCardNoAlreadyExistsException warrantyCardNo is already save
     *                                              in the database
     */
    public void validateProductStock(ProductStock ps) throws SerialNoAlreadyExistsException, WarrantyCardNoAlreadyExistsException {

        if (productStockRepository.findByProductIdAndSerialNo(ps.getProductId(), ps.getSerialNo()).isPresent()) {
            throw new SerialNoAlreadyExistsException(ps.getProductId(), ps.getSerialNo());
        }

        if (!StringUtils.isBlank(ps.getWarrantyCardNo()) && productStockRepository.findByProductIdAndWarrantyCardNo(ps.getProductId(), ps.getWarrantyCardNo()).isPresent()) {
            throw new WarrantyCardNoAlreadyExistsException(ps.getProductId(), ps.getWarrantyCardNo());
        }
    }

    public ProductStock save(ProductStock e) {

        try {
            validateProductStock(e);

            e.setCreated(new Date());
            if (e.isTransient()) {
                e.setStatus(ProductStockStatusEnum.CREATED);
            }

            if (e.getTradingBranchId() != null && e.getPreviousTradingBranchId() != null && !e.getTradingBranchId().equals(e.getPreviousTradingBranchId())) {
                // log product stock movement history
                ProductStockHistory productStockHistory = new ProductStockHistory();
                productStockHistory.setAction(ProductStockStatusEnum.TRANSFERRED);
                productStockHistory.setEventDate(new Date());
                productStockHistory.setProductStock(e);
                productStockHistory.setSourceTradingBranchId(e.getPreviousTradingBranchId());
                productStockHistory.setTargetTradingBranchId(e.getTradingBranchId());
                productStockHistoryRepository.save(productStockHistory);
            }

            return productStockRepository.save(e);

        } catch (SerialNoAlreadyExistsException | WarrantyCardNoAlreadyExistsException e1) {
            e.setErrMessage(e1.getMessage());
            return e;
        }
    }

    public List<ProductStock> save(List<ProductStock> newProducts) {

        List<ProductStock> result = new ArrayList<>();

        if (newProducts != null && !newProducts.isEmpty()) {
            for (ProductStock e : newProducts) {
                result.add(save(e));
            }
        }

        return result;
    }
}
