package com.xyl.mmall.cart.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.xyl.mmall.JobPropertyConfiguration;
import com.xyl.mmall.backend.facade.POItemFacade;
import com.xyl.mmall.base.BaseJob;
import com.xyl.mmall.base.JobCodeInfo;
import com.xyl.mmall.base.JobParam;
import com.xyl.mmall.base.JobPath;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.common.facade.OrderFacade;
import com.xyl.mmall.controller.JobController;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dto.ProductSKUDTO;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.param.ProductSKUSearchParam;
import com.xyl.mmall.itemcenter.service.ItemProductService;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.mainsite.facade.CartFacade;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.util.DateUtil;
import com.xyl.mmall.util.ResourceTextUtil;

/**
 * 同步前n天到今天所有将要上线档期的sku
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Service
@JobPath("/inventory/sync")
public class SyncInventoryJob extends BaseJob {

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ScheduleFacade scheduleFacade;

	@Autowired
	private CartFacade cartFacade;

	private static Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private POItemFacade poItemFacade;

	@Autowired
	private BusinessFacade businessFacade;

	@Autowired
	private MessagePushFacade messagePushFacade;

	private static final ResourceBundle mailResourceBundule = ResourceTextUtil.getResourceBundleByName("content.mail");

	@Autowired
	private JobPropertyConfiguration propertyConfiguration;

	@Autowired
	private POProductService poProductService;
	
	@Autowired
	private ItemProductService itemProductService;

	private static final int NUMS_PER_BATCH = 1000;
	
	private static final int NUMS_PER_BATCH_PO = 10;

	@Override
	public boolean execute(JobParam param) {
		executeJob();
		return true;
//		Date dateStart = (Date) param.getParamMap().get(JobCodeInfo.START_TIME_PARAM);
//		Date dateEnd = (Date) param.getParamMap().get(JobCodeInfo.END_TIME_PARAM);
//
//		logger.info("sync inventory dateStart:" + dateStart + ",dateEnd:" + dateEnd);
//		List<Long> failList = new ArrayList<Long>();
//
//		List<Long> skuIdList = this.getSkuListFromSchedule(dateStart, dateEnd);
//		if (skuIdList == null || skuIdList.isEmpty()) {
//			logger.warn("no schedule,startTime:" + dateStart + ",endTime:" + dateEnd);
//			return this.sendMail(failList, 0, param);
//		}
//
//		int batchNums = skuIdList.size() / NUMS_PER_BATCH;
//		if (batchNums <= 0) {
//			// 单批次处理
//			boolean flag = this.processWithBatch(skuIdList, dateStart, dateEnd, param, failList);
//			if (!flag) {
//				logger.info("error ,sync inventory dateStart:" + dateStart + ",dateEnd:" + dateEnd);
//			}
//			return this.sendMail(failList, skuIdList.size(), param);
//		}
//
//		// 多批次处理
//		List<Long> resultList = new ArrayList<>();
//		Integer lastProcess = null;
//		for (int i = 0; i < batchNums; i++) {
//			int first = i * NUMS_PER_BATCH;
//			int last = first + NUMS_PER_BATCH;
//			// 批次分片
//			List<Long> s1 = skuIdList.subList(first, last);
//
//			boolean flag = this.processWithBatch(s1, dateStart, dateEnd, param, failList);
//			if (!flag) {
//				logger.info("error ,sync inventory dateStart:" + dateStart + ",dateEnd:" + dateEnd);
//			}
//			resultList.addAll(s1);
//			lastProcess = last;
//		}
//
//		// 处理余下数据
//		int remainNums = skuIdList.size() - batchNums * NUMS_PER_BATCH;
//		if (remainNums > 0) {
//			int first = lastProcess;
//			int last = lastProcess + remainNums;
//			List<Long> s1 = skuIdList.subList(first, last);
//			boolean flag = this.processWithBatch(s1, dateStart, dateEnd, param, failList);
//			if (!flag) {
//				logger.info("error ,sync inventory dateStart:" + dateStart + ",dateEnd:" + dateEnd);
//			}
//			resultList.addAll(s1);
//		}
//
//		if (skuIdList.size() != resultList.size()) {
//			logger.error("库存同步处理失败" + "dateStart:" + dateStart + ",dateEnd:" + dateEnd);
//			// failList=resultList;
//		}
//		return this.sendMail(failList, skuIdList.size(), param);
	}

	private boolean processWithBatch(List<Long> skuIdList, Date dateStart, Date dateEnd, JobParam param,
			List<Long> failList) {
		// 2.获取对应sku列表库存
		List<SkuOrderStockDTO> skuOrderStockList = orderFacade.getSkuOrderStockDTOListBySkuIds(skuIdList);
		if (skuOrderStockList == null || skuOrderStockList.size() <= 0) {
			logger.warn("cannot find sku stock record");
			failList.addAll(skuIdList);
			return false;
		}

		// 3.将对应sku值存入内存
		boolean resFlag = this.fillSkuCountToCache(skuOrderStockList, failList);
		if (!resFlag) {
			logger.error("inventory sync is not succed!");
		}
		return resFlag;
	}

	private boolean sendMail(List<Long> failList, long allCount, JobParam jobParam) {
		String sendMailSwitch = ResourceTextUtil.getTextFromResourceByKey(mailResourceBundule,
				"sync.inventory.sendmail");
		if (!Boolean.TRUE.toString().equalsIgnoreCase(sendMailSwitch)) {
			return true;
		}

		if (jobParam.getParamMap().get(JobCodeInfo.SEND_MAIL) == null) {
			logger.warn("no mail address to send");
			return true;
		}
		Date curDate = new Date();
		String curDateShow = DateUtil.parseDateToString(DateUtil.DATE_TIME_FORMAT, curDate);
		String title = ResourceTextUtil.getTextFromResourceByKey(mailResourceBundule, "sync.inventory.title",
				propertyConfiguration.getEnv(), curDateShow);

		Date dateStart = (Date) jobParam.getParamMap().get(JobCodeInfo.START_TIME_PARAM);
		Date dateEnd = (Date) jobParam.getParamMap().get(JobCodeInfo.END_TIME_PARAM);

		String dateStartShow = DateUtil.parseDateToString(DateUtil.DATE_TIME_FORMAT, dateStart);
		String dateEndShow = DateUtil.parseDateToString(DateUtil.DATE_TIME_FORMAT, dateEnd);

		StringBuilder contentBuffer = new StringBuilder(1024);
		contentBuffer.append("同步档期从:" + dateStartShow + ",到:" + dateEndShow + "<br/>");
		contentBuffer.append("共:" + allCount + "个sku" + "<br/>");
		contentBuffer.append("成功:" + (allCount - failList.size()) + "个sku<br/>");
		contentBuffer.append("失败:" + failList.size() + "个sku" + "<br/>");

		if (failList.size() > 0) {
			contentBuffer.append("失败列表(skuId集合):" + "<br/>");
			contentBuffer.append(failList.get(0));
			for (int i = 1; i < failList.size(); i++) {
				contentBuffer.append("," + failList.get(i));
				if (i % 10 == 0) {
					contentBuffer.append("<br/>");
				}
			}
		}
		if (jobParam.getParamMap().get(JobCodeInfo.SEND_MAIL) == null) {
			return true;
		}
		String mailParam = (String) jobParam.getParamMap().get(JobCodeInfo.SEND_MAIL);
		String[] mailArr = mailParam.split("\\;");
		for (String mail : mailArr) {
			messagePushFacade.sendMail(MailType.NORMAL, mail, title, contentBuffer.toString());
		}
		return true;
	}

	private boolean fillSkuCountToCache(List<SkuOrderStockDTO> skuOrderStockList, List<Long> failList) {
		boolean successAllFlag = true;
		for (SkuOrderStockDTO skuStock : skuOrderStockList) {
			boolean flag = cartFacade.setInventoryCount(skuStock.getSkuId(), skuStock.getStockCount());
			if (!flag) {
				logger.error("cannot set the sku count to cache,skuId:" + skuStock.getSkuId() + ",count:"
						+ skuStock.getStockCount());
				successAllFlag = false;
				failList.add(skuStock.getSkuId());
			}
		}
		return successAllFlag;
	}

	private List<Long> getSkuListFromSchedule(Date startDate, Date endDate) {
		List<Schedule> scheduleList = this.getScheduleByArea(startDate, endDate);
		List<Long> skuIdList = new ArrayList<Long>();
		if (scheduleList == null || scheduleList.size() <= 0) {
			return skuIdList;
		}

		List<Long> poIdList = new ArrayList<Long>();
		for (Schedule schedule : scheduleList) {
			poIdList.add(schedule.getId());
		}

		List<PoSku> poSkuList =this.getPoSkuListBySplit(poIdList);

		if (poSkuList == null || poSkuList.isEmpty()) {
			return skuIdList;
		}

		for (PoSku poSku : poSkuList) {
			skuIdList.add(poSku.getId());
		}
		return skuIdList;
	}

	// 分片获取poSku列表
	private List<PoSku> getPoSkuListBySplit(List<Long> poIdList) {
		List<PoSku> resultPoSkuList = new ArrayList<>();

		if (poIdList == null || poIdList.size() <= 0) {
			return resultPoSkuList;
		}

		int batchNums = poIdList.size() / NUMS_PER_BATCH_PO;
		if (batchNums <= 0) {
			resultPoSkuList = this.getPoSkuBySubList(poIdList);
			return resultPoSkuList;
		}

		// 多批次处理
		List<Long> resultCheckList = new ArrayList<>();
		Integer lastProcess = null;
		for (int i = 0; i < batchNums; i++) {
			int first = i * NUMS_PER_BATCH_PO;
			int last = first + NUMS_PER_BATCH_PO;
			// 批次分片
			List<Long> s1 = poIdList.subList(first, last);

			resultPoSkuList.addAll(this.getPoSkuBySubList(s1));
			resultCheckList.addAll(s1);
			lastProcess = last;
		}

		// 处理余下数据
		int remainNums = poIdList.size() - batchNums * NUMS_PER_BATCH_PO;
		if (remainNums > 0) {
			int first = lastProcess;
			int last = lastProcess + remainNums;
			List<Long> s1 = poIdList.subList(first, last);

			resultPoSkuList.addAll(this.getPoSkuBySubList(s1));
			resultCheckList.addAll(s1);
		}

		if (resultCheckList.size() != poIdList.size()) {
			logger.error("===error getPoSkuListBySplit,resultCheckList!=resultPoSkuList,resultPoSkuList size:"
					+ resultPoSkuList.size() + ",resultCheckList size:" + resultCheckList.size());
		}
		return resultPoSkuList;
	}

	private List<PoSku> getPoSkuBySubList(List<Long> subList) {
		PoSkuSo so = new PoSkuSo();
		so.setPoIdList(subList);
		List<PoSku> resultList = poItemFacade.getPoSkuListByParam(so);
		return resultList;
	}

	private List<Schedule> getScheduleByArea(Date startDate, Date endDate) {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		List<AreaDTO> areaList = businessFacade.getAreaList();
		if (areaList == null || areaList.isEmpty()) {
			logger.warn("no area found whne syncInventory");
			return scheduleList;
		}
		for (AreaDTO areaDTO : areaList) {
			List<Schedule> tmpList = scheduleFacade.getScheduleListByTime(areaDTO.getId(), startDate.getTime(),
					endDate.getTime());
			if (tmpList != null && tmpList.size() > 0) {
				scheduleList.addAll(tmpList);
			}
		}
		
		//去重
		Set<Long> sets=new HashSet<>();
		List<Schedule> scheduleResultList=new ArrayList<>();
		for(Schedule schedule:scheduleList){
			if(!sets.contains(schedule.getId())){
				sets.add(schedule.getId());
				scheduleResultList.add(schedule);
			}
		}
		
		return scheduleResultList;
	}
	
	//凌晨1点同步
	@Override
	@Scheduled(cron="0 0 1 * * ? ")
	public void executeJob(){
		//同步库存到nkv
		int total = syncOrderStockTOCache(0);
		System.out.println("sync product stock total num:"+total);
		//一次处理500条
		if(total>500){
			int dealTime = total/500+1;
			for(int i=1;i<dealTime;++i){
				syncOrderStockTOCache(i*500);
			}
		}
	}
	
	private int syncOrderStockTOCache(int offset){
		List<Long> failList = new ArrayList<Long>();
		ProductSKUSearchParam searchParam = new ProductSKUSearchParam();
		searchParam.setOffset(offset);
		searchParam.setLimit(500);
		searchParam.setStatus(4);//已上架
		BasePageParamVO<ProductSKUDTO> basePageDTOs = itemProductService.searchProductSKU(searchParam);
		Function<ProductSKUDTO, Long> function = new Function<ProductSKUDTO, Long>() {
			@Override
			public Long apply(ProductSKUDTO arg0) {
				return arg0.getId();
			}
		};
		
		List<Long>skuIdList = Lists.transform(basePageDTOs.getList(), function);
		if (skuIdList == null || skuIdList.isEmpty()) {
			logger.warn("no product,startTime:" );
		}

		// 单批次处理
		boolean flag = this.processWithBatch(skuIdList, null,null, null, failList);
		if (!flag) {
			logger.info("error ,sync inventory:");
		}
		return basePageDTOs.getTotal();
	}
}
