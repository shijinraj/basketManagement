package com.wayfair.products.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Item {

	@Id
    @Column(name = "user_id")
	private Long id;

	@NotNull(message = "Item Quantity is required")
	private Integer quantity;
	
	@NotNull(message = "Item price is required")
	private Double price;
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

}
