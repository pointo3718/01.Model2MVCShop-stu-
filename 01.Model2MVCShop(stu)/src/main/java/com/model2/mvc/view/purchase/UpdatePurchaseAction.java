package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdatePurchaseAction extends Action {

	   @Override
	   public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	      System.out.println("update purchase 시작");
	      
	      int tranNo = Integer.parseInt(request.getParameter("tranNo"));
	      
	      PurchaseVO purchaseVO = new PurchaseVO();
	      
//	      HttpSession session = request.getSession();
//	      UserVO userVO = (UserVO) session.getAttribute("user");
//	      
//	      purchaseVO.setBuyerId(userVO);
	      purchaseVO.setTranNo(tranNo);
	      purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
	      purchaseVO.setReceiverName(request.getParameter("receiverName"));
	      purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
	      purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
	      purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
	      purchaseVO.setDivyDate(request.getParameter("receiverDate"));
	      
	      PurchaseService service = new PurchaseServiceImpl();
	      service.updatePurchase(purchaseVO); 
	      
	      System.out.println("update purchase 종료");
	      return "forward:/getPurchase.do?tranNo="+tranNo;
	   }
	}

