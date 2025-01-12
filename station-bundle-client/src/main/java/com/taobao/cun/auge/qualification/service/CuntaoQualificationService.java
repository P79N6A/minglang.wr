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
	
	boolean reSubmitLocalQualification(Qualification qualification);
	
	public PageDto<Qualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
	
	C2BSettleInfo queryC2BSettleInfo(Long taobaoUserId);
	
	List<String>  querySubmitedQualifications();
	
    Qualification queryHavanaC2BQualification(Long taobaoUserId);

    public void syncInvalidQuali(int pageSize);
    
    public void invalidQualification(Long taobaoUserId);
    
    public void recoverQualification(Long taobaoUserId);
    
    public Qualification querEnterpriceC2BQualification(Long taobaoUserId);
    

    public Qualification queryLocalQualification(Long taobaoUserId);

    List<Qualification> queryHistoryQualification(Long taobaoUserId);
    
    
    public void updateQualificationStatus(Long taobaoUserId,Integer status);

	/**
	 * 判断营业执照是否认证通过
	 * @param taobaoUserId
	 */
	public Boolean checkValidQualification(Long taobaoUserId);

}
