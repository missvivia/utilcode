package com.xyl.mmall.controller;


import java.io.BufferedReader;
import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.cms.dto.AreaDTO;
import com.xyl.mmall.cms.dto.PromotionContentDTO;
import com.xyl.mmall.cms.dto.PromotionContentVO;
import com.xyl.mmall.cms.enums.PromotionContentStatus;
import com.xyl.mmall.cms.enums.PromotionContentTab;
import com.xyl.mmall.cms.enums.PromotionContentType;
import com.xyl.mmall.cms.facade.BusinessFacade;
import com.xyl.mmall.cms.facade.LeftNavigationFacade;
import com.xyl.mmall.cms.facade.PromotionContentFacade;
import com.xyl.mmall.cms.facade.ScheduleBannerFacade;
import com.xyl.mmall.cms.facade.ScheduleFacade;
import com.xyl.mmall.cms.facade.util.ScheduleUtil;
import com.xyl.mmall.cms.meta.Business;
import com.xyl.mmall.cms.meta.PromotionContent;
import com.xyl.mmall.cms.vo.ScheduleVO;
import com.xyl.mmall.member.service.AgentService;
import com.xyl.mmall.saleschedule.meta.ScheduleSiteRela;


/**
 * 推广内容管理
 * 
 * @author hzliujie
 * 
 */
@Controller
public class PromotionContentController extends BaseController {
	
	@Autowired
	private PromotionContentFacade promotionContentFacade;

	@Autowired
	private ScheduleBannerFacade scheduleBannerFacade;
	
	@Autowired
	private ScheduleFacade scheduleFacade;
	
	@Autowired
	private BusinessFacade businessFacade;
	
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 推广内容管理
	 * @return
	 */
	@RequestMapping(value = { "/content"})
	public String content(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/content/spread";
	}
	
	/**
	 * 推广内容管理
	 * @return
	 */
	@RequestMapping(value = { "/content/spread"})
	@RequiresPermissions(value = { "content:spread" })
	public String spread(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/content/spread";
	}
	
	/**
	 * 消息通知管理
	 * @return
	 */
	@RequestMapping(value = { "/content/note"})
	public String note(Model model) {
		model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
		return "pages/content/note";
	}
	
	/**
	 * 添加推广内容
	 * @param content
	 * @param searchTime
	 * @return
	 */
	@RequestMapping(value = { "/content/spread/upsert"})
	@ResponseBody
	public Map<String, Object> add(PromotionContentDTO content) {
		Map<String, Object> map = new HashMap<>();
		Long currTime = new Date().getTime();
		content.setCreateTime(currTime);
		content.setUpdateTime(currTime);
		content.setStartTime(getFirstSecondOfTime(content.getStartTime()));
		content.setEndTime(getLastSecondOfTime(content.getEndTime()));
		content.setDevice(PromotionContentType.WEB);
		PromotionContent result = promotionContentFacade.addContent(content);
		if(result==null){
			map.put("code", 201);
			map.put("result", result);
		}else{
			map.put("code", 200);
			List<PromotionContentDTO> pcdList = getPromotionContentListByTab(content.getAreaId(),content.getSearchTime(),content.getPositionId());
			map.put("code", 200);
			map.put("result", pcdList);
		}
		return map;
	}
	
	/**
	 * 获取所有tab下的推广数据
	 * @param searchTime
	 * @param provinceId
	 * @return
	 */
	@RequestMapping(value = { "/content/spread/list/"} ,method = RequestMethod.GET)
	public Map<String, Object> list(Long searchTime,Integer provinceId) {
		Map<String, Object> map = new HashMap<>();
		List<PromotionContentVO> result = new ArrayList<PromotionContentVO>();
		try{
			result = getPromotionContentList(searchTime,provinceId);
			map.put("code", 200);
			map.put("result", result);
			
		}catch(Exception e){
			map.put("code", 201);
			map.put("result", result);
		}
		return map;
	}
	
