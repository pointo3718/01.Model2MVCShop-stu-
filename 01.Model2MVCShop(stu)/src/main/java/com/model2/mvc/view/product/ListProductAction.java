package com.model2.mvc.view.product;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
//import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class ListProductAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("Product list Action 시작");
		
		SearchVO searchVO = new SearchVO();
		ProductVO productVO = new ProductVO();
//		PurchaseVO purchaseVO = new PurchaseVO();
		
		
//		productVO.setProTranCode(purchaseVO.getTranCode());
		
		int page = 1;
		if(request.getParameter("page")!=null)
			page=Integer.parseInt(request.getParameter("page"));
		
		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		
		ProductService service=new ProductServiceImpl();
		HashMap<String,Object> map=service.getProductList(searchVO);
		
		System.out.println("list map"+map);
		System.out.println(productVO);
		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		
		System.out.println("Product list Action 종료");
		return "forward:/product/listProduct.jsp";
	}
}
