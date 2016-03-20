/**
 * 网新新云联技术有限公司
 */
package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.exception.ServiceException;
import com.xyl.mmall.framework.util.DateUtil;
import com.xyl.mmall.saleschedule.dao.ProductSKULimitConfigDao;
import com.xyl.mmall.saleschedule.dao.ProductSKULimitRecordDao;
import com.xyl.mmall.saleschedule.dao.nkv.ProductSKULimitNkv;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitConfigDTO;
import com.xyl.mmall.saleschedule.dto.ProductSKULimitRecordDTO;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitConfig;
import com.xyl.mmall.saleschedule.meta.ProductSKULimitRecord;
import com.xyl.mmall.saleschedule.service.ProductSKULimitService;

/**
 * ProductSKULimitServiceImpl.java created by yydx811 at 2015年11月17日 下午1:47:43
 * 商品限购service接口实现
 *
 * @author yydx811
 */
@Service
public class ProductSKULimitServiceImpl implements ProductSKULimitService {

	private static Logger logger = Logger.getLogger(ProductSKULimitServiceImpl.class);

	@Autowired
	private ProductSKULimitNkv productSKULimitNkv;

	@Autowired
	private ProductSKULimitConfigDao productSKULimitConfigDao;

	@Autowired
	private ProductSKULimitRecordDao productSKULimitRecordDao;

	@Override
	public boolean addProductSKULimitConfig(ProductSKULimitConfigDTO skuLimitConfigDTO) {
		return productSKULimitConfigDao.addProductSKULimitConfig(skuLimitConfigDTO);
	}

	@Override
	public ProductSKULimitConfigDTO getProductSKULimitConfigBySkuId(long productSKUId) {
		ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(productSKUId);
		if (skuLimitConfig == null) {
			return null;
		}
		return new ProductSKULimitConfigDTO(skuLimitConfig);
	}

	@Override
	public boolean updateProductSKULimitConfig(ProductSKULimitConfigDTO skuLimitConfigDTO) {
		return productSKULimitConfigDao.updateProductSKULimitConfig(skuLimitConfigDTO);
	}

	@Override
	public boolean deleteProductSKULimitConfig(long productSKUId) {
		return productSKULimitConfigDao.deleteProductSKULimitConfigBySkuId(productSKUId);
	}

