package com.taobao.cun.auge.station.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDetailDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerFlowerNameApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerService")
@HSFProvider(serviceInterface = PartnerService.class)
public class PartnerServiceImpl implements PartnerService {
	@Resource
	private PartnerBO partnerBO;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	CountyStationBO countyStationBO;
	
	@Autowired
	StationApplySyncBO syncStationApplyBO;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	
	@Override
	public void updateById(PartnerDto partnerDto) {
		partnerBO.updatePartner(partnerDto);
	}

	@Override
	public PartnerDto getPartnerByAlilangUserId(String aliLangUserId) {
		return PartnerConverter.toPartnerDto(partnerBO.getPartnerByAliLangUserId(aliLangUserId));
	}

	@Override
	public PartnerDto getNormalPartnerByTaobaoUserId(Long taobaoUserId){
		return PartnerConverter.toPartnerDto(partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId));
	}

	@Override
	public PartnerDetailDto getPartnerDetail(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null == rel) {
			return null;
		}
		PartnerDetailDto result=new PartnerDetailDto();
		//组装合伙人数据
		Partner partner=partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
		if(partner==null){
			throw new AugeBusinessException("无法找到合伙人信息");
		}
		result.setAliPay(partner.getAlipayAccount());
		result.setEmail(partner.getEmail());
		result.setFlowerName(partner.getFlowerName());
		result.setIdenNum(partner.getIdenNum());
		result.setName(partner.getName());
		result.setPartnerType(rel.getType());
		result.setTaobaoNick(partner.getTaobaoNick());
		result.setTaobaoUserId(partner.getTaobaoUserId());
		result.setMobile(partner.getMobile());
		result.setGmtServiceBegin(rel.getServiceBeginTime());
		result.setBirthday(partner.getBirthday());
		//组装村点信息
		Station station=stationBO.getStationById(rel.getStationId());
		if(station==null){
			throw new AugeBusinessException("无法找到村点信息");
		}
		result.setAddressDetail(station.getAddress());
		result.setCity(station.getCityDetail());
		result.setCounty(station.getCountyDetail());
		result.setProvince(station.getProvinceDetail());
		result.setStationId(station.getId());
		result.setStationName(station.getName());
		result.setTown(station.getTownDetail());
		result.setProvinceCode(station.getProvince());
		result.setCityCode(station.getCity());
		result.setCountyCode(station.getCounty());
		result.setTownCode(station.getTown());
		result.setLat(station.getLat());
		result.setLng(station.getLng());
		result.setVillageDetail(station.getVillageDetail());
		result.setIsOnTown(station.getIsOnTown());
		result.setVillageCode(station.getVillage());
		//县信息
		CountyStation county=countyStationBO.getCountyStationByOrgId(station.getApplyOrg());
		if(county==null){
			throw new AugeBusinessException("无法找到县点信息");
		}
		result.setCountyName(county.getName());
		return result;
	}

	@Override
	public void modifyPartnerDetail(Long taobaoUserId, String mobile,
			String email, Date birthday) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(mobile);
        Assert.notNull(email);
        Assert.notNull(birthday);
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null == rel) {
			throw new AugeBusinessException("当前状态无法修改");
		}
		//验证手机号是否被使用
		if(!partnerInstanceBO.judgeMobileUseble(taobaoUserId, null, mobile)){
			throw new AugeBusinessException("该手机号已被使用");
		}
        PartnerDto dto=new PartnerDto();
        dto.setTaobaoUserId(taobaoUserId);
        dto.setMobile(mobile);
        dto.setEmail(email);
        dto.setBirthday(birthday);
        dto.setId(rel.getPartnerId());
        partnerBO.updatePartner(dto);
        //同步stationApply;
        syncStationApplyBO.updateStationApply(rel.getId(), SyncStationApplyEnum.UPDATE_BASE);
        //同步菜鸟
        if (isNeedToUpdateCainiaoStation(rel.getState())) {
			generalTaskSubmitService.submitUpdateCainiaoStation(rel.getId(),String.valueOf(taobaoUserId));
		}
	}
	
	private boolean isNeedToUpdateCainiaoStation(String state) {
		return PartnerInstanceStateEnum.DECORATING.getCode().equals(state) || PartnerInstanceStateEnum.SERVICING.getCode().equals(state)
				|| PartnerInstanceStateEnum.CLOSING.getCode().equals(state);
	}
	

	@Override
	public void applyFlowName(PartnerFlowerNameApplyDto dto) {
		partnerBO.applyFlowName(dto);
	}

	@Override
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetail(Long taobaoUserId) {
		return partnerBO.getFlowerNameApplyDetail(taobaoUserId);
	}

	@Override
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetailById(Long id) {
		return partnerBO.getFlowerNameApplyDetailById(id);
	}

}
