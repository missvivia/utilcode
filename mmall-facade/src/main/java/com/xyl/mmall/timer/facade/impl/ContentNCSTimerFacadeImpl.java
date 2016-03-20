package com.xyl.mmall.timer.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netease.print.common.meta.RetArg;
import com.netease.print.common.util.CollectionUtil;
import com.netease.print.common.util.RetArgUtil;
import com.netease.print.daojar.meta.base.DDBParam;
import com.xyl.mmall.content.dto.ArticleDTO;
import com.xyl.mmall.content.dto.ArticlePublishedDTO;
import com.xyl.mmall.content.dto.NCSContentDispatchLogDTO;
import com.xyl.mmall.content.enums.ContentType;
import com.xyl.mmall.content.enums.NCSIndexDispatchState;
import com.xyl.mmall.content.service.HelpArticleService;
import com.xyl.mmall.content.service.HelpContentNCSService;
import com.xyl.mmall.framework.annotation.Facade;
import com.xyl.mmall.timer.facade.ContentNCSTimerFacade;

/**
 * 
 * @author hzwangjianyi@corp.netease.com
 *
 */
@Facade("contentNCSFacade")
public class ContentNCSTimerFacadeImpl implements ContentNCSTimerFacade {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private HelpArticleService helpArticleService;
	
	@Autowired
	private HelpContentNCSService helpContentNCSService;

