package com.taobao.cun.auge.cuncounty.alarm;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;
import com.taobao.cun.auge.cuncounty.tag.CountyTagUtils;
import com.taobao.cun.auge.cuncounty.tag.Publisher;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
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

    @Resource
    private Publisher publisher;

    @Override
    public void alarm() {
        Flux.generate(publisher::publish)
                .map(t->t.getT2())
                .filter(this::filter)
                .parallel(3)
                .map(this::buildProtocolAlarmMsg)
                .subscribe(this::doAlarm);
    }

    private boolean filter(CuntaoCountyListItem item){
        return CountyTagUtils.containAlarmTags(item.getTags());
    }

    private Optional<AppMsgPushInfoDto> buildProtocolAlarmMsg(CuntaoCountyListItem item){
        Optional<CuntaoCountyTagEnum> optional = CountyTagUtils.getProtocolTag(item.getTags());
        if(optional.isPresent()){
            CuntaoCountyTagEnum tag = optional.get();
            if(CuntaoCountyTagEnum.protocolNotExists.getCode().equals(tag.getCode())){
                return buildMsg(item, String.format(NEW_ALARM, item.getCountyName()));
            }
            if(CuntaoCountyTagEnum.protocolMaybeNotExists.getCode().equals(tag.getCode())){
                return buildMsg(item, String.format(FILL_ALARM, item.getCountyName()));
            }
            if(CuntaoCountyTagEnum.protocolWillExpire.getCode().equals(tag.getCode())){
                return buildMsg(item, String.format(RENEW_ALARM, item.getCountyName(), day(item)));
            }
        }
        return Optional.empty();
    }

    private int day(CuntaoCountyListItem item){
        LocalDate today = LocalDate.now();
        LocalDate protocolLocalDate = LocalDate.parse(item.getProtocolEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Period period = Period.between(protocolLocalDate, today);
        return period.getDays();
    }
}
