package com.taobao.cun.auge.alilang;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.alilang.dto.AlilangForceInstallConfigDto;
import com.taobao.cun.auge.alilang.dto.AlilangProfileDto;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.diamond.client.Diamond;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.CollectionUtils;

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
            }
        }
        if (partnerInstance != null && partnerInstance.getPartnerId() != null) {
            Partner partner = partnerBO.getPartnerById(partnerInstance.getPartnerId());
            if (partner != null) {
                profile.setUserName(partner.getName());
            }
        }
        return profile;
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

    private boolean isForceInstallAlilang(PartnerStationRel partnerInstance) {
        //是否强制安装阿里郎
        try {
            String alilangForceInstallConfig = Diamond.getConfig(ALILANG_FORCE_INSTALL_DATAID, ALILANG_TOPIC_GROUP, 3000);
            if (StringUtils.isBlank(alilangForceInstallConfig)) {
                return false;
            }
            AlilangForceInstallConfigDto config = JSON.parseObject(alilangForceInstallConfig, AlilangForceInstallConfigDto.class);
            Long taobaoUserId = partnerInstance.getTaobaoUserId();
            List<Long> whiteList = config.getWhiteList();
            List<Long> forceInstallOrgList = config.getForceInstallOrgIdList();
            if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(taobaoUserId)) {
                return false;
            }
            if (CollectionUtils.isEmpty(forceInstallOrgList)) {
                return false;
            }
            Station station = stationBO.getStationById(partnerInstance.getStationId());
            Long orgId = station.getApplyOrg();
            CuntaoOrgDto currentOrg = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
            while (null != currentOrg ) {
                if (forceInstallOrgList.contains(currentOrg.getId())) {
                    return true;
                }
                currentOrg = currentOrg.getParent();
            }
        } catch (Exception e) {
            logger.error(ERROR_MSG + "isForceInstallAlilang", e);
        }
        return false;
    }

}