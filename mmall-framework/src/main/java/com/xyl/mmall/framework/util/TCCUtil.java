package com.xyl.mmall.framework.util;

import java.util.ArrayList;
import java.util.List;

import com.netease.backend.tcc.error.ParticipantException;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.daojar.util.PrintDaoUtil;
import com.netease.print.daojar.util.ReflectUtil;
import com.netease.print.daojar.util.SqlGenUtil;
import com.netease.space.framework.dao.sql.AbstractDaoSqlBase;
import com.xyl.mmall.framework.interfaces.TCCMetaExam;
import com.xyl.mmall.framework.interfaces.TCCMetaInterface;

/**
 * @author dingmingliang
 * 
 */
public class TCCUtil {

	/**
	 * 根据tranId,获得相关的数据集合
	 * 
	 * @param tranId
	 * @param dao
	 * @return
	 */
	public static <T> List<T> getListByTranId(long tranId, AbstractDaoSqlBase<T> dao) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(PrintDaoUtil.genSelectSql(dao));
		SqlGenUtil.appendExtParamObject(sql, "tranId", tranId);
		return dao.queryObjects(sql.toString());
	}

	/**
	 * 根据tranId,删除相关的数据集合
	 * 
	 * @param tranId
	 * @param dao
	 * @return
	 */
	public static <T> boolean deleteByTranId(long tranId, AbstractDaoSqlBase<T> dao) {
		StringBuilder sql = new StringBuilder(256);
		sql.append(PrintDaoUtil.genDeleteSql(dao));
		SqlGenUtil.appendExtParamObject(sql, "tranId", tranId);
		return dao.getSqlSupport().excuteUpdate(sql.toString()) > 0;
	}

	/**
	 * 根据主对象(sou),生成对应的TCC对象
	 * 
	 * @param desClass
	 * @param sou
	 * @param tccExam
	 *            TCC对象需要的额外参数
	 * @return
	 */
	public static <T1, T2> T1 convertToTCCMeta(Class<T1> desClass, T2 sou, TCCMetaExam tccExam) {
		boolean isFilterEnum = false;
		T1 des = null;
		try {
			des = desClass.newInstance();
			ReflectUtil.convertObj(des, sou, isFilterEnum);
			if (des instanceof TCCMetaInterface) {
				((TCCMetaInterface) des).setCtimeOfTCC(tccExam.getCtimeOfTCC());
				((TCCMetaInterface) des).setTranId(tccExam.getTranId());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return des;
	}

	/**
	 * 根据主对象(sou),生成对应的TCC对象
	 * 
	 * @param desClass
	 * @param souList
	 * @param tccExam
	 *            TCC对象需要的额外参数
	 * @return
	 */
	public static <T1, T2> List<T1> convertToTCCMetaList(Class<T1> desClass, List<T2> souList, TCCMetaExam tccExam) {
		if (CollectionUtil.isEmptyOfCollection(souList))
			return null;

		List<T1> desList = new ArrayList<>();
		for (T2 sou : souList)
			desList.add(convertToTCCMeta(desClass, sou, tccExam));
		return desList;
	}

	/**
	 * 检查UUID是否合法
	 * 
	 * @param uuid
	 * @throws ParticipantException
	 */
	public static void checkUUID(Long uuid) throws ParticipantException {
		if (uuid == null) {
			String message = "UUID is Null!";
			short code = 0;
			throw new ParticipantException(message, code);
		}
	}
}