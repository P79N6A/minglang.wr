package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.common.utils.BeanCopyUtils;
import com.taobao.cun.auge.dal.domain.WisdomCountyApply;
import com.taobao.cun.auge.station.dto.WisdomCountyApplyDto;
import com.taobao.cun.auge.station.enums.WisdomCountyStateEnum;

/**
 * Created by xiao on 16/10/17.
 */
public class WisdomCountyApplyConverter {

    public static WisdomCountyApplyDto toDto(WisdomCountyApply wisdomCountyApply){
        if (wisdomCountyApply == null){
            return null;
        }
        WisdomCountyApplyDto dto = new WisdomCountyApplyDto();
        BeanCopyUtils.copyNotNullProperties(wisdomCountyApply, dto);
        dto.setState(WisdomCountyStateEnum.valueof(wisdomCountyApply.getState()));
        return dto;
    }

    public static WisdomCountyApply dtoTo(WisdomCountyApplyDto dto){
        if (dto == null){
            return null;
        }
        WisdomCountyApply wisdomCountyApply = new WisdomCountyApply();
        BeanCopyUtils.copyNotNullProperties(dto, wisdomCountyApply);
        wisdomCountyApply.setState(dto.getState().getCode());
        return wisdomCountyApply;
    }

}
