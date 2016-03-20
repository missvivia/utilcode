package com.xyl.mmall;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netease.print.daojar.meta.base.IncrField;
import com.xyl.mmall.OrderConfig;
import com.xyl.mmall.order.dao.OrderSkuCartItemDao;
import com.xyl.mmall.order.dto.OrderSkuCartItemDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { OrderConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class DaoJarTest {

	static {
		System.setProperty("spring.profiles.active", "dev");
	}

	@Autowired
	private OrderSkuCartItemDao orderSkuCartItemDao;

	@Test
	public void testDao() {
		OrderSkuCartItemDTO dto = new OrderSkuCartItemDTO();
		dto.setOrderCartItemId(2);
		dto.setSkuId(11);
		dto.setUserId(3);

		Collection<String> fieldNameCollOfUpdate = new ArrayList<>();
		Collection<String> fieldNameCollOfWhere = new ArrayList<>();
		Collection<IncrField<?>> ifColl = new ArrayList<>();
		String extWhereSql = "1=2";

		IncrField<Long> ifItem = new IncrField<Long>("skuId", -1L);
		ifColl.add(ifItem);
		//
		// fieldNameCollOfUpdate.add("skuId");
		fieldNameCollOfUpdate.add("userId");

		fieldNameCollOfWhere.add("skuId");
		fieldNameCollOfWhere.add("orderCartItemId");

		// orderSkuCartItemDao.saveObject(dto);
		// orderSkuCartItemDao.getObjectByPrimaryKeyAndPolicyKey(dto);

		// orderSkuCartItemDao.update(dto, dto, fieldNameCollOfUpdate,
		// fieldNameCollOfWhere, ifColl, extWhereSql);

	}
}
