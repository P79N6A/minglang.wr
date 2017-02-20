package com.taobao.cun.auge.meeting.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.PartnerMeeting;
import com.taobao.cun.auge.dal.domain.PartnerMeetingAttemp;
import com.taobao.cun.auge.dal.domain.PartnerMeetingAttempExample;
import com.taobao.cun.auge.dal.domain.PartnerMeetingExample;
import com.taobao.cun.auge.dal.domain.PartnerMeetingExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerMeetingAttempMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMeetingMapper;
import com.taobao.cun.auge.meeting.dto.MeetingAttempDto;
import com.taobao.cun.auge.meeting.dto.MeetingDto;
import com.taobao.cun.auge.meeting.enums.MeetingAttempStatusEnum;
import com.taobao.cun.auge.meeting.enums.MeetingStatusEnum;
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
		meet.setStatus(MeetingStatusEnum.NORMAL.getCode());
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
		if(!meet.getOwnerId().equals(meeting.getOwnerId())){
			throw new AugeServiceException("不是会议发起人，无权限修改");
		}
		meet.setGmtStart(meeting.getGmtStart());
		meet.setGmtEnd(meeting.getGmtEnd());
		meet.setTitle(meeting.getTitle());
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
			att.setStatus(MeetingAttempStatusEnum.INVITED.getCode());
			att.setMeetingId(meetingId);
			partnerMeetingAttempMapper.insert(att);
		}
	}

	@Override
	public void cancelMeeting(String meetingCode, String operator) {
		PartnerMeetingExample example = new PartnerMeetingExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andMeetingCodeEqualTo(meetingCode);
		List<PartnerMeeting> meets=partnerMeetingMapper.selectByExample(example);
		if(meets.size()==0){
			throw new AugeServiceException("meet not find:"+meetingCode);
		}
		PartnerMeeting meet=meets.get(0);
		if(!meet.getOwnerId().equals(operator)){
			throw new AugeServiceException("不是会议发起人，无权限取消");
		}
		meet.setIsDeleted("y");
		meet.setGmtModified(new Date());
		partnerMeetingMapper.updateByPrimaryKey(meet);
	}


	public PageDto<MeetingDto> queryMeetingsByCondition(String meetingCode,Date gmtStartMax,Date gmtStartMin,Date gmtEndMax,Date gmtEndMin,String userId,int pageNum,int pageSize,String orderType) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("gmtStartMax", gmtStartMax);
		param.put("gmtStartMin", gmtStartMin);
		param.put("gmtEndMax", gmtEndMax);
		param.put("gmtEndMin", gmtEndMin);
		param.put("meetingCode", meetingCode);
		param.put("attemperId", userId);
		param.put("pageS", pageSize);
		param.put("pageN", pageNum);
		param.put("orderType", orderType);
		int count=partnerMeetingMapper.queryMeetingsCountByCondition(param);
		List<PartnerMeeting> meetings=partnerMeetingMapper.queryMeetingsByCondition(param);
		List<MeetingDto> resultList=new ArrayList<MeetingDto>();
		List<Long> meetingIds=new ArrayList<Long>();
		Map<Long,MeetingDto> resultMap=new HashMap<Long,MeetingDto>();
		for(PartnerMeeting meeting:meetings){
			MeetingDto dto=new MeetingDto();
			BeanUtils.copyProperties(meeting, dto);
			resultList.add(dto);
			meetingIds.add(meeting.getId());
			resultMap.put(meeting.getId(), dto);
		}
		//组装参与者信息
		if(meetingIds.size()>0){
			param.clear();
			param.put("meetingIds", meetingIds);
			List<MeetingAttempDto> attemps=partnerMeetingMapper.queryMeetingAttemps(param);
			for(MeetingAttempDto attemp:attemps){
				List<MeetingAttempDto> temp=resultMap.get(attemp.getMeetingId()).getMeetingAttemps();
				if(temp==null){
					temp=new ArrayList<MeetingAttempDto>();
					resultMap.get(attemp.getMeetingId()).setMeetingAttemps(temp);
				}
				temp.add(attemp);
			}
		}
		PageDto<MeetingDto> result=new PageDto<MeetingDto>();
		result.setTotal(count);
		result.setItems(resultList);
		return result;
	}

	@Override
	public MeetingDto attempMeeting(String userId, String userType,
			String meetingCode) {
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(userType)||StringUtils.isEmpty(meetingCode)){
			throw new AugeServiceException("param is null");
		}
		List<MeetingDto> meetings=queryMeetingsByCondition(meetingCode,null,null,null,null,userId,1,1,null).getItems();
		if(meetings.size()==0){
			throw new AugeServiceException("您未被邀请参加会议");
		}
		MeetingDto meeting=meetings.get(0);
		PartnerMeetingAttempExample ex=new PartnerMeetingAttempExample();
		ex.createCriteria().andIsDeletedEqualTo("n").andMeetingIdEqualTo(meeting.getId()).andAttemperIdEqualTo(userId);
		List<PartnerMeetingAttemp> attemps=partnerMeetingAttempMapper.selectByExample(ex);
		for(PartnerMeetingAttemp attemp:attemps){
			attemp.setGmtModified(new Date());
			attemp.setModifier(userId);
			attemp.setStatus(MeetingAttempStatusEnum.ATTEMPED.getCode());
			partnerMeetingAttempMapper.updateByPrimaryKey(attemp);
		}
		return meeting;
	}

	@Override
	public void closeMeeting(String meetingCode, String operator, Date gmtEnd) {
		PartnerMeetingExample example = new PartnerMeetingExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andMeetingCodeEqualTo(meetingCode);
		List<PartnerMeeting> meets=partnerMeetingMapper.selectByExample(example);
		if(meets.size()==0){
			throw new AugeServiceException("meet not find:"+meetingCode);
		}
		PartnerMeeting meet=meets.get(0);
		if(!meet.getOwnerId().equals(operator)){
			throw new AugeServiceException("不是会议发起人，无权限结束会议");
		}
		meet.setGmtEnd(gmtEnd);
		meet.setGmtModified(new Date());
		partnerMeetingMapper.updateByPrimaryKey(meet);
	}
}
