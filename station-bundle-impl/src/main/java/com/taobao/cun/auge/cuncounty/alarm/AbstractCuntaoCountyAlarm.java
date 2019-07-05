package com.taobao.cun.auge.cuncounty.alarm;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.cun.crius.event.ExtEvent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractCuntaoCountyAlarm implements CuntaoCountyAlarm{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CuntaoUserOrgService cuntaoUserOrgService;
    @Resource
    private AppBizLogBo appBizLogBo;

    protected Optional<AppMsgPushInfoDto> buildMsg(CuntaoCountyListItem item, String textContent){
        List<String> empIds = getUserIds(item);
        if(empIds.isEmpty()){
            return Optional.empty();
        }
        AppMsgPushInfoDto appMsgPushInfoDto = new AppMsgPushInfoDto();
        AppMsgPushInfoDto infoDto = new AppMsgPushInfoDto();
        infoDto.setAction("all");
        infoDto.setAppId("cuntaoCRM");
        AppMsgPushInfoDto.AppMsgPushContent content = infoDto.new AppMsgPushContent();
        content.setBizId(item.getId());
        content.setTitle(textContent);
        content.setPushTitle("请注意跟进");
        content.setContent("请注意跟进");
        infoDto.setContent(content);
        infoDto.setMsgType("cuntaoCRM" + "govbusiness");
        infoDto.setMsgTypeDetail("govbusiness");
        infoDto.setReceivers(Lists.newArrayList(empIds));
        infoDto.setReceiverType("EMPIDS");
        infoDto.setSender(0L);
        return Optional.of(appMsgPushInfoDto);
    }

    private List<String> getUserIds(CuntaoCountyListItem item){
        List<CuntaoUserOrgVO> users = cuntaoUserOrgService.getCuntaoOrgUsers(Lists.newArrayList(item.getOrgId()), Lists.newArrayList(UserRoleEnum.COUNTY_LEADER.getCode()));
        if(CollectionUtils.isNotEmpty(users)){
            return users.stream().map(u->u.getLoginId()).distinct().collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

    protected void doAlarm(Optional<AppMsgPushInfoDto> appMsgPushInfoDto){
        appMsgPushInfoDto.ifPresent(msg->{
            SimpleAppBizLog simpleAppBizLog = new SimpleAppBizLog();
            simpleAppBizLog.setBizKey(msg.getContent().getBizId());
            simpleAppBizLog.setBizType("push_msg");
            simpleAppBizLog.setCreator("sys");
            simpleAppBizLog.setState("NEW");
            simpleAppBizLog.setMessage(msg.getContent().getTitle());
            appBizLogBo.addLog(simpleAppBizLog);
            //EventDispatcherUtil.dispatch("CUN_APP_STATION_INSPECTION_MSG_PUSH", new ExtEvent(JSON.toJSONString(msg)));
        });
    }
}
