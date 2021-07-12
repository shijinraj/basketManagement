package com.wayfair.products.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wayfair.products.domain.Basket;
import com.wayfair.products.service.BasketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "REST APIs related to Cart")
@RestController
@RequestMapping("/api/baskets")
public class BasketController {

	@Autowired
	private BasketService basketService;

	@ApiOperation("Create a basket")
	@PutMapping
	public Basket createBasket(@RequestHeader("customerId") Long customerId,
			@RequestParam(value = "productId") Long productId, @RequestParam(value = "quantity") Integer quantity) {
		return basketService.createBasket(customerId, productId, quantity);
	}

	@ApiOperation("Update a basket")
	@PutMapping("/{id}")
	public Basket updateProuctInBasket(@RequestHeader("customerId") Long customerId,
			@PathVariable(value = "id") Long basketId, @RequestParam(value = "productId") Long productId,
			@RequestParam(value = "quantity") Integer quantity) {
		return basketService.updateProuctInBasket(customerId, basketId, productId, quantity);
	}

	@ApiOperation("Delete a basket or item")
	@DeleteMapping("/{id}")
	public void deleteProuctInBasket(@RequestHeader("customerId") Long customerId,
			@PathVariable(value = "id") Long basketId, @RequestParam(value = "productId") Long productId,
			@RequestParam(value = "quantity", required = false) Integer quantity) {
		basketService.deleteBasketItems(customerId, basketId, productId,quantity);
	}

}
