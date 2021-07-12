package com.wayfair.products.service;

import com.wayfair.products.domain.Basket;

public interface BasketService {
	
	/*Basket createBasket(Basket basket);*/
	
	Basket createBasket(Long customerId,Long productId, Integer quantity);
	
	Basket updateProuctInBasket(Long customerId,Long basketId, Long productId, Integer quantity);
	
	void deleteBasketItems(Long customerId,Long basketId, Long productId,Integer quantity);

}
