package com.xyl.mmall.cms.facade.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyl.mmall.cart.service.CartService;
import com.xyl.mmall.itemcenter.meta.PoSku;
import com.xyl.mmall.itemcenter.param.PoSkuSo;
import com.xyl.mmall.itemcenter.service.POProductService;
import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.mainsite.facade.MessagePushFacade;
import com.xyl.mmall.order.dto.SkuOrderStockDTO;
import com.xyl.mmall.order.service.OrderService;
import com.xyl.mmall.order.service.SkuOrderStockService;

/**
 * 同步库存帮助类
 * 
 * @author hzzhaozhenzuo
 * 
 */
@Component
public class SyncInventoryHelper {

	private static final Logger logger = LoggerFactory.getLogger(SyncInventoryHelper.class);

	private static final int NUMS_PER_BATCH = 1000;

	@Autowired
	private OrderService orderService;

	@Autowired
	private MessagePushFacade messagePushFacade;
	
	@Autowired
	private SkuOrderStockService skuOrderStockService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private POProductService poProductService;

	private static final String mailList = "yuyang@163.com";
	
	@Autowired
	private ProfilePropertyConfiguration profilePropertyConfiguration;

	/**
	 * 提供对外同步对应档期的sku库存
	 * 
	 * @param poIdList
	 * @return
	 */
	public boolean inventoryPo(List<Long> poIdList) {
		try {
			if(poIdList==null || poIdList.size()<=0){
				logger.warn("poIdList is null when invoke inventoryPo");
				return true;
			}
			return this.processInnner(poIdList);
		} catch (Exception e) {
			logger.error("inventoryPo error,poIdList:" + poIdList.toString(), e);
			this.sendErrorEmail(poIdList, new ArrayList<Long>(), e.getMessage());
			return false;
		}
	}
	
	public boolean clearInventory(List<Long> poIdList) {
		try {
			if(poIdList==null || poIdList.size()<=0){
				logger.warn("poIdList is null when invoke clearInventory");
				return false;
			}
			boolean flag = true;
			for(Long poId:poIdList){
				flag = cartService.setInventoryCount(poId, 0);
				if (!flag) {
					logger.error("cannot set the sku count cache to 0,skuId:" + poId);
				}
			}
			return flag;
		} catch (Exception e) {
			logger.error("clearInventory error,poIdList:" + poIdList.toString(), e);
			this.sendErrorEmail(poIdList, new ArrayList<Long>(), e.getMessage());
			return false;
		}
	}
	
	public boolean processInnerSKU(List<Long> skuIdList) {
		List<Long> failIdList = new ArrayList<Long>();
		int batchNums = skuIdList.size() / NUMS_PER_BATCH;
		if (batchNums <= 0) {
			// 单批次处理
			boolean flag = this.processWithBatch(skuIdList, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + skuIdList.toString());
				this.sendErrorEmail(skuIdList, failIdList, "将sku对应库存加入nkv内存失败");
			}
			return flag && (failIdList == null || failIdList.size() <= 0);
		}

