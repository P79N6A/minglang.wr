package com.taobao.cun.auge.station.transfer;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJob;
import com.taobao.cun.auge.dal.domain.CountyStationTransferJobExample;
import com.taobao.cun.auge.dal.mapper.CountyStationTransferJobMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.convert.CountyStationConverter;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferDetail;
import com.taobao.cun.auge.station.transfer.dto.TransferJob;
import com.taobao.cun.common.util.BeanCopy;

/**
 * 交接任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TransferJobBo {
	@Resource
	private CountyStationTransferJobMapper countyStationTransferJobMapper;
	@Resource
	private CountyStationBO countyStationBO;
	@Resource
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	@Resource
	private StationQueryService stationQueryService;
	
	public CountyStationTransferDetail getCountyStationTransferDetail(Long id) {
		CountyStationTransferJob countyStationTransferJob = countyStationTransferJobMapper.selectByPrimaryKey(id);
		
		CountyStationTransferDetail countyStationTransferDetail = new CountyStationTransferDetail();
		countyStationTransferDetail.setType(countyStationTransferJob.getType());
		if(!Strings.isNullOrEmpty(countyStationTransferJob.getAttachments())) {
			countyStationTransferDetail.setAttachments(Splitter.on(',')
					.splitToList(countyStationTransferJob.getAttachments()));
		}
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationTransferJob.getCountyStationId());
		countyStationTransferDetail.setCountyStation(CountyStationConverter.toCountyStationDto(countyStation));
		
		CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(countyStationTransferJob.getTargetTeamOrgId());
		countyStationTransferDetail.setTargetTeamOrgFullNamePath(cuntaoOrgDto.getFullNamePath());
		countyStationTransferDetail.setTargetTeamOrgId(cuntaoOrgDto.getId());
		
		List<String> stationIdList = Splitter.on(',').splitToList(countyStationTransferJob.getStationIdList());
		
		List<Long> stationIds = Lists.newArrayList();
		for(String stationId : stationIdList) {
			stationIds.add(Long.valueOf(stationId));
		}
		countyStationTransferDetail.setStations(stationQueryService.queryStations(stationIds));
		return countyStationTransferDetail;
	}
	
	public List<CountyStationTransferJob> getCountyStationTransferJobs(Long countyId, String type, String state) {
		CountyStationTransferJobExample example = new CountyStationTransferJobExample();
		example.createCriteria().andCountyStationIdEqualTo(countyId).andTypeEqualTo(type).andStateEqualTo(state);
		return countyStationTransferJobMapper.selectByExample(example);
	}
	
	public void updateTransferJobState(Long id, String state) {
		CountyStationTransferJob record = new CountyStationTransferJob();
		record.setId(id);
		record.setState(state);
		record.setGmtModified(new Date());
		countyStationTransferJobMapper.updateByPrimaryKeySelective(record);
	}
	
	public CountyStationTransferJob createCountyStationTransferJob(TransferJob transferJob) {
		CountyStationTransferJob countyStationTransferJob = BeanCopy.copy(CountyStationTransferJob.class, transferJob);
		countyStationTransferJob.setGmtCreate(new Date());
		countyStationTransferJob.setGmtModified(new Date());
		countyStationTransferJob.setState("NEW");
		countyStationTransferJobMapper.insert(countyStationTransferJob);
		return countyStationTransferJob;
	}
}
