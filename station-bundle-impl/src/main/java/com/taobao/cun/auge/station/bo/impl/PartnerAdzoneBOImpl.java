package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.UnionAdzone;
import com.taobao.cun.auge.dal.domain.UnionAdzoneExample;
import com.taobao.cun.auge.dal.mapper.UnionAdzoneExtMapper;
import com.taobao.cun.auge.dal.mapper.UnionAdzoneMapper;
import com.taobao.cun.auge.station.bo.PartnerAdzoneBO;
import com.taobao.cun.auge.station.dto.NewuserOrderStat;
import com.taobao.cun.auge.station.dto.PartnerAdzoneInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PartnerAdzoneBOImpl implements PartnerAdzoneBO {
    @Autowired
    private UnionAdzoneMapper unionAdzoneMapper;
    @Autowired
    private UnionAdzoneExtMapper unionAdzoneExtMapper;

    @Override
    public String getUnionPid(Long taobaoUserId, Long stationId) {
        Assert.notNull(taobaoUserId, "taobaoUserId is null");
        Assert.notNull(stationId, "stationId is null");
        UnionAdzoneExample example = new UnionAdzoneExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId).andStationIdEqualTo(stationId);
        List<UnionAdzone> adzones = unionAdzoneMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(adzones)) {
            return null;
        }
        return adzones.iterator().next().getMmPid();
    }

    @Override
    public PartnerAdzoneInfoDto getPartnerAdzoneInfoByPid(String pid) {
        Assert.notNull(pid, "pid is null");
        UnionAdzoneExample example = new UnionAdzoneExample();
        example.createCriteria().andIsDeletedEqualTo("n").andMmPidEqualTo(pid);
        List<UnionAdzone> adzones = unionAdzoneMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(adzones)) {
            return null;
        }
        UnionAdzone adzone = adzones.iterator().next();
        PartnerAdzoneInfoDto dto = new PartnerAdzoneInfoDto();
        dto.setPid(pid);
        dto.setStationId(adzone.getStationId());
        dto.setTaobaoUserId(adzone.getTaobaoUserId());
        return dto;
    }

    @Override
    public void addAdzone(Long taobaoUserId, Long stationId, String pid) {
        UnionAdzone adzone = new UnionAdzone();
        adzone.setMmPid(pid);
        adzone.setTaobaoUserId(taobaoUserId);
        adzone.setStationId(stationId);
        DomainUtils.beforeInsert(adzone, "sys");
        unionAdzoneMapper.insertSelective(adzone);
    }

    @Override
    public NewuserOrderStat getNewuserOrderStat(Long adzoneId, String statDate, String currentUpdateDate) {
        Assert.notNull(adzoneId, "adzoneId is null");
        Assert.notNull(statDate, "statDate is null");
        Assert.notNull(currentUpdateDate, "currentUpdateDate is null");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("adzoneId",adzoneId);
        param.put("statDate",statDate);
        param.put("currentUpdateDate",currentUpdateDate);
        List<NewuserOrderStat> statList = unionAdzoneExtMapper.getNewuserOrderStat(param);
        NewuserOrderStat stat = null;
        if (CollectionUtils.isEmpty(statList)) {
            stat = new NewuserOrderStat();
        } else {
            stat = statList.iterator().next();
        }
        return stat;
    }
}
