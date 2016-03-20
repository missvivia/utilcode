/**
 * 
 */
package com.xyl.mmall.cms.facade;

import java.util.List;

import com.xyl.mmall.cms.vo.PointVO;
import com.xyl.mmall.cms.vo.SiteCMSVO;
import com.xyl.mmall.cms.vo.UserPointVO;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.promotion.dto.PointDTO;
import com.xyl.mmall.promotion.meta.Point;

/**
 * @author jmy
 *
 */
public interface PointFacade {
	/**
	 * 根据查询条件获取分页列表
	 * @param queryObject
	 * @param page
	 * @return
	 */
	List<PointVO> getPointList(PointDTO queryObject,BasePageParamVO<PointVO> page);

	/**
	 * 根据积分调整id查询对于的积分调整VO对象
	 * @param id 积分调整id
	 * @return 积分调整VO对象或null
	 */
	PointVO getPoint(long id);

	/**
	 * 根据用户名、手机号、邮箱关键字搜索用户及其积分信息。
	 * @param basePageParamVO 分页信息
	 * @param searchValue 用户名、手机号、邮箱关键字
	 * @return
	 */
	List<UserPointVO> getUserPointList(BasePageParamVO<UserPointVO> basePageParamVO, String searchValue);

	/**
	 * 积分调整申请状态变更
	 * @param id
	 * @param siteId
	 * @param state
	 * @return 调整成功与否
	 */
	boolean changeState(long id, int state);

	/**
	 * 保存积分调整
	 * @param point
	 * @return 保存后对象
	 */
	Point savePoint(Point point);

	/**
	 * 获取当前站点
	 * @return 如果数据库没有当前站点，则默认为全部站点
	 */
	SiteCMSVO getCurrentSite();

	UserPointVO getUserPoint(long userId);
}
