/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.cms.dao;

import java.util.List;

import com.netease.print.daojar.dao.AbstractDao;
import com.xyl.mmall.cms.dto.SiteCMSDTO;
import com.xyl.mmall.cms.meta.SiteCMS;
import com.xyl.mmall.framework.vo.BasePageParamVO;

/**
 * SiteCMSDao.java created by yydx811 at 2015年7月16日 下午5:14:55
 * 站点dao接口
 *
 * @author yydx811
 */
public interface SiteCMSDao extends AbstractDao<SiteCMS> {

	/**
	 * 获取站点列表
	 * @param searchValue 搜索条件
	 * @return BasePageParamVO<SiteCMSDTO>
	 */
	public BasePageParamVO<SiteCMSDTO> getSiteCMSList(String searchValue, BasePageParamVO<SiteCMSDTO> pageParamVO);
	
	/**
	 * 添加站点
	 * @param siteCMS
	 * @return long id
	 */
	public long addSiteCMS(SiteCMS siteCMS);
	
	/**
	 * 更新站点
	 * @param siteCMS
	 * @return int
	 */
	public int updateSiteCMS(SiteCMS siteCMS);
	
	/**
	 * 批量删除站点
	 * @param ids
	 * @return
	 */
	public int deleteBulkSiteCMS(List<Long> ids);
	
	/**
	 * 按id列表获取站点
	 * @param ids
	 * @return
	 */
	public List<SiteCMS> getSiteCMSList(List<Long> ids);

	/**
	 * 获取所有站点
	 * @return
	 */
	public List<SiteCMS> getAllSiteCMSList();
}
