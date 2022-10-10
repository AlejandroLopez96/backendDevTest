package com.alex.zara.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alex.zara.model.ProductDetail;
import com.alex.zara.utils.constants.ProductConstants;

@RestController
@RequestMapping(ProductConstants.PATH_PRODUCT_CONTROLLER)
public interface ProductController {
	
	@GetMapping(ProductConstants.PATH_SIMILAR_PRODUCTS)
	public ResponseEntity<Set<ProductDetail>> getSimilarProducts(@PathVariable String productId);
	
}
