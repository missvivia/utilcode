package com.xyl.mmall.saleschedule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xyl.mmall.saleschedule.dto.ScheduleMagicCubeDTO;
import com.xyl.mmall.saleschedule.meta.ScheduleMagicCube;
import com.xyl.mmall.saleschedule.service.ScheduleMagicCubeService;

/**
 * 
 * @author hzzhanghui
 * 
 */
@Service
public class ScheduleMagicCubeServiceImpl extends ScheduleBaseService implements ScheduleMagicCubeService {

	@Override
	public boolean saveScheduleMagicCube(ScheduleMagicCubeDTO mcDTO) {
		if (mcDTO == null) {
			return true;
		}

		logger.debug("saveScheduleMagicCube(" + mcDTO + ")");
		return magicCubeDao.saveScheduleMagicCube(mcDTO.getMagicCube());
	}

	@Override
	public boolean saveShceduleMagicCubeList(List<ScheduleMagicCubeDTO> mcDTOList) {
		if (mcDTOList == null) {
			return true;
		}

		logger.debug("saveShceduleMagicCubeList(" + mcDTOList + ")");

		List<ScheduleMagicCube> mcList = new ArrayList<ScheduleMagicCube>();
		for (ScheduleMagicCubeDTO mcDTO : mcDTOList) {
			mcList.add(mcDTO.getMagicCube());
		}

		return magicCubeDao.saveShceduleMagicCubeList(mcList);
	}

	@Override
	public boolean updateScheduleMagicCubeBySupplierId(ScheduleMagicCubeDTO mcDTO) {
		if (mcDTO == null) {
			return true;
		}
		logger.debug("updateScheduleMagicCubeBySupplierId(" + mcDTO + ")");

		return magicCubeDao.updateScheduleMagicCubeBySupplierId(mcDTO.getMagicCube());
	}

	@Override
	public boolean deleteScheduleMagicCubeBySupplierId(long supplierId) {
		logger.debug("deleteScheduleMagicCubeBySupplierId(" + supplierId + ")");
		return magicCubeDao.deleteScheduleMagicCubeBySupplierId(supplierId);
	}

	@Override
	public ScheduleMagicCubeDTO getScheduleMagicCubeBySupplierId(long supplierId) {
		logger.debug("getScheduleMagicCubeBySupplierId(" + supplierId + ")");

		ScheduleMagicCube mc = magicCubeDao.getScheduleMagicCubeBySupplierId(supplierId);
		if (mc == null) {
			return null;
		}

		ScheduleMagicCubeDTO mcDTO = new ScheduleMagicCubeDTO();
		mcDTO.setMagicCube(mc);

		return mcDTO;
	}
}
