package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.mail.CainiaoCountyUpdateMessage;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;
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
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	@Resource
	private CainiaoCountyUpdateMessage cainiaoCountyUpdateMessage;
	
	void createCainiaoCounty(Long countyId){
		CainiaoCounty cainiaoCounty = cuntaoCountyExtMapper.getCainiaoCounty(countyId);
		if(cainiaoCounty == null) {
			return;
		}
		Long caiNiaostationId = caiNiaoAdapter.addCountyByOrg(toCainiaoStationDto(cainiaoCounty));
        if (caiNiaostationId == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "同步菜鸟驿站失败");
        } else {
        	CuntaoCainiaoStationRelDto relDO = new CuntaoCainiaoStationRelDto();
            relDO.setObjectId(countyId);
            relDO.setCainiaoStationId(caiNiaostationId);
            relDO.setType(CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
            relDO.setOperator(cainiaoCounty.getCreator());
            cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDO);
        }
	}
	
	private CaiNiaoStationDto toCainiaoStationDto(CainiaoCounty cainiaoCounty) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyBo.getCuntaoCounty(cainiaoCounty.getCountyId());
    	CaiNiaoStationDto stationDto = new CaiNiaoStationDto();
        stationDto.setStationName(cuntaoCountyDto.getName());
        stationDto.setStationAddress(BeanConvertUtils.convertAddress(cainiaoCounty));
        String contact = getUserName(cainiaoCounty.getCreator());
        stationDto.setContact(contact == null ? cainiaoCounty.getCreator() : contact);
        stationDto.setStationType(4);
        stationDto.setLoginId(cainiaoCounty.getCreator());
        stationDto.setStationId(cainiaoCounty.getCountyId());
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

	public void updateCainiaoCounty(CuntaoCountyDto cuntaoCounty, CainiaoCountyDto oldCainiaoCounty, CainiaoCountyDto newCainiaoCounty) {
		cainiaoCountyUpdateMessage.sendEmail(cuntaoCounty, oldCainiaoCounty, newCainiaoCounty);
	}
}
