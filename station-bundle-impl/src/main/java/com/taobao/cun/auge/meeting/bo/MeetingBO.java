package com.taobao.cun.auge.meeting.bo;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.meeting.dto.MeetingDto;

public interface MeetingBO {

	public String createMeeting(MeetingDto meeting);
	
	public void modifyMeeting(MeetingDto meeting);
	
	public void cancelMeeting(String meetingCode,String operator);
	
	public List<MeetingDto> queryMeetingsByCondition(String meetingCode,
			Date gmtStartMax, Date gmtStartMin, Date gmtEndMax, Date gmtEndMin,
			String userId);
	
	public MeetingDto attempMeeting(String userId, String userType,
			String meetingCode);
}
