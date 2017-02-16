package com.taobao.cun.auge.meeting.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.dal.domain.PartnerMeeting;
import com.taobao.cun.auge.dal.domain.PartnerMeetingAttemp;
import com.taobao.cun.auge.dal.domain.PartnerMeetingAttempExample;
import com.taobao.cun.auge.dal.domain.PartnerMeetingExample;
import com.taobao.cun.auge.dal.domain.PartnerMeetingExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerMeetingAttempMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMeetingMapper;
import com.taobao.cun.auge.meeting.dto.MeetingAttempDto;
import com.taobao.cun.auge.meeting.dto.MeetingDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
@Component("meetingBO")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class MeetingBOImpl implements MeetingBO{

	@Autowired
	PartnerMeetingMapper partnerMeetingMapper;
	
	@Autowired
	PartnerMeetingAttempMapper partnerMeetingAttempMapper;
	
	
	@Override
	public String createMeeting(MeetingDto meeting) {
		if(StringUtils.isEmpty(meeting.getMeetingCode())){
			throw new AugeServiceException("meetingCode is null");
		}
		PartnerMeeting meet=new PartnerMeeting();
		BeanUtils.copyProperties(meeting, meet);
		meet.setGmtCreate(new Date());
		meet.setGmtModified(new Date());
		meet.setCreator(meeting.getOwnerId());
		meet.setModifier(meeting.getOwnerId());
		meet.setIsDeleted("n");
		partnerMeetingMapper.insert(meet);
		initMeetingAttemps(meeting.getMeetingAttemps(),meet.getId(),meeting.getOwnerId());
		return meet.getMeetingCode();
	}

	@Override
	public void modifyMeeting(MeetingDto meeting) {
		if(StringUtils.isEmpty(meeting.getMeetingCode())){
			throw new AugeServiceException("meetingCode is null");
		}
		PartnerMeetingExample example = new PartnerMeetingExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andMeetingCodeEqualTo(meeting.getMeetingCode());
		List<PartnerMeeting> meets=partnerMeetingMapper.selectByExample(example);
		if(meets.size()==0){
			throw new AugeServiceException("meet not find:"+meeting.getMeetingCode());
		}
		PartnerMeeting meet=meets.get(0);
		if(meet.getOwnerId().equals(meeting.getOwnerId())){
			throw new AugeServiceException("不是会议发起人，无权限修改");
		}
		BeanUtils.copyProperties(meeting, meet);
		meet.setGmtModified(new Date());
		partnerMeetingMapper.updateByPrimaryKeySelective(meet);
		//批量删除原先邀约人员
		PartnerMeetingAttempExample ex=new PartnerMeetingAttempExample();
		ex.createCriteria().andIsDeletedEqualTo("n").andMeetingIdEqualTo(meet.getId());
		List<PartnerMeetingAttemp> attemps=partnerMeetingAttempMapper.selectByExample(ex);
		for(PartnerMeetingAttemp attemp:attemps){
			attemp.setIsDeleted("y");
			attemp.setGmtModified(new Date());
			partnerMeetingAttempMapper.updateByPrimaryKey(attemp);
		}
		initMeetingAttemps(meeting.getMeetingAttemps(),meet.getId(),meeting.getOwnerId());
	}

	private void initMeetingAttemps(List<MeetingAttempDto> meetingAttemps,Long meetingId,String operator){
		for(MeetingAttempDto attemp:meetingAttemps){
			PartnerMeetingAttemp att=new PartnerMeetingAttemp();
			BeanUtils.copyProperties(attemp, att);
			att.setGmtCreate(new Date());
			att.setGmtModified(new Date());
			att.setCreator(operator);
			att.setModifier(operator);
			att.setIsDeleted("n");
			att.setStatus("INVITED");
			att.setMeetingId(meetingId);
			partnerMeetingAttempMapper.insert(att);
		}
	}
}
