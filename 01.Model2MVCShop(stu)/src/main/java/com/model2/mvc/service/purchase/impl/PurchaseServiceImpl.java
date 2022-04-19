package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class PurchaseServiceImpl implements PurchaseService {
	
	private PurchaseDAO purchaseDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
	}
	public void addPurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.addPuchase(purchaseVO);
	}
	public PurchaseVO getPurchase(int tranNo) throws Exception {
		return purchaseDAO.getProduct(tranNo);
	}
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updatePuchase(purchaseVO);
	}
	public HashMap<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		
		return purchaseDAO.getPurchaseList(searchVO, buyerId);
	}
//	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {
//		
////		return purchaseDAO.getSaleList(searchVO);
//		return null;
//	}
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updateTranCode(purchaseVO);
	}

}
