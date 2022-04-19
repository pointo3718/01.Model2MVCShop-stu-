package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;


public class PurchaseDAO {

	public PurchaseDAO() {
	}
	
	public void addPuchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,1,sysdate,?)";
		
		PreparedStatement Pstmt = con.prepareStatement(sql);
		Pstmt.setInt(1,purchaseVO.getPurchaseProd().getProdNo());
		Pstmt.setString(2, purchaseVO.getBuyerId().getUserId());
		Pstmt.setString(3, purchaseVO.getPaymentOption());
		Pstmt.setString(4, purchaseVO.getReceiverName());
		Pstmt.setString(5, purchaseVO.getReceiverPhone());
		Pstmt.setString(6, purchaseVO.getDivyAddr());
		Pstmt.setString(7, purchaseVO.getDivyRequest());
//		Pstmt.setString(8, purchaseVO.getTranCode());
		Pstmt.setString(8, purchaseVO.getDivyDate());
		Pstmt.executeUpdate();
	}

	public PurchaseVO getProduct(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction t,product p "
				+ "WHERE t.prod_no=p.prod_no AND t.tran_no = ?";
				
		
		PreparedStatement Pstmt = con.prepareStatement(sql);
		Pstmt.setInt(1, tranNo);

		ResultSet rs = Pstmt.executeQuery();

		PurchaseVO purchaseVO = new PurchaseVO();
		// 상세조회에 뜨는 정보 물품번호(purProd?tran_no?),구매자아이디(buyerId),구매방법(payoption),구매자이름(receiver_name),구매자번호(reciver_Phone),
		// 구매자주소(dlvy_add), 구매요청사항(dlvy_reuest), 배송희망일 (dlv_date), 주문일(order_data) =9개
		while (rs.next()) {
			ProductVO productVO = new ProductVO();
			UserVO userVO = new UserVO();
			userVO.setUserId(rs.getString("buyer_id")); //varchar2 정보 불러들임
			productVO.setProdNo(rs.getInt("prod_no"));  //number 정보 불러들임
			// 아래서부터 purchase 시작
			purchaseVO.setTranNo(rs.getInt("tran_no")); //number 구매번호 ??  04/12 getPurchaseProd().getProdNo());이건뭐지;;; 분명 purProd가 그거면 tranno는 어따쓰누
			purchaseVO.setPurchaseProd(productVO); // number(16) [PR prod_no] // 물품정보 이거랑 위에거랑 하는게 뭐지;; 똑같은건지 확인해봐야할듯 기억
			purchaseVO.setBuyerId(userVO); //varchar2(20)[PR user_id] 구매자 
			purchaseVO.setPaymentOption(rs.getString("payment_option").trim()); //char(3) 지불방식
			purchaseVO.setReceiverName(rs.getString("receiver_name")); //varchar2(20) 받는사람이름
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone")); //varchar2(14) 받는사람번호
			purchaseVO.setDivyAddr(rs.getString("demailaddr")); //varchar2 배송주소
			purchaseVO.setDivyRequest(rs.getString("dlvy_request")); //varchar2 배송 요구사항
			purchaseVO.setTranCode(rs.getString("tran_status_code").trim()); // char(3) 구매상태코드 (얘가 필요할까? list 에서 걸어주고 하면되는거 아닌가 변화를 줘야하나?)
			purchaseVO.setOrderDate(rs.getDate("order_data")); //date 구매일
			purchaseVO.setDivyDate(rs.getString("dlvy_date")); //date 배송희망일
		}
		return purchaseVO;
	}
	
	public void updatePuchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date=? WHERE tran_no=?";
		
		PreparedStatement Pstmt = con.prepareStatement(sql);
		Pstmt.setString(1, purchaseVO.getPaymentOption());
		Pstmt.setString(2, purchaseVO.getReceiverName());
		Pstmt.setString(3, purchaseVO.getReceiverPhone());
		Pstmt.setString(4, purchaseVO.getDivyAddr());
		Pstmt.setString(5, purchaseVO.getDivyRequest());
		Pstmt.setString(6, purchaseVO.getDivyDate());
		Pstmt.setInt(7, purchaseVO.getTranNo());
		Pstmt.executeUpdate();
	}
	
public HashMap<String, Object> getPurchaseList(SearchVO searchVO,String buyerId) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT t.tran_no, p.prod_no,u.user_id,t.receiver_name,t.receiver_phone,t.tran_status_code "
				+ "FROM transaction t,product p,users u "
				+ "WHERE t.prod_no=p.prod_no AND t.buyer_id=u.user_id AND u.user_id=? ";
				System.out.println("sql print"+sql);
		PreparedStatement Pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		Pstmt.setString(1, buyerId);

		ResultSet rs = Pstmt.executeQuery();
		
		System.out.println(buyerId);
		
		rs.last();
		int total = rs.getRow();
		System.out.println("로우의 수:" + total);
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));
		
		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		
		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				PurchaseVO purchaseVO = new PurchaseVO();
				UserVO userVO = new UserVO();
				
				userVO.setUserId(rs.getString("user_id"));
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseVO.setBuyerId(userVO);
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setTranCode(rs.getString("tran_status_code").trim());
				
				list.add(purchaseVO);
				
				if (!rs.next())
					break;
			}
		}
		map.put("list", list);
		
		Pstmt.close();
		con.close();
			
		return map;
	}

	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
	
	
		Connection con = DBUtil.getConnection();
		
//		String sql = "UPDATE transaction t product p SET tran_status_code=? WHERE t.prod_no = p.prod_no AND tran_no =?";
		String sql = "UPDATE transaction t product p SET tran_status_code=? WHERE t.prod_no = p.prod_no AND t.prod_no =?";
		//String sql = "UPDATE transaction SET tran_status_code WHERE prod_no=?" 
		
		PreparedStatement Pstmt = con.prepareStatement(sql);
		Pstmt.setString(1, purchaseVO.getTranCode().trim());
		Pstmt.setInt(2, purchaseVO.getTranNo());
		Pstmt.executeUpdate();
		
	
	}

//	public HashMap<String,Object> 

	
}
