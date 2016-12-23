package com.taobao.cun.auge.alilang;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.crius.exam.dto.UserExamCalDto;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("alilangHomePageService")
@HSFProvider(serviceInterface= AlilangHomePageService.class)
public  class AlilangHomePageServiceImpl implements AlilangHomePageService {

	
	@Autowired
	private AlilangTopicBO alilangBO;
	@Autowired
	private ExamUserDispatchService examUserDispatchService;
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
	private StationBO stationBO;
	@Autowired
	private PartnerBO partnerBO;
	@Override
	public List<AlilangTopicDto> getTopics() {
		return alilangBO.getTopics();
	}




	@Override
	public UserExamProfile getUserExamProfile(Long taobaoUserId) {
		UserExamCalDto examDto = examUserDispatchService.queryUserExamCal(taobaoUserId).getResult();
		UserExamProfile profile = new UserExamProfile();
		if(examDto != null){
			profile.setUnFinishExamCount(examDto.getUnJoinExamNums());
			profile.setUnFinishExamNames(examDto.getUnJoinExamNames());
		}
		return profile;
	}



	@Override
	public UserProfile getUserProfile(Long taobaoUserId) {
		UserProfile profile = new UserProfile();
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance != null && partnerInstance.getServiceBeginTime() != null){
			Date start = partnerInstance.getServiceBeginTime();
			Integer joinDays = Days.daysBetween(new DateTime(start), new DateTime(new Date())).getDays();
			profile.setJoinDays(joinDays+1);
		}
		if(partnerInstance != null && partnerInstance.getStationId() != null){
			Station station = stationBO.getStationById(partnerInstance.getStationId());
			if(station != null){
				profile.setStationName(station.getName());
			}
		}
		if(partnerInstance != null && partnerInstance.getPartnerId() != null){
			Partner partner = partnerBO.getPartnerById(partnerInstance.getPartnerId());
			if(partner != null){
				profile.setUserName(partner.getName());
			}
		}
		return profile;
	}




   
}
