package com.taobao.cun.auge.lx.bo.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.LxErrorCodes;
import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.lx.bo.LxPartnerBO;
import com.taobao.cun.auge.lx.dto.LxPartnerAddDto;
import com.taobao.cun.auge.lx.dto.LxPartnerListDto;
import com.taobao.cun.auge.lx.service.LxPartnerMobileService;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.InstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.diamond.client.Diamond;
import com.taobao.mtee.rmb.RmbResult;
import com.taobao.mtee.rmb.RmbService;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

/**
 * 拉新伙伴 状态 1.服务中，2已停业
 * 
 * @author quanzhu.wangqz
 *
 */
@Component("lxPartnerBO")
public class LxPartnerBOImpl implements LxPartnerBO {
	
	private static final Logger logger = LoggerFactory.getLogger(LxPartnerMobileService.class);

	@Autowired
	private StationBO stationBO;

	@Autowired
	private PartnerBO partnerBO;

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;

	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	@Autowired
	private ManualReleaseDistributeLock distributeLock;
	
	@Autowired
	private PartnerAdzoneService partnerAdzoneService;
	
	@Autowired
	private RmbService rmbService;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean addLxPartner(LxPartnerAddDto param) {
		Objects.requireNonNull(param, "参数不能为空");
		Objects.requireNonNull(param.getTaobaoNick(), "请填写真实有效的淘宝账号");
		Objects.requireNonNull(param.getMobile(), "请填写该账号绑定的手机号");
		Objects.requireNonNull(param.getName(), "请填写姓名");
		Objects.requireNonNull(param.getpTaobaoUserId(), "所属村小二不能为空");
		Long taobaoUserId = validateParam(param);

		boolean lockResult = false;
		try {
			lockResult = distributeLock.lock("lxPartner-taobaoUserId", String.valueOf(taobaoUserId), 5);
			if (!lockResult) {
				throw new AugeBusinessException(LxErrorCodes.CONCURRENT_UPDATE_ERROR_CODE, "请稍后重试");
			}
			addLx(param, taobaoUserId);
			if(isSync()){
			partnerAdzoneService.createAdzone(taobaoUserId);
			}
		} catch (Exception e) {
			logger.error("LxPartnerBOImpl.addLxPartner error! param:" + taobaoUserId, e);
			throw new AugeBusinessException(LxErrorCodes.LX_CREATE_PID_ERROR, "今日已无法再邀请拉新伙伴。请稍安勿躁，明天再尝试哦");
		} finally {
			if (lockResult) {
				distributeLock.unlock("lxPartner-taobaoUserId", String.valueOf(taobaoUserId));
			}
		}
		return Boolean.TRUE;
	}

	private Long validateParam(LxPartnerAddDto param) {
		// 账号必须是存在的，不能为空 账号必须和填写的手机号绑定
		ResultDO<BaseUserDO> baseUserDOresult = uicReadServiceClient.getBaseUserByNick(param.getTaobaoNick());
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			throw new AugeBusinessException(LxErrorCodes.TAOBAONICK_STATUS_ERROR_CODE, "该淘宝账号不存在或状态异常，请与申请人核实!");
		}
		BaseUserDO baseUserDO = baseUserDOresult.getModule();
		// TODO: 校验账号是否黑灰账号  临时注释掉
		
