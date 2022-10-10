package com.alex.zara.controller.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.alex.zara.controller.ProductController;
import com.alex.zara.exception.NotFoundException;
import com.alex.zara.model.ProductDetail;
import com.alex.zara.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ProductControllerImpl implements ProductController{

	@Autowired
	private ProductService productService;
	
	public ResponseEntity<Set<ProductDetail>> getSimilarProducts(@PathVariable String productId) {
		
		log.info("Start ProductController.getSimilarProduct with param productId: {}", productId);
		
		Set<ProductDetail> result = null;
		
		try {
			result = this.productService.getSimilarProducts(productId);
			
		} catch (NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Finish ProductController.getSimilarProduct");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
