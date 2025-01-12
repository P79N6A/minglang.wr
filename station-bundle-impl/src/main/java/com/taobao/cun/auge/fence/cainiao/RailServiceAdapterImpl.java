package com.taobao.cun.auge.fence.cainiao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cainiao.dms.sorting.api.IRailService;
import com.cainiao.dms.sorting.api.hsf.model.BaseResult;
import com.cainiao.dms.sorting.api.model.RailInfoResult;
import com.cainiao.dms.sorting.common.constant.SortingConstants;
import com.cainiao.dms.sorting.common.dataobject.rail.RailAreaCover;
import com.cainiao.dms.sorting.common.dataobject.rail.RailBusinessTag;
import com.cainiao.dms.sorting.common.dataobject.rail.RailDistance;
import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;
import com.cainiao.dms.sorting.common.dataobject.rail.RailKeyword;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.POIUtils;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.common.util.BeanCopy;

/**
 * 调用菜鸟围栏接口
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class RailServiceAdapterImpl implements RailServiceAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${cainiao.fence.cpcode}")
	private String cpcode;
	@Resource
	private IRailService railService;
	
	private static Long COUNTRY_ID = 86L;

	private RailInfoResult getCainiaoFence(Long cainianFenceId){
		BaseResult<RailInfoResult> result = null;
		try {
			result = railService.getRailInfoById(cainianFenceId);
		}catch(Exception e){
			throw Throwables.propagate(e);
		}

		if(result.isSuccess()){
			return result.getResult();
		}else{
			return null;
		}
	}
	@Override
	public boolean isExistsCainiaoFence(Long cainianFenceId){
		return getCainiaoFence(cainianFenceId) != null;
	}

	@Override
	public String getCainiaoFenceJSON(Long cainianFenceId){
		RailInfoResult result = getCainiaoFence(cainianFenceId);
		if(result != null){
			return JSON.toJSONString(result);
		}else{
			return null;
		}
	}

	@Override
	public Long addCainiaoFence(FenceEntity fenceEntity) {
		RailInfoRequest request = toCainiaoFence(fenceEntity);
		logger.info("request:{}", JSON.toJSONString(request));
		BaseResult<Long> result = railService.addRail(request);
		if(result.isSuccess()) {
			return result.getResult();
		}else {
			throw new RailException(request, "addRail:code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	@Override
	public void updateCainiaoFence(FenceEntity fenceEntity) {
		RailInfoRequest request = toCainiaoFence(fenceEntity);
		logger.info("request:{}", JSON.toJSONString(request));
		BaseResult<Boolean> result = railService.updateRailById(request);
		if(!result.isSuccess()) {
			throw new RailException(request, "updateRailById:code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	@Override
	public void deleteCainiaoFence(Long cainiaoFenceId) {
		RailInfoRequest request = new RailInfoRequest();
		request.setId(cainiaoFenceId);
		request.setCpCode(cpcode);
		BaseResult<Boolean> result = railService.deleteRailById(request);
		if(!result.isSuccess()) {
			throw new RailException(request, "deleteRailById:code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	private RailInfoRequest toCainiaoFence(FenceEntity fenceEntity) {
		RailInfoRequest request = new RailInfoRequest();
		if(fenceEntity.getCainiaoFenceId() != null && fenceEntity.getCainiaoFenceId() > 0) {
			request.setId(fenceEntity.getCainiaoFenceId());
		}
		request.setRailType(fenceEntity.getType());
		request.setRailName(fenceEntity.getName());
		request.setRailBusinessTags(buildRailBusinessTags(fenceEntity));
		request.setStatus(fenceEntity.getState().equals(FenceConstants.ENABLE) ? SortingConstants.UseStatus.STATUS_ENABLED : SortingConstants.UseStatus.STATUS_DISABLED);
		request.setSiteId("CUNTAO_" + fenceEntity.getCainiaoStationId());
		request.setCountryId(COUNTRY_ID);
		request.setProvinceId(Long.valueOf(fenceEntity.getProvince()));
		request.setCityId(Long.valueOf(fenceEntity.getCity()));
		request.setCpCode(cpcode);
		buildRange(request, fenceEntity);
		return request;
	}
	
	@SuppressWarnings("unchecked")
	private void buildRange(RailInfoRequest request, FenceEntity fenceEntity) {
		RailDistance railDistance = new RailDistance();
		railDistance.setDistance(-1L);
		railDistance.setLongitude(String.valueOf(POIUtils.toStanardPOI(fenceEntity.getLng())));
		railDistance.setLatitude(String.valueOf(POIUtils.toStanardPOI(fenceEntity.getLat())));
		if(!Strings.isNullOrEmpty(fenceEntity.getCounty())) {
			railDistance.setAreaId(Long.valueOf(fenceEntity.getCounty()));
		}else {
			railDistance.setAreaId(Long.valueOf(fenceEntity.getCity()));
		}
		
		if(!Strings.isNullOrEmpty(fenceEntity.getRangeRule())) {
			Range range = JSON.parseObject(fenceEntity.getRangeRule(), Range.class);
			if(range.getDistance() != null) {
				railDistance.setDistance(range.getDistance());
			}
			
			List<RailKeyword> keywords = Lists.newArrayList();
			if(!Strings.isNullOrEmpty(range.getDivision())) {
				Map<String, String> division = JSON.parseObject(range.getDivision(), Map.class);
				RailAreaCover railAreaCover = new RailAreaCover();
				railAreaCover.setAreaName(division.get("name"));
				railAreaCover.setAreaId(Long.parseLong(division.get("code")));
				request.setRailAreaCover(railAreaCover);
			}
			
			if(range.getMatch() != null) {
				range.getMatch().forEach((k, v)->{
					RailKeyword keyword = new RailKeyword();
					keyword.setKeyword(v);
					keyword.setTownId(Long.parseLong(fenceEntity.getTown()));
					keywords.add(keyword);
				});
			}
			
			request.setKeywords(keywords);
		}
		request.setRailDistance(railDistance);
	}
	
	@Override
	public void updateCainiaoFenceState(FenceEntity fenceEntity) {
		RailInfoResult request = new RailInfoResult();
		request.setId(fenceEntity.getCainiaoFenceId());
		request.setCpCode(cpcode);
		request.setStatus(fenceEntity.getState().equals(FenceConstants.ENABLE) ? SortingConstants.UseStatus.STATUS_ENABLED : SortingConstants.UseStatus.STATUS_DISABLED);
		BaseResult<Boolean> result = railService.updateRailStatusById(request);
		if(!result.isSuccess()) {
			throw new RailException(BeanCopy.copy(RailInfoRequest.class, request), "updateRailStatusById:code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private List<RailBusinessTag> buildRailBusinessTags(FenceEntity fenceEntity){
		List<RailBusinessTag> railBusinessTags = Lists.newArrayList();
		RailBusinessTag versionTag = new RailBusinessTag();
		versionTag.setTagKey("version");
		versionTag.setTagValue(String.valueOf(fenceEntity.getVersion()));
		railBusinessTags.add(versionTag);
		
		if(!Strings.isNullOrEmpty(fenceEntity.getUserType())) {
			RailBusinessTag tag = new RailBusinessTag();
			tag.setTagKey("userType");
			tag.setTagValue(fenceEntity.getUserType());
			railBusinessTags.add(tag);
		}
		
		if(!Strings.isNullOrEmpty(fenceEntity.getDefaultCheck())) {
			RailBusinessTag tag = new RailBusinessTag();
			tag.setTagKey("defaultCheck");
			tag.setTagValue(fenceEntity.getDefaultCheck());
			railBusinessTags.add(tag);
		}
		
		if(!Strings.isNullOrEmpty(fenceEntity.getCommodity())) {
			Map<String, Object> commodities = JSON.parseObject(fenceEntity.getCommodity(), Map.class);
			commodities.forEach((k,v)->{
				RailBusinessTag tag = new RailBusinessTag();
				tag.setTagKey(k);
				if(v instanceof Iterable) {
					tag.setTagValue(Joiner.on(",").join((Iterable<?>)v));
				}else {
					tag.setTagValue(v.toString());
				}
				
				railBusinessTags.add(tag);
			});
		}
		
		return railBusinessTags;
	}
	
	static class Range{
		private Long distance;
		
		private Map<String, String> match;
		
		private String division;

		public String getDivision() {
			return division;
		}

		public void setDivision(String division) {
			this.division = division;
		}

		public Long getDistance() {
			return distance;
		}

		public void setDistance(Long distance) {
			this.distance = distance;
		}

		public Map<String, String> getMatch() {
			return match;
		}

		public void setMatch(Map<String, String> match) {
			this.match = match;
		}
	}
}
