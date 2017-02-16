package com.taobao.cun.auge.meeting.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.meeting.MeetingService;
import com.taobao.cun.auge.meeting.bo.MeetingBO;
import com.taobao.cun.auge.meeting.dto.MeetingAttempDto;
import com.taobao.cun.auge.meeting.dto.MeetingDto;
import com.taobao.cun.auge.meeting.dto.MeetingQueryCondition;
import com.taobao.cun.auge.meeting.util.Client;
import com.taobao.cun.auge.meeting.util.Constants;
import com.taobao.cun.auge.meeting.util.HttpHeader;
import com.taobao.cun.auge.meeting.util.HttpSchema;
import com.taobao.cun.auge.meeting.util.Method;
import com.taobao.cun.auge.meeting.util.Request;
import com.taobao.cun.auge.meeting.util.Response;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("meetingService")
@HSFProvider(serviceInterface = MeetingService.class)
@SuppressWarnings("rawtypes")
public class MeetingServiceImpl implements MeetingService {
	private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	AppResourceBO appResourceBO;

	@Autowired
	MeetingBO meetingBO;
	
	@Override
	public String saveMeeting(MeetingDto meeting) {
		try{
		validateMeetingParam(meeting);
		validateAttempers(meeting);
		if(StringUtils.isEmpty(meeting.getMeetingCode())){
			//新建会议
			Map meetingResult=createRemoteMeeting(meeting.getTitle());
			meeting.setMeetingCode(String.valueOf(meetingResult.get("conferenceCode")));
			meeting.setMeetingPassword(String.valueOf(meetingResult.get("passwd")));
			meeting.setMeetingUrl(String.valueOf(meetingResult.get("conferenceUrl")));
			meeting.setMeetingUuid(String.valueOf(meetingResult.get("conferenceUuid")));
			return meetingBO.createMeeting(meeting);
		}else{
			//修改会议
			meetingBO.modifyMeeting(meeting);
			return meeting.getMeetingCode();
		}
		}catch(Exception e){
			logger.error("saveMeeting error:"+meeting.getMeetingCode(),e);
			throw new AugeServiceException("会议保存失败."+e.getMessage());
		}
	}
	
	private void validateMeetingParam(MeetingDto meeting){
		if(meeting==null||meeting.getMeetingAttemps()==null||meeting.getMeetingAttemps().size()==0){
			throw new AugeServiceException("param is null");
		}
		if(StringUtils.isEmpty(meeting.getMeetingType())){
			throw new AugeServiceException("meetingType is null");
		}
		if(StringUtils.isEmpty(meeting.getOwnerId())){
			throw new AugeServiceException("ownerId is null");
		}
		if(StringUtils.isEmpty(meeting.getOwnerType())){
			throw new AugeServiceException("ownerType is null");
		}
		if(StringUtils.isEmpty(meeting.getTitle())){
			throw new AugeServiceException("title is null");
		}
		if(meeting.getGmtStart()==null){
			throw new AugeServiceException("gmtStart is null");
		}
		if(meeting.getGmtEnd()==null){
			throw new AugeServiceException("gmtEnd is null");
		}
		for(MeetingAttempDto attemp:meeting.getMeetingAttemps()){
			if(StringUtils.isEmpty(attemp.getAttemperId())){
				throw new AugeServiceException("attemperId is null");
			}
			if(StringUtils.isEmpty(attemp.getAttemperType())){
				throw new AugeServiceException("attemperType is null");
			}
		}
	}
	
	private void validateAttempers(MeetingDto meeting){
		//目前只有村小二会议，
		//验证参与者是否都是服务中或者装修中的村小二
		List<Long> userIds=new ArrayList<Long>();
		List<String> instanceType=new ArrayList<String>();
		List<String> states=new ArrayList<String>();
		for(MeetingAttempDto dto:meeting.getMeetingAttemps()){
			userIds.add(new Long(dto.getAttemperId()));
		}
		instanceType.add("TP");
		states.add(PartnerInstanceStateEnum.DECORATING.getCode());
		states.add(PartnerInstanceStateEnum.SERVICING.getCode());
		List<PartnerStationRel> rels=partnerInstanceBO.getBatchActivePartnerInstance(userIds, instanceType, states);
		l1:for(MeetingAttempDto dto:meeting.getMeetingAttemps()){
			for(PartnerStationRel rel:rels){
				if(rel.getTaobaoUserId().compareTo(new Long(dto.getAttemperId()))==0){
					continue l1;
				}
			}
			throw new AugeServiceException("该id无法找到有效村小二："+dto.getAttemperId());
		}
	}

