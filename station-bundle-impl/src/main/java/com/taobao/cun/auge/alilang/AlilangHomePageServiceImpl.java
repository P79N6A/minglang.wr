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



	public Integer getUnJoinExamCount(Long taobaoUserId){
		return examUserDispatchService.queryUserExamCal(taobaoUserId).getResult().getUnJoinExamNums();
	}


	@Override
	public Integer getUnFinishExamCount(Long taobaoUserId) {
		return examUserDispatchService.queryUserExamCal(taobaoUserId).getResult().getUnJoinExamNums();
	}



	@Override
	public UserProfile getUserProfile(Long taobaoUserId) {
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		Date start = partnerInstance.getServiceBeginTime();
		Integer joinDays = Days.daysBetween(new DateTime(start), new DateTime(new Date())).getDays();  
		Station station = stationBO.getStationById(partnerInstance.getStationId());
		Partner partner = partnerBO.getPartnerById(partnerInstance.getPartnerId());
		UserProfile profile = new UserProfile();
		profile.setJoinDays(joinDays);
		profile.setStationName(station.getName());
		profile.setUserName(partner.getName());
		return profile;
	}




   
}
