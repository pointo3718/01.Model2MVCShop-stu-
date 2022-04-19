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
		// ����ȸ�� �ߴ� ���� ��ǰ��ȣ(purProd?tran_no?),�����ھ��̵�(buyerId),���Ź��(payoption),�������̸�(receiver_name),�����ڹ�ȣ(reciver_Phone),
		// �������ּ�(dlvy_add), ���ſ�û����(dlvy_reuest), �������� (dlv_date), �ֹ���(order_data) =9��
		while (rs.next()) {
			ProductVO productVO = new ProductVO();
			UserVO userVO = new UserVO();
			userVO.setUserId(rs.getString("buyer_id")); //varchar2 ���� �ҷ�����
			productVO.setProdNo(rs.getInt("prod_no"));  //number ���� �ҷ�����
			// �Ʒ������� purchase ����
			purchaseVO.setTranNo(rs.getInt("tran_no")); //number ���Ź�ȣ ??  04/12 getPurchaseProd().getProdNo());�̰ǹ���;;; �и� purProd�� �װŸ� tranno�� �������
			purchaseVO.setPurchaseProd(productVO); // number(16) [PR prod_no] // ��ǰ���� �̰Ŷ� �����Ŷ� �ϴ°� ����;; �Ȱ������� Ȯ���غ����ҵ� ���
			purchaseVO.setBuyerId(userVO); //varchar2(20)[PR user_id] ������ 
			purchaseVO.setPaymentOption(rs.getString("payment_option").trim()); //char(3) ���ҹ��
			purchaseVO.setReceiverName(rs.getString("receiver_name")); //varchar2(20) �޴»���̸�
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone")); //varchar2(14) �޴»����ȣ
			purchaseVO.setDivyAddr(rs.getString("demailaddr")); //varchar2 ����ּ�
			purchaseVO.setDivyRequest(rs.getString("dlvy_request")); //varchar2 ��� �䱸����
			purchaseVO.setTranCode(rs.getString("tran_status_code").trim()); // char(3) ���Ż����ڵ� (�갡 �ʿ��ұ�? list ���� �ɾ��ְ� �ϸ�Ǵ°� �ƴѰ� ��ȭ�� ����ϳ�?)
			purchaseVO.setOrderDate(rs.getDate("order_data")); //date ������
			purchaseVO.setDivyDate(rs.getString("dlvy_date")); //date ��������
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
		System.out.println("�ο��� ��:" + total);
		
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
