package com.xyl.mmall.saleschedule.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.netease.print.daojar.util.SqlGenUtil;
import com.xyl.mmall.saleschedule.dao.ScheduleMagicCubeDao;
import com.xyl.mmall.saleschedule.enums.DBField.MagicCubeField;
import com.xyl.mmall.saleschedule.meta.ScheduleMagicCube;

/**
 * 
 * @author hzzhanghui
 */
@Repository
public class ScheduleMagicCubeDaoImpl extends ScheduleBaseDao<ScheduleMagicCube> implements ScheduleMagicCubeDao {

	private String sqlUpdate = "UPDATE " + tableName + " SET " + MagicCubeField.scheduleId + "=?, "
			+ MagicCubeField.logDay + "=?, " + MagicCubeField.sale + "=?, " + MagicCubeField.saleCnt + "=?, "
			+ MagicCubeField.buyerCnt + "=?, " + MagicCubeField.saleRate + "=?, " + MagicCubeField.supplyMoney + "=?, "
			+ MagicCubeField.skuCnt + "=?, " + MagicCubeField.uv + "=?, " + MagicCubeField.pv + "=?, "
			+ MagicCubeField.rsv1 + "=?, " + MagicCubeField.rsv2 + "=?, " + MagicCubeField.rsv3 + "=? WHERE "
			+ MagicCubeField.supplierId + "=?";

	private String sqlDelete = "DELETE FROM " + tableName + " WHERE " + MagicCubeField.supplierId + "=?";

	@Override
	public boolean saveShceduleMagicCubeList(List<ScheduleMagicCube> mcList) {
		logger.debug("saveShceduleMagicCubeList: " + mcList);
		try {
			return addObjects(mcList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean saveScheduleMagicCube(ScheduleMagicCube mc) {
		logger.debug("saveScheduleMagicCube: " + mc);
		try {
			addObject(mc);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateScheduleMagicCubeBySupplierId(ScheduleMagicCube mc) {
		logger.debug("updateScheduleMagicCubeBySupplierId: " + mc);
		Object[] args = new Object[] { mc.getScheduleId(), mc.getLogDay(), mc.getSale(), mc.getSaleCnt(),
				mc.getBuyerCnt(), mc.getSaleRate(), mc.getSupplyMoney(), mc.getSkuCnt(), mc.getUv(), mc.getPv(),
				mc.getRsv1(), mc.getRsv2(), mc.getRsv3(), mc.getSupplierId() };
		try {
			getSqlSupport().excuteUpdate(sqlUpdate, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean deleteScheduleMagicCubeBySupplierId(long supplierId) {
		logger.debug("deleteScheduleMagicCubeBySupplierId: " + supplierId);
		Object[] args = new Object[] { supplierId };
		try {
			getSqlSupport().excuteUpdate(sqlDelete, args);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public ScheduleMagicCube getScheduleMagicCubeBySupplierId(long supplierId) {
		logger.debug("getScheduleMagicCubeBySupplierId: " + supplierId);
		StringBuilder sql = new StringBuilder(256);
		sql.append(genSelectSql());
		SqlGenUtil.appendExtParamObject(sql, MagicCubeField.supplierId, supplierId);
		try {
			return queryObject(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
