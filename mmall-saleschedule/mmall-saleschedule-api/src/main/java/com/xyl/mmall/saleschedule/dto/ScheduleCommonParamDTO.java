package com.xyl.mmall.saleschedule.dto;

import java.io.Serializable;
import java.util.List;

import com.xyl.mmall.saleschedule.enums.CheckState;
import com.xyl.mmall.saleschedule.enums.ScheduleState;

/**
 * Schedule 接口参数DTO。
 * 
 * 有值的就填上，没值的就不填。各个方法按需所取。
 * 
 * @author hzzhanghui
 * 
 */
public class ScheduleCommonParamDTO implements Serializable {

	private static final long serialVersionUID = -7892303670273392289L;

	public long poId;

	public long userId;

	public long supplierId;

	// for CMS is same meaning 'saleAreaId'
	// for Backend, this field will be not used
	public long curSupplierAreaId;
	
	// & value for sale site
	public long saleSiteFlag;
	
	// & value for allowed site list
	public long allowedAreaIdListFlag;

	public long startDate;

	public long endDate;
	
	// true: where createTime>(<) startDate
	// false: where startTime>(<) startDate
	public boolean createWhereFlag = false; 

	// same meaning of offset passed from web
	public int curPage;

	public int pageSize;

	public List<Long> brandIdList;

	public List<Long> poIdList;
	
	public List<Long> allowedAreaIdList;

	public List<ScheduleState> poStatusList;
	
	public List<CheckState> bannerOrPageStatusList;
	
	public List<Long> supplierIdList;

	public OrderFlag orderByFlag = OrderFlag.ID_ASC;

	public static enum OrderFlag implements Serializable {
		ID_ASC, ID_DESC, STARTTIME_ASC, STARTTIME_DESC, CREATETIME_ASC, CREATETIME_DESC, SHOWORDER_ASC, SHOWORDER_DESC, SUBMITTIME_ASC, SUBMITTIME_DESC;
	}
}
