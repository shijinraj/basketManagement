package com.wayfair.products.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wayfair.products.domain.Basket;
import com.wayfair.products.domain.Item;
import com.wayfair.products.domain.Product;
import com.wayfair.products.domain.User;
import com.wayfair.products.repository.BasketRepository;
import com.wayfair.products.repository.ProductRepository;
import com.wayfair.products.repository.UserRepository;

@Transactional
@Service
public class BasketServiceImpl implements BasketService {

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;


	@Override
	public Basket createBasket(Long customerId, Long productId, Integer quantity) {
		validate(customerId, productId, quantity);

		Product product = getProduct(productId);
		Item item = Item.builder().id(productId).quantity(quantity).price(product.getPrice() * quantity).build();

		Basket basket = Basket.builder().user(getUser(customerId)).items(Collections.singleton(item)).build();

		return basketRepository.save(basket);
	}

	public Product getProduct(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid productId : " + productId));
	}

	public User getUser(Long customerId) {
		// Check whether the customerId present in the system. Else throw
		// IllegalArgumentException
		return userRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid customerId : " + customerId));

	}

	public void validate(Long customerId, Long productId, Integer quantity) {
		// Validate the customerId , productId and quantity
		Assert.notNull(customerId, "Invalid customerId : " + customerId);
		Assert.notNull(productId, "Invalid prouductId : " + productId);
		Assert.isTrue(quantity != null && quantity > 0, "Invalid quantity : " + quantity);
	}

	@Override
	public Basket updateProuctInBasket(Long customerId, Long basketId, Long productId, Integer quantity) {

		validate(customerId, productId, quantity);
		// Validate the basketId
		Assert.notNull(basketId, "Invalid basketId : " + basketId);

		Basket availableBasket = getBasket(basketId);

		validateUser(customerId, availableBasket);

		Product product = getProduct(productId);

		boolean isItemUpdated = updateItem(availableBasket, product,quantity);

		Optional.of(isItemUpdated).filter(isItemAvailable -> isItemAvailable == false).ifPresent(isItemNotAvailable -> {
			Optional.of(availableBasket).filter(basektDetails -> basektDetails.getItems() == null)
					.ifPresent(basektDetails -> basektDetails.setItems(new HashSet<Item>()));
			Item item = Item.builder().id(productId).quantity(quantity).price(product.getPrice() * quantity).build();
			availableBasket.getItems().add(item);
		});

		return basketRepository.save(availableBasket);
	}

	public Basket getBasket(Long basketId) {
		// Throws IllegalArgumentException for Invalid basketId
		Basket availableBasket = basketRepository.findById(basketId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid basketId : " + basketId));
		return availableBasket;
	}

	public boolean updateItem(Basket availableBasket, Product product, Integer quantity) {

		boolean isItemAvailable = isItemAvailableInBasket(product.getId(), availableBasket);

		if (isItemAvailable) {
			Optional.of(availableBasket).map(Basket::getItems).map(Collection::stream).orElseGet(Stream::empty)
					.filter(itemDetails -> itemDetails.getId().equals(product.getId())).findFirst()
					.ifPresent(itemDetails -> {
						itemDetails.setQuantity(itemDetails.getQuantity() + quantity);
						itemDetails.setPrice(itemDetails.getQuantity() * product.getPrice());
					});
		}

		return isItemAvailable;
	}

	public boolean isItemAvailableInBasket(Long productId, Basket availableBasket) {
		boolean isItemAvailable = Optional.of(availableBasket).map(Basket::getItems).map(Collection::stream)
				.orElseGet(Stream::empty).anyMatch(itemDetails -> itemDetails.getId().equals(productId));
		return isItemAvailable;
	}

	public void validateUser(Long customerId, Basket availableBasket) {
		// Throws IllegalArgumentException for Invalid customerId
		Optional.of(availableBasket).map(Basket::getUser).filter(user -> user.getId().equals(customerId))
				.orElseThrow(() -> new IllegalArgumentException("Invalid customerId : " + customerId));
	}

	@Override
	public void deleteBasketItems(Long customerId, Long basketId, Long productId, Integer quantity) {

		// Validate the customerId and productId
		Assert.notNull(customerId, "Invalid customerId : " + customerId);
		Assert.notNull(productId, "Invalid prouductId : " + productId);
		// Validate the basketId
		Assert.notNull(basketId, "Invalid basketId : " + basketId);

		Basket availableBasket = getBasket(basketId);

		validateUser(customerId, availableBasket);

		Product product = getProduct(productId);

		boolean isItemAvailableInBasket = isItemAvailableInBasket(productId, availableBasket);

		Optional.of(isItemAvailableInBasket).filter(value -> value == true)
				.orElseThrow(() -> new IllegalArgumentException("Invalid productId : " + productId));

		// removed an item if quantity is null or available quantity is equal to
		// quantity
		availableBasket.getItems().removeIf(itemDetails -> isRemoveItemOrAllItems(productId, quantity, itemDetails));

		if (quantity != null) {
			Assert.isTrue(quantity < 1, "Invalid quantity : " + quantity);
			validateItemQuantity(productId, availableBasket, quantity);

			availableBasket.getItems().stream().filter(itemDetails -> itemDetails.getId().equals(productId))
					.filter(itemDetails -> itemDetails.getQuantity() > quantity).findFirst().ifPresent(itemDetails -> {
						itemDetails.setQuantity(itemDetails.getQuantity() - quantity);
						itemDetails.setPrice((itemDetails.getQuantity() - quantity) * product.getPrice());
					});
		}
		
		basketRepository.save(availableBasket);

	}

	public boolean isRemoveItemOrAllItems(Long productId, Integer quantity, Item itemDetails) {
		return itemDetails.getId().equals(productId) && (quantity == null || isRemovesAllItems(quantity, itemDetails));
	}

	public boolean isRemovesAllItems(Integer quantity, Item itemDetails) {
		return quantity != null && (quantity == itemDetails.getQuantity());
	}

	public void validateItemQuantity(Long productId, Basket availableBasket, Integer quantity) {
		boolean isValidProductQuantity = Optional.of(availableBasket).map(Basket::getItems).map(Collection::stream)
				.orElseGet(Stream::empty).filter(itemDetails -> itemDetails.getId().equals(productId))
				.anyMatch(itemDetails -> itemDetails.getQuantity() >= quantity);

		// Throws IllegalArgumentException for Invalid quantity
		Optional.of(isValidProductQuantity).filter(value -> value == true)
				.orElseThrow(() -> new IllegalArgumentException("Invalid operation"));
	}

}
