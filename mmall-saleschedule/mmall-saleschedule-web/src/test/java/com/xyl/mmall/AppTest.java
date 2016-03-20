package com.xyl.mmall;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xyl.mmall.SalescheduleConfig;
import com.xyl.mmall.saleschedule.dto.PODTO;
import com.xyl.mmall.saleschedule.dto.POListDTO;
import com.xyl.mmall.saleschedule.meta.Brand;
import com.xyl.mmall.saleschedule.meta.Schedule;
import com.xyl.mmall.saleschedule.service.BrandService;
import com.xyl.mmall.saleschedule.service.ScheduleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SalescheduleConfig.class, PropertyPlaceholderAutoConfiguration.class })
@ActiveProfiles("dev")
public class AppTest {

	static {
		System.setProperty("spring.profiles.active", "dev");
	}

	@Resource
	private BrandService brandService;
	
	@Resource ScheduleService scheduleService;
	
	@Test
	public void testGetBrand() throws Exception {
		List<Brand> lists=brandService.getBrandByName("GXG");
		System.out.println(lists);
		Thread.sleep(1000);
		List<Brand> lists2=brandService.getBrandByName("GXG");
		System.out.println(lists2);
	}
	
	@Test
	public void brandDataMigration() {
		brandService.syncData();
	}
	
	@Test
	public void testPO() throws Exception {
		POListDTO poListDTO = scheduleService.getScheduleListForChl(1, 0x01L << 18, System.currentTimeMillis());
		
		for (PODTO po : poListDTO.getPoList()) {
			Schedule s = po.getScheduleDTO().getSchedule();
			System.out.println(s.getId() + " -- " + s.getTitle() + " -- " + s.getShowOrder());
		}
	}
	
}
