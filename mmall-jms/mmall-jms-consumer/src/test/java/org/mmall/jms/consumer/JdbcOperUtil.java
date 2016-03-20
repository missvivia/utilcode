package org.mmall.jms.consumer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyl.mmall.ip.meta.LocationCode;

public class JdbcOperUtil {

	private static final String sql = "select * from Mmall_IP_LocationCode where `Level`=1";
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		System.out.println(getData());
	}

	public static List<LocationCode> getData()
			throws InstantiationException, IllegalAccessException {
		String url = "jdbc:mysql://jipiao1.photo.163.org:4332/vstore";
		String username = "mysql";
		String password = "mysql";
		List<LocationCode> locationCodeList = new ArrayList<LocationCode>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url, username, password);
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				locationCodeList.add(createLocationCode(rs));
			}

		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return locationCodeList;
	}

	private static LocationCode createLocationCode(ResultSet rs)
			throws SQLException {
		LocationCode locationCode = new LocationCode();
		locationCode.setId(rs.getLong("id"));
		locationCode.setLocationName(rs.getString("locationName"));
		locationCode.setCode(rs.getLong("code"));
		locationCode.setParentCode(rs.getLong("parentCode"));
		locationCode.setValid(rs.getBoolean("valid"));
		locationCode.setProvinceHead(rs.getString("provinceHead"));
		return locationCode;
	}

	public static Map<String, Class> getFieldNameMap(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			throw new RuntimeException("类:" + clazz.getName() + "没有字段值");
		}
		Map<String, Class> fMap = new HashMap<String, Class>();
		List<String> fNames = new ArrayList<String>();
		for (Field field : fields) {
			if (Modifier.toString(field.getModifiers()).indexOf("static") > 0) {
				continue;
			}
			fMap.put(field.getName(), field.getType());
		}
		return fMap;
	}

}
