package com.xyl.mmall.mobile.facade;

import com.xyl.mmall.mobile.ios.facade.pageView.index.IndexVO;

public interface MobileIndexFacade {
	public IndexVO getIndexVO(boolean fresh, String path,String FileName) throws  Exception;
}
