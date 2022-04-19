package com.model2.mvc.service.product;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.product.vo.ProductVO;

public interface ProductService {
	
	public ProductVO getProduct(int prodNo) throws Exception;
	
	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception;
	
	public void addProduct(ProductVO productVO) throws Exception;
	
	public void updateProduct(ProductVO productVO) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
}
