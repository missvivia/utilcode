package com.xyl.mmall.order.result;

import java.io.Serializable;

import com.netease.print.daojar.util.ReflectUtil;

/**
 * 返回结果: 添加订单(TCC模型)-try步骤
 * 
 * @author dingmingliang
 * 
 */
public class TryAddOrderByTCCResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20140909L;

	/**
	 * 0:失败,1:成功,2:库存不足,3:参数不正确
	 */
	private Integer resultCode;

	/**
	 * 0:失败,1:成功,2:库存不足,3:参数不正确
	 * 
	 * @return
	 */
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * 0:失败,1:成功,2:库存不足,3:参数不正确
	 * 
	 * @param resultCode
	 */
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 0:失败,1:成功,2:库存不足,3:参数不正确<br>
	 * 只在resultCode第一次设置时有用
	 * 
	 * @param resultCode
	 */
	public void setResultCodeWhenFirst(Integer resultCode) {
		if (this.resultCode == null)
			this.resultCode = resultCode;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ReflectUtil.genToString(this);
	}
}
