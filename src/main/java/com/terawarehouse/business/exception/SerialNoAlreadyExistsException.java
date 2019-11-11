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
package com.terawarehouse.business.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.broodcamp.business.exception.BusinessNonRuntimeException;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 * 
 * @since 0.0.1
 * @version 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ResponseStatus(value = HttpStatus.CONFLICT)
public class SerialNoAlreadyExistsException extends BusinessNonRuntimeException {

    private static final long serialVersionUID = 2811363285174806464L;

    public SerialNoAlreadyExistsException(UUID productId, String serialNo) {
        super("PROD-ERR-001", String.format("SerialNo %s already exists for product with id %s", serialNo, productId));
    }
}
