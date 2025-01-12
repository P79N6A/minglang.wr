package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.cainiao.cuntaonetwork.constants.station.StationStatus;
import com.alibaba.cainiao.cuntaonetwork.dto.station.StationDTO;
import com.alibaba.cainiao.cuntaonetwork.result.Result;
import com.alibaba.cainiao.cuntaonetwork.service.station.StationReadService;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.domain.StationTransInfo;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationDataCheckBO;
import com.taobao.cun.auge.station.bo.StationTransInfoBO;
import com.taobao.cun.auge.station.dto.StationTransInfoDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationTransInfoTypeEnum;

@Component("stationDataCheckBO")
public class StationDataCheckBOImpl implements StationDataCheckBO {

	private static final Logger logger = LoggerFactory
			.getLogger(StationDataCheckBO.class);
	@Autowired
	private StationReadService stationReadService;

	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	StationMapper stationMapper;

	public static final String ADDRESS_SPLIT = "^^^";

	@Autowired
	PartnerBO partnerBO;
	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	@Autowired
	StationTransInfoBO stationTransInfoBO;

	@Override
	public void checkAllWithCainiao(List<Long> stationIds) {
		List<Station> stationList = new ArrayList<Station>();

		if (CollectionUtils.isNotEmpty(stationIds)) {// 指定参数
			StationExample example = new StationExample();
			example.createCriteria().andIsDeletedEqualTo("n")// .andStatusIn(vaildStatus)//.andCreatorNotEqualTo(CREATOR)
					.andIdIn(stationIds);
			stationList = stationMapper.selectByExample(example);
			batchCheck(stationList);
		} else {
			StationExample example = new StationExample();
			example.createCriteria().andIsDeletedEqualTo("n");
			example.setOrderByClause("id asc");
			int count = stationMapper.countByExample(example);
			logger.info("check station begin,count={}", count);
			int pageSize = 200;
			int pageNum = 1;
			int total = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			while (pageNum <= total) {
				logger.info("check-station-doing {},{}", pageNum, pageSize);
				PageHelper.startPage(pageNum, pageSize);
				stationList = stationMapper.selectByExample(example);
				batchCheck(stationList);
				pageNum++;
			}
		}
		logger.info("check-station-finish");

	}

	private void batchCheck(List<Station> stationList) {
		if (CollectionUtils.isNotEmpty(stationList)) {
			for (Station ca : stationList) {
				try {
					check(ca);
				} catch (Exception e) {
					logger.error(
							"check asset error,asset="
									+ JSONObject.toJSONString(ca), e);
				}
			}
		}
	}

	private void check(Station a) {
		Long stationId = a.getId();
		CuntaoCainiaoStationRel cRel = cuntaoCainiaoStationRelBO
				.queryCuntaoCainiaoStationRel(stationId,
						CuntaoCainiaoStationRelTypeEnum.STATION);
		if (cRel == null) {
			return;
		}
		Result<StationDTO> stationResult = stationReadService
				.queryStationById(cRel.getCainiaoStationId());
		if (!stationResult.isSuccess()) {
			logger.error("check.cainiao.queryStationById error:" + stationId
					+ ":cainiaoStationId=" + cRel.getCainiaoStationId(),
					stationResult.getErrorMessage());
			return;
		}
		StationDTO stationDTO = stationResult.getData();

		PartnerStationRel rel = partnerInstanceBO
				.findPartnerInstanceByStationId(stationId);
		if (rel != null) {
			checkPartner(stationId, cRel, stationDTO, rel);
		}
		checkStatus(a, stationId, cRel, stationDTO);
		checkLatLng(a, stationId, cRel, stationDTO);
		checkStationNum(a, stationId, cRel, stationDTO);
		checkAddress(a, stationId, cRel, stationDTO);
	}

	private void checkPartner(Long stationId, CuntaoCainiaoStationRel cRel,
			StationDTO stationDTO, PartnerStationRel rel) {
		Partner p = partnerBO.getPartnerById(rel.getPartnerId());

		if (p.getMobile() != null) {
			if (!p.getMobile().equals(stationDTO.getMobile())) {
				logger.error("check.cainiao.mobile.error:" + stationId
						+ ":orgMobile=" + p.getMobile() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoMobile="
						+ stationDTO.getMobile());
			}
		}
		if (p.getName() != null) {
			if (!p.getName().equals(stationDTO.getContact())) {
				logger.error("check.cainiao.partnerName.error:" + stationId
						+ ":orgContact=" + p.getName() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoContact="
						+ stationDTO.getContact());
			}
		}
	}

