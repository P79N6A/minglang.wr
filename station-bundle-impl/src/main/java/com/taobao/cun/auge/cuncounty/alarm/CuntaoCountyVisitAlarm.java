package com.taobao.cun.auge.cuncounty.alarm;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.contactrecord.bo.CuntaoGovContactRecordQueryBo;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordDetailDto;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRecordWayEnum;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.tag.Publisher;
import com.taobao.cun.auge.platform.dto.AppMsgPushInfoDto;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 拜访预警
 *
 * 根据政府拜访记录做预警
 *
 * @author chengyu.zhoucy
 */
@Component("cuntaoCountyVisitAlarm")
public class CuntaoCountyVisitAlarm extends AbstractCuntaoCountyAlarm {
    private final static String VISIT_1_ALARM = "【%s】您本月未对政府进行拜访，请至少保证每月对政府部门进行一次拜访";
    private final static String VISIT_3_ALARM = "【%s】您近90天未对政府进行上门拜访，请至少保证每90天上门拜访一次";

    @Resource
    private Publisher publisher;
    @Resource
    private CuntaoGovContactRecordQueryBo cuntaoGovContactRecordQueryBo;

    @Override
    public void alarm() {
        Flux.create(publisher::alarmPublish)
                .onErrorContinue((e,o)->logger.error(e.getMessage(), e))
                .map(t->t.getT2())
                .parallel(3)
                .flatMap(this::buildVisitAlarmMsg)
                .subscribe(this::doAlarm);
    }

    private Flux<Optional<AppMsgPushInfoDto>> buildVisitAlarmMsg(CuntaoCountyListItem item){
        CuntaoGovContactRecordDetailDto latestRecord = cuntaoGovContactRecordQueryBo.queryLatestRecord(item.getId());

        List<Optional<AppMsgPushInfoDto>> msgs = Lists.newArrayList();

        if(latestRecord == null || isNMonthBefore(latestRecord, 0)){
            msgs.add(buildMsg(item, String.format(VISIT_1_ALARM, item.getName())));
        }

        CuntaoGovContactRecordDetailDto latestDoorToDoorRecord = cuntaoGovContactRecordQueryBo.queryLatestRecord(item.getId(), CuntaoGovContactRecordWayEnum.DOOR_TO_DOOR.getCode());
        if(latestDoorToDoorRecord == null || isNDayBefore(latestDoorToDoorRecord, 90)){
            msgs.add(buildMsg(item, String.format(VISIT_3_ALARM, item.getName())));
        }

        return Flux.fromIterable(msgs);
    }

    private boolean isNMonthBefore(CuntaoGovContactRecordDetailDto record, int month) {
        Date before = DateUtils.addMonths(new Date(), -1 * month);
        return DateUtils.truncatedCompareTo(record.getContactDate(), before, Calendar.MONTH) < 0;
    }

    private boolean isNDayBefore(CuntaoGovContactRecordDetailDto record, int days) {
        Date before = DateUtils.addDays(new Date(), -1 * days);
        return DateUtils.truncatedCompareTo(record.getContactDate(), before, Calendar.DATE) < 0;
    }
}
