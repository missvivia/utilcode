package com.xyl.mmall;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.xyl.mmall.CartJobConfig;
import com.xyl.mmall.cart.clean.CartRDBOperUtil;
import com.xyl.mmall.cart.clean.RDBResult;
import com.xyl.mmall.cart.dto.CartDTO;
import com.xyl.mmall.cart.service.CartInventoryCleanerService;
import com.xyl.mmall.cart.service.CartService;

/**
 * Unit test for simple App.
 */
@ContextConfiguration(classes = { CartJobConfig.class })
public class AppTest extends BaseAppTest {

	@Autowired
	private CartRDBOperUtil cartRDBOperUtil;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartInventoryCleanerService cartInventoryCleanerService;

	@Test
	public void testCleanCart() throws Exception {
		int areaId=33;
		
		
		//data
		boolean flag=cartService.addCartUpdateTimeToCache(31234, areaId, new Date());
		System.out.println(flag);
		
		// 1.get the point
		int[] pointArr = cartInventoryCleanerService.getPositionShouldProcessedByCurrentJob(areaId);
		if (pointArr == null || pointArr.length != 2) {
			System.out.println("false");
		}

		// 2.update the point
		boolean updateflag = cartInventoryCleanerService.setUpPoint(areaId, pointArr[1], pointArr[0]);
		if (!updateflag) {
			System.out.println("false");
		}

		// 3.begin clean the cache
		boolean cleanFlag = cartInventoryCleanerService.cleanOverTimeCartForJob(areaId, pointArr[1]);

		// 4.finally update the status for current point
		boolean updateStatusFlag = cartInventoryCleanerService.pointFlagToSuccessOrFail(cleanFlag, areaId, pointArr[1],
				pointArr[1]);
		
		System.out.println(updateStatusFlag);
	}

	@Test
	public void testCart() throws Exception {
		CartDTO cartDTO = cartService.getCart(231, 33);
		System.out.println(cartDTO);
	}

	@Test
	public void testCartRDb() throws Exception {
		String key = "test_cart_rdb_2014";

		int successCount = 0;
		for (int i = 0; i < 9000; i++) {
			String field = "a" + i;
			boolean flag = cartRDBOperUtil.putOrReplaceForRDBOfMap(key, field, field);
			if (flag) {
				successCount++;
			}
		}

		System.out.println("successCount:" + successCount);

		// show
		for (int i = 0; i < 9000; i++) {
			String field = "a" + i;
			RDBResult rdbResult = cartRDBOperUtil.getFromRDBOfMapByKeyAndField(key, field);
			if (rdbResult.isSearchSuccess() && rdbResult.getByteRes() != null) {
				String s = new String(rdbResult.getByteRes());
				System.out.println(s);
			}
		}

	}

}
