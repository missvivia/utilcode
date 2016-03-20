package com.xyl.mmall.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.PromotionContentDTO;
import com.xyl.mmall.cms.enums.PromotionContentStatus;
import com.xyl.mmall.cms.enums.PromotionContentType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.vo.ScheduleVO;


/**
 * 推广内容管理
 * 
 * @author hzliujie
 * 
 */
@Controller
public class MobileBannerController extends BaseController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(MobileBannerController.class);
	
	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Autowired
	private ScheduleBannerFacade scheduleBannerFacade;
	
	@Autowired
	private ScheduleFacade scheduleFacade;
	
	@Autowired
	private BusinessFacade businessFacase;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
    @RequestMapping(value ="/app/focuspicture", method = RequestMethod.GET)
    @RequiresPermissions(value = { "app:focuspicture" })
    public String focusPictureMnage(Model model){
    	model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
        return "pages/app/focuspicture";
    }
	
	/**
	 * 添加推广内容
	 * @param content
	 * @param searchTime
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/add"})
	@ResponseBody
	public Map<String, Object> add(HttpServletRequest request, HttpServletResponse response) {	
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		String jsonString;
		try {
			jsonString = getJson(request.getInputStream());
			PromotionContentDTO content = JSON.parseObject(jsonString,PromotionContentDTO.class);
			Long currTime = new Date().getTime();
			content.setCreateTime(currTime);
			content.setUpdateTime(currTime);
			content.setStartTime(getFirstSecondOfTime(content.getStartTime()));
			content.setEndTime(getLastSecondOfTime(content.getEndTime()));
			//指定添加类型为移动端
			content.setDevice(PromotionContentType.MOBILE);
			PromotionContent pc = promotionContentFacade.addContent(content);
			result.put("conent", pc);
			map.put("result", pc);
			if(pc==null){
				map.put("code", 201);
			}else{
				map.put("code", 200);
			}
			
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			map.put("code", 201);
		}
		return map;

	}
	
	/**
	 * 获取所有tab下的推广数据
	 * @param searchTime
	 * @param provinceId
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/list"} ,method = RequestMethod.GET)
	public Map<String, Object> list(Long searchTime,Long provinceId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result = new HashMap<>();
		List<PromotionContentDTO> list = null;
		try{
			list = getPromotionContentList(provinceId,searchTime);
			map.put("code", 200);
			result.put("list", list);
			
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			map.put("code", 201);
		}
		map.put("result",result);
		return map;
	}
	
	/**
	 * 获取所有站点数据
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/getProvinceList"} ,method = RequestMethod.GET)
	public Map<String, Object> getProvinceList() {
		Map<String, Object> map = new HashMap<>();
		List<AreaDTO> areaList = businessFacase.getAreaList();
		if(areaList==null || areaList.size()==0){
			map.put("code", 201);
			map.put("result", areaList);
		}else{
			map.put("code", 200);
			map.put("result", areaList);
		}
		return map;
	}
	
	/**
	 * 校验po是否存在
	 * @param poId
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/checkpo"},method = RequestMethod.GET)
	public Map<String, Object> checkpo(long poId) {
		Map<String, Object> map = new HashMap<>();
		ScheduleVO schedulVO = scheduleFacade.getScheduleById(poId);
		if(schedulVO.getPo().getScheduleDTO().getSchedule()==null){
			map.put("code", 401);
			String msg = "PO编号不存在";
			Map<String,String> m = new HashMap<String,String>();
			m.put("msg", msg);
			map.put("result", m);
		}else{
			map.put("code", 200);
			String msg = "PO编号ok";
			Map<String,String> m = new HashMap<String,String>();
			m.put("msg", msg);
			map.put("result", m);		
		}
		return map;
	}
	
	

	/**
	 * 删除一个推广内容
	 * @param provinceId
	 * @param searchTime
	 * @param positionId
	 * @param cid
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/delete"},method = RequestMethod.GET)
	public Map<String, Object> delete(Long id) {
		Map<String, Object> map = new HashMap<>();
		if(promotionContentFacade.deletePromotionContent(id)){
			map.put("code", 200);
		}else{
			map.put("code", 200);		
		}
		return map;
	}
	
	/**
	 * 更新一个推广内容
	 * @param content
	 * @param searchTime
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/update"})
	@ResponseBody
	public Map<String, Object> update(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		String jsonString;
		try {
			jsonString = getJson(request.getInputStream());
			PromotionContentDTO content = JSON.parseObject(jsonString,PromotionContentDTO.class);
			Long currTime = new Date().getTime();
			content.setUpdateTime(currTime);
			content.setStartTime(getFirstSecondOfTime(content.getStartTime()));
			content.setEndTime(getLastSecondOfTime(content.getEndTime()));
			if(promotionContentFacade.updatePromotionContent(content)){
				map.put("code", 200);
			}else{
				map.put("code", 201);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			map.put("code", 201);
		}
		return map;
	}
	
	/**
	 * 同一个tab下面的推广内容排序
	 * @param list
	 * @param provinceId
	 * @param searchTime
	 * @param positionId
	 * @param cid
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/order"},method = RequestMethod.GET)
	public Map<String, Object> switchOrder(Long searchTime,Long provinceId,Long id,int move) {
		Map<String, Object> map = new HashMap<>();
		try{
			promotionContentFacade.adjustSequenceOfId(id, move, searchTime);
			map.put("code", 200);
			return map;
		} catch (Exception e) {
			map.put("code", 201);
			return map;
		}
	}
	
	/**
	 * 上下线调整
	 * @param provinceId
	 * @param id
	 * @param move
	 * @param searchTime
	 * @return
	 */
	@RequestMapping(value = { "/focuspicture/online"},method = RequestMethod.GET)
	public Map<String, Object> onOffline(Long id,int action) {
		Map<String, Object> map = new HashMap<>();
		try{
			promotionContentFacade.changeOnlineStatusPC(id, action);
			map.put("code", 200);
			return map;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			map.put("code", 201);
			return map;
		}
	}
	
	
	/**
	 * 获取对应省份时间的数据
	 * @param provinceId
	 * @param searchTime
	 * @param positionId
	 */
	private List<PromotionContentDTO> getPromotionContentList(Long provinceId,Long searchTime){
		List<PromotionContent> pcList = promotionContentFacade.getMobilePCByProvTimeDevice(searchTime, provinceId);
		List<PromotionContentDTO> pcdList = new ArrayList<PromotionContentDTO>();
		for(PromotionContent content : pcList){
				PromotionContentDTO pcd = new PromotionContentDTO(content);
				pcd.setType(content.getPositionId());
				ScheduleVO scheduleVo = scheduleFacade.getScheduleById(pcd.getBusinessId());
				if(scheduleVo!=null && scheduleVo.getPo()!=null && scheduleVo.getPo().getScheduleDTO()!=null && scheduleVo.getPo().getScheduleDTO().getSchedule()!=null){
					pcd.setBrandName(scheduleVo.getPo().getScheduleDTO().getSchedule().getBrandName());
					pcd.setSupplierName(scheduleVo.getPo().getScheduleDTO().getSchedule().getSupplierName());
				}
				Long currTime = new Date().getTime();
				if(currTime>=content.getStartTime() && currTime<=content.getEndTime())
					pcd.setStatus(PromotionContentStatus.ONLINE);
				else
					pcd.setStatus(PromotionContentStatus.OFFLINE);
				pcdList.add(pcd);
		}
		Collections.sort(pcdList,new PromotionContentDTO());  
		return pcdList;
	}
	
	/**
	 * 将日期毫秒数转换成当天最后毫秒数
	 * @param curr
	 * @return
	 */
	private Long getLastSecondOfTime(Long curr){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		try {
			time =  sdf2.parse(sdf1.format(curr)+" 23:59:59");
			return time.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1L;
	}
	
	
	/**
	 * 将日期毫秒数转换成当天第一个毫秒数
	 * @param curr
	 * @return
	 */
	private Long getFirstSecondOfTime(Long curr){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		try {
			time =  sdf2.parse(sdf1.format(curr)+" 00:00:00");
			return time.getTime();
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return -1L;
	}
	
	/**
	 * 从InputStream获取json字符串
	 * @param input
	 * @return
	 */
	private String getJson(InputStream input){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line = null;
		try {
			while((line=reader.readLine())!=null){
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}