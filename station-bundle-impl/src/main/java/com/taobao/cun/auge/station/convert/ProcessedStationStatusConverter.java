package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.station.dto.ProcessedStationStatusDto;
import com.taobao.cun.auge.station.enums.ProcessedStationStatusEnum;

public class ProcessedStationStatusConverter {

	private static final HashMap<String,String> mapping=new HashMap<String,String>();
	static{
		mapping.put("SETTLING_SIGNING_WAIT_FROZEN_WAIT_PROCESS",ProcessedStationStatusEnum.SUMITTED.getCode());
		mapping.put("SETTLING_SIGNED_WAIT_FROZEN_WAIT_PROCESS",ProcessedStationStatusEnum.CONFIRMED.getCode());
		mapping.put("NEW",ProcessedStationStatusEnum.UNPAY_COURSE.getCode());
		mapping.put("UNDECORATE",ProcessedStationStatusEnum.UNPAY_DECORATE.getCode());
		mapping.put("PAY",ProcessedStationStatusEnum.UNSIGNED.getCode());
		mapping.put("DECORATING",ProcessedStationStatusEnum.DEC_WAIT_AUDIT.getCode());
		mapping.put("SERVICING",ProcessedStationStatusEnum.SERVICING.getCode());
		mapping.put("CLOSING",ProcessedStationStatusEnum.QUIT_APPLYING.getCode());
		mapping.put("CLOSING_TO_AUDIT",ProcessedStationStatusEnum.QUIT_APPLYING.getCode());
		mapping.put("CLOSED",ProcessedStationStatusEnum.QUIT_APPLY_CONFIRMED.getCode());
		mapping.put("QUITING_TO_AUDIT_WAIT_THAW",ProcessedStationStatusEnum.QUITAUDITING.getCode());
		mapping.put("QUITING_AUDIT_PASS_WAIT_THAW",ProcessedStationStatusEnum.CLOSED_WAIT_THAW.getCode());
		mapping.put("QUIT",ProcessedStationStatusEnum.QUIT.getCode());
	}
	
	
	public static List<ProcessedStationStatusDto> toProcessedStationStatusDtos(List<ProcessedStationStatus> statusList){
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		for (ProcessedStationStatus p : statusList) {
			System.out.println(p.getStatus());
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
		System.out.println(map.size());
		List<ProcessedStationStatusDto> list=new ArrayList<>();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String processedStationStatus=iter.next();
			Integer count = map.get(processedStationStatus);
			ProcessedStationStatusDto dto=new ProcessedStationStatusDto();
			dto.setProcessedStationStatus(processedStationStatus);
			dto.setCount(count);
			list.add(dto);
		}
		return list;
	}
}
