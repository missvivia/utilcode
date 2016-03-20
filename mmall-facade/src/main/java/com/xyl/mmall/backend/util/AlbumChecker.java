package com.xyl.mmall.backend.util;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.xyl.mmall.backend.vo.AlbumVO;
import com.xyl.mmall.cms.facade.util.BaseChecker;

/**
 * 
 * @author hzzhanghui
 * 
 */
public final class AlbumChecker extends BaseChecker {

	public AlbumChecker(Logger logger) {
		super(logger);
	}

	private ErrChecker checkLongNullAndMinus(Long val, String msg) {
		ErrChecker checker = new ErrChecker();
		if (val == null || val < 0) {
			return buildErrChecker(checker, msg);
		}

		return checker;
	}

	public ErrChecker checkMoveImg(Long toCategoryId) {
		return checkLongNullAndMinus(toCategoryId, "toCategoryId id cannot be null !");
	}

	public ErrChecker checkGetUserImgListByUserId(Long categoryId, Long startTime, Long endTime) {
		ErrChecker checker = new ErrChecker();
		checker = checkLongNullAndMinus(categoryId, "CategoryId id cannot be null !");
		if (!checker.check) {
			return checker;
		}
		checker = checkLongNullAndMinus(startTime, "startTime cannot < 0 !");
		if (!checker.check) {
			return checker;
		}
		checker = checkLongNullAndMinus(endTime, "endTime cannot < 0 !");
		if (!checker.check) {
			return checker;
		}

		return checker;
	}

	public ErrChecker checkAddDir(JSONArray paramJson) {
		ErrChecker checker = new ErrChecker();
		if (paramJson == null || paramJson.size() == 0) {
			return buildErrChecker(checker, "Please at least pass one category!!!");
		}

		return checker;
	}

	public ErrChecker checkAddDirName(String categoryName) {
		return checkStringEmpty(categoryName, "Category name cannot be null when create or update !!!");
	}

	public ErrChecker checkGetImgById(Long imgId) {
		return checkLong(imgId, "Image id cannot be null!!");
	}

	public ErrChecker checkGetImgByIdResults(AlbumVO vo, Long imgId) {
		return checkListEmpty(vo.getDto().getAlbumImgList(), "Cannot find image with id '" + imgId + "' !!");
	}

}
