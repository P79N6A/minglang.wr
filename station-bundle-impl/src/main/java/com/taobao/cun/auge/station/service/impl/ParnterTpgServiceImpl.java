package com.taobao.cun.auge.station.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.cainiao.cuntaonetwork.dto.foundation.FeatureDTO;
import com.alibaba.cainiao.cuntaonetwork.dto.station.StationDTO;
import com.alibaba.cainiao.cuntaonetwork.param.Modifier;
import com.alibaba.cainiao.cuntaonetwork.result.Result;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationReadService;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationWriteService;
import com.taobao.cun.auge.dal.domain.PartnerTpg;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerTpgBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.PartnerTpgService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerTpgService")
@HSFProvider(serviceInterface = ParnterTpgServiceImpl.class)
public class ParnterTpgServiceImpl implements PartnerTpgService {

	private static final Logger logger = LoggerFactory.getLogger(ParnterTpgServiceImpl.class);
	
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
		try {
			if(partnerInstanceId == null){
				throw new AugeSystemException("参数为空");
			}
			if(!checkExists(partnerInstanceId)){
				PartnerInstanceDto partnerInstance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
				if(!PartnerInstanceTypeEnum.TPA.getCode().equals(partnerInstance.getType().getCode())){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeSystemException("只支持淘帮手升级供赢通会员");
				}
				Long cainiaoStationId = cainiaoStationRelBO.getCainiaoStationId(partnerInstance.getStationId());
				if(cainiaoStationId == null){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeBusinessException("菜鸟物流站点不存在");
				}
				//if not exist tpg tag add tag
				if(!partnerTpgBO.checkTag(partnerInstance.getTaobaoUserId())){
					boolean addTagResult = partnerTpgBO.addTpgTag(partnerInstance.getTaobaoUserId());
					if(!addTagResult){
						logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] uic打标失败");
						throw new AugeBusinessException("uic打标失败");
					}
				}
				Result<StationDTO> stationResult = stationReadService.queryStationById(cainiaoStationId);
				if(!stationResult.isSuccess()){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 查询菜鸟物流站点失败");
					throw new AugeBusinessException("查询菜鸟物流站点失败");
				}
				//updateCaiNiaoFeature
				FeatureDTO feature = stationResult.getData().getFeature();
				feature.put("ctpType", "CtPG");
				boolean updateFeaturesResult = stationWriteService.putStationFeatures(cainiaoStationId, feature.asMap(), Modifier.newSystem()).getData();
				if(!updateFeaturesResult){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 更新菜鸟供赢通标示失败");
					throw new AugeBusinessException("更新菜鸟供赢通标示失败");
				}
				PartnerTpg parnterTpg = new PartnerTpg();
				parnterTpg.setGmtCreate(new Date());
				parnterTpg.setGmtModified(new Date());
				parnterTpg.setCreator("system");
				parnterTpg.setModifier("system");
				parnterTpg.setIsDeleted("n");
				parnterTpg.setPartnerInstanceId(partnerInstanceId);
				parnterTpg.setTaobaoUserId(partnerInstance.getTaobaoUserId());
				parnterTpg.setCainiaoStationId(cainiaoStationId);
				parnterTpg.setCainiaoStationFeature(feature.toString());
				parnterTpg.setUicTpgFlag("y");
				partnerTpgBO.addPartnerTpg(parnterTpg);
			}
			
			return true;
		} catch (Exception e) {
			logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 系统异常",e);
			throw new AugeSystemException("系统异常");
		}
		
	}

	private boolean checkExists(Long partnerInstanceId) {
		return partnerTpgBO.queryByParnterInstanceId(partnerInstanceId).isPresent();
	}

	@Override
	public boolean degradeTpg(Long partnerInstanceId) {
		try {
			Optional<PartnerTpg> tpgResult = partnerTpgBO.queryByParnterInstanceId(partnerInstanceId);
			if (tpgResult.isPresent()) {
				PartnerInstanceDto partnerInstance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
				PartnerTpg partnerTpg =	tpgResult.get();
				if(partnerTpgBO.checkTag(partnerInstance.getTaobaoUserId())){
					if(!partnerTpgBO.removeTpgTag(partnerInstance.getTaobaoUserId())){
						logger.error("degradeTpg error!partnerInstanceId["+partnerInstanceId+"] 删除UIC供赢通标示失败");
						throw new AugeBusinessException("删除UIC供赢通标示失败");
					}
				}
				Long cainiaoStationId = cainiaoStationRelBO.getCainiaoStationId(partnerInstance.getStationId());
				if(cainiaoStationId == null){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 菜鸟物流站点不存在");
					throw new AugeBusinessException("菜鸟物流站点不存在");
				}
				Result<StationDTO> stationResult = stationReadService.queryStationById(cainiaoStationId);
				if(!stationResult.isSuccess()){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 查询菜鸟物流站点失败");
					throw new AugeBusinessException("查询菜鸟物流站点失败");
				}
				//updateCaiNiaoFeature
				FeatureDTO feature = stationResult.getData().getFeature();
				feature.put("ctpType", "CtPA");
				boolean updateFeaturesResult = stationWriteService.putStationFeatures(cainiaoStationId, feature.asMap(), Modifier.newSystem()).getData();
				if(!updateFeaturesResult){
					logger.error("upgradeTpg error!partnerInstanceId["+partnerInstanceId+"] 更新菜鸟供赢通标示失败");
					throw new AugeBusinessException("更新菜鸟供赢通标示失败");
				}
				partnerTpgBO.deletePartnerTpg(partnerTpg.getId());
			}
			return true;
		} catch (Exception e) {
			logger.error("degradeTpg error!partnerInstanceId["+partnerInstanceId+"] 系统异常",e);
			throw new AugeSystemException("系统异常");
		}
	
	}

}
