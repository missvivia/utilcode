package com.xyl.mmall.itemcenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.print.common.util.CollectionUtil;
import com.xyl.mmall.framework.annotation.Transaction;
import com.xyl.mmall.framework.vo.BasePageParamVO;
import com.xyl.mmall.itemcenter.dao.product.PoProductUserFavDao;
import com.xyl.mmall.itemcenter.dto.PoProductUserFavDTO;
import com.xyl.mmall.itemcenter.meta.PoProductUserFav;
import com.xyl.mmall.itemcenter.param.ProductUserFavParam;
import com.xyl.mmall.itemcenter.service.PoProductUserFavService;

@Service
public class PoProductUserFavServiceImpl implements PoProductUserFavService{

	@Autowired
	private PoProductUserFavDao poProductUserFavDao;
	
	@Transaction
	@Override
	public boolean addPoProductIntoFavList(long userId, long poId) {
		return poProductUserFavDao.addPoProductIntoFavList(userId, poId);
	}

	@Transaction
	@Override
	public boolean removePoProductFromFavList(long userId, long poId) {
		return poProductUserFavDao.removePoProductFromFavList(userId, poId);
	}

	@Override
	public List<PoProductUserFavDTO> getPoProductFavListByUserIdOrPoIds(
			Long userId, List<Long> poIds) {
		List<PoProductUserFav> poProductUserFavs = poProductUserFavDao.getPoProductFavListByUserIdOrPoIds(userId, poIds);
		return convertPoProductUserFavToDTO(poProductUserFavs);
	}

	@Override
	public BasePageParamVO<PoProductUserFavDTO> getPageProductUserFavDTOByUserId(
			ProductUserFavParam param) {
		List<PoProductUserFav> poProductUserFavs = poProductUserFavDao.getPageProductUserFavDTOByUserId(param);
		BasePageParamVO<PoProductUserFavDTO> basePageParamVO = new BasePageParamVO<PoProductUserFavDTO>();
		basePageParamVO.setTotal(param.getTotalCount());
		basePageParamVO.setList(convertPoProductUserFavToDTO(poProductUserFavs));
		return basePageParamVO;
	}
	
	private List<PoProductUserFavDTO> convertPoProductUserFavToDTO(List<PoProductUserFav> poProductUserFavs){
		List<PoProductUserFavDTO> resultDtos = new ArrayList<PoProductUserFavDTO>();
		if(CollectionUtil.isNotEmptyOfList(poProductUserFavs)){
			for(PoProductUserFav poProductUserFav:poProductUserFavs){
				resultDtos.add(new PoProductUserFavDTO(poProductUserFav));
			}
		}
		return resultDtos;
	}

}
