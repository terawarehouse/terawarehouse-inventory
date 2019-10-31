package com.terawarehouse.web.application.inventory;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Edward P. Legaspi | czetsuya@gmail.com
 */
@RestController
@RequestMapping(path = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProductStockQueryController {

}
