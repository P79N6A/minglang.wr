package com.taobao.cun.auge.fence.convert;

import com.taobao.cun.auge.dal.domain.FenceTemplate;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.enums.FenceTypeEnum;
import com.taobao.cun.auge.fence.enums.FenceUserTypeEnum;
import org.springframework.beans.BeanUtils;

/**
 * Created by xiao on 18/6/25.
 */
public class FenceTemplateConvertor {

    public static FenceTemplateDto convertToDto(FenceTemplate fenceTemplate) {
        FenceTemplateDto dto = new FenceTemplateDto();
        BeanUtils.copyProperties(fenceTemplate, dto);
        dto.setTypeEnum(FenceTypeEnum.valueOf(fenceTemplate.getType()));
        dto.setUserTypeEnum(FenceUserTypeEnum.valueOf(fenceTemplate.getUserType()));
        return dto;
    }

}
