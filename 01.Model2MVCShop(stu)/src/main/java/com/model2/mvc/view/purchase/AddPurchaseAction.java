package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("add action 시작");
		
		int prodNo = (Integer.parseInt(request.getParameter("prodNo")));
		
		ProductService productService = new ProductServiceImpl();
		ProductVO productVO = productService.getProduct(prodNo);
		
//		ProductVO productVO = new ProductVO();
//		UserVO userVO = new UserVO();
	
//		productVO.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
//		userVO.setUserId(request.getParameter("userId"));
//		System.out.println(productVO);
//		System.out.println(userVO);
		HttpSession session = request.getSession();
		UserVO userVO = (UserVO) session.getAttribute("user");
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setBuyerId(userVO);
//		userVO.setUserId(request.getParameter("buyerId"));
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("receiverDate"));
//		purchaseVO.setTranCode(request.getParameter("tran_status_code"));
//		purchaseVO.setBuyerId(userVO);
		
		System.out.println("유저"+userVO);
		System.out.println("구매"+purchaseVO);
		
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(purchaseVO);
		
//		HttpSession session = request.getSession();
//		session.setAttribute("purchaseVo", purchaseVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
		System.out.println("add action 종료");
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