	private Map createRemoteMeeting(String title) {
		String path=appResourceBO.queryAppValueNotAllowNull("PARTNER_MEETING","path");
        String appId=appResourceBO.queryAppValueNotAllowNull("PARTNER_MEETING","appId");
        String appKey=appResourceBO.queryAppValueNotAllowNull("PARTNER_MEETING","appKey");
		String host=appResourceBO.queryAppValueNotAllowNull("PARTNER_MEETING","host");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
		List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();
		Request request = new Request(Method.POST_STRING, HttpSchema.HTTP
				+ host, path, appId,appKey,
				Constants.DEFAULT_TIMEOUT);
		request.setHeaders(headers);
		request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

		Map<String, String> querys = new HashMap<String, String>();
		querys.put("clientappid", appId);
		querys.put("conferencename", title);
		querys.put("ts", String.valueOf(System.currentTimeMillis()));
		request.setQuerys(querys);
		// 调用服务端
		try {
			Response response = Client.execute(request);
			Map result = JSON.parseObject(response.getBody(), Map.class);
			if((Boolean)result.get("success")!=true){
				throw new AugeServiceException("创建会议失败：" +result.get("errorMsg"));
			}
			return (Map) result.get("data");
		} catch (Exception e) {
			throw new AugeServiceException("创建会议失败：" + e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception{
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
		List<String> CUSTOM_HEADERS_TO_SIGN_PREFIX = new ArrayList<String>();
		Request request = new Request(Method.POST_STRING, HttpSchema.HTTP
				+ "100.69.195.131", "/alimeetingvc/api/tp/video/conferenceCreate.json", "demo_test","d574c6ab-5123-4498-abdf-7be38b717b7a",
				Constants.DEFAULT_TIMEOUT);
		request.setHeaders(headers);
		request.setSignHeaderPrefixList(CUSTOM_HEADERS_TO_SIGN_PREFIX);

		Map<String, String> querys = new HashMap<String, String>();
		querys.put("clientappid", "demo_test");
		querys.put("conferencename", "test");
		querys.put("ts", String.valueOf(System.currentTimeMillis()));
		request.setQuerys(querys);
		// 调用服务端
		try {
			Response response = Client.execute(request);
			System.out.println(response.getBody());
			Map result = JSON.parseObject(response.getBody(), Map.class);
			if((Boolean)result.get("success")!=true){
				throw new AugeServiceException("创建会议失败：" +result.get("exception"));
			}
			System.out.println((Map) result.get("data")); 
		} catch (Exception e) {
			throw new AugeServiceException("创建会议失败：" + e.getMessage());
		}
	}
	@Override
	public void cancelMeeting(String meetingCode,String operator){
       if(StringUtils.isEmpty(meetingCode)||StringUtils.isEmpty(operator)){
			throw new AugeServiceException("取消会议失败：param is null");
       }
	}

	@Override
	public List<MeetingDto> queryMeetingForClient(MeetingQueryCondition condition) {
		try {
			if (StringUtils.isEmpty(condition.getUserId())
					|| StringUtils.isEmpty(condition.getUserType())
					|| StringUtils.isEmpty(condition.getQueryType())) {
				throw new AugeServiceException("param is null");
			}
			Date now = new Date();
			if (StringUtils.isNotEmpty(condition.getMeetingCode())) {
				// 查询具体会议
				return meetingBO.queryMeetingsByCondition(
						condition.getMeetingCode(), null, null, null, null,
						condition.getUserId());
			} else if ("history".equals(condition.getQueryType())) {
				// 查询历史会议
				return meetingBO.queryMeetingsByCondition(
						condition.getMeetingCode(), null, null, now, null,
						condition.getUserId());
			} else if ("future".equals(condition.getQueryType())) {
				// 查询未来的会议
				return meetingBO.queryMeetingsByCondition(
						condition.getMeetingCode(), null, null, null, now,
						condition.getUserId());
			} else if ("today".equals(condition.getQueryType())) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(now);
				calendar.add(calendar.DATE, +1);
				Date tommorow = calendar.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateToday = formatter.format(now);
				String dateTommorow = formatter.format(tommorow);
				return meetingBO.queryMeetingsByCondition(
						condition.getMeetingCode(), formatter.parse(dateToday),
						formatter.parse(dateTommorow), null, now,
						condition.getUserId());
			}
			return new ArrayList<MeetingDto>();
		} catch (Exception e) {
			logger.error("queryMeeting error:", e);
			throw new AugeServiceException("会议查询失败." + e.getMessage());
		}
	}

	@Override
	public MeetingDto attempMeeting(String userId, String userType,
			String meetingCode) {
		return meetingBO.attempMeeting(userId, userType, meetingCode);
	}

}
