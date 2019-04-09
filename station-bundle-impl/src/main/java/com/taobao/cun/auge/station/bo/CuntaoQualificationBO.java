package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;


public interface CuntaoQualificationBO {

	public CuntaoQualification getCuntaoQualificationByTaobaoUserId(Long taobaoUserId);
	
	public void updateQualification(CuntaoQualification cuntaoQualification);
	
	public void saveQualification(CuntaoQualification cuntaoQualification);
	
	public Page<CuntaoQualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition);
	
	public void deletedQualificationById(Long id);
	 
	public void submitLocalQualification(CuntaoQualification qualification);
	 
	public void reSubmitLocalQualification(CuntaoQualification qualification);
	 
	public void submitHavanaQualification(Long taobaoUserId);
	
	public List<CuntaoQualification> queryHistoriesByTaobaoUserId(Long taobaoUserId);

	/**
	 * 判断营业执照是否认证通过
	 * @param taobaoUserId
	 */
	public Boolean checkValidQualification(Long taobaoUserId);

}
