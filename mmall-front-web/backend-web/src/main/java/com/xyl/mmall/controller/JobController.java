package com.xyl.mmall.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.framework.enums.ErrorCode;
import com.xyl.mmall.framework.vo.BaseJsonVO;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.promotion.utils.DateUtils;
import com.xyl.mmall.saleschedule.meta.Schedule;

@Controller
@RequestMapping("job")
public class JobController {

	// 获取n天之内有效的将要上线的档期
	private static final int PRE_DAY_NUM_FOR_START = -2;

	private static final int PRE_DAY_NUM_FOR_END = 1;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private CartFacade cartFacade;

	private static Logger logger = LoggerFactory.getLogger(JobController.class);
	
	@Autowired
	private POItemFacade poItemFacade;
	
	@RequestMapping("addInventory")
	@ResponseBody
	public BaseJsonVO testAddInventory(@RequestParam long skuid,@RequestParam int num){
		BaseJsonVO vo=new BaseJsonVO();
		boolean flag=cartFacade.setInventoryCount(skuid, num);
		vo.setResult(flag);
		return vo;
	}
	
	@RequestMapping("searchInventory")
	@ResponseBody
	public BaseJsonVO getInventory(@RequestParam long skuid){
		BaseJsonVO vo=new BaseJsonVO();
		Map<Long,Integer> map=cartFacade.getInventoryCount(Arrays.asList(skuid));
		vo.setResult(map);
		return vo;
	}
	

	/**
	 * 同步库存，从将要上线的档期中取出n个sku，并更新到nkv内存中 
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sync/inventory", method = RequestMethod.GET)
	public @ResponseBody BaseJsonVO syncInventory(Model model, @RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime) {
		BaseJsonVO retObj = new BaseJsonVO();
		retObj.setCode(ErrorCode.SUCCESS);

		Date curDate = new Date();
		Date curDateYMD = DateUtils.dateFormat(curDate, DateUtils.DATE_FORMAT);
		Date dateStart;
		Date dateEnd;
		// time
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			dateStart = DateUtils.parseStringToDate(DateUtils.DATE_TIME_FORMAT, startTime);
			dateEnd = DateUtils.parseStringToDate(DateUtils.DATE_TIME_FORMAT, endTime);
		} else {
			dateStart = DateUtils.addDay(curDateYMD, PRE_DAY_NUM_FOR_START);
			dateEnd = DateUtils.addDay(curDateYMD, PRE_DAY_NUM_FOR_END);
		}

		List<Long> skuIdList = this.getSkuListFromSchedule(dateStart, dateEnd);
		if (skuIdList == null || skuIdList.isEmpty()) {
			logger.warn("no schedule,startTime:" + dateStart + ",endTime:" + dateEnd);
			return retObj;
		}

		// 2.获取对应sku列表库存
		List<SkuOrderStockDTO> skuOrderStockList = orderFacade.getSkuOrderStockDTOListBySkuIds(skuIdList);
		if (skuOrderStockList == null || skuOrderStockList.size() <= 0) {
			logger.warn("cannot find sku stock record,startTime:" + dateStart + ",endTime:" + dateEnd);
			return retObj;
		}

		// 3.将对应sku值存入内存
		this.fillSkuCountToCache(skuOrderStockList);

		return retObj;
	}

	private void fillSkuCountToCache(List<SkuOrderStockDTO> skuOrderStockList) {
		for (SkuOrderStockDTO skuStock : skuOrderStockList) {
			boolean flag = cartFacade.setInventoryCount(skuStock.getSkuId(), skuStock.getStockCount());
			if (!flag) {
				logger.error("cannot set the sku count to cache,skuId:" + skuStock.getSkuId() + ",count:"
						+ skuStock.getStockCount());
			}
		}
	}

	private List<Long> getSkuListFromSchedule(Date startDate, Date endDate) {
		List<Schedule> scheduleList = scheduleFacade.getScheduleListByTime(0,
				startDate.getTime(), endDate.getTime());
		List<Long> skuIdList = new ArrayList<Long>();
		if (scheduleList == null || scheduleList.size() <= 0) {
			return skuIdList;
		}

		List<Long> poIdList = new ArrayList<Long>();
		for (Schedule schedule : scheduleList) {
			poIdList.add(schedule.getId());
		}
		
		long oldTime1=System.currentTimeMillis();
		PoSkuSo so=new PoSkuSo();
		so.setPoIdList(poIdList);;
		List<PoSku> poSkuList=poItemFacade.getPoSkuListByParam(so);
		
		long cost1=System.currentTimeMillis()-oldTime1;
		logger.info("=====search poSku is:"+cost1);

		for (PoSku poSku : poSkuList) {
			skuIdList.add(poSku.getId());
		}

		return skuIdList;
	}

}
