//package com.xyl.mmall.controller;
////package com.xyl.mmall.controller;
////
////
////import java.util.Arrays;
////import java.util.Date;
////import java.util.Map;
////import java.util.Map.Entry;
////
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.bind.annotation.ResponseBody;
////
////import com.xyl.mmall.backend.facade.CartCleanCacheFacade;
////import com.xyl.mmall.cart.dto.CartDTO;
////import com.xyl.mmall.cart.dto.CartItemDTO;
////import com.xyl.mmall.framework.enums.ErrorCode;
////import com.xyl.mmall.framework.vo.BaseJsonVO;
////import com.xyl.mmall.mainsite.facade.CartFacade;
////import com.xyl.mmall.mainsite.vo.CartVO;
////import com.xyl.mmall.promotion.utils.DateUtils;
////
////@Controller
////@RequestMapping("/cart/clean")
////public class TestCartCleanCacheJob {
////
////	@Autowired
////	private CartCleanCacheFacade cartCleanCacheFacade;
////
////	@Autowired
////	private CartFacade cartFacade;
////
////	private static final Logger logger = LoggerFactory.getLogger(TestCartCleanCacheJob.class);
////	
////	@RequestMapping("/searchcache")
////	@ResponseBody
////	public BaseJsonVO searchCache(@RequestParam long userid,@RequestParam int areaid){
////		BaseJsonVO vo = new BaseJsonVO();
////		CartVO cart=cartFacade.getCart(userid, areaid, null);
////		vo.setResult(cart);
////		return vo;
////	}
////	
////	@RequestMapping("invalid")
////	@ResponseBody
////	public BaseJsonVO invalid(@RequestParam long userid) {
////		BaseJsonVO vo = new BaseJsonVO();
////		boolean flag=cartCleanCacheFacade.cleanAllArea(-1,userid);
////		logger.info("invalid flag:"+flag);
////		vo.setCode(ErrorCode.SUCCESS);
////		return vo;
////	}
////
////	@RequestMapping("search")
////	@ResponseBody
////	public BaseJsonVO search(@RequestParam long userid,@RequestParam int areaid) {
////		BaseJsonVO vo = new BaseJsonVO();
////
////		// check the overtime cart
////		CartDTO resCartDTO = cartCleanCacheFacade.getCartAll(userid, areaid);
////		System.out.println(resCartDTO);
////
////		vo.setResult(resCartDTO);
////		vo.setCode(ErrorCode.SUCCESS);
////		return vo;
////	}
////
////	@RequestMapping("searcharea")
////	@ResponseBody
////	public BaseJsonVO searchAreaData(@RequestParam int areaid, @RequestParam int pos,
////			@RequestParam(required = false) long userid) {
////		BaseJsonVO vo = new BaseJsonVO();
////		
////		int[] arrs=cartCleanCacheFacade.getPositionShouldProcessedByCurrentJob(areaid);
////
////		Map<String, Object> maps = cartCleanCacheFacade.getAreaData(areaid, pos);
////		vo.setResult(maps);
////
////		if (maps == null || maps.isEmpty()) {
////			return vo;
////		}
////
////		// check the overtime cart
////		CartDTO resCartDTO = cartCleanCacheFacade.getCartAll(userid, areaid);
////		System.out.println(resCartDTO);
////
////		logger.info("area point:" + pos + ",elements:");
////		for (Entry<String, Object> entry : maps.entrySet()) {
////			String timeValue = (String) entry.getValue();
////			long timeLong = Long.valueOf(timeValue);
////
////			// userId
////			String[] arr = entry.getKey().split("\\-");
////
////			// 用户购物车实际更新时间
////			long userIdGet = Long.valueOf(arr[1]);
////			
////			long cartTimeForUserTemp = cartCleanCacheFacade.getCartTime(userIdGet, areaid);
////			String cartTimeForUserStrTemp = DateUtils
////					.parseLongToString(DateUtils.DATE_TIME_FORMAT, cartTimeForUserTemp);
////
////			String time = DateUtils.parseLongToString(DateUtils.DATE_TIME_FORMAT, timeLong);
////			logger.info("key:" + entry.getKey() + ",value:" + entry.getValue() + ",time in str:" + time + ",real cart time:"
////					+ cartTimeForUserStrTemp);
////			logger.info("should clean:" + time.equals(cartTimeForUserStrTemp));
////
////			logger.info("leftTime:" + (System.currentTimeMillis() - cartTimeForUserTemp));
////		}
////
////		vo.setCode(ErrorCode.SUCCESS);
////		return vo;
////	}
////
////	@RequestMapping("/mock")
////	@ResponseBody
////	public BaseJsonVO mockDataController(@RequestParam long skuid, @RequestParam long userid) {
////		BaseJsonVO vo = new BaseJsonVO();
////		int areaId = -1;
////
////		// mock data
////		int deltaCount = 3;
////		boolean mockFlag = this.mockCartData(userid, areaId, skuid, deltaCount);
////		if (!mockFlag) {
////			return vo;
////		}
////
////		// add the updateTime to cache
////		boolean addUpdateFlag = cartFacade.addCartUpdateTimeToCache(userid, areaId, new Date()
////				);
////		if (!addUpdateFlag) {
////			return vo;
////		}
////
////		// check the valid cart item
////		int count = cartFacade.getCartValidCount(userid, areaId);
////		System.out.println("count=" + count);
////		CartDTO oldCartDTO = cartCleanCacheFacade.getCartAll(userid, areaId);
////		System.out.println(oldCartDTO);
////
////		return vo;
////	}
////
////	private boolean mockCartData(long userid, int provinceId, long skuid, int deltaCount) {
////		CartItemDTO cartItem = new CartItemDTO();
////		cartItem.setSkuid(skuid);
////		cartItem.setCount(deltaCount);
////		return cartCleanCacheFacade
////				.mockAddCartItemToCart(userid, provinceId, "NKV_CART_VALID", Arrays.asList(cartItem));
////	}
////
////	@RequestMapping("start")
////	@ResponseBody
////	public BaseJsonVO beginClean(@RequestParam(required=false) long userid) {
////		BaseJsonVO vo = new BaseJsonVO();
////
////		int areaId = -1;
////		boolean distributeProcess = false;
////
////		// 1.get the point
////		int[] pointArr = cartCleanCacheFacade.getPositionShouldProcessedByCurrentJob(areaId);
////		if (pointArr == null || pointArr.length != 2) {
////			logger.error("error init the point record for cart clean");
////			return vo;
////		}
////
////		// 2.update the point
////		boolean updateflag = cartCleanCacheFacade.setUpPoint(areaId, pointArr[1], pointArr[0]);
////		if (!updateflag) {
////			logger.error("cannot update the point at method:beginClean,areaId:" + areaId + ",distributeProcess:"
////					+ distributeProcess);
////			return vo;
////		}
////
////		// 3.begin clean the cache
////		boolean cleanFlag = cartCleanCacheFacade.cleanOverTimeCartForJob(areaId, pointArr[1]);
////
////		// 4.finally update the status for current point
////		boolean updateStatusFlag = cartCleanCacheFacade.pointFlagToSuccessOrFail(cleanFlag, areaId,
////				pointArr[1], pointArr[1]);
////
////		if (!updateStatusFlag) {
////			logger.error("cannot update the status for current point when finish clean cache for cart");
////			return vo;
////		}
////
////		// check the overtime cart
////		CartDTO resCartDTO = cartCleanCacheFacade.getCartAll(userid, areaId);
////		System.out.println(resCartDTO);
////
////		vo.setCode(ErrorCode.SUCCESS);
////		return vo;
////	}
////
////}
////
