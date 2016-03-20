package com.xyl.mmall.itemcenter.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.framework.vo.BasePageParamVO;

public class SQLUtils {
	static public void appendExtParamObject(StringBuilder sql, String column, Object obj) {
		if (obj.getClass().getName().equals("java.lang.String")) {
			String value = (String) obj;
			if (!StringUtils.isBlank(value)) {
				value = value.replaceAll("\\\\", "\\\\\\\\");
				SqlGenUtil.appendExtParamObject(sql, column, value);
			}

		} else
			SqlGenUtil.appendExtParamObject(sql, column, obj);
	}
	
	/**
	 * 追加order by参数
	 * @param sql
	 * @param columns 排序字段，不能为空
	 * @param isASC 排序顺序，true为升序，false降序。空为升序，不为空必须与排序字段长度相等
	 * @throws SQLException 
	 */
	public static void appendExtOrderByColumns(StringBuilder sql, String[] columns, boolean[] isASC) throws SQLException {
		sql.append(" ORDER BY ");
		// 排序字段不能为空
		if (columns == null || columns.length == 0) {
			throw new SQLException("Appand ext order by columns error! Param columns is blank!");
		}
		// 排序顺序如果不为空，长度要与排序字段相等
		if (isASC != null && isASC.length != 0) {
			if (isASC.length != columns.length) {
				throw new SQLException("Appand ext order by columns error! Param isASC length is not equal with columns!");
			}
		}

		if (StringUtils.isBlank(columns[0]))
			throw new SQLException("Appand ext order by columns error! Param columns is blank!");
		else
			sql.append(columns[0]);
		if (!isASC[0]) {
			sql.append(" DESC");
		}
		for (int i = 1; i < columns.length; i++) {
			if (StringUtils.isBlank(columns[i]))
				throw new SQLException("Appand ext order by columns error! Param columns is blank!");
			else
				sql.append(", ").append(columns[i]);
			if (!isASC[i]) {
				sql.append(" DESC");
			}
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 */
	public static void getPageSql(StringBuilder sql, BasePageParamVO<?> basePageParamVO) {
		int offset = basePageParamVO.getStartRownum() - 1;
		int limit = basePageParamVO.getPageSize();
		sql.append(" LIMIT ").append(offset).append(", ").append(limit);
	}
	
	/**
	 * insert语句
	 * @param obj
	 * @param tableName
	 * @param hasEnter
	 * @param excludeFieldNameSet
	 * @return
	 */
	public static <T> String insertSQL(T obj, String tableName, boolean hasEnter, Set<String> excludeFieldNameSet) {
		if (obj == null) {
			return null;
		}
		Map<Field, Object> fieldAndValueMap = SqlGenUtil.genMapForSql(obj, null);
		if (CollectionUtil.isNotEmptyOfCollection(excludeFieldNameSet)) {
			for (String excludeFieldName : excludeFieldNameSet) {
				for (Field f : fieldAndValueMap.keySet()) {
					if (StringUtils.equals(f.getName(), excludeFieldName)) {
						fieldAndValueMap.remove(f);
						break;
					}
				}
			}
		}

		List<String> columnNameArray = new ArrayList<String>();
		List<Object> columnValueArray = new ArrayList<Object>();
		genNameValueArrayByMap(fieldAndValueMap, columnNameArray, columnValueArray, getDBCreateTimeName(obj));

		String sql = genInsertSql(tableName, columnNameArray);
		String regexOfQuestion = "##--##--##", regexOfSQuote = "##''##''##";
		sql = sql.replaceAll("\\?", regexOfQuestion);

		for (Object value : columnValueArray) {
			StringBuilder rValue = new StringBuilder(128);
			if (value instanceof String) {
				rValue.append("'");
				value = ((String) value).replaceAll("\r", "");
				value = ((String) value).replaceAll("\n", "");
				value = ((String) value).replaceAll("'", regexOfSQuote);
			}
			rValue.append(value);
			if (value instanceof String) {
				rValue.append("'");
			}
			sql = sql.replaceFirst(regexOfQuestion, rValue.toString());
		}
		sql = sql.replaceAll(regexOfSQuote, "\\\\'");
		sql = sql + ";";
		return hasEnter ? sql + "\r\n" : sql;
	}
	
	/**
	 * 返回在数据库里,CreateTime字段的名称
	 * 
	 * @param obj
	 * @return
	 */
	private static <T1> String getDBCreateTimeName(T1 obj) {
		// 1.读取Class上的AnnonOfClass
		Class<?> clazz = obj.getClass();
		AnnonOfClass annonOfClass = genAnnonOfClass(clazz);
		// 2.返回在数据库里,CreateTime字段的名称
		return annonOfClass.dbCreateTimeName();
	}

	/**
	 * 读取Class上定义的TableName
	 * 
	 * @param clazz
	 * @return
	 */
	private static AnnonOfClass genAnnonOfClass(Class<?> clazz) {
		// 1.读取Class上的AnnonOfClass
		AnnonOfClass annonOfClass = clazz.getAnnotation(AnnonOfClass.class);
		if (annonOfClass == null) {
			// 如果读取不到,则尝试去父类读取
			Class<?> superClazz = clazz.getSuperclass();
			if (superClazz == null)
				return null;
			else
				return genAnnonOfClass(superClazz);
		} else
			return annonOfClass;
	}
	
	/**
	 * 将Map的Key和Value分解成顺序完全一致的两个List
	 * 
	 * @param columnMap
	 * @param columnNameArray
	 * @param columnValueArray
	 * @param dbCreateTimeName
	 *            在数据库里,CreateTime字段的名称
	 */
	private static void genNameValueArrayByMap(Map<Field, Object> columnMap, List<String> columnNameArray,
			List<Object> columnValueArray, String dbCreateTimeName) {
		for (Field field : columnMap.keySet()) {
			String dbName = SqlGenUtil.genDBName(field);
			if (StringUtils.isBlank(dbName))
				continue;
			columnNameArray.add(dbName);
			columnValueArray.add(columnMap.get(field));
		}
		if (StringUtils.isNotBlank(dbCreateTimeName)) {
			columnNameArray.add(dbCreateTimeName);
			columnValueArray.add(new Timestamp(System.currentTimeMillis()));
		}
	}

	/**
	 * 生成Insert的Prepare语句
	 * 
	 * @param tableName
	 *            表名
	 * @param columnNameList
	 *            数据库字段名
	 * @return
	 */
	private static String genInsertSql(String tableName, List<String> columnNameList) {
		// 0.参数判断
		if (StringUtils.isBlank(tableName)) {
			return null;
		}
		if (CollectionUtil.isEmptyOfCollection(columnNameList)) {
			return null;
		}

		// 1.生成列的名字和值
		StringBuilder columnNameSb = new StringBuilder(256);
		StringBuilder columnValueSb = new StringBuilder(256);
		int index = 0;
		for (String columnName : columnNameList) {
			columnNameSb.append(columnName);
			columnValueSb.append("?");
			if (index < columnNameList.size() - 1) {
				columnNameSb.append(",");
				columnValueSb.append(",");
			}
			index++;
		}

		// 2.生成SQL
		StringBuilder sql = new StringBuilder(256);
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		sql.append(columnNameSb);
		sql.append(") VALUES(");
		sql.append(columnValueSb);
		sql.append(")");
		return sql.toString();
	}
}
