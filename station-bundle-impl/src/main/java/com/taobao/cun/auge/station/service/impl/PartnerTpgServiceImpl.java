package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import com.alibaba.cainiao.cuntaonetwork.dto.foundation.FeatureDTO;
import com.alibaba.cainiao.cuntaonetwork.dto.station.StationDTO;
import com.alibaba.cainiao.cuntaonetwork.param.Modifier;
import com.alibaba.cainiao.cuntaonetwork.result.Result;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationReadService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;

import com.taobao.cun.auge.dal.domain.PartnerTpg;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerTpgBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.PartnerTpgService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("partnerTpgService")
@HSFProvider(serviceInterface = PartnerTpgService.class)
public class PartnerTpgServiceImpl implements PartnerTpgService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerTpgServiceImpl.class);
	
	@Autowired
	private PartnerTpgBO partnerTpgBO;
	
	@Autowired
	private StationReadService stationReadService;
	
	@Resource
	private StationWriteService stationWriteService;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private CuntaoCainiaoStationRelBO cainiaoStationRelBO;
	@Override
	public boolean upgradeTpg(Long partnerInstanceId) {
				if(partnerInstanceId == null){
					throw new AugeSystemException("参数为空");
				}
				Optional<PartnerTpg> partnerTpgResult = partnerTpgBO.queryByParnterInstanceId(partnerInstanceId);
				PartnerInstanceDto partnerInstance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
				if(!PartnerInstanceTypeEnum.TPA.getCode().equals(partnerInstance.getType().getCode())){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeSystemException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"只支持淘帮手升级供赢通会员");
				}
				if(!PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerInstance.getState().getCode())){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 淘帮手必须服务中");
					throw new AugeSystemException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"淘帮手必须服务中");
				}
				Long cainiaoStationId = cainiaoStationRelBO.getCainiaoStationId(partnerInstance.getStationId());
				if(cainiaoStationId == null){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"菜鸟物流站点不存在");
				}
				Result<StationDTO> stationResult = stationReadService.queryStationById(cainiaoStationId);
				if(!stationResult.isSuccess()){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 查询菜鸟物流站点失败");
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"查询菜鸟物流站点失败");
				}
				FeatureDTO feature = stationResult.getData().getFeature();
				if(feature.get("ctpType") != null && feature.get("ctpType").equals("CTPA1_0")){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 查询菜鸟物流站点失败");
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"降级淘帮手无法升级成供赢通");
				}
				
				PartnerTpg partnerTpg = partnerTpgResult.isPresent()?partnerTpgResult.get():addPartnerTpg(partnerInstance);
				//if not exist tpg tag add tag
				if(!partnerTpgBO.checkTag(partnerInstance.getTaobaoUserId())){
					boolean addTagResult = partnerTpgBO.addTpgTag(partnerInstance.getTaobaoUserId());
					if(!addTagResult){
						partnerTpg.setUicTpgFlag("upgrade_fail");
						updatePartnerTpg(partnerTpg);
						logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] uic打标失败");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"uic打标失败");
					}else{
						partnerTpg.setUicTpgFlag("upgrade_success");
						updatePartnerTpg(partnerTpg);
					}
				}
				//updateCaiNiaoFeature
				if(feature.get("ctpType") != null && !feature.get("ctpType").equals("CtPG")){
					feature.put("ctpType", "CtPG");
					 Result<Boolean> updateFeaturesResult= stationWriteService.putStationFeatures(cainiaoStationId, feature.asMap(), Modifier.newSystem());
					if(updateFeaturesResult == null || !updateFeaturesResult.isSuccess()||!updateFeaturesResult.getData()){
						partnerTpg.setCainiaoStationFeature("upgrade_fail");
						partnerTpg.setCainiaoStationId(cainiaoStationId);
						updatePartnerTpg(partnerTpg);
						logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 更新菜鸟供赢通标示失败,errorMessage:"+updateFeaturesResult!=null?updateFeaturesResult.getErrorMessage():"updateFeatureResult is null");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"更新菜鸟供赢通标示失败");
					}else{
						partnerTpg.setCainiaoStationFeature("upgrade_success");
						partnerTpg.setCainiaoStationId(cainiaoStationId);
						updatePartnerTpg(partnerTpg);
					}
				}
			return true;
	}

	private PartnerTpg addPartnerTpg(PartnerInstanceDto partnerInstance) {
		PartnerTpg parnterTpg = new PartnerTpg();
		parnterTpg.setGmtCreate(new Date());
		parnterTpg.setGmtModified(new Date());
		parnterTpg.setCreator("system");
		parnterTpg.setModifier("system");
		parnterTpg.setIsDeleted("n");
		parnterTpg.setPartnerInstanceId(partnerInstance.getId());
		parnterTpg.setTaobaoUserId(partnerInstance.getTaobaoUserId());
		partnerTpgBO.addPartnerTpg(parnterTpg);
		return parnterTpg;
	}

	private void updatePartnerTpg(PartnerTpg parnterTpg){
		partnerTpgBO.updatePartnerTpg(parnterTpg);
	}
	
	

	@Override
	public boolean degradeTpg(Long partnerInstanceId) {
			Optional<PartnerTpg> tpgResult = partnerTpgBO.queryByParnterInstanceId(partnerInstanceId);
			if (tpgResult.isPresent()) {
				PartnerInstanceDto partnerInstance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
				PartnerTpg partnerTpg =	tpgResult.get();
				if(partnerTpgBO.checkTag(partnerInstance.getTaobaoUserId())){
					if(!partnerTpgBO.removeTpgTag(partnerInstance.getTaobaoUserId())){
						partnerTpg.setUicTpgFlag("degrade_fail");
						updatePartnerTpg(partnerTpg);
						logger.error("degradeTpg error!partnerInstanceId["+partnerInstanceId+"] 删除UIC供赢通标示失败");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"删除UIC供赢通标示失败");
					}else{
						partnerTpg.setUicTpgFlag("degrade_success");
						updatePartnerTpg(partnerTpg);
					}
				}
				Long cainiaoStationId = cainiaoStationRelBO.getCainiaoStationId(partnerInstance.getStationId());
				if(cainiaoStationId == null){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"菜鸟物流站点不存在");
				}
				Result<StationDTO> stationResult = stationReadService.queryStationById(cainiaoStationId);
				if(!stationResult.isSuccess()){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 查询菜鸟物流站点失败");
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"查询菜鸟物流站点失败");
				}
				FeatureDTO feature = stationResult.getData().getFeature();
				if(feature.get("ctpType") != null && feature.get("ctpType").equals("CtPG")){
					feature.put("ctpType", "CtPA");
					boolean updateFeaturesResult = stationWriteService.putStationFeatures(cainiaoStationId, feature.asMap(), Modifier.newSystem()).getData();
					if(!updateFeaturesResult){
						partnerTpg.setUicTpgFlag("degrade_fail");
						updatePartnerTpg(partnerTpg);
						logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 更新菜鸟供赢通标示失败");
						throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"更新菜鸟供赢通标示失败");
					}
				}
				partnerTpg.setCainiaoStationFeature("degrade_success");
				updatePartnerTpg(partnerTpg);
				partnerTpgBO.deletePartnerTpg(partnerTpg.getId());
			}
			return true;
	}

	@Override
	public boolean isPartnerTpg(Long partnerInstanceId) {
		return partnerTpgBO.queryByParnterInstanceId(partnerInstanceId).isPresent();
	}

    @Override
    public boolean hasTpgTag(Long taobaoUserId) {
        return partnerTpgBO.checkTag(taobaoUserId);
    }

}
