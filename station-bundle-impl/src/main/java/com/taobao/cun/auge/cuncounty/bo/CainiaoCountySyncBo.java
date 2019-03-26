package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 菜鸟县仓同步
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CainiaoCountySyncBo {
	@Resource
	private CaiNiaoAdapter caiNiaoAdapter;
	@Resource
	private CuntaoCountyBo cuntaoCountyBo;
	@Resource
	private EnhancedUserQueryService enhancedUserQueryService;
	@Resource
	private CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	void syncNewCounty(CainiaoCountyDto cainiaoCountyDto, String operator){
		Long caiNiaostationId = caiNiaoAdapter.addCountyByOrg(toCainiaoStationDto(cainiaoCountyDto, operator));
        if (caiNiaostationId == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "同步菜鸟驿站失败");
        } else {
        	CuntaoCainiaoStationRelDto relDO = new CuntaoCainiaoStationRelDto();
            relDO.setObjectId(cainiaoCountyDto.getCountyId());
            relDO.setCainiaoStationId(caiNiaostationId);
            relDO.setType(CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
            relDO.setOperator(operator);
            cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDO);
        }
	}
	
	private CaiNiaoStationDto toCainiaoStationDto(CainiaoCountyDto cainiaoCountyDto, String operator) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(cainiaoCountyDto.getCountyId());
    	CaiNiaoStationDto stationDto = new CaiNiaoStationDto();
        stationDto.setStationName(cuntaoCountyDto.getName());
        stationDto.setStationAddress(BeanConvertUtils.convertAddress(cainiaoCountyDto));
        String contact = getUserName(operator);
        stationDto.setContact(contact == null ? operator : contact);
        stationDto.setStationType(4);
        stationDto.setLoginId(operator);
        stationDto.setStationId(cainiaoCountyDto.getCountyId());
        return stationDto;
    }
	
	private String getUserName(String empId) {
		try {
			EnhancedUser user = enhancedUserQueryService.getUser(empId);
			if(user != null) {
				return user.getLastName();
			}else {
				return null;
			}
		} catch (BucException e) {
			return null;
		}
	}
}
