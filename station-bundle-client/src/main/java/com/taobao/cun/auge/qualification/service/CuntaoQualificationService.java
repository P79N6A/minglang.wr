package com.taobao.cun.auge.qualification.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;

public interface CuntaoQualificationService {

	
	public void syncCuntaoQulificationFromMetaq(Long taobaoUserId,Long qualiId,int eidType);
	
	public void syncCuntaoQulification(Long taobaoUserId);
	
	Qualification queryC2BQualification(Long taobaoUserId);
	
	boolean submitLocalQualification(Qualification qualification);
	
	void submitHavanaQualification(Long taobaoUserId);
	
	public PageDto<Qualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
	
	C2BSettleInfo queryC2BSettleInfo(Long taobaoUserId);
	
	List<String>  querySubmitedQualifications();
	
    Qualification queryHavanaC2BQualification(Long taobaoUserId);

    public void syncInvalidQuali(int pageSize);
    
    public void invalidQualification(Long taobaoUserId);
    
    public void recoverQualification(Long taobaoUserId);
    
    public Qualification querEnterpriceC2BQualification(Long taobaoUserId);
}
