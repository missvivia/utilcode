package com.xyl.mmall.order.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xyl.mmall.order.dto.OrderCancelInfoDTO;
import com.xyl.mmall.order.param.TryAddOrderByTCCParam;
import com.xyl.mmall.order.result.TryAddOrderByTCCResult;
import com.xyl.mmall.order.service.OrderTCCService;

/**
 * @author dingmingliang
 * 
 */
@Service("orderTCCServiceRPCImpl")
public class OrderTCCServiceRPCImpl implements OrderTCCService {
	
	private static Logger logger = LoggerFactory.getLogger(OrderTCCServiceRPCImpl.class);

	@Resource(name = "orderTCCServiceInternalImpl")
	OrderTCCService orderTCCServiceInternalImpl;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#tryAddOrderByTCC(long,
	 *      com.xyl.mmall.order.param.TryAddOrderByTCCParam)
	 */
//	public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId, TryAddOrderByTCCParam param) {
//		try {
//			orderTCCServiceInternalImpl.tryAddOrderByTCC(tranId, param);
//		} catch (Exception ex) {
//			logger.error("try and order tcc error:",ex);
//		}
//		TryAddOrderByTCCResult result = param.getResult();
//		if (result == null) {
//			result = new TryAddOrderByTCCResult();
//			result.setResultCode(0);
//		}
//		return result;
//	}
    public TryAddOrderByTCCResult tryAddOrderByTCC(long tranId,
            Map<Long, TryAddOrderByTCCParam> paramMap)
    {
        try
        {
            logger.info("####################OrderTCCServiceRPCImpl远程调用TryAddOrderByTCC####################");
            orderTCCServiceInternalImpl.tryAddOrderByTCC(tranId, paramMap);
        }
        catch (Exception ex)
        {
            logger.error("try and order tcc error:", ex);
        }
        
        TryAddOrderByTCCParam param = null;
        for (Long businessId : paramMap.keySet())
        {
            param = paramMap.get(businessId);
            break;
        }
        TryAddOrderByTCCResult result = param.getResult();
        if (result == null)
        {
            result = new TryAddOrderByTCCResult();
            result.setResultCode(0);
        }
        return result;
    }

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#confirmAddOrderByTCC(long)
	 */
	public boolean confirmAddOrderByTCC(long tranId) {
		boolean isSucc = false;
		try {
			isSucc = orderTCCServiceInternalImpl.confirmAddOrderByTCC(tranId);
		} catch (Exception ex) {
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#cancelAddOrderByTCC(long)
	 */
	public boolean cancelAddOrderByTCC(long tranId) {
		boolean isSucc = false;
		try {
			isSucc = orderTCCServiceInternalImpl.cancelAddOrderByTCC(tranId);
		} catch (Exception ex) {
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#tryCallOffOrderByTCC(long,
	 *      com.xyl.mmall.order.dto.OrderCancelInfoDTO, boolean)
	 */
	public boolean tryCallOffOrderByTCC(long tranId, List<OrderCancelInfoDTO> orderCancelInfoDTOs, boolean canRecyleStock) {
		boolean isSucc = false;
		try {
			isSucc = orderTCCServiceInternalImpl.tryCallOffOrderByTCC(tranId, orderCancelInfoDTOs, canRecyleStock);
		} catch (Exception ex) {
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#confirmCallOffOrderByTCC(long)
	 */
	public boolean confirmCallOffOrderByTCC(long tranId) {
		boolean isSucc = false;
		try {
			isSucc = orderTCCServiceInternalImpl.confirmCallOffOrderByTCC(tranId);
		} catch (Exception ex) {
		}
		return isSucc;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.xyl.mmall.order.service.OrderTCCService#cancelCallOffOrderByTCC(long)
	 */
	public boolean cancelCallOffOrderByTCC(long tranId) {
		boolean isSucc = false;
		try {
			isSucc = orderTCCServiceInternalImpl.cancelCallOffOrderByTCC(tranId);
		} catch (Exception ex) {
		}
		return isSucc;
	}

}
