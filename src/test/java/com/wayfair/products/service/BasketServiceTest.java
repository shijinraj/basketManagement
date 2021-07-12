package com.wayfair.products.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wayfair.products.domain.Basket;
import com.wayfair.products.domain.Item;
import com.wayfair.products.domain.Product;
import com.wayfair.products.domain.User;
import com.wayfair.products.repository.BasketRepository;
import com.wayfair.products.repository.ProductRepository;
import com.wayfair.products.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@DisplayName("Basket Service Test")
@TestInstance(Lifecycle.PER_CLASS)
class BasketServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private BasketRepository basketRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private BasketService basketService = new BasketServiceImpl();

	@BeforeAll
	void setUpBeforeClass() throws Exception {

	}

	@Test
	void testCreateBasketWithInvalidInputs() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(null, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(1l, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(1l, 1l, null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(1l, 1l, 0));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(1l, 1l, 1));

		when(productRepository.findById(1l)).thenReturn(Optional.of(Product.builder().id(1l).price(1.5).build()));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.createBasket(1l, 1l, 1));
	}

	@Test
	void testCreateBasket() {
		// Given
		Product product = Product.builder().id(1l).price(1.5).build();
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		User user = User.builder().id(1l).name("testUser").build();
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		Item item = Item.builder().id(product.getId()).quantity(1).price(product.getPrice() * 1l).build();

		Basket basket = Basket.builder().user(user).items(Collections.singleton(item)).build();
		when(basketRepository.save(any())).thenReturn(basket);
		Assertions.assertEquals(basket, basketService.createBasket(user.getId(), product.getId(), 1));
	}

	@Test
	void testUpdateProuctInBasketWithInvalidInputs() {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> basketService.updateProuctInBasket(null, 1l, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> basketService.updateProuctInBasket(1l, null, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> basketService.updateProuctInBasket(1l, 1l, null, 1));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> basketService.updateProuctInBasket(1l, 1l, 1l, 0));
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> basketService.updateProuctInBasket(1l, 1l, 1l, 1));
	}

	@Test
	void testUpdateProuctInBasketWithExistingItem() {

		// Given
		Product product = Product.builder().id(1l).price(1.5).build();
		User user = User.builder().id(1l).name("testUser").build();
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		Item item = Item.builder().id(product.getId()).quantity(1).price(product.getPrice() * 1l).build();
		Basket basket = Basket.builder().id(1l).user(user).items(Collections.singleton(item)).build();
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(basketRepository.findById(basket.getId())).thenReturn(Optional.of(basket));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		Basket expectedBasket = Basket.builder().id(1l).user(user)
				.items(Collections.singleton(Item.builder().id(product.getId()).quantity(2).price(3.0).build()))
				.build();
		when(basketRepository.save(expectedBasket)).thenReturn(expectedBasket);

		// When
		Basket updatedBasket = basketService.updateProuctInBasket(1l, 1l, 1l, 1);
		// Then
		Assertions.assertEquals(expectedBasket, updatedBasket);

	}

	@Test
	void testUpdateProuctInBasketWithNewItem() {

		// Given
		Product product = Product.builder().id(1l).price(1.5).build();
		User user = User.builder().id(1l).name("testUser").build();
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		Item item = Item.builder().id(product.getId()).quantity(1).price(product.getPrice() * 1l).build();
		Set<Item> items = new HashSet<>();
		items.add(item);
		Basket basket = Basket.builder().id(1l).user(user).items(items).build();

		Product productNew = Product.builder().id(2l).price(2.0).build();
		when(productRepository.findById(productNew.getId())).thenReturn(Optional.of(productNew));

		when(basketRepository.findById(basket.getId())).thenReturn(Optional.of(basket));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		Item newItem = Item.builder().id(productNew.getId()).quantity(1).price(productNew.getPrice()).build();
		Set<Item> expectedItems = new HashSet<>();
		expectedItems.add(item);
		expectedItems.add(newItem);
		Basket expectedBasket = Basket.builder().id(1l).user(user).items(expectedItems).build();
		when(basketRepository.save(expectedBasket)).thenReturn(expectedBasket);

		// When
		Basket updatedBasket = basketService.updateProuctInBasket(1l, 1l, productNew.getId(), 1);
		// Then
		Assertions.assertEquals(expectedBasket, updatedBasket);

	}

	@Test
	void testDeleteBasketItemsWithInvalidInputs() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.deleteBasketItems(null, 1l, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.deleteBasketItems(1l, null, 1l, 1));
		Assertions.assertThrows(IllegalArgumentException.class, () -> basketService.deleteBasketItems(1l, 1l, null, 1));
	}

	@Test
	void testDeleteBasketItem() {
		// Given
		Product product = Product.builder().id(1l).price(1.5).build();
		User user = User.builder().id(1l).name("testUser").build();
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		Item item = Item.builder().id(product.getId()).quantity(1).price(product.getPrice() * 1l).build();
		Basket basket = Basket.builder().id(1l).user(user).items(Collections.singleton(item)).build();
		when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
		when(basketRepository.findById(basket.getId())).thenReturn(Optional.of(basket));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		basketService.deleteBasketItems(null, 1l, 1l, 1);
	}

}
