package com.taobao.cun.auge.meeting.bo;

import java.util.Date;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.meeting.dto.MeetingDto;

public interface MeetingBO {

	public String createMeeting(MeetingDto meeting);
	
	public void modifyMeeting(MeetingDto meeting);
	
	public void cancelMeeting(String meetingCode,String operator);
	
	public PageDto<MeetingDto> queryMeetingsByCondition(String meetingCode,
			Date gmtStartMax, Date gmtStartMin, Date gmtEndMax, Date gmtEndMin,
			String userId,int pageNum,int pageSize,String orderType);
	
	public MeetingDto attempMeeting(String userId, String userType,
			String meetingCode);
	
	public void closeMeeting(String meetingCode, String operator, Date gmtEnd);

}
