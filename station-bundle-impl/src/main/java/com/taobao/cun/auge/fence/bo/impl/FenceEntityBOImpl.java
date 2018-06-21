package com.taobao.cun.auge.fence.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/6/20.
 */
@Component
public class FenceEntityBOImpl implements FenceEntityBO {

    @Autowired
    private FenceEntityMapper entityMapper;

    @Override
    public void enableEntityListByTemplateId(Long templateId, String operator) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setStatus(FenceConstants.ENABLE);
        DomainUtils.beforeUpdate(fenceEntity, operator);
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

    @Override
    public void disableEntityListByTemplateId(Long templateId, String operator) {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTemplateIdEqualTo(templateId);
        FenceEntity fenceEntity = new FenceEntity();
        fenceEntity.setStatus(FenceConstants.DISABLE);
        DomainUtils.beforeUpdate(fenceEntity, operator);
        entityMapper.updateByExampleSelective(fenceEntity, example);
    }

}