		if (baseUserDO.getMobilePhone() == null || !baseUserDO.getMobilePhone().equals(param.getMobile())) {
			throw new AugeBusinessException(LxErrorCodes.MOBILE_CHECK_ERROR_CODE, "请填写该账号绑定的手机号");
		}
		Long taobaoUserId = baseUserDO.getUserId();
		// TODO: 校验账号是否黑灰账号
		if (!checkFkByTaobaoUserId(taobaoUserId)) {
			throw new AugeBusinessException(LxErrorCodes.TAOBAONICK_FK_CHECK_ERROR_CODE, "无法邀请该账号成为拉新伙伴，请尝试其他淘宝账号");
		}
		// 判断淘宝账号是否使用中
		PartnerStationRel pi = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null != pi) {
			if (InstanceTypeEnum.LX.getCode().equals(pi.getType())
					&& PartnerInstanceStateEnum.CLOSED.getCode().equals(pi.getState())) {
				throw new AugeBusinessException(LxErrorCodes.TAOBAONICK_BUSI_CHECK_ERROR_CODE, "无法邀请该账号成为拉新伙伴，请尝试其他淘宝账号");
			}
			if (InstanceTypeEnum.LX.getCode().equals(pi.getType())
					&& PartnerInstanceStateEnum.SERVICING.getCode().equals(pi.getState())) {
				throw new AugeBusinessException(LxErrorCodes.TAOBAONICK_SAME_LX_ERROR_CODE, "无法邀请该账号成为拉新伙伴，请尝试其他淘宝账号");
			}
			throw new AugeBusinessException(LxErrorCodes.TAOBAONICK_OTHER_BUSI_CHECK_ERROR_CODE,
					"不支持邀请村小二/淘帮手/优盟/村拍档的淘宝账号成为拉新伙伴，请尝试其他淘宝账号");
		}
		// 判断手机号是否已经被使用
		if (!partnerInstanceBO.judgeMobileUseble(taobaoUserId, null, param.getMobile())) {
			throw new AugeBusinessException(LxErrorCodes.MOBILE_USED_ERROR_CODE, "该手机号已被使用");
		}
		Integer maxCount = getMaxCount();

		Integer curCount = partnerInstanceBO.getActiveLxPartnerByParentStationId(param.getpTaobaoUserId());
		// 校验可剩余名额>0
		if (maxCount-curCount <= 0) {
			throw new AugeBusinessException(LxErrorCodes.QUOTA_CHECK_ERROR_CODE, "当前邀请名额已用完，无法再邀请拉新伙伴");
		}
		return taobaoUserId;
	}

	private Integer getMaxCount() {
		Integer maxCount = 3;
		String lxPartnerMaxCount = null;
		try {
			lxPartnerMaxCount = Diamond.getConfig("lxPartnerMaxCount", "DEFAULT_GROUP", 3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(lxPartnerMaxCount)) {
			maxCount = Integer.parseInt(lxPartnerMaxCount);
		}
		return maxCount;
	}
	
	public Long addLx(LxPartnerAddDto lxPartnerAddDto, Long taobaoUserId) {

		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(lxPartnerAddDto.getpTaobaoUserId());
		if (null == rel) {
			throw new AugeBusinessException(LxErrorCodes.PARENT_PARTNER_INFO_CHECK_ERROR, "所属村小二不存在");
		}
		Station station = stationBO.getStationById(rel.getStationId());

		Long stationId = addLxStation(lxPartnerAddDto, taobaoUserId, station);
        Long partnerId = addLxPartner(lxPartnerAddDto, taobaoUserId);
        PartnerInstanceDto dto = new PartnerInstanceDto();
        dto.setState(PartnerInstanceStateEnum.SERVICING);
        Date serviceBeginTime = new Date();
        dto.setApplyTime(serviceBeginTime);
        dto.setServiceBeginTime(serviceBeginTime);
        dto.setOpenDate(serviceBeginTime);
        dto.setApplierId(lxPartnerAddDto.getOperator());
        dto.setApplierType(lxPartnerAddDto.getOperatorType().getCode());
        dto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
        dto.setVersion(0L);
        dto.setParentStationId(rel.getStationId());
        dto.setStationId(stationId);
        dto.setPartnerId(partnerId);
        dto.setType(PartnerInstanceTypeEnum.LX);
        dto.copyOperatorDto(lxPartnerAddDto);
        dto.setTaobaoUserId(taobaoUserId);
        return partnerInstanceBO.addPartnerStationRel(dto);
	}

	private Long addLxPartner(LxPartnerAddDto lxPartnerAddDto, Long taobaoUserId) {
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
        PartnerDto partnerDto =new  PartnerDto();
        partnerDto.copyOperatorDto(lxPartnerAddDto);
        if (partner == null) {
            partnerDto.setState(PartnerStateEnum.NORMAL);
            partnerDto.setName(lxPartnerAddDto.getName());
            partnerDto.setTaobaoNick(lxPartnerAddDto.getTaobaoNick());
            partnerDto.setTaobaoUserId(taobaoUserId);
            partnerDto.setMobile(lxPartnerAddDto.getMobile());
            return  partnerBO.addPartner(partnerDto);
        } else {
            partnerDto.setId(partner.getId());
            partnerDto.setName(lxPartnerAddDto.getName());
            partnerDto.setTaobaoNick(lxPartnerAddDto.getTaobaoNick());
            partnerDto.setTaobaoUserId(taobaoUserId);
            partnerDto.setMobile(lxPartnerAddDto.getMobile());
            partnerDto.setState(PartnerStateEnum.NORMAL);
            partnerBO.updatePartner(partnerDto);
            return partner.getId();
        }
	}

	private Long addLxStation(LxPartnerAddDto lxPartnerAddDto, Long taobaoUserId, Station station) {
		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.SERVICING);
		stationDto.copyOperatorDto(lxPartnerAddDto);
		stationDto.setStationType(StationType.STATION.getType());
		stationDto.setTaobaoNick(lxPartnerAddDto.getTaobaoNick());
		stationDto.setTaobaoUserId(taobaoUserId);
		stationDto.setApplyOrg(station.getApplyOrg());
		stationDto.setName(lxPartnerAddDto.getName()+"-"+lxPartnerAddDto.getMobile().substring(7, lxPartnerAddDto.getMobile().length()));
		stationDto.setOwnDept("opdept");
		stationDto.setTransferState("FINISHED");
		Long stationId = stationBO.addStation(stationDto);
		return stationId;
	}

	@Override
	public LxPartnerListDto listLxPartner(Long taobaoUserId) {
		LxPartnerListDto res = new LxPartnerListDto();
		res.setMaxCount(getMaxCount());
		res.setLxPartners( partnerInstanceBO.getActiveLxListrByParentStationId(taobaoUserId));
		return res;
	}
	
	private Boolean isSync() {
		String isSync="y";
		try {
			isSync= Diamond.getConfig("auge.lx.isSync.alimama", "DEFAULT_GROUP", 3000);
		} catch (IOException e) {
			return Boolean.TRUE;
		}
		if ("y".equals(isSync)) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
		
		
		
	}
	
	private Boolean checkFkByTaobaoUserId(Long taobaoUserId) {
		String isCheck="y";
		try {
			isCheck= Diamond.getConfig("auge.lx.identifyrisk.asac", "DEFAULT_GROUP", 3000);
		} catch (IOException e) {
			logger.error("LxPartnerMobileService.checkFkByTaobaoUserId error! param:" + taobaoUserId, e);
			return Boolean.FALSE;
		}
		if ("y".equals(isCheck)) {
			com.taobao.mtee.rmb.RmbParameter param = new com.taobao.mtee.rmb.RmbParameter();
			param.setUserId(String.valueOf(taobaoUserId));
			param.setAsac("1A193134QEPGFXYYXX7IHG");
		    RmbResult rmbResult = rmbService.identifyRisk(param);
		    return  rmbResult.isSafe();
		}
		return Boolean.TRUE;
		
		
	}

	@Override
	public Boolean deleteByTaobaoUserId(Long taobaoUserId) {
		// 判断淘宝账号是否使用中
		PartnerStationRel pi = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null != pi && InstanceTypeEnum.LX.getCode().equals(pi.getType())) {
			partnerInstanceBO.deletePartnerStationRel(pi.getId(), "system");
			partnerBO.deletePartner(pi.getPartnerId(), "system");
			stationBO.deleteStation(pi.getStationId(), "system");
			return Boolean.TRUE;
		}
		throw new AugeBusinessException(LxErrorCodes.SYSTEM_ERROR_CODE, "当前账号没有找到");
	}

	@Override
	public Boolean closedByTaobaoUserId(Long taobaoUserId) {
		PartnerStationRel pi = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null != pi && InstanceTypeEnum.LX.getCode().equals(pi.getType())) {
			stationBO.changeState(pi.getStationId(), StationStatusEnum.SERVICING, StationStatusEnum.CLOSED,"system");
			partnerInstanceBO.changeState(pi.getId(), PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSED, "system");
			String pid = partnerAdzoneService.getUnionPid(taobaoUserId, pi.getStationId());
			if (StringUtils.isNotEmpty(pid)) {
				partnerAdzoneService.clearTairByPid(pid);
			}
			
			return Boolean.TRUE;

		}
		throw new AugeBusinessException(LxErrorCodes.SYSTEM_ERROR_CODE, "当前账号没有找到");
	}
}
