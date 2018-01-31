package com.taobao.cun.auge.station.service.impl;

import java.util.List;
import java.util.Map;

import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.station.bo.WisdomCountyApplyBO;
import com.taobao.cun.auge.station.condition.WisdomCountyApplyCondition;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyAuditDto;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.service.WisdomCountyApplyService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xiao on 16/10/17.
 */
@Service("wisdomCountyApplyService")
@HSFProvider(serviceInterface = WisdomCountyApplyService.class)
public class WisdomCountyApplyServiceImpl implements WisdomCountyApplyService{

    @Autowired
    WisdomCountyApplyBO wisdomCountyApplyBO;

    @Autowired
    AttachmentService criusAttachmentService;

    @Override
    public WisdomCountyApplyDto getWisdomCountyApplyByCountyId(Long countyId){
        ValidateUtils.notNull(countyId);
        WisdomCountyApplyDto dto = wisdomCountyApplyBO.getWisdomCountyApplyByCountyId(countyId);
        if (dto != null){
            dto.setAttachmentDtos(criusAttachmentService.getAttachmentList(dto.getId(), AttachmentBizTypeEnum.WISDOM_COUNTY_APPLY));
        }
        return dto;
    }

    @Transactional
    @Override
    public Long addWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto){
        ValidateUtils.notNull(wisdomCountyApplyDto);
        ValidateUtils.notEmpty(wisdomCountyApplyDto.getAttachmentDtos());
        Long applyId = wisdomCountyApplyBO.addWisdomCountyApply(wisdomCountyApplyDto);
        criusAttachmentService.modifyAttachementBatch(wisdomCountyApplyDto.getAttachmentDtos(), applyId, AttachmentBizTypeEnum.WISDOM_COUNTY_APPLY, OperatorConverter.convert(wisdomCountyApplyDto));
        return applyId;
    }

    @Override
    public WisdomCountyApplyDto getWisdomCountyApplyById(Long id){
        ValidateUtils.notNull(id);
        WisdomCountyApplyDto dto = wisdomCountyApplyBO.getWisdomCountyApplyById(id);
        if (dto != null){
            dto.setAttachmentDtos(criusAttachmentService.getAttachmentList(id, AttachmentBizTypeEnum.WISDOM_COUNTY_APPLY));
        }
        return dto;
    }

    @Override
    public PageDto<WisdomCountyApplyDto> queryByPage(WisdomCountyApplyCondition condition){
        ValidateUtils.notNull(condition);
        return wisdomCountyApplyBO.queryByPage(condition);
    }

    @Override
    public Map<Long, WisdomCountyApplyDto> getWisdomCountyApplyByCountyIds(List<Long> ids){
        ValidateUtils.notEmpty(ids);
        return wisdomCountyApplyBO.getWisdomCountyApplyByCountyIds(ids);
    }

    @Override
    public void updateWisdomCountyApply(WisdomCountyApplyDto wisdomCountyApplyDto){
        ValidateUtils.notNull(wisdomCountyApplyDto.getId());
        wisdomCountyApplyBO.updateWisdomCountyApply(wisdomCountyApplyDto);
    }

    @Override
    public void deleteWisdomCountyApplyByCountyId(Long countyId, String operator){
        ValidateUtils.notNull(countyId);
        ValidateUtils.notNull(operator);
        wisdomCountyApplyBO.deleteWisdomCountyApplyByCountyId(countyId, operator);
    }

    @Override
    public boolean audit(WisdomCountyApplyAuditDto dto){
        ValidateUtils.notNull(dto.getId());
        ValidateUtils.notNull(dto.getState());
        return wisdomCountyApplyBO.audit(dto);
    }

    @Transactional
    @Override
    public void apply(WisdomCountyApplyDto wisdomCountyApplyDto){
        ValidateUtils.notNull(wisdomCountyApplyDto);
        deleteWisdomCountyApplyByCountyId(wisdomCountyApplyDto.getCountyId(), wisdomCountyApplyDto.getOperator());
        addWisdomCountyApply(wisdomCountyApplyDto);
    }


}
