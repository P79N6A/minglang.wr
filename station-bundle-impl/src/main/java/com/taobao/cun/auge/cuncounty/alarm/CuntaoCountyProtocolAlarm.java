package com.taobao.cun.auge.cuncounty.alarm;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyGovContractBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyProtocolRiskEnum;
import com.taobao.cun.auge.cuncounty.tag.Publisher;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 协议预警
 *
 * 根据县点上的标做预警
 *
 * @author chengyu.zhoucy
 */
@Component("cuntaoCountyProtocolAlarm")
public class CuntaoCountyProtocolAlarm extends AbstractCuntaoCountyAlarm {
    private final static String RENEW_ALARM = "您所负责的【%s】合同于%d天到期，请及时进行处理";
    private final static String NEW_ALARM = "您所负责的【%s】未签订合同，请您判断是否同政府有合作关系，若有及时到ORG补全政府协议信息";
    private final static String FILL_ALARM = "您所负责的【%s】疑似未签协议，请您及时到ORG补充政府协议编号及协议时间";
    private final static String EXPIRE_ALARM = "您所负责的【%s】合同已过期%d天，请及时进行处理";
    @Resource
    private CuntaoCountyGovContractBo cuntaoCountyGovContractBo;
    @Resource
    private Publisher publisher;

    @Override
    public void alarm() {
        Flux.create(publisher::alarmPublish)
                .onErrorContinue((e,o)->logger.error(e.getMessage(), e))
                .map(t->t.getT2())
                .parallel(3)
                .map(this::buildProtocolAlarmMsg)
                .subscribe(this::doAlarm);
    }

    private Optional<AppMsgPushInfoDto> buildProtocolAlarmMsg(CuntaoCountyListItem item){
        CuntaoCountyProtocolRiskEnum cuntaoCountyProtocolRiskEnum = cuntaoCountyGovContractBo.checkContractRisk(item.getId());
        if(cuntaoCountyProtocolRiskEnum != null){
            if(CuntaoCountyProtocolRiskEnum.protocolNotExists.getCode().equals(cuntaoCountyProtocolRiskEnum.getCode())){
                return buildMsg(item, String.format(NEW_ALARM, item.getName()));
            }
            if(CuntaoCountyProtocolRiskEnum.protocolMaybeNotExists.getCode().equals(cuntaoCountyProtocolRiskEnum.getCode())){
                return buildMsg(item, String.format(FILL_ALARM, item.getName()));
            }
            if(CuntaoCountyProtocolRiskEnum.protocolWillExpire.getCode().equals(cuntaoCountyProtocolRiskEnum.getCode())){
                return buildMsg(item, String.format(RENEW_ALARM, item.getName(), day(item)));
            }
            if(CuntaoCountyProtocolRiskEnum.protocolExpire.getCode().equals(cuntaoCountyProtocolRiskEnum.getCode())){
                return buildMsg(item, String.format(EXPIRE_ALARM, item.getName(), -1 * day(item)));
            }
        }
        return Optional.empty();
    }

    private long day(CuntaoCountyListItem item){
        LocalDate today = LocalDate.now();
        LocalDate protocolLocalDate = LocalDate.parse(item.getProtocolEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return protocolLocalDate.toEpochDay() - today.toEpochDay();
    }
}
