package com.taobao.cun.auge.alipay.bo.impl;

import com.taobao.cun.auge.alipay.bo.StationAlipayInfoBO;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.StationAlipayInfo;
import com.taobao.cun.auge.dal.domain.StationAlipayInfoExample;
import com.taobao.cun.auge.dal.mapper.StationAlipayInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("stationAlipayInfoBO")
public class StationAlipayInfoBOImpl implements StationAlipayInfoBO {

    @Autowired
    StationAlipayInfoMapper stationAlipayInfoMapper;


    @Override
    public StationAlipayInfo getStationAlipayInfo(String taobaoUserId) {

        ValidateUtils.notNull(taobaoUserId);
        StationAlipayInfoExample example = new StationAlipayInfoExample();
        StationAlipayInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        List<StationAlipayInfo> resList=stationAlipayInfoMapper.selectByExample(example);
        return  ResultUtils.selectOne(resList);
    }

    @Override
    public StationAlipayInfo getCountyStationById(Long id) {
        ValidateUtils.notNull(id);
        StationAlipayInfoExample example = new StationAlipayInfoExample();
        StationAlipayInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<StationAlipayInfo> resList=stationAlipayInfoMapper.selectByExample(example);
        return  ResultUtils.selectOne(resList);
    }

    @Override
    public Long addStationAlipayInfo(StationAlipayInfo stationAlipayInfo) {
        stationAlipayInfoMapper.insert(stationAlipayInfo);
        return stationAlipayInfo.getId();
    }

    @Override
    public void updateStationAlipayInfo(StationAlipayInfo stationAlipayInfo) {

        stationAlipayInfoMapper.updateByPrimaryKey(stationAlipayInfo);

    }
}
