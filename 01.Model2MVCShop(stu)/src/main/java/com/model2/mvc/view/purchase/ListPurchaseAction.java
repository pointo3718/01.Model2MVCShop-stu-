package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;

public class ListPurchaseAction extends Action{
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		System.out.println("list purchase Action 시작");
		SearchVO searchVO = new SearchVO();
		
		int page = 1;
		
		if(request.getParameter("page")!=null)
			page=Integer.parseInt(request.getParameter("page"));
		
		searchVO.setPage(page);
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		
		HttpSession session = request.getSession(); //user id정보가 필요함 session 로그인 정보 가져옴
		UserVO userVO = (UserVO)session.getAttribute("user");
		String buyerId = userVO.getUserId(); 
		
		PurchaseService service=new PurchaseServiceImpl();
		HashMap<String,Object> map=service.getPurchaseList(searchVO, buyerId); //id 값도 필요함 service에 추가
		//service 수정후 Impl도 수정해주기;
		System.out.println(2);
		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		System.out.println(map);
		System.out.println("list purchase Action 종료");
		
		return "forward:/purchase/listPurchase.jsp";
	}
}
