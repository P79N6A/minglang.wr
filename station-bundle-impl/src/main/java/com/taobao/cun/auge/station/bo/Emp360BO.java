package com.taobao.cun.auge.station.bo;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.dto.EmpInfoDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface Emp360BO {

	public static final String HR_HSF_APP_NAME = "cuntaobopsApp";

	public String getName(String workNo)throws AugeServiceException ;

	public Map<String, EmpInfoDto> getEmpInfoByWorkNos(List<String> workNos)throws AugeServiceException ;
}
