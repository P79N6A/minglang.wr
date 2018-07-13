package com.taobao.cun.auge.fence.cainiao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cainiao.dms.sorting.api.IRailService;
import com.cainiao.dms.sorting.api.hsf.model.BaseResult;
import com.cainiao.dms.sorting.common.dataobject.rail.RailBusinessTag;
import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;
import com.cainiao.dms.sorting.common.dataobject.rail.RailKeyword;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.constant.FenceConstants;

/**
 * 调用菜鸟围栏接口
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class RailServiceAdapterImpl implements RailServiceAdapter {
	@Resource
	private IRailService railService;
	
	@Override
	public Long addCainiaoFence(FenceEntity fenceEntity) {
		BaseResult<Long> result = railService.addRail(toCainiaoFence(fenceEntity));
		if(result.isSuccess()) {
			return result.getResult();
		}else {
			throw new RailException("code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	@Override
	public void updateCainiaoFence(FenceEntity fenceEntity) {
		BaseResult<Boolean> result = railService.updateRailById(toCainiaoFence(fenceEntity));
		if(!result.isSuccess()) {
			throw new RailException("code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	@Override
	public void deleteCainiaoFence(Long cainiaoFenceId) {
		RailInfoRequest request = new RailInfoRequest();
		request.setId(cainiaoFenceId);
		BaseResult<Boolean> result = railService.deleteRailById(request);
		if(!result.isSuccess()) {
			throw new RailException("code=" + result.getErrorCode() + ",msg=" + result.getErrorMsg());
		}
	}

	private RailInfoRequest toCainiaoFence(FenceEntity fenceEntity) {
		RailInfoRequest request = new RailInfoRequest();
		request.setId(fenceEntity.getCainiaoFenceId());
		request.setRailType(fenceEntity.getType());
		request.setRailName(fenceEntity.getName());
		request.setRailBusinessTags(buildRailBusinessTags(fenceEntity));
		request.setStatus(fenceEntity.getState().equals(FenceConstants.ENABLE) ? 1 : 2);
		request.setSiteId(fenceEntity.getCainiaoFenceId());
		request.setProvinceId(Long.valueOf(fenceEntity.getProvince()));
		request.setCityId(Long.valueOf(fenceEntity.getCity()));
		request.setCountryId(Long.valueOf(fenceEntity.getCounty()));
		if(!Strings.isNullOrEmpty(fenceEntity.getTown())) {
			request.setAreaId(Long.valueOf(fenceEntity.getTown()));
		}
		buildRange(request, fenceEntity);
		return request;
	}
	
	private void buildRange(RailInfoRequest request, FenceEntity fenceEntity) {
		if(!Strings.isNullOrEmpty(fenceEntity.getRangeRule())) {
			Range range = JSON.parseObject(fenceEntity.getRangeRule(), Range.class);
			if(range.getDistance() != null) {
				request.setDistance(range.getDistance());
			}
			
			List<RailKeyword> keywords = Lists.newArrayList();
			if(!Strings.isNullOrEmpty(range.getDivision())) {
				RailKeyword keyword = new RailKeyword();
				keyword.setKeyword(range.getDivision());
				keyword.setTownId(Long.parseLong(fenceEntity.getTown()));
				keywords.add(keyword);
			}
			
			if(range.getMatch() != null) {
				range.getMatch().forEach((k, v)->{
					RailKeyword keyword = new RailKeyword();
					keyword.setKeyword(k);
					keyword.setTownId(Long.parseLong(fenceEntity.getTown()));
					keywords.add(keyword);
				});
			}
			
			request.setKeywords(keywords);
			
			if(range.getLngLat() != null) {
				request.setLongitude(range.getLngLat().lng);
				request.setLatitude(range.getLngLat().lat);
			}
		}
	}
	
	private List<RailBusinessTag> buildRailBusinessTags(FenceEntity fenceEntity){
		List<RailBusinessTag> railBusinessTags = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(fenceEntity.getUserType())) {
			RailBusinessTag tag = new RailBusinessTag();
			tag.setTagKey("userType");
			tag.setTagValue(fenceEntity.getUserType());
			railBusinessTags.add(tag);
		}
		
		if(!Strings.isNullOrEmpty(fenceEntity.getCommodity())) {
			RailBusinessTag tag = new RailBusinessTag();
			tag.setTagKey("commodity");
			tag.setTagValue(fenceEntity.getCommodity());
			railBusinessTags.add(tag);
		}
		
		return railBusinessTags;
	}
	
	static class Range{
		private Long distance;
		
		private Map<String, String> match;
		
		private LngLat lngLat;
		
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

		public LngLat getLngLat() {
			return lngLat;
		}

		public void setLngLat(LngLat lngLat) {
			this.lngLat = lngLat;
		}
	}
	
	static class LngLat{
		String lng;
		
		String lat;

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}
	}
}
