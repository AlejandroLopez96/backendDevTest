package com.alex.zara.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.alex.zara.exception.NotFoundException;
import com.alex.zara.model.ProductDetail;

@Service
public interface ProductService {

	public Set<ProductDetail> getSimilarProducts(String productId) throws NotFoundException, Exception;

}
