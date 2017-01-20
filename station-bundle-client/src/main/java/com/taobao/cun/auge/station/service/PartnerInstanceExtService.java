package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;

public interface PartnerInstanceExtService {
	
	/**
	 * 根据合伙人的村点的id，查询子成员最大名额
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long partnerStationId);
	
	/**
	 * 根据合伙人的村点的id，查询子成员最大名额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerMaxChildNumByInsId(Long instanceId);

	/**
	 * 校验下一级是否超过名额限制，没有超过返回false，超过，返回true
	 * 
	 * @param parentStationId
	 *            父站点id
	 * 
	 * @return
	 */
	public Boolean validateChildNum(Long parentStationId);

	/**
	 * 查询合伙人扩展信息
	 * 
	 * @param instanceIds
	 *            合伙人实例id
	 * @return
	 */
	public List<PartnerInstanceExtDto> findPartnerExtInfos(List<Long> instanceIds);
	
	 /**
	   * 在原有的值上，添加increaseNum个子成员，最大不超过@see#PartnerInstanceExtConstant.MAX_CHILD_NUM
	   * 
	   * @param instanceId
	   * @param increaseNum
	   * @param reason
	   * @param operatorDto
	   */
	  public void addPartnerMaxChildNum(Long instanceId, Integer increaseNum, PartnerMaxChildNumChangeReasonEnum reason,
	      OperatorDto operatorDto);
	  
	  /**
	   * 在原有的值上，减少decreaseNum个子成员，最少不少于@see#TpaGmvCheckConfiguration.defaultTpaNum4Tp
	   * @param instanceId
	   * @param decreaseNum
	   * @param reason
	   * @param operatorDto
	   */
	  public void decreasePartnerMaxChildNum(Long instanceId, Integer decreaseNum, PartnerMaxChildNumChangeReasonEnum reason,
		      OperatorDto operatorDto);
	  
	  /**
	   * 修改最大子成员配额，最大不超过@see#TpaGmvCheckConfiguration.maxTpaNum4Tp
	   * 
	   * @param instanceId
	   * @param maxChildNum
	   *            修改后的值
	   * @param reason
	   * @param operatorDto
	   */
	  public void updatePartnerMaxChildNum(PartnerChildMaxNumUpdateDto updateDto);
	  
	  /**
	   * 初始化最大子成员配额
	   * 
	   * @param instanceId
	   * @param initMaxChildNum 初始化值，最大不超过@see#TpaGmvCheckConfiguration.maxTpaNum4Tp
	   * @param operatorDto
	   */
	  public void initPartnerMaxChildNum(Long instanceId, Integer initMaxChildNum, OperatorDto operatorDto);
	
}