	@Override
	@Transaction
	public int changeProductSKULimitRecord(long userId, long skuId, int deltaCount) {
		// 获取限购配置
		ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(skuId);
		// 限购配置是否存在
		if (skuLimitConfig == null) {
			return -1;
		}
		long now = System.currentTimeMillis();
		// 判断限购时间
		if (skuLimitConfig.getStartTime() > now || skuLimitConfig.getEndTime() < now) {
			return -2;
		}
		boolean isSuccess = false;
		ProductSKULimitRecord skuLimitRecord = productSKULimitRecordDao.getProductSKULimitRecord(userId, skuId);
		long endTime = 0l;
		int period = (int) skuLimitConfig.getPeriod();
		if (skuLimitRecord == null) {
			if (deltaCount > skuLimitConfig.getLimitNumber()) {
				return -3;
			}
			skuLimitRecord = new ProductSKULimitRecord();
			skuLimitRecord.setProductSKUId(skuId);
			skuLimitRecord.setUserId(userId);
			skuLimitRecord.setTotalNumber(deltaCount);
			skuLimitRecord.setCreateTime(DateUtil.getDateBegin(now));
			skuLimitRecord.setLastBuyTime(now);
			try {
				isSuccess = productSKULimitRecordDao.addProductSKULimitRecord(skuLimitRecord);
			} catch (Exception e) {
				// 兼容unique key
				ProductSKULimitRecord oldRecord = productSKULimitRecordDao.getProductSKULimitRecord(userId, skuId);
				if (oldRecord == null) {
					throw e;
				}
				// 加锁判断
				oldRecord = productSKULimitRecordDao.getLockByKey(oldRecord);
				// 判断是否在购买周期
				endTime = skuLimitRecord.getCreateTime() + period * DateUtil.ONE_DAY_MILLISECONDS;
				int total = 0;
				if (endTime > now) {
					total = skuLimitRecord.getTotalNumber() + deltaCount;
					skuLimitRecord.setCreateTime(oldRecord.getCreateTime());
				} else {
					total = deltaCount;
				}
				if (total > skuLimitConfig.getLimitNumber()) {
					return -3;
				}
				skuLimitRecord.setTotalNumber(total);
				isSuccess = productSKULimitRecordDao.updateProductSKULimitRecord(skuLimitRecord);
			}
		} else {
			skuLimitRecord = productSKULimitRecordDao.getLockByKey(skuLimitRecord);
			// 判断是否在购买周期
			endTime = skuLimitRecord.getCreateTime() + period * DateUtil.ONE_DAY_MILLISECONDS;
			int total = 0;
			if (endTime > now) {
				total = skuLimitRecord.getTotalNumber() + deltaCount;
			} else {
				total = deltaCount;
				skuLimitRecord.setCreateTime(DateUtil.getDateBegin(now));
			}
			if (total > skuLimitConfig.getLimitNumber()) {
				return -3;
			}
			skuLimitRecord.setLastBuyTime(now);
			skuLimitRecord.setTotalNumber(total);
			isSuccess = productSKULimitRecordDao.updateProductSKULimitRecord(skuLimitRecord);
		}
		if (endTime == 0l) {
			endTime = skuLimitRecord.getCreateTime() + period * DateUtil.ONE_DAY_MILLISECONDS;
		}
		if (endTime > skuLimitConfig.getEndTime()) {
			endTime = skuLimitConfig.getEndTime();
		}
		long expire = endTime - now;

		// 更新缓存
		if (isSuccess) {
			try {
				productSKULimitNkv.changeProductSKULimit(skuId, userId, deltaCount, (int) (expire / 1000l));
			} catch (Exception e) {
				logger.error(e);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public ProductSKULimitRecordDTO getProductSKULimitRecordNoCache(long userId, long skuId) {
		return new ProductSKULimitRecordDTO(productSKULimitRecordDao.getProductSKULimitRecord(userId, skuId));
	}

	@Override
	@Transaction
	public int updateProductSKULimitRecord(long userId, long skuId, int canBuyNum) {
		// 获取限购配置
		ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(skuId);
		// 限购配置是否存在
		if (skuLimitConfig == null) {
			return -1;
		}
		int totalNum = skuLimitConfig.getLimitNumber() - canBuyNum;
		if (totalNum < 0) {
			totalNum = 0;
		}
		ProductSKULimitRecord limitRecord = productSKULimitRecordDao.getProductSKULimitRecord(userId, skuId);
		int deltaCount = 0;
		if (limitRecord == null) {
			deltaCount = totalNum;
		} else {
			deltaCount = totalNum - limitRecord.getTotalNumber();
		}
		if (deltaCount == 0) {
			return 1;
		}
		return changeProductSKULimitRecord(userId, skuId, deltaCount);
	}

	@Override
	public Map<Long, Integer> getSkuIsAllowedBuyLimitResultMap(long userId, Map<Long, Integer> skuCountMap) {
		Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
		int result = 0, finalResult = 0;
		for (Entry<Long, Integer> entry : skuCountMap.entrySet()) {
			result = 0;
			// 获取限购配置
			ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(entry
					.getKey());
			// 限购配置是否存在
			if (skuLimitConfig == null) {
				continue;
			}
			long now = System.currentTimeMillis();
			// 判断限购时间
			if (skuLimitConfig.getStartTime() > now || skuLimitConfig.getEndTime() < now) {
				result = -1;// 超过限购时间
				finalResult = -1;
			}
			ProductSKULimitRecord skuLimitRecord = productSKULimitRecordDao.getProductSKULimitRecord(userId,
					entry.getKey());
			int total = 0;
			if (skuLimitRecord == null) {
				total = entry.getValue();
			} else {
				// 判断是否在购买周期
				long endTime = skuLimitRecord.getCreateTime() + skuLimitConfig.getPeriod()
						* DateUtil.ONE_DAY_MILLISECONDS;

				if (endTime > now) {
					total = skuLimitRecord.getTotalNumber() + entry.getValue();
				} else {
					total = entry.getValue();
				}
			}
			if (total > skuLimitConfig.getLimitNumber()) {
				result = -2;// 超过限购数量
				finalResult = -1;
			}
			resultMap.put(entry.getKey(), result);
		}
		resultMap.put(0l, finalResult);
		return resultMap;
	}

	@Override
	public Map<Long, ProductSKULimitConfigDTO> getProductSKULimitConfigDTOMap(long userId, List<Long> skuIds) {
		Map<Long, ProductSKULimitConfigDTO> resultMap = new HashMap<Long, ProductSKULimitConfigDTO>();
		int buyerNum = 0;
		for (Long skuId : skuIds) {
			// 获取限购配置
			ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(skuId);
			// 限购配置是否存在
			if (skuLimitConfig == null) {
				continue;
			}
			ProductSKULimitConfigDTO productSKULimitConfigDTO = new ProductSKULimitConfigDTO(skuLimitConfig);
			resultMap.put(skuId, productSKULimitConfigDTO);
			long now = System.currentTimeMillis();
			// 判断限购时间
			if (skuLimitConfig.getStartTime() > now || skuLimitConfig.getEndTime() < now) {
				productSKULimitConfigDTO.setAllowedNum(0);
				continue;
			}
			buyerNum = getTotalBuyNum(skuId, userId);
			if (buyerNum < 0) {
				buyerNum = 0;
			}
			productSKULimitConfigDTO.setAllowedNum(skuLimitConfig.getLimitNumber() - buyerNum);
		}
		return resultMap;
	}

	@Override
	@Transaction
	public int batchChangeProductSKULimitRecords(long userId, Map<Long, Integer> skuCountMap) throws ServiceException {
		int result = 0;
		List<Long> ids = new ArrayList<Long>(skuCountMap.size());
		for (Entry<Long, Integer> entry : skuCountMap.entrySet()) {
			result = changeProductSKULimitRecord(userId, entry.getKey(), entry.getValue());
			ids.add(entry.getKey());
			// 返回错误删除缓存
			if (!(result == -1 || result == 1)) {
				for (Long skuId : ids) {
					productSKULimitNkv.delProductSKULimit(skuId, userId);
				}
				throw new ServiceException("Change productsku limit record error. UserId : " + userId + ", SkuId : "
						+ entry.getKey());
			}
		}
		return result;
	}

	@Override
	public int getTotalBuyNumFromCache(long skuId, long userId) {
		return productSKULimitNkv.getProductSKULimit(skuId, userId);
	}

	@Override
	public int syncCache(long skuId, long userId) {
		// 获取限购配置
		ProductSKULimitConfig skuLimitConfig = productSKULimitConfigDao.getProductSKULimitConfigBySkuId(skuId);
		// 限购配置是否存在
		if (skuLimitConfig == null) {
			return -1;
		}
		long now = System.currentTimeMillis();
		// 判断限购时间
		if (skuLimitConfig.getStartTime() > now || skuLimitConfig.getEndTime() < now) {
			return -2;
		}
		ProductSKULimitRecordDTO limitRecordDTO = getProductSKULimitRecordNoCache(userId, skuId);
		if (limitRecordDTO != null) {
			// 判断是否在购买周期
			int period = (int) skuLimitConfig.getPeriod();
			long endTime = limitRecordDTO.getCreateTime() + period * DateUtil.ONE_DAY_MILLISECONDS;
			int total = limitRecordDTO.getTotalNumber();
			if (endTime > now) {
				long expire = endTime - now;
				if (productSKULimitNkv.setProductSKULimit(skuId, userId, total, (int) expire)) {
					return total;
				}
			} else {
				if (productSKULimitNkv.setProductSKULimit(skuId, userId, 0, (int) DateUtil.ONE_DAY_SECONDS)) {
					return 0;
				}
			}
		} else {
			if (productSKULimitNkv.setProductSKULimit(skuId, userId, 0, (int) DateUtil.ONE_DAY_SECONDS)) {
				return 0;
			}
		}
		productSKULimitNkv.delProductSKULimit(skuId, userId);
		return -3;
	}

	@Override
	public int getTotalBuyNum(long skuId, long userId) {
		int total = productSKULimitNkv.getProductSKULimit(skuId, userId);
		if (total < 0) {
			total = syncCache(skuId, userId);
			if (total < 0) {
				productSKULimitNkv.setProductSKULimit(skuId, userId, 0, (int) DateUtil.ONE_DAY_SECONDS);
				return 0;
			}
		}
		return total;
	}

	@Override
	public boolean clearLimitCache(long skuId) {
		return productSKULimitNkv.clearProductSKULimit(skuId);
	}

	@Override
	public int deleteSKULimitRecordBySkuId(long skuId) {
		return productSKULimitRecordDao.deleteSKULimitRecordBySkuId(skuId);
	}
}