		// 多批次处理
		List<Long> resultList = new ArrayList<>();
		Integer lastProcess = null;
		for (int i = 0; i < batchNums; i++) {
			int first = i * NUMS_PER_BATCH;
			int last = first + NUMS_PER_BATCH;
			// 批次分片
			List<Long> s1 = skuIdList.subList(first, last);

			boolean flag = this.processWithBatch(s1, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + s1.toString());
				this.sendErrorEmail(skuIdList, failIdList, "将sku对应库存加入nkv内存失败");
			}
			resultList.addAll(s1);
			lastProcess = last;
		}

		// 处理余下数据
		int remainNums = skuIdList.size() - batchNums * NUMS_PER_BATCH;
		if (remainNums > 0) {
			int first = lastProcess;
			int last = lastProcess + remainNums;
			List<Long> s1 = skuIdList.subList(first, last);
			boolean flag = this.processWithBatch(s1, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + s1.toString());
			}
			resultList.addAll(s1);
		}

		if (skuIdList.size() != resultList.size()) {
			logger.error("inventoryPo处理失败,poIdList:" + skuIdList.toString());
			this.sendErrorEmail(skuIdList, failIdList, "分批处理后的sku数量与实际sku数量不符");
			return false;
		}

		if (failIdList != null && failIdList.size() > 0) {
			this.sendErrorEmail(skuIdList, failIdList, "将sku对应库存加入nkv内存失败");
		}

		return (failIdList == null || failIdList.size() <= 0);
	}
	
	private boolean processInnner(List<Long> poIdList) {
		List<Long> failIdList = new ArrayList<>();
		if (poIdList == null || poIdList.isEmpty()) {
			logger.error("inventoryPo error,poIdList is empty");
			poIdList = new ArrayList<>();
			this.sendErrorEmail(poIdList, failIdList, "传入的档期id集合为空");
			return false;
		}

		// 获取指定档期下poSku集合
		List<PoSku> poSkuList = this.getPoSkuBySubList(poIdList);
		if (poSkuList == null || poSkuList.isEmpty()) {
			logger.error("can not find poSkus for poIdList:" + poIdList.toString());
			this.sendErrorEmail(poIdList, failIdList, "无法找到对应po的posku");
			return false;
		}

		List<Long> skuIdList = new ArrayList<>();
		for (PoSku poSku : poSkuList) {
			skuIdList.add(poSku.getId());
		}

		if (skuIdList == null || skuIdList.isEmpty()) {
			logger.error("can not find skuIds for poIdList:" + poIdList);
			this.sendErrorEmail(poIdList, failIdList, "无法找到对应po的posku");
			return false;
		}

		int batchNums = skuIdList.size() / NUMS_PER_BATCH;
		if (batchNums <= 0) {
			// 单批次处理
			boolean flag = this.processWithBatch(skuIdList, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + skuIdList.toString());
				this.sendErrorEmail(poIdList, failIdList, "将sku对应库存加入nkv内存失败");
			}
			return flag && (failIdList == null || failIdList.size() <= 0);
		}

		// 多批次处理
		List<Long> resultList = new ArrayList<>();
		Integer lastProcess = null;
		for (int i = 0; i < batchNums; i++) {
			int first = i * NUMS_PER_BATCH;
			int last = first + NUMS_PER_BATCH;
			// 批次分片
			List<Long> s1 = skuIdList.subList(first, last);

			boolean flag = this.processWithBatch(s1, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + s1.toString());
				this.sendErrorEmail(poIdList, failIdList, "将sku对应库存加入nkv内存失败");
			}
			resultList.addAll(s1);
			lastProcess = last;
		}

		// 处理余下数据
		int remainNums = skuIdList.size() - batchNums * NUMS_PER_BATCH;
		if (remainNums > 0) {
			int first = lastProcess;
			int last = lastProcess + remainNums;
			List<Long> s1 = skuIdList.subList(first, last);
			boolean flag = this.processWithBatch(s1, failIdList);
			if (!flag) {
				logger.error("error ,inventoryPo for :" + s1.toString());
			}
			resultList.addAll(s1);
		}

		if (skuIdList.size() != resultList.size()) {
			logger.error("inventoryPo处理失败,poIdList:" + poIdList.toString());
			this.sendErrorEmail(poIdList, failIdList, "分批处理后的sku数量与实际sku数量不符");
			return false;
		}

		if (failIdList != null && failIdList.size() > 0) {
			this.sendErrorEmail(poIdList, failIdList, "将sku对应库存加入nkv内存失败");
		}

		return (failIdList == null || failIdList.size() <= 0);
	}

	private boolean processWithBatch(List<Long> skuIdList, List<Long> failList) {
		List<SkuOrderStockDTO> skuOrderStockList = skuOrderStockService.getSkuOrderStockDTOListBySkuIds(skuIdList);
		if (skuOrderStockList == null || skuOrderStockList.size() <= 0) {
			logger.error("no skuOrdderStock find for :"+skuIdList.toString());
			failList.addAll(skuIdList);
			return false;
		}

		boolean resFlag = this.fillSkuCountToCache(skuOrderStockList, failList);
		if (!resFlag) {
			logger.error("inventory sync is not succed!");
		}
		return resFlag;
	}

	private boolean fillSkuCountToCache(List<SkuOrderStockDTO> skuOrderStockList, List<Long> failList) {
		boolean successAllFlag = true;
		for (SkuOrderStockDTO skuStock : skuOrderStockList) {
			boolean flag = cartService.setInventoryCount(skuStock.getSkuId(), skuStock.getStockCount());
			if (!flag) {
				logger.error("cannot set the sku count to cache,skuId:" + skuStock.getSkuId() + ",count:"
						+ skuStock.getStockCount());
				successAllFlag = false;
				failList.add(skuStock.getSkuId());
			}
		}
		return successAllFlag;
	}

	private List<PoSku> getPoSkuBySubList(List<Long> poIdList) {
		PoSkuSo so = new PoSkuSo();
		so.setPoIdList(poIdList);
		List<PoSku> resultList = poProductService.getPoSkuListByParam(so);
		return resultList;
	}

	private boolean sendErrorEmail(List<Long> poIdList, List<Long> failIdList, String errorDesc) {
		try {
			if (StringUtils.isEmpty(mailList)) {
				logger.warn("no mail found for inventory po err");
				return true;
			}
			String[] mailArr = mailList.split("\\;");
			String title = profilePropertyConfiguration.getEnv()+"环境;同步指定po列表失败";
			StringBuilder contentBuffer = new StringBuilder(32);
			contentBuffer.append("<br/>要同步的档期集合：<br/>");
			contentBuffer.append(poIdList.toString());
			contentBuffer.append("<br/>--------------------------------");

			if (!StringUtils.isEmpty(errorDesc)) {
				contentBuffer.append("<br/>错误描述:" + errorDesc);
				contentBuffer.append("<br/>--------------------------------");
			}

			contentBuffer.append("<br/>失败skuId集合:<br/>");
			contentBuffer.append(failIdList.toString());
			for (String mail : mailArr) {
				messagePushFacade.sendMail(MailType.NORMAL, mail, title, contentBuffer.toString());
			}
		} catch (Exception e) {
			logger.error("send mail error when invoke syncInventoryHelper", e);
			return false;
		}
		return true;
	}
}
