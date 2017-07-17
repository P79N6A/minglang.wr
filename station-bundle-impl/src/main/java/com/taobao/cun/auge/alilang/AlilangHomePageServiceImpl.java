package com.taobao.cun.auge.alilang;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.alilang.dto.AlilangForceInstallConfigDto;
import com.taobao.cun.auge.alilang.dto.AlilangProfileDto;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.crius.exam.dto.UserExamCalDto;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.diamond.client.Diamond;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;


@Service("alilangHomePageService")
@HSFProvider(serviceInterface = AlilangHomePageService.class)
public class AlilangHomePageServiceImpl implements AlilangHomePageService {
    private static final Logger logger = LoggerFactory.getLogger(AlilangHomePageServiceImpl.class);
    private static final String ALILANG_FORCE_INSTALL_DATAID = "com.taobao.cun:alilangForceInstall.json";
    private static final String ALILANG_TOPIC_GROUP = "DEFAULT_GROUP";
    private static final String ERROR_MSG = "ALILANG_HOMEPAGE_SERVICE_ERROR|";


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

    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;


    @Override
    public List<AlilangTopicDto> getTopics() {
        return alilangBO.getTopics();
    }


    @Override
    public UserExamProfile getUserExamProfile(Long taobaoUserId) {
        UserExamCalDto examDto = examUserDispatchService.queryUserExamCal(taobaoUserId).getResult();
        UserExamProfile profile = new UserExamProfile();
        if (examDto != null) {
            profile.setUnFinishExamCount(examDto.getUnJoinExamNums());
            profile.setUnFinishExamNames(examDto.getUnJoinExamNames());
        }
        return profile;
    }


    @Override
    public UserProfile getUserProfile(Long taobaoUserId) {
        UserProfile profile = new UserProfile();
        PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (partnerInstance != null && partnerInstance.getServiceBeginTime() != null) {
            Date start = partnerInstance.getServiceBeginTime();
            Integer joinDays = Days.daysBetween(new DateTime(start), new DateTime(new Date())).getDays();
            profile.setJoinDays(joinDays + 1);
        }
        if (partnerInstance != null && partnerInstance.getStationId() != null) {
            Station station = stationBO.getStationById(partnerInstance.getStationId());
            if (station != null) {
                profile.setStationName(station.getName());
                profile.setStationId(station.getId());
            }
        }
        if (partnerInstance != null && partnerInstance.getPartnerId() != null) {
            Partner partner = partnerBO.getPartnerById(partnerInstance.getPartnerId());
            if (partner != null) {
                profile.setUserName(partner.getName());
                profile.setBirthday(partner.getBirthday());
                profile.setFlowerName(partner.getFlowerName());
            }
        }
        return profile;
    }
    
    @Override
	public UserProfile getUserProfileByAlilangUserId(String alilangUserId) {
		if(StringUtils.isEmpty(alilangUserId)){
			return null;
		}
		Partner partner=partnerBO.getPartnerByAliLangUserId(alilangUserId);
		if(partner!=null){
			return getUserProfile(partner.getTaobaoUserId());
		}else{
			return new UserProfile();
		}
	}

    @Override
    public AlilangProfileDto getAlilangProfile(Long taobaoUserId) {
        AlilangProfileDto profile = new AlilangProfileDto();
        profile.setTaobaoUserId(taobaoUserId);
        try {
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            Partner partner = partnerBO.getPartnerById(partnerInstance.getPartnerId());
            profile.setAlilangUserId(partner.getAlilangUserId());
            boolean forceInstallAlilang = isForceInstallAlilang(partnerInstance);
            profile.setForceInstallAlilang(forceInstallAlilang);
        } catch (Exception e) {
            logger.error(ERROR_MSG + "getAlilangProfile", e);
        }
        return profile;
    }


    private boolean isForceInstallAlilang(PartnerStationRel partnerInstance) throws Exception {
        //是否强制安装阿里郎
        try {
        	if(!PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerInstance.getState())){
        		//非服务中的，不强绑定
        		return false;
        	}
            String alilangForceInstallConfig = Diamond.getConfig(ALILANG_FORCE_INSTALL_DATAID, ALILANG_TOPIC_GROUP, 3000);
            if (StringUtils.isBlank(alilangForceInstallConfig)) {
                return false;
            }
            AlilangForceInstallConfigDto config = JSON.parseObject(alilangForceInstallConfig, AlilangForceInstallConfigDto.class);
            Long taobaoUserId = partnerInstance.getTaobaoUserId();
            List<Long> whiteList = config.getWhiteList();
            List<Long> forceInstallOrgList = config.getForceInstallOrgIdList();
            List<String> forceInstallPartnerTypeList = config.getForceInstallPartnerTypeList();
            // 不在本次强制安装的合伙人类型列表里面
            if (CollectionUtils.isEmpty(forceInstallPartnerTypeList) || !forceInstallPartnerTypeList.contains(StringUtils.upperCase(partnerInstance.getType()))) {
                return false;
            }
            //白名单
            if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(taobaoUserId)) {
                return false;
            }
            //是否全国强制安装
            if (config.isForceAllPartnerInstall()) {
                return true;
            }
            if (CollectionUtils.isEmpty(forceInstallOrgList)) {
                return false;
            }
            Station station = stationBO.getStationById(partnerInstance.getStationId());
            Long orgId = station.getApplyOrg();
            CuntaoOrgDto currentOrg = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
            while (null != currentOrg) {
                if (forceInstallOrgList.contains(currentOrg.getId())) {
                    return true;
                }
                currentOrg = currentOrg.getParent();
            }
            return false;
        } catch (Exception e) {
            logger.error(ERROR_MSG + "isForceInstallAlilang", e);
            throw e;
        }
       
    }


	@Override
	public List<UserProfile> queryUserForMeeting(String name, Long taobaoUserId) {
		if (taobaoUserId == null || taobaoUserId == 0l) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"taobaoUserId is null");
		}
		PartnerStationRel rel = partnerInstanceBO
				.getActivePartnerInstance(taobaoUserId);
		if (rel == null) {
			return null;
		}
		if (StringUtils.isEmpty(name)) {
			Station station = stationBO.getStationById(rel.getStationId());
			// 默认查询同一县的村小二
			return partnerInstanceBO.queryUserProfileForAlilangMeeting(
					station.getApplyOrg(), null);
		} else {
			// 根据花名或姓名搜索全国村小二
			return partnerInstanceBO.queryUserProfileForAlilangMeeting(null,
					name);
		}
	}


	

}