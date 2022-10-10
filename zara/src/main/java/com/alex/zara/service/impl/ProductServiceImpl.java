package com.alex.zara.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.alex.zara.exception.NotFoundException;
import com.alex.zara.model.ProductDetail;
import com.alex.zara.service.ProductService;
import com.alex.zara.utils.Utils;
import com.alex.zara.utils.constants.ProductConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
//	If we have differents profiles of compilation this env variable gives us the chance to select values 
//	from application-{profile}.properties and the generic application.properties
	private Environment env;
	
	public Set<ProductDetail> getSimilarProducts(String productId) throws NotFoundException, Exception {
		log.info("Start ProductService.getSimilarProducts with productId: {}", productId);
		Set<ProductDetail> result = null;
		
		try {
			Set<String> listSimilarIds = this.getSimilarIds(productId);
			
			result = this.getSimilarProductsByListSimilarIds(listSimilarIds);
			
		} catch (Exception e) {
			log.error("There is an Exception in ProductService.getSimilarProducts: {}", e.getMessage());
			throw e;
		}
		
		log.info("Finish ProductService.getSimilarProducts : {}", Utils.jsonMe(result));
		return result;
	}
	
	private Set<String> getSimilarIds(String productId) throws Exception {
		log.info("Start ProductService.getSimilarIds");
		Set<String> similarIds = new HashSet<>();
		RestTemplate restTemplate = new RestTemplate();
		StringBuilder url = new StringBuilder();
		url.append(this.env.getProperty("url.api.mocks"));
		url.append(ProductConstants.PATH_EXTERNAL_PRODUCT);
		url.append("/" + productId);
		url.append(ProductConstants.PATH_EXTERNAL_SIMILAR_IDS);
		
		try {
			ResponseEntity<Set<String>> resp = restTemplate.exchange(url.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<Set<String>>() {});
			if (resp != null && resp.getStatusCode().is2xxSuccessful()) {
				similarIds.addAll(resp.getBody());
			} else {
				log.warn("Not found products for productId: {}", productId);
			}
		} catch (Exception e) {
			log.error("There is an error in ProductService.getSimilarIds: {}", e.getMessage());
			throw e;
		}
		
		log.info("Finish ProductService.getSimilarIds : {}", similarIds);
		return similarIds;
	}
	
	private Set<ProductDetail> getSimilarProductsByListSimilarIds(Set<String> listSimilarIds) throws NotFoundException, Exception {
		log.info("Start ProductService.getSimilarProductsByListSimilarIds");
		Set<ProductDetail> listSimilarProducts = new HashSet<>();
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			listSimilarIds.stream().forEach(similarId -> {
				StringBuilder url = new StringBuilder();
				url.append(this.env.getProperty("url.api.mocks"));
				url.append(ProductConstants.PATH_EXTERNAL_PRODUCT);
				url.append("/" + similarId);
				ResponseEntity<ProductDetail> resp = restTemplate.exchange(url.toString(), HttpMethod.GET, null, ProductDetail.class);
				if (resp != null && resp.getStatusCode().is2xxSuccessful()) {
					listSimilarProducts.add(resp.getBody());
				} else {
					log.info("Not found product with id: {}", similarId);
				}
			});
			
			if (listSimilarProducts.isEmpty()) {
				throw new NotFoundException(ProductConstants.MESSAGE_NOT_FOUND_EXCEPTION);
			}
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().is4xxClientError()) {
				log.error("There is an NOT_FOUND_ERROR in ProductService.getSimilarProductsByListSimilarIds: {}", e.getMessage());
				throw new NotFoundException(ProductConstants.MESSAGE_NOT_FOUND_EXCEPTION);
			}
		} catch (Exception e) {
			log.error("There is an error in ProductService.getSimilarProductsByListSimilarIds: {}", e.getMessage());
			throw e;
		}
		
		log.info("Finish ProductService.getSimilarProductsByListSimilarIds");
		return listSimilarProducts;
	}
	
}