	private void checkAddress(Station a, Long stationId,
			CuntaoCainiaoStationRel cRel, StationDTO stationDTO) {
		if (StringUtils.isNotEmpty(a.getProvince())) {
			Long province = Long.parseLong(a.getProvince());
			if (!province.equals(stationDTO.getProvinceId())) {
				logger.error("check.cainiao.ProvinceId.error:" + stationId
						+ ":orgProvinceId=" + a.getProvince()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoProviderId=" + stationDTO.getProvinceId());
			}
		} else {
			if (stationDTO.getProvinceId() != null) {
				logger.error("check.cainiao.ProvinceId.error:" + stationId
						+ ":orgProvinceId=" + a.getProvince()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoProviderId=" + stationDTO.getProvinceId());
			}
		}
		if (StringUtils.isNotEmpty(a.getCity())) {
			Long city = Long.parseLong(a.getCity());
			if (!city.equals(stationDTO.getCityId())) {
				logger.error("check.cainiao.city.error:" + stationId
						+ ":orgCity=" + a.getProviderId()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoCity=" + stationDTO.getCityId());
			}
		} else {
			if (stationDTO.getCityId() != null) {
				logger.error("check.cainiao.city.error:" + stationId
						+ ":orgCity=" + a.getProviderId()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoCity=" + stationDTO.getCityId());
			}
		}
		if (StringUtils.isNotEmpty(a.getCounty())) {
			Long county = Long.parseLong(a.getCounty());
			if (!county.equals(stationDTO.getCountyId())) {
				logger.error("check.cainiao.county.error:" + stationId
						+ ":orgCounty=" + a.getCounty() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoCounty="
						+ stationDTO.getCountyId());
			}
		} else {
			if (stationDTO.getCountyId() != null) {
				logger.error("check.cainiao.county.error:" + stationId
						+ ":orgCounty=" + a.getCounty() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoCounty="
						+ stationDTO.getCountyId());
			}
		}
		if (StringUtils.isNotEmpty(a.getTown())) {
			Long town = Long.parseLong(a.getTown());
			if (!town.equals(stationDTO.getTownId())) {
				logger.error("check.cainiao.town.error:" + stationId
						+ ":orgTown=" + a.getTown() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoTown="
						+ stationDTO.getTownId());
			}
		} else {
			if (stationDTO.getTownId() != null) {
				logger.error("check.cainiao.town.error:" + stationId
						+ ":orgTown=" + a.getTown() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoTown="
						+ stationDTO.getTownId());
			}
		}

		if (StringUtils.isNotEmpty(a.getVillage())) {
			Long village = Long.parseLong(a.getVillage());
			if (!village.equals(stationDTO.getCountryId())) {
				logger.error("check.cainiao.village.error:" + stationId
						+ ":orgVillage=" + a.getVillage()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoVillage=" + stationDTO.getCountryId());
			}
		} else {
			if (stationDTO.getCountryId() != null) {
				logger.error("check.cainiao.village.error:" + stationId
						+ ":orgVillage=" + a.getVillage()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoVillage=" + stationDTO.getCountryId());
			}
		}

		if (!getAddress(a).equals(stationDTO.getAddress())) {
			logger.error("check.cainiao.address.error:" + stationId
					+ ":orgAddress=" + getAddress(a) + ":cainiaoStationId="
					+ cRel.getCainiaoStationId() + ":cainiaoAddress="
					+ stationDTO.getAddress());
		}
	}

	private String getAddress(Station stationAddress) {
		StringBuilder address = new StringBuilder();
		if (stationAddress != null) {
			String villageDetail = "";
			if (!StringUtil.isBlank(stationAddress.getVillageDetail())) {
				villageDetail = stationAddress.getVillageDetail();
			}
			address.append(stationAddress.getProvinceDetail())
					.append(ADDRESS_SPLIT)
					.append(StringUtil.isBlank(stationAddress.getCityDetail()) ? " "
							: stationAddress.getCityDetail())
					.append(ADDRESS_SPLIT)
					.append(StringUtil.isBlank(stationAddress.getCountyDetail()) ? " "
							: stationAddress.getCountyDetail())
					.append(ADDRESS_SPLIT)
					.append(StringUtil.isBlank(stationAddress.getTownDetail()) ? " "
							: stationAddress.getTownDetail())
					.append(ADDRESS_SPLIT)
					.append(StringUtil.isBlank(stationAddress.getAddress()) ? " "
							: villageDetail + stationAddress.getAddress());
		}
		return address.toString();
	}

	private void checkStationNum(Station a, Long stationId,
			CuntaoCainiaoStationRel cRel, StationDTO stationDTO) {
		if (a.getStationNum() != null) {
			if (!a.getStationNum().equals(stationDTO.getCtCode())) {
				logger.error("check.cainiao.stationNum.error:" + stationId
						+ ":orgStationNum=" + a.getStationNum()
						+ ":cainiaoStationId=" + cRel.getCainiaoStationId()
						+ ":cainiaoStationNum=" + stationDTO.getCtCode());
			}
		}

		if (a.getName() != null) {
			String name = (a.getStationNum() == null ? "" : a.getStationNum())
					+ a.getName();
			if (!name.equals(stationDTO.getName())) {
				logger.error("check.cainiao.name.error:" + stationId
						+ ":orgName=" + name + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoName="
						+ stationDTO.getName());
			}
		}

	}

	private void checkLatLng(Station a, Long stationId,
			CuntaoCainiaoStationRel cRel, StationDTO stationDTO) {
		if (a.getLat() != null) {
			Double lat = Double.parseDouble(StringUtil.isEmpty(a.getLat()) ? "0" : PositionUtil
					.converDown(a.getLat()));
			if (stationDTO.getLat() == null || !lat.equals(Double.parseDouble(stationDTO.getLat()))) {
				logger.error("check.cainiao.Lat.error:" + stationId
						+ ":orgLat=" + lat + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoLat="
						+ stationDTO.getLat());
			}
		}
		if (a.getLng() != null) {
			Double lng = Double.parseDouble(StringUtil.isEmpty(a.getLng()) ? "0" : PositionUtil
					.converDown(a.getLng()));
			if (stationDTO.getLng() == null || !lng.equals(Double.parseDouble(stationDTO.getLng()))) {
				logger.error("check.cainiao.Lng.error:" + stationId
						+ ":orgLng=" + lng + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoLng="
						+ stationDTO.getLng());
			}
		}
	}

	private void checkStatus(Station a, Long stationId,
			CuntaoCainiaoStationRel cRel, StationDTO stationResult) {
		StationStatus stationStatus = stationResult.getStationStatus();
		if (StationStatusEnum.DECORATING.getCode().equals(a.getStatus())
				|| StationStatusEnum.SERVICING.getCode().equals(a.getStatus())) {
			if (StationStatus.NORMAL.value() != stationStatus.value()) {
				logger.error("check.cainiao.status.error:" + stationId
						+ ":orgstatus=" + a.getStatus() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoStatus="
						+ stationStatus.value());
			}

		} else if (StationStatusEnum.CLOSED.getCode().equals(a.getStatus())) {
			if (StationStatus.PAUSE.value() != stationStatus.value()) {
				logger.error("check.cainiao.status.error:" + stationId
						+ ":orgstatus=" + a.getStatus() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoStatus="
						+ stationStatus.value());
			}
		} else if (StationStatusEnum.QUIT.getCode().equals(a.getStatus())) {
			if (StationStatus.DELETE.value() != stationStatus.value()) {
				logger.error("check.cainiao.status.error:" + stationId
						+ ":orgstatus=" + a.getStatus() + ":cainiaoStationId="
						+ cRel.getCainiaoStationId() + ":cainiaoStatus="
						+ stationStatus.value());
			}
		}
	}

	@Override
	public void checkAllWithTrans(List<Long> insIds) {
		
		List<PartnerStationRel> relList = new ArrayList<PartnerStationRel>();
		if (CollectionUtils.isNotEmpty(insIds)) {// 指定参数
			PartnerStationRelExample example = new PartnerStationRelExample();
			example.createCriteria().andIsDeletedEqualTo("n").andIsCurrentEqualTo("y").andIdIn(insIds);
			relList = partnerStationRelMapper.selectByExample(example);
			batchCheckTrans(relList);
		} else {
			PartnerStationRelExample example = new PartnerStationRelExample();
			List<String> sList = new ArrayList<String>();
			sList.add("SERVICING");
			sList.add("DECORATING");
			List<String> tList = new ArrayList<String>();
			sList.add("WAIT_TRANS");
			sList.add("TRANS_ING");
			
			example.createCriteria().andIsDeletedEqualTo("n").andIsCurrentEqualTo("y").andStateIn(sList).andTypeEqualTo("TP")
			.andTransStatusIn(tList);
			example.setOrderByClause("id asc");
			int count = partnerStationRelMapper.countByExample(example);
			logger.info("check trans begin,count={}", count);
			int pageSize = 200;
			int pageNum = 1;
			int total = count % pageSize == 0 ? count / pageSize : count
					/ pageSize + 1;
			while (pageNum <= total) {
				logger.info("check-trans-doing {},{}", pageNum, pageSize);
				PageHelper.startPage(pageNum, pageSize);
				relList = partnerStationRelMapper.selectByExample(example);
				batchCheckTrans(relList);
				pageNum++;
			}
		}
		logger.info("check-trans-finish");
	}
	
	private void batchCheckTrans(List<PartnerStationRel> stationList) {
		if (CollectionUtils.isNotEmpty(stationList)) {
			for (PartnerStationRel ca : stationList) {
				try {
					checkTrans(ca);
				} catch (Exception e) {
					logger.error(
							"check trans error,asset="
									+ JSONObject.toJSONString(ca), e);
				}
			}
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	private void checkTrans(PartnerStationRel ca) {
		StationTransInfo st   = stationTransInfoBO.getLastTransInfoByStationId(ca.getStationId());
		if (st != null) {
			return;
		}
		if ("WAIT_TRANS".equals(ca.getTransStatus())) {
			saveTransInfo(ca);
		}else if ("TRANS_ING".equals(ca.getTransStatus())) {
			saveTransInfo(ca);
			stationTransInfoBO.changeTransing(ca.getStationId(), "system");
		}
	}
	
	 private void saveTransInfo(PartnerStationRel rel) {
	        StationTransInfoDto trDto = new StationTransInfoDto();
	        
	        trDto.setFromBizType(StationTransInfoTypeEnum.STATION_TO_YOUPIN.getFromBizType().getCode());
	        trDto.setToBizType(StationTransInfoTypeEnum.STATION_TO_YOUPIN.getToBizType().getCode());
	        trDto.setOldOpenDate(rel.getOpenDate());
	        trDto.setOperator("system");
	        trDto.setOperateTime(new Date());
	        trDto.setOperatorType("SYSTEM");
	        trDto.setRemark(StationTransInfoTypeEnum.STATION_TO_YOUPIN.getDescription());
	        trDto.setStationId(rel.getStationId());
	        trDto.setStatus(PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode());
	        trDto.setTaobaoUserId(String.valueOf(rel.getTaobaoUserId()));
	        trDto.setType(StationTransInfoTypeEnum.STATION_TO_YOUPIN.getType().name());
	        trDto.setIsModifyLnglat("n");
	        stationTransInfoBO.addTransInfo(trDto);
//	        if ("y".equals(transDto.getIsModifyLnglat())) {
//	            if (OperatorTypeEnum.BUC.getCode().equals(transDto.getOperatorType().getCode())) {
//	                try {
//	                    EnhancedUser enhancedUser = enhancedUserQueryService.getUser(transDto.getOperator());
//
//	                    CuntaoUserRole role = new CuntaoUserRole();
//	                    role.setCreator(transDto.getOperator());
//	                    role.setModifier(transDto.getOperator());
//	                    role.setOrgId(orgId);
//	                    role.setEndTime(DateUtil.addDays(new Date(), 7));
//	                    role.setRoleName("LNG_LAT_MANAGER");
//	                    role.setUserId(transDto.getOperator());
//	                    role.setUserName(enhancedUser.getLastName());
//	                    cuntaoUserRoleService.addCunUserRole(role);
//	                } catch (BucException e) {
//	                    logger.error("Query user failed, user id : " + transDto.getOperator(), e);
//	                }
//	            }
//	        }
	    }

}