	/**
	 * 获取所有站点数据
	 * @return
	 */
	@RequestMapping(value = { "/content/getProvinceList"} ,method = RequestMethod.GET)
	public Map<String, Object> getProvinceList() {
		Map<String, Object> map = new HashMap<>();
		//List<AreaDTO> areaList = businessFacase.getAreaList();
		long userId = SecurityContextUtils.getUserId();
		List<AreaDTO> areaList = businessFacade.getAreadByIdList(agentService.findAgentSiteIdsByPermission(userId, "content:spread"));
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
	@RequestMapping(value = { "/content/checkpo"},method = RequestMethod.GET)
	public Map<String, Object> checkpo(long poId,long areaId) {
		Map<String, Object> map = new HashMap<>();
		ScheduleVO schedulVO = scheduleFacade.getScheduleById(poId);
		if(schedulVO.getPo().getScheduleDTO().getSchedule()==null){
			map.put("code", 401);
			String msg = "PO编号不存在";
			Map<String,String> m = new HashMap<String,String>();
			m.put("msg", msg);
			map.put("result", m);
		}else{
			List<ScheduleSiteRela> list = schedulVO.getPo().getScheduleDTO().getSiteRelaList();
			boolean find = false;
			if(list!=null && list.size()>0){
				for(ScheduleSiteRela site:list){
					if(site.getSaleSiteId()==areaId){
						find = true;
						break;
					}
				}
			}
			if(!find){
				map.put("code", 401);
				String msg = "PO编号不属于该站点";
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
	@RequestMapping(value = { "/content/spread/delete"},method = RequestMethod.GET)
	public Map<String, Object> delete(Long provinceId,Long searchTime,Integer positionId,Long cid) {
		Map<String, Object> map = new HashMap<>();
		if(promotionContentFacade.deletePromotionContent(cid)){
			map.put("code", 201);
			map.put("result", null);
		}else{
			List<PromotionContentDTO> pcdList = getPromotionContentListByTab(provinceId,searchTime,positionId);
			map.put("code", 200);
			map.put("result", pcdList);		
		}
		return map;
	}
	
	/**
	 * 更新一个推广内容
	 * @param content
	 * @param searchTime
	 * @return
	 */
	@RequestMapping(value = { "/content/spread/update"})
	@ResponseBody
	public Map<String, Object> update(PromotionContentDTO content) {
		Map<String, Object> map = new HashMap<>();
		Long currTime = new Date().getTime();
		content.setUpdateTime(currTime);
		content.setStartTime(getFirstSecondOfTime(content.getStartTime()));
		content.setEndTime(getLastSecondOfTime(content.getEndTime()));
		if(!promotionContentFacade.updatePromotionContent(content)){
			map.put("code", 201);
			map.put("result", null);
		}else{
			List<PromotionContentDTO> pcdList = getPromotionContentListByTab(content.getAreaId(),content.getSearchTime(),content.getPositionId());
			map.put("code", 200);
			map.put("result", pcdList);		
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
	@RequestMapping(value = { "/content/spread/order"},method = RequestMethod.POST)
	public Map<String, Object> switchOrder(HttpServletRequest request, HttpServletResponse response) {
		String jsonString;
		try {
			jsonString = getJson(request.getInputStream());
			JSONObject json = JSON.parseObject(jsonString);
			Long provinceId = json.getLong("provinceId");
			Long searchTime = json.getLong("searchTime");
			int positionId = json.getIntValue("positionId");
			List<PromotionContentDTO> list = JSON.parseArray(json.getString("list"), PromotionContentDTO.class);
			Map<String, Object> map = new HashMap<>();
			for(PromotionContentDTO content:list){
				Long currTime = new Date().getTime();
				content.setUpdateTime(currTime);
				if(!promotionContentFacade.updatePromotionContent(content)){
					map.put("code", 201);
					break;
				}
			}
			List<PromotionContentDTO> pcdList = getPromotionContentListByTab(provinceId,searchTime,positionId);
			map.put("code", 200);
			map.put("result", pcdList);		
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取所有tab下的数据
	 * @param searchTime
	 * @param provinceId
	 * @return
	 */
	private List<PromotionContentVO> getPromotionContentList(Long searchTime,Integer provinceId){
		List<PromotionContent> pcList = null;
		List<PromotionContentDTO> pcdList = null;
		List<PromotionContentVO> result = new ArrayList<PromotionContentVO>();
		int[] arr = new int[]{1,2,3,5};
		for(int i=0;i<arr.length;i++){
			pcdList = new ArrayList<PromotionContentDTO>();
			PromotionContentVO pcv = new PromotionContentVO();
			pcv.setType(arr[i]);
			pcv.setName(PromotionContentTab.getDescByInt(arr[i]));
			pcList = promotionContentFacade.getPromotionContentByAreaAndTimeAndPosition(searchTime,provinceId,arr[i]);
			if(pcList != null && pcList.size()>0){
				for(PromotionContent content : pcList){
					PromotionContentDTO pcd = new PromotionContentDTO(content);
					pcd.setType(content.getPositionId());
					ScheduleVO schedulevo = scheduleFacade.getScheduleById(content.getBusinessId());
					if(schedulevo!=null && schedulevo.getPo()!=null && schedulevo.getPo().getScheduleDTO()!=null && schedulevo.getPo().getScheduleDTO().getSchedule()!=null){
						pcd.setBrandName(ScheduleUtil.getCombinedBrandName(schedulevo.getPo().getScheduleDTO()
								.getSchedule().getBrandNameEn(), schedulevo.getPo().getScheduleDTO().getSchedule().getBrandName()));
						pcd.setSupplierName(schedulevo.getPo().getScheduleDTO().getSchedule().getSupplierName());
					}
					Long currTime = new Date().getTime();
					if(currTime>=content.getStartTime() && currTime<=content.getEndTime())
						pcd.setStatus(PromotionContentStatus.ONLINE);
					else
						pcd.setStatus(PromotionContentStatus.OFFLINE);
					pcdList.add(pcd);
				}
			}
			Collections.sort(pcdList,new PromotionContentDTO());
			pcv.setList(pcdList);
			result.add(pcv);
		}
		return result;
	}
	
	
	/**
	 * 获取某个tab下的数据
	 * @param provinceId
	 * @param searchTime
	 * @param positionId
	 */
	private List<PromotionContentDTO> getPromotionContentListByTab(Long provinceId,Long searchTime,Integer positionId){
		List<PromotionContent> pcList = promotionContentFacade.getPromotionContentByAreaAndTimeAndPosition(searchTime,provinceId,positionId);
		List<PromotionContentDTO> pcdList = new ArrayList<PromotionContentDTO>();
		for(PromotionContent content : pcList){
				PromotionContentDTO pcd = new PromotionContentDTO(content);
				pcd.setType(content.getPositionId());
				ScheduleVO scheduleVo = scheduleFacade.getScheduleById(pcd.getBusinessId());
				if(scheduleVo!=null && scheduleVo.getPo()!=null && scheduleVo.getPo().getScheduleDTO()!=null && scheduleVo.getPo().getScheduleDTO().getSchedule()!=null){
					pcd.setBrandName(ScheduleUtil.getCombinedBrandName(scheduleVo.getPo().getScheduleDTO()
							.getSchedule().getBrandNameEn(), scheduleVo.getPo().getScheduleDTO().getSchedule().getBrandName()));
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
}