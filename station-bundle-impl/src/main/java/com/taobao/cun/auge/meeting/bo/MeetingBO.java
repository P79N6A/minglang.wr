package com.taobao.cun.auge.meeting.bo;

import com.taobao.cun.auge.meeting.dto.MeetingDto;

public interface MeetingBO {

	public String createMeeting(MeetingDto meeting);
	
	public void modifyMeeting(MeetingDto meeting);
}
