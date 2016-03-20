/*
 * 2014-9-9
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.dao;

import java.util.List;

import com.xyl.mmall.promotion.dto.PromotionQueryBeanDTO;
import com.xyl.mmall.promotion.enums.PlatformType;
import com.xyl.mmall.promotion.meta.Promotion;

/**
 * PromotionDao.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-9
 * @since 1.0
 */
public interface PromotionDao {

	List<Promotion> getPromotionList(long userId, boolean isNoExpiredOnly, int state, long mulriplePermission,
			long remainPermission, PlatformType platformType, int limit, int offset);

	Promotion addPromotion(Promotion promotion);

	boolean updatePromotion(Promotion promotion);

	Promotion getPromotionById(long id);

	int getPromotionCount(long userId, boolean isNoExpiredOnly, int state, long areaPermission);

	Promotion getPromotionByPO(String po, long id, long start, long end, boolean isValid);

	List<Promotion> getCommitPromotions(PromotionQueryBeanDTO promotionQueryBeanDTO);

	int getCommitPromotionCount(PromotionQueryBeanDTO promotionQueryBeanDTO);

}
