package com.taobao.cun.auge.insurance;

public interface CutaoInsuranceService {


	public Boolean hasInsurance(Long taobaoUserId);
	
	public Integer hasReInsurance(Long taobaoUserId);
	
}
