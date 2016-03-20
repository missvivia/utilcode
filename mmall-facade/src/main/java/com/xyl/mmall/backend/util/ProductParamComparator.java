package com.xyl.mmall.backend.util;

import java.util.Comparator;

import com.xyl.mmall.mainsite.vo.BaseNameValueVO;

public class ProductParamComparator implements Comparator<BaseNameValueVO> {

	@Override
	public int compare(BaseNameValueVO arg0, BaseNameValueVO arg1) {
		return arg0.getType() - arg1.getType();
	}

}
