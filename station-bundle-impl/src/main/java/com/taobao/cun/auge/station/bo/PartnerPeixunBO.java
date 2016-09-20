package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.notify.message.StringMessage;

/**
 * 合伙人培训服务
 * @author yi.shaoy
 *
 */
public interface PartnerPeixunBO {
	
	/**
	 * 初始化入驻培训记录
	 * @param userId
	 * @return
	 */
	public PartnerCourseRecord initPartnerApplyInRecord(Long userId);
	
	/**
	 * 根据培训类型、课程code初始化培训记录,若存在，不重复创建
	 */
	public PartnerCourseRecord initPeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode);

	/**
	 * 处理crm培训平台培训订单消息
	 * @param strMessage
	 * @param ob
	 */
	public void handlePeixunProcess(StringMessage strMessage, JSONObject ob);
	
	/**
	 * 根据合伙人id查询入驻培训记录
	 * @param userId
	 * @return
	 */
	public PartnerPeixunDto queryApplyInPeixunRecord(Long userId);
	
	public List<PartnerPeixunDto> queryBatchPeixunRecord(List<Long> userIds);
	
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId);
	
	public void dispatchApplyInExamPaper(Long userId, String taobaoNick);
	
	/**
	 * 删除培训记录,仅处理未下单记录
	 * @param userId
	 */
	public void invalidPeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode);

}
