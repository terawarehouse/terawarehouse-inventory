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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.terawarehouse.business.exception.SerialNoAlreadyExistsException;
import com.terawarehouse.business.exception.WarrantyCardNoAlreadyExistsException;
import com.terawarehouse.data.entity.inventory.ProductStock;
import com.terawarehouse.data.entity.inventory.ProductStockStatusEnum;
import com.terawarehouse.data.repository.inventory.ProductStockRepository;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @since 0.0.1
 * @version 0.0.1
 */
@SpringBootTest
public class ProductStockServiceTests {

    private final UUID dealerId = UUID.fromString("072a435c-02aa-11ea-8d71-362b9e155667");
    private final UUID productId = UUID.fromString("072a45c9-02aa-11ea-8d71-362b9e155667");
    private final UUID productStockId = UUID.fromString("072a45c8-02aa-11ea-8d71-362b9e155667");
    private final UUID tradingBranchId = UUID.fromString("072a4712-02aa-11ea-8d71-362b9e155667");
    private final String serialNoEmpty = "ANDROID-001";
    private final String serialNo = "ANDROID-002";
    private final String warrantyCardNo = "2019-001";
    private final String warrantyCardNoEmpty = "2019-002";

    @TestConfiguration
    static class ProductStockServiceImplTestContextConfiguration {

        @Bean
        public ProductStockCommandService productStockCommandService() {
            return new ProductStockCommandService();
        }
    }

    @Autowired
    private ProductStockCommandService productStockCommandService;

    @MockBean
    private ProductStockRepository productStockRepository;

    @BeforeEach
    public void setup() {

        ProductStock ps = new ProductStock();
        ps.setDealerId(dealerId);
        ps.setId(productStockId);
        ps.setProductId(productId);
        ps.setStatus(ProductStockStatusEnum.CREATED);
        ps.setTradingBranchId(tradingBranchId);
        ps.setWarrantyCardNo(warrantyCardNo);
        ps.setSerialNo(serialNo);

        Mockito.when(productStockRepository.save(ps)).thenReturn(ps);
        Mockito.when(productStockRepository.findById(productStockId)).thenReturn(Optional.of(ps));
        Mockito.when(productStockRepository.findByProductIdAndSerialNo(productId, serialNo)).thenReturn(Optional.of(ps));
        Mockito.when(productStockRepository.findByProductIdAndSerialNo(productId, serialNoEmpty)).thenReturn(Optional.empty());
        Mockito.when(productStockRepository.findByProductIdAndWarrantyCardNo(productId, warrantyCardNo)).thenReturn(Optional.of(ps));
        Mockito.when(productStockRepository.findByProductIdAndWarrantyCardNo(productId, warrantyCardNoEmpty)).thenReturn(Optional.empty());
    }

    @Test
    public void whenValidSerialNo_thenProductStockShouldBeFound() {

        ProductStock ps = productStockRepository.findByProductIdAndSerialNo(productId, serialNo).get();
        assertThat(ps.getSerialNo()).isEqualTo(serialNo);
    }

    @Test
    public void whenValidateSerialNo_thenThrowSerialFoundException() {

        ProductStock ps = new ProductStock();
        ps.setProductId(productId);
        ps.setSerialNo(serialNo);

        assertThatThrownBy(() -> productStockCommandService.validateProductStock(ps)).isInstanceOf(SerialNoAlreadyExistsException.class);
    }

    @Test
    public void whenValidateWarrantyCardNo_thenThrowWarrantyFoundException() {

        ProductStock ps = new ProductStock();
        ps.setProductId(productId);
        ps.setWarrantyCardNo(warrantyCardNo);

        assertThatThrownBy(() -> productStockCommandService.validateProductStock(ps)).isInstanceOf(WarrantyCardNoAlreadyExistsException.class);
    }

    @Test
    public void whenSaveValidateSerialNo_thenCatchSerialFoundException() {

        ProductStock ps = new ProductStock();
        ps.setProductId(productId);
        ps.setSerialNo(serialNo);

        assertThat(productStockCommandService.save(ps).getErrMessage()).contains("PROD-ERR-001");
    }

    @Test
    public void whenSaveValidateWarrantyCardNo_thenCatchWarrantyFoundException() {

        ProductStock ps = new ProductStock();
        ps.setProductId(productId);
        ps.setWarrantyCardNo(warrantyCardNo);

        assertThat(productStockCommandService.save(ps).getErrMessage()).contains("PROD-ERR-002");
    }

    @Test
    public void whenSave_thenOk() {

        ProductStock ps = new ProductStock();
        ps.setDealerId(dealerId);
        ps.setId(productStockId);
        ps.setProductId(productId);
        ps.setStatus(ProductStockStatusEnum.CREATED);
        ps.setTradingBranchId(tradingBranchId);
        ps.setWarrantyCardNo(warrantyCardNoEmpty);
        ps.setSerialNo(serialNoEmpty);

        Mockito.when(productStockRepository.save(ps)).thenReturn(ps);

        ProductStock newProductStock = productStockCommandService.save(ps);
        assertThat(newProductStock.getId()).isEqualTo(productStockId);
    }

    @Test
    public void whenSaveList_thenOk() {

        ProductStock ps1 = new ProductStock();
        ps1.setDealerId(dealerId);
        ps1.setId(productStockId);
        ps1.setProductId(productId);
        ps1.setStatus(ProductStockStatusEnum.CREATED);
        ps1.setTradingBranchId(tradingBranchId);
        ps1.setWarrantyCardNo(warrantyCardNoEmpty);
        ps1.setSerialNo(serialNoEmpty);

        UUID productStockId2 = UUID.randomUUID();
        ProductStock ps2 = new ProductStock();
        ps2.setDealerId(dealerId);
        ps2.setId(productStockId2);
        ps2.setProductId(productId);
        ps2.setStatus(ProductStockStatusEnum.CREATED);
        ps2.setTradingBranchId(tradingBranchId);
        ps2.setWarrantyCardNo(warrantyCardNoEmpty + "-1");
        ps2.setSerialNo(serialNoEmpty + "-1");

        Mockito.when(productStockRepository.save(ps1)).thenReturn(ps1);
        Mockito.when(productStockRepository.save(ps2)).thenReturn(ps2);

        List<ProductStock> productStocks = new ArrayList<>();
        productStocks.add(ps1);
        productStocks.add(ps2);

        List<ProductStock> newProductStocks = productStockCommandService.save(productStocks);
        assertThat(newProductStocks.size()).isEqualTo(2);
        assertThat(newProductStocks.get(0).getId()).isEqualTo(productStockId);
        assertThat(newProductStocks.get(1).getId()).isEqualTo(productStockId2);
    }

}
