/**
 * 
 */
package com.xyl.mmall;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netease.print.common.util.DateFormatUtil;
import com.netease.print.common.util.FileOperationUtil;
import com.netease.print.daojar.basic.ConnectionManagerQSImpl;
import com.xyl.mmall.order.enums.OrderFormState;

/**
 * @author lihui
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OnlineOrderTest {

	@Configuration
	static class Config {
		@Bean(initMethod = "init")
		public ConnectionManagerQSImpl connectionManagerQSImpl() {
			ConnectionManagerQSImpl client = new ConnectionManagerQSImpl();
			// TEST
//			 client.setUrl("jdbc:mysql://10.120.152.84:6000/vstore-test");
//			 client.setUser("vstoretest");
//			 client.setPass("Y7_ue0OLp2");
			// PROD
			 client.setUrl("jdbc:mysql://192.168.24.10:6000/mmallnode1");
			 client.setUser("xyl-ddb");
			 client.setPass("xyl-mmall");
			client.setDriver("com.mysql.jdbc.Driver");
			client.setMaxActive(50);
			client.setMaxIdle(60000);
			client.setMaxWait(60000);
			client.setTestOnBorrow(true);
			client.setValidationQuery("select 1");
			return client;
		}
	}

	@Autowired
	private ConnectionManagerQSImpl connectionManagerQSImpl;

	@Test
	public void testGetOrder() {
		Connection connection = connectionManagerQSImpl.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		BufferedWriter bw = FileOperationUtil.genBufferedWriter(
				"D:/sms/order" + DateFormatUtil.getFormatDateType1(System.currentTimeMillis()) + ".txt", false);
		FileOperationUtil.writeBufferedWriterWithEnter(bw, "用户Id,用户账号,订单Id,下单时间,支付时间,订单状态,联系手机,固定电话,收件人,省,市,区,街道,详细地址");
		try {
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			long endTime = now.getTimeInMillis();
			System.out.println(endTime);
			now.add(Calendar.DAY_OF_MONTH, -1);
			long startTime = now.getTimeInMillis();
			System.out.println(startTime);
			String sql = "Select a.userId, a.userName, o.orderId, o.orderFormState, o.orderTime, o.payTime, e.consigneeMobile, e.consigneeTel, e.consigneeName, e.province, e.city, e.section, e.street, e.address from Mmall_Member_UserProfile a, Mmall_Order_OrderForm o, Mmall_Order_OrderExpInfo e where a.userId = o.userId and o.orderId = e.orderId and o.orderTime > "
					+ startTime + " And o.orderTime <" + endTime;
			System.out.println(sql);
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			
			HSSFWorkbook hssfworkbook = new HSSFWorkbook();
			HSSFSheet hssfsheet = hssfworkbook.createSheet("订单统计");
			HSSFRow titleRow = hssfsheet.createRow(0);
			buildTitle(titleRow);
			int rowIdx = 1;
			
			while (rs.next()) {
				
				String userId = rs.getLong("userId") + "";
				String userName = rs.getString("userName");
				String orderId = rs.getLong("orderId") + "";
				String orderTime = DateFormatUtil.getFormatDateType14(rs.getLong("orderTime"));
				String payTime = (rs.getLong("payTime") == 0L ? "": DateFormatUtil.getFormatDateType14(rs.getLong("payTime")));
				String orderState = OrderFormState.genEnumByIntValueSt(rs.getInt("orderFormState")).getDesc();
				String consigneeMobile = rs.getString("consigneeMobile");
				String consigneeTel = rs.getString("consigneeTel");
				String consigneeName = rs.getString("consigneeName");
				String province = rs.getString("province");
				String city = rs.getString("city");
				String section = rs.getString("section");
				String street = rs.getString("street");
				String address = rs.getString("address");
				
				HSSFRow row = hssfsheet.createRow(rowIdx++);
				buildCell(row, (short)0, userId);
				buildCell(row, (short)1, userName);
				buildCell(row, (short)2, orderId);
				buildCell(row, (short)3, orderTime);
				buildCell(row, (short)4, payTime);
				buildCell(row, (short)5, orderState);
				buildCell(row, (short)6, consigneeMobile);
				buildCell(row, (short)7, consigneeTel);
				buildCell(row, (short)8, consigneeName);
				buildCell(row, (short)9, province);
				buildCell(row, (short)10, city);
				buildCell(row, (short)11, section);
				buildCell(row, (short)12, street);
				buildCell(row, (short)13, address);
				
				String data = userId + "," + userName + "," + orderId + ","
						+ orderTime + ","
						+ payTime + ","
						+ orderState + ","
						+ consigneeMobile + "," + consigneeTel + ","
						+ consigneeName + "," + province + "," + city
						+ "," + section + "," + street + "," + address;
				System.out.println(data);
				FileOperationUtil.writeBufferedWriterWithEnter(bw, data);
			}
			
			FileOutputStream fileoutputstream = new FileOutputStream("c:\\var\\order2015-02-04.xls");
			hssfworkbook.write(fileoutputstream);
			fileoutputstream.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			FileOperationUtil.closeBufferedWriter(bw);
		}
	}
	
	private void buildCell(HSSFRow hssfrow, short idx, String val) {
		HSSFCell cell = hssfrow.createCell(idx);
		HSSFRichTextString str = new HSSFRichTextString(val);
		cell.setCellValue(str);
	}
	
	private void buildTitle(HSSFRow hssfrow) {
		buildCell(hssfrow, (short)0, "用户Id");
		buildCell(hssfrow, (short)1, "用户账号");
		buildCell(hssfrow, (short)2, "订单Id");
		buildCell(hssfrow, (short)3, "下单时间");
		buildCell(hssfrow, (short)4, "支付时间");
		buildCell(hssfrow, (short)5, "订单状态");
		buildCell(hssfrow, (short)6, "联系手机");
		buildCell(hssfrow, (short)7, "固定电话");
		buildCell(hssfrow, (short)8, "收件人");
		buildCell(hssfrow, (short)9, "省");
		buildCell(hssfrow, (short)10, "市");
		buildCell(hssfrow, (short)11, "区");
		buildCell(hssfrow, (short)12, "街道");
		buildCell(hssfrow, (short)13, "详细地址");
	}
	
//	@Test
//	public void testGetCorpOrder() {
//		Connection connection = connectionManagerQSImpl.getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		BufferedWriter bw = FileOperationUtil.genBufferedWriter(
//				"D:/sms/corporder" + DateFormatUtil.getFormatDateType1(System.currentTimeMillis()) + ".txt", false);
//		FileOperationUtil.writeBufferedWriterWithEnter(bw, "用户Id,用户账号,联系手机,固定电话,收件人");
//		try {
//			Calendar now = Calendar.getInstance();
//			now.set(Calendar.HOUR_OF_DAY, 0);
//			now.set(Calendar.MINUTE, 0);
//			now.set(Calendar.SECOND, 0);
//			now.set(Calendar.MILLISECOND, 0);
//			long endTime = now.getTimeInMillis();
//			System.out.println(endTime);
//			now.add(Calendar.DAY_OF_MONTH, -1);
//			long startTime = now.getTimeInMillis();
//			System.out.println(startTime);
//			String sql = "Select c.userId, c.userName, o.couponCode, e.consigneeMobile, e.consigneeTel, e.consigneeName FROM Mmall_Order_OrderExpInfo e, Mmall_Promotion_CouponOrder o, Mmall_Member_UserProfile c where o.couponCode in ('wyng000','wyng001','wyng002','wyng003') And e.orderId = o.orderId And c.userId = o.userId order by couponCode";
//			System.out.println(sql);
//			ps = connection.prepareStatement(sql);
//			rs = ps.executeQuery();
//			while (rs.next()) {
//				String data = rs.getLong("userId") + "," + rs.getString("userName") + "," + rs.getString("couponCode") + ","
//						+ rs.getString("consigneeMobile") + "," + rs.getString("consigneeTel") + ","
//						+ rs.getString("consigneeName");
//				System.out.println(data);
//				FileOperationUtil.writeBufferedWriterWithEnter(bw, data);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (rs != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			FileOperationUtil.closeBufferedWriter(bw);
//		}
//	}
}