	/**
	 * 
	 * (non-Javadoc)
	 * @see com.xyl.mmall.timer.facade.ContentNCSTimerFacade#dispatch()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RetArg dispatch() {
		RetArg retArg = new RetArg();
		long totalCount = 0, dispatchSuccCountOfNCS = 0, dispatchSuccCountOfDB = 0;
		long minId = 0;
		NCSIndexDispatchState[] states = new NCSIndexDispatchState[] {
				NCSIndexDispatchState.WAITING_TO_DISPATCH, NCSIndexDispatchState.WAITING_TO_DELETE
		};
		// DDBParam param = DDBParam.genParamX(100);
		DDBParam param = DDBParam.genParamX(2);
		param.setAsc(true);
		param.setOrderColumn("id");
		RetArg queryArg = helpArticleService.findNCSContentDispatchLogByDispatchStatesWithMinId(minId, states, param);
		List<NCSContentDispatchLogDTO> logDTOList = RetArgUtil.get(queryArg, ArrayList.class);
		while(!CollectionUtil.isEmptyOfList(logDTOList)) {
			for(NCSContentDispatchLogDTO logDTO : logDTOList) {
				if(null == logDTO) {
					logger.warn("null logDTO in logDTOList");
					continue;
				}
				totalCount++;
				long logId = logDTO.getId();
				ContentType ct = logDTO.getContentType();
				long foreignPrimaryId = logDTO.getForeignPrimaryId();
				int result = 0;
				switch(ct) {
				case ARTICLE:
					ArticleDTO article = helpArticleService.getArticleById(foreignPrimaryId);
					if(null == article) {
						logger.warn("null ArticleDTO for ncs log " + logId);
					} else {
						result = dispatchArticle(article, logDTO);
					}
					break;
				case ARTICLE_PUBLISHED:
					ArticlePublishedDTO articlePublished = helpArticleService.getArticlePublishedById(foreignPrimaryId);
					if(null == articlePublished) {
						logger.warn("null ArticlePublishedDTO for ncs log " + logId);
					} else {
						result = dispatchArticlePublished(articlePublished, logDTO);
					}
					break;
				default:
					result = 0;
					break;
				}
				if(result > 0) {
					dispatchSuccCountOfNCS++;
					if(result > 1) {
						dispatchSuccCountOfDB++;
					}
				}
			}
			NCSContentDispatchLogDTO lastLogDTO = logDTOList.get(logDTOList.size() - 1);
			if(null == lastLogDTO) {
				logger.error("null lastLogDTO in logDTOList");
				break;
			}
			DDBParam remoteParam = RetArgUtil.get(queryArg, DDBParam.class);
			if(null == remoteParam) {
				logger.error("null remoteParam in queryArg");
				break;
			}
			if(remoteParam.isHasNext()) {
				minId = lastLogDTO.getId();
				queryArg = helpArticleService.findNCSContentDispatchLogByDispatchStatesWithMinId(minId, states, param);
				logDTOList = RetArgUtil.get(queryArg, ArrayList.class);
			} else {
				break;
			}
		}
		Boolean isSucc = (totalCount == dispatchSuccCountOfNCS && totalCount == dispatchSuccCountOfDB) ? Boolean.TRUE : Boolean.FALSE;
		RetArgUtil.put(retArg, isSucc);
		RetArgUtil.put(retArg, "[dispatch()] total:" + totalCount + ", dispatchSuccCountOfNCS:" + dispatchSuccCountOfNCS 
				+ ", dispatchSuccCountOfDB:" + dispatchSuccCountOfDB);
		return retArg;
	}

	/**
	 * 0: both ncs and db failed
	 * 1: ncs succ, db failed
	 * 2: both ncs and db succ
	 * @param article
	 * @param logDTO
	 * @return
	 */
	private int dispatchArticle(ArticleDTO article, NCSContentDispatchLogDTO logDTO) {
		int result = 0;
		if(null == article || null == logDTO) {
			return result;
		}
		RetArg retArg = null;
		Boolean isSucc = null;
		switch(logDTO.getDispatchState()) {
		case WAITING_TO_DISPATCH:
			retArg = helpContentNCSService.indexArticle(article);
			isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE == isSucc) {
				if(helpArticleService.updateDispatchLog(logDTO, NCSIndexDispatchState.DISPATCHED, 
						new NCSIndexDispatchState[] {NCSIndexDispatchState.WAITING_TO_DISPATCH})) {
					result = 2;
				} else {
					result = 1;
				}
			}
			break;
		case WAITING_TO_DELETE:
			retArg = helpContentNCSService.deleteArticle(article);
			isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE == isSucc) {
				if(helpArticleService.updateDispatchLog(logDTO, NCSIndexDispatchState.DELETED, 
						new NCSIndexDispatchState[] {NCSIndexDispatchState.WAITING_TO_DELETE})) {
					result = 2;
				} else {
					result = 1;
				}
			}
			break;
		default:
			break;
		}
		return result;
	}
	
	/**
	 * 0: both ncs and db failed
	 * 1: ncs succ, db failed
	 * 2: both ncs and db succ
	 * @param articlePublished
	 * @param logDTO
	 * @return
	 */
	private int dispatchArticlePublished(ArticlePublishedDTO articlePublished, NCSContentDispatchLogDTO logDTO) {
		int result = 0;
		if(null == articlePublished || null == logDTO) {
			return result;
		}
		RetArg retArg = null;
		Boolean isSucc = null;
		switch(logDTO.getDispatchState()) {
		case WAITING_TO_DISPATCH:
			retArg = helpContentNCSService.indexArticlePublished(articlePublished);
			isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE == isSucc) {
				if(helpArticleService.updateDispatchLog(logDTO, NCSIndexDispatchState.DISPATCHED, 
						new NCSIndexDispatchState[] {NCSIndexDispatchState.WAITING_TO_DISPATCH})) {
					result = 2;
				} else {
					result = 1;
				}
			}
			break;
		case WAITING_TO_DELETE:
			retArg = helpContentNCSService.deleteArticlePublished(articlePublished);
			isSucc = RetArgUtil.get(retArg, Boolean.class);
			if(Boolean.TRUE == isSucc) {
				if(helpArticleService.updateDispatchLog(logDTO, NCSIndexDispatchState.DELETED, 
						new NCSIndexDispatchState[] {NCSIndexDispatchState.WAITING_TO_DELETE})) {
					result = 2;
				} else {
					result = 1;
				}
			}
			break;
		default:
			break;
		}
		return result;
	}
}
