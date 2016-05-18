package com.taobao.cun.auge.station.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.dal.mapper.StationApplyMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;



@Component("syncStationApplyBO")
public class SyncStationApplyBOImpl implements SyncStationApplyBO{
	
	@Autowired
	StationApplyMapper stationApplyMapper;
	
	@Autowired
	ProtocolBO protocolBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	PartnerBO partnerBO;
	
	
	@Override
	public Long syncTemp(Long partnerInstanceId) throws AugeServiceException {
		PartnerStationRel  instance = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
		if (instance ==null || PartnerInstanceTypeEnum.TPV.equals(instance.getType())) {
			return null;
		}
		Station station = stationBO.getStationById(instance.getStationId());
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		Long stationAppyId= instance.getStationApplyId();
		StationApply stationApply = bulidStationApply(station,partner,instance,null);
		if (stationAppyId == null) {//新增
			stationApplyMapper.insert(stationApply);
		}else {//修改
			stationApply.setId(stationAppyId);
			stationApplyMapper.updateByPrimaryKey(stationApply);
		}
		return stationAppyId;
	}
	
	private StationApply bulidStationApply(Station station,Partner partner,PartnerStationRel instance,
			PartnerLifecycleItems PartnerLifecycle) {
			StationApply stationApply = new StationApply(); 
			//村点信息
			stationApply.setName(station.getName());
			stationApply.setCovered(station.getCovered());
			stationApply.setProducts(station.getProducts());
			stationApply.setLogisticsState(station.getLogisticsState());
			stationApply.setDescription(station.getDescription());
			stationApply.setFormat(station.getFormat());
			stationApply.setOwnOrgId(station.getApplyOrg());
			stationApply.setStationNum(station.getStationNum());
			stationApply.setAreaType(station.getAreaType());
			stationApply.setFixedType(station.getFixedType());

			stationApply.setProvince(station.getProvince());
			stationApply.setProvinceDetail(station.getProvinceDetail());
			stationApply.setCity(station.getCity());
			stationApply.setCityDetail(station.getCityDetail());
			stationApply.setCounty(station.getCounty());
			stationApply.setCountyDetail(station.getCountyDetail());
			stationApply.setTown(station.getTown());
	        stationApply.setTownDetail(station.getTownDetail());
	        stationApply.setAddressDetail(station.getAddress());
	        stationApply.setLat(station.getLat());
	        stationApply.setLng(station.getLng());
	        stationApply.setVillage(station.getVillage());
	        stationApply.setVillageDetail(station.getVillageDetail());
	        stationApply.setCreator(station.getCreater());
	        stationApply.setModifier(station.getModifier());
	        
	        //人员信息
	        stationApply.setTaobaoUserId(partner.getTaobaoUserId());
	        stationApply.setTaobaoNick(partner.getTaobaoNick());
	        stationApply.setEmail(partner.getEmail());
	        stationApply.setAlipayAccount(partner.getAlipayAccount());
	        stationApply.setIdenNum(partner.getIdenNum());
	        stationApply.setMobile(partner.getMobile());
	        stationApply.setBusinessType(partner.getBusinessType());
	        stationApply.setApplierName(partner.getName());
	        
	        //实例信息
	        stationApply.setServiceBeginDate(instance.getServiceBeginTime());
	        stationApply.setServiceEndDate(instance.getServiceEndTime());
	        stationApply.setApplyTime(instance.getApplyTime());
	        stationApply.setApplierType(instance.getApplierType());
	        stationApply.setApplierId(instance.getApplierId());
	        stationApply.setOpenDate(instance.getOpenDate());
	        stationApply.setOperatorType(instance.getType());

	        stationApply.setPartnerStationId(instance.getParentStationId());
	        stationApply.setQuitType(instance.getCloseType());
	        
	        //状态
	        convertInstanceState2StationApplyState(instance.getType(),instance.getState(),PartnerLifecycle);
	        
	        return stationApply;
	}
	
	public static String convertInstanceState2StationApplyState(String partnerType, String instatnceState,PartnerLifecycleItems PartnerLifecycle) {
        if (PartnerInstanceStateEnum.TEMP.getCode().equals(instatnceState)) {
           return StationApplyStateEnum.TEMP.getCode();
        } else if (PartnerInstanceStateEnum.SETTLING.getCode().equals(instatnceState)) {
        	if (PartnerLifecycle != null) {
        		if (PartnerLifecycleCurrentStepEnum.ROLE_APPROVE.equals(PartnerLifecycle.getCurrentStep())) {
        			if (PartnerInstanceTypeEnum.TPA.equals(partnerType)) {
        				 return StationApplyStateEnum.TPA_TEMP.getCode();
        			}
        		}else if (PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL.equals(PartnerLifecycle.getCurrentStep())) {
        			return StationApplyStateEnum.SUMITTED.getCode();
        		}else if (PartnerLifecycleCurrentStepEnum.BOND.equals(PartnerLifecycle.getCurrentStep())) {
        			return StationApplyStateEnum.CONFIRMED.getCode();
        		}
        	}
        }else if (PartnerInstanceStateEnum.SETTLE_FAIL.getCode().equals(instatnceState)) {
        	return StationApplyStateEnum.TPA_AUDIT_FAIL.getCode();
        }else if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instatnceState)) {
        	return StationApplyStateEnum.DECORATING.getCode();
        }else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instatnceState)) {
        	if (PartnerInstanceTypeEnum.TPA.equals(partnerType)) {
        		return StationApplyStateEnum.TPA_SERVICING.getCode();
        	}else if(PartnerInstanceTypeEnum.TP.equals(partnerType)) {
        		return StationApplyStateEnum.SERVICING.getCode();
        	}
        }else if (PartnerInstanceStateEnum.CLOSING.getCode().equals(instatnceState)) {
        	return StationApplyStateEnum.QUIT_APPLYING.getCode();
        }else if (PartnerInstanceStateEnum.CLOSED.getCode().equals(instatnceState)) {
        	return StationApplyStateEnum.QUIT_APPLY_CONFIRMED.getCode();
        }else if (PartnerInstanceStateEnum.QUITING.getCode().equals(instatnceState)) {
        	if (PartnerLifecycle != null) {
        		if (PartnerLifecycleCurrentStepEnum.ROLE_APPROVE.equals(PartnerLifecycle.getCurrentStep())) {
        		return StationApplyStateEnum.QUITAUDITING.getCode();
        		}else if (PartnerLifecycleCurrentStepEnum.BOND.equals(PartnerLifecycle.getCurrentStep())) {
        			return StationApplyStateEnum.CLOSED_WAIT_THAW.getCode();
        		}
        	}
        }else if (PartnerInstanceStateEnum.QUIT.getCode().equals(instatnceState)) {
        	return StationApplyStateEnum.QUIT.getCode();
        }
        return null;
        
    }

	@Override
	public void syncTPATemp(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncTPATempAduitNotPass(Long partnerInstanceId)throws AugeServiceException {
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncTPDegrade(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncSumitted(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void synctempSubmit(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncConfirm(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncfreeze(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncModify(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncForcedClosure(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncClosing(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncForcedClosureAudit(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncCloseConfirm(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncQuiting(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncQuitingAudit(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncHasthaw(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncDecorating(Long partnerInstanceId)throws AugeServiceException {
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncServicing(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}

	@Override
	public void syncDelete(Long partnerInstanceId) throws AugeServiceException{
		syncTemp(partnerInstanceId);
		
	}
	
}
