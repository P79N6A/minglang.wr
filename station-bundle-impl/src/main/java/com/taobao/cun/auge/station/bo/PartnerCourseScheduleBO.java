package com.taobao.cun.auge.station.bo;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerCourseSchedule;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleReflectDto;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleShowDto;

public interface PartnerCourseScheduleBO {
	
	public void reflectCourseSchedule(PartnerCourseScheduleReflectDto dto);

	public List<PartnerCourseScheduleShowDto> getCourseSchedule(
			Long taobaoUserId, Date gmtStart, Date gmtEnd);
			
	public List<PartnerCourseSchedule> getScheduleByCourseCode(String courseCode);
}
