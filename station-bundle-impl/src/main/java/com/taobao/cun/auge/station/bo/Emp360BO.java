package com.taobao.cun.auge.station.bo;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.station.dto.EmpInfoDto;

public interface Emp360BO {

	public static final String HR_HSF_APP_NAME = "cuntaobopsApp";

	public String getName(String workNo);

	public Map<String, EmpInfoDto> getEmpInfoByWorkNos(List<String> workNos);
}
