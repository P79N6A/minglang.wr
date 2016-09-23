package com.taobao.cun.auge.station.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.station.dto.StationEnterStatusDto;
import com.taobao.cun.auge.station.dto.StationQuitStatusDto;
import com.taobao.cun.auge.station.dto.StationStatisticDto;
import com.taobao.cun.auge.station.dto.StationStatusDto;
import com.taobao.cun.auge.station.enums.ProcessedStationStatusEnum;

public class ProcessedStationStatusConverter {

	private static final Map<String,String> mapping=new HashMap<String,String>();
	static{
		mapping.put("ING_SETTLING_SIGNING_WAIT_FROZEN_WAIT_PROCESS",ProcessedStationStatusEnum.SUMITTED.getCode());
		mapping.put("ING_SETTLING_SIGNED_WAIT_FROZEN_WAIT_PROCESS",ProcessedStationStatusEnum.CONFIRMED.getCode());
		mapping.put("COR_NEW",ProcessedStationStatusEnum.UNPAY_COURSE.getCode());
		mapping.put("DEC_UNDECORATE",ProcessedStationStatusEnum.UNPAY_DECORATE.getCode());
		mapping.put("COR_PAY",ProcessedStationStatusEnum.UNSIGNED.getCode());
		mapping.put("DEC_WAIT_AUDIT",ProcessedStationStatusEnum.DEC_WAIT_AUDIT.getCode());
		mapping.put("ED_SERVICING",ProcessedStationStatusEnum.SERVICING.getCode());
		mapping.put("ING_CLOSING",ProcessedStationStatusEnum.QUIT_APPLYING.getCode());
		mapping.put("ING_CLOSING_TO_AUDIT",ProcessedStationStatusEnum.QUIT_APPLYING.getCode());
		mapping.put("ING_CLOSING_TO_START",ProcessedStationStatusEnum.QUIT_APPLYING.getCode());
		mapping.put("ED_CLOSED",ProcessedStationStatusEnum.QUIT_APPLY_CONFIRMED.getCode());
		mapping.put("ING_QUITING_TO_AUDIT_WAIT_THAW",ProcessedStationStatusEnum.QUITAUDITING.getCode());
		mapping.put("ING_QUITING_AUDIT_PASS_WAIT_THAW",ProcessedStationStatusEnum.CLOSED_WAIT_THAW.getCode());
		mapping.put("ED_QUIT",ProcessedStationStatusEnum.QUIT.getCode());
		mapping.put("ED_DECORATING", ProcessedStationStatusEnum.DECORATING.getCode());
		mapping.put("DEC_DECORATING", ProcessedStationStatusEnum.DEC_FEEDING_BACK.getCode());
	}
	
	
	public static StationStatisticDto toProcessedStationStatusDtos(List<ProcessedStationStatus> statusList){
		Map<String,Integer> map=new HashMap<String,Integer>();
		for (ProcessedStationStatus p : statusList) {
			String key = mapping.get(p.getStatus());
			if(key!=null){
				if(map.containsKey(key)){
					Integer val=map.get(key);
					val=val+p.getCount();
					map.put(key, val);
				}
				else{
					map.put(key, p.getCount());
				}
			}
			
		}

		StationStatisticDto stationStatisticDto =new StationStatisticDto();
		
		StationEnterStatusDto enterDto=new StationEnterStatusDto();
		enterDto.setConfirmed(getStatusDto(map,ProcessedStationStatusEnum.CONFIRMED));
		enterDto.setDecorating(getStatusDto(map,ProcessedStationStatusEnum.DECORATING));
		enterDto.setUnpayDecorate(getStatusDto(map,ProcessedStationStatusEnum.UNPAY_DECORATE));
		enterDto.setUnpayCourse(getStatusDto(map,ProcessedStationStatusEnum.UNPAY_COURSE));
		enterDto.setUnsigned(getStatusDto(map,ProcessedStationStatusEnum.UNSIGNED));
		enterDto.setDecFeedingBack(getStatusDto(map,ProcessedStationStatusEnum.DEC_FEEDING_BACK));
		enterDto.setDecWaitAudit(getStatusDto(map,ProcessedStationStatusEnum.DEC_WAIT_AUDIT));
		enterDto.setServicing(getStatusDto(map,ProcessedStationStatusEnum.SERVICING));
		
		StationQuitStatusDto quitDto=new StationQuitStatusDto();
		quitDto.setQuitApplying(getStatusDto(map,ProcessedStationStatusEnum.QUIT_APPLYING));
		quitDto.setQuitApplyConfirmed(getStatusDto(map,ProcessedStationStatusEnum.QUIT_APPLY_CONFIRMED));
		quitDto.setQuitAuditing(getStatusDto(map,ProcessedStationStatusEnum.QUITAUDITING));
		quitDto.setClosedWaitThaw(getStatusDto(map,ProcessedStationStatusEnum.CLOSED_WAIT_THAW));
		quitDto.setQuit(getStatusDto(map,ProcessedStationStatusEnum.QUIT));
		
		stationStatisticDto.setEnterFlow(enterDto);
		stationStatisticDto.setQuitFlow(quitDto);
		return stationStatisticDto;
	}


	private static StationStatusDto getStatusDto(Map<String, Integer> map,ProcessedStationStatusEnum statusEnum) {
		StationStatusDto statusDto=new StationStatusDto();
		statusDto.setStatus(statusEnum);
		Integer sumittedCnt = map.get(statusEnum.getCode());
		statusDto.setCount(sumittedCnt==null?0:sumittedCnt);
		return statusDto;
	}

	
}
