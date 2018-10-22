package com.taobao.cun.auge.insurance;

import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;

public interface CuntaoInsuranceService {

	PersonInfoDto queryPersonInfo(Long taobaoUserId, String cpCode);

	BusinessInfoDto queryBusinessInfo(Long taobaoUserId, String cpCode);

	Boolean hasInsurance(Long taobaoUserId);
	
	Integer hasReInsurance(Long taobaoUserId);

}
