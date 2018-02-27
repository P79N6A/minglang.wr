package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecordExample;
import com.taobao.cun.auge.dal.mapper.CuntaoFlowRecordMapper;
import com.taobao.cun.auge.flowRecord.condition.CuntaoFlowRecordCondition;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Component("cuntaoFlowRecordBO")
public class CuntaoFlowRecordBOImpl implements CuntaoFlowRecordBO {

    @Autowired
    CuntaoFlowRecordMapper cuntaoFlowRecordMapper;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void addRecord(CuntaoFlowRecord record) {
        DomainUtils.beforeInsert(record, record.getOperatorWorkid());
        cuntaoFlowRecordMapper.insertSelective(record);

    }

    @Override
    public CuntaoFlowRecord getRecordByTargetIdAndType(CuntaoFlowRecordCondition condition) {
        CuntaoFlowRecordExample example = new CuntaoFlowRecordExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTargetIdEqualTo(condition.getTargetId())
            .andTargetTypeEqualTo(condition.getTargetType());
        List<CuntaoFlowRecord> recordList = cuntaoFlowRecordMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(recordList)) {
            return recordList.get(0);
        }
        return null;
    }

}
