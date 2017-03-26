package com.taobao.cun.auge.meeting;

import java.util.Date;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.meeting.dto.MeetingDto;
import com.taobao.cun.auge.meeting.dto.MeetingQueryCondition;

/**
 * 会议服务
 * @author yi.shaoy
 *
 */
public interface MeetingService {

	public String saveMeeting(MeetingDto meeting);
	
	public void cancelMeeting(String meetingCode,String operator);
	
	public PageDto<MeetingDto> queryMeetingForClient(MeetingQueryCondition condition);
	
	public MeetingDto attempMeeting(String userId,String userType,String meetingCode);
	
	public void closeMeeting(String meetingCode,String operator,Date gmtEnd);
	
	public Long getFutureMeetingCount(String userId);
}
