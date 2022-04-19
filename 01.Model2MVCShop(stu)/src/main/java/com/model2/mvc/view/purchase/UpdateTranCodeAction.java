package com.model2.mvc.view.purchase;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;


public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ProductVO productVO = new ProductVO();
		PurchaseVO purchaseVO = new PurchaseVO();
//		String tranCode = request.getParameter("tranCode");
//		productVO.setProTranCode(tranCode);
		
		productVO.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setTranCode(request.getParameter("tranCode"));
		
		String menu = request.getParameter("menu");
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updateTranCode(purchaseVO);
		
		if(menu.equals("manage")) {
			return "redirect:/listProduct.do?menu=manage";
		}else {
			return "redirect:/listProduct.do?menu=search";
		}
		
	}
}
