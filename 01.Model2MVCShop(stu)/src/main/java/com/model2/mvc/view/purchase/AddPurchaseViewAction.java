package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class AddPurchaseViewAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("add View 시작");
		
		int prodNo = Integer.parseInt(request.getParameter("prod_no"));
		System.out.println(prodNo);
		
		ProductService service = new ProductServiceImpl();
		ProductVO productVO = service.getProduct(prodNo);
		
//		HttpSession session = request.getSession();
//        UserVO dbVO = (UserVO)session.getAttribute("user");
//        System.out.println(dbVO);
		
//        request.setAttribute("user", dbVO);
		request.setAttribute("productVO", productVO);		
		System.out.println(productVO);
		
		System.out.println("add View 종료");
		return "forward:/purchase/addPurchase.jsp";
	}

}
