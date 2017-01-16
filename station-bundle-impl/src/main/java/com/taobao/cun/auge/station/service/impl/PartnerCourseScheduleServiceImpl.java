package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.station.bo.PartnerCourseScheduleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleReflectDto;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleShowDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.service.PartnerCourseScheduleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
/**
 * 
 * @author yi.shaoy
 *
 */
@Service("partnerCourseScheduleService")
@HSFProvider(serviceInterface = PartnerCourseScheduleService.class)
public class PartnerCourseScheduleServiceImpl implements PartnerCourseScheduleService{

	@Autowired
	PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	PartnerCourseScheduleBO partnerCourseScheduleBO;
	
	@Override
	public Boolean hasSignCourseScheduleProtocol(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerProtocolRelDto dto = partnerProtocolRelBO
				.getPartnerProtocolRelDto(ProtocolTypeEnum.COURSE_SCHEDULE,
						taobaoUserId, PartnerProtocolRelTargetTypeEnum.PARTNER);
		if (dto == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void signCourseScheduleProtocol(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerProtocolRelDto dto = partnerProtocolRelBO
				.getPartnerProtocolRelDto(ProtocolTypeEnum.COURSE_SCHEDULE,
						taobaoUserId, PartnerProtocolRelTargetTypeEnum.PARTNER);
		if (dto != null) {
			// 已经签署过
			return;
		}
		partnerProtocolRelBO.signProtocol(taobaoUserId, taobaoUserId,
				ProtocolTypeEnum.COURSE_SCHEDULE, new Date(), new Date(), null,
				String.valueOf(taobaoUserId),
				PartnerProtocolRelTargetTypeEnum.PARTNER);
	}

	@Override
	public void reflectCourseSchedule(PartnerCourseScheduleReflectDto dto) {
		partnerCourseScheduleBO.reflectCourseSchedule(dto);
		
	}

	@Override
	public List<PartnerCourseScheduleShowDto> getCourseSchedule(
			Long taobaoUserId, Date gmtStart, Date gmtEnd) {
		Assert.notNull(taobaoUserId);
		if(!hasSignCourseScheduleProtocol(taobaoUserId)){
			throw new AugeServiceException("您还未报名，无法参与课程播放");
		}
		return partnerCourseScheduleBO.getCourseSchedule(
				 taobaoUserId,  gmtStart,  gmtEnd);
	}

}
