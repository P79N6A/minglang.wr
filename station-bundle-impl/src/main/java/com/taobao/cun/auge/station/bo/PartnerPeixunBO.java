package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.station.condition.PartnerPeixunQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunStatusCountDto;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.notify.message.ObjectMessage;
import com.taobao.notify.message.StringMessage;

/**
 * 合伙人培训服务
 * @author yi.shaoy
 *
 */
public interface PartnerPeixunBO {
	
	/**
	 * 根据培训类型、课程code初始化培训记录,若存在，不重复创建
	 */
	public PartnerCourseRecord initPeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode);

	/**
	 * 处理培训签到消息
	 * @param strMessage
	 * @param ob
	 */
	public void handlePeixunFinishSucess(StringMessage strMessage, JSONObject ob);
	/**
	 * 处理培训订单付款成功消息
	 * @param strMessage
	 * @param ob
	 */
	public void handlePeixunPaymentProcess(StringMessage strMessage, JSONObject ob);
	
	
	public List<PartnerPeixunDto> queryBatchPeixunRecord(List<Long> userIds,String courseType,String courseCode);
	
	public void dispatchApplyInExamPaper(Long userId, String taobaoNick,String paperId);
	
	/**
	 * 删除培训记录,仅处理未下单记录
	 * @param userId
	 */
	public void invalidPeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode);
	
	/**
	 * 查询在线培训记录
	 * @param userId
	 * @param courseCode
	 * @return
	 */
	public PartnerPeixunDto queryOnlineCourseRecord(Long userId, String courseCode);
			
	/**
	 * 根据课程code、userId、课程类型查询线下课程的培训记录 
	 */
	public PartnerCourseRecord queryOfflinePeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode);
	
	/**
	 * 获取签到码
	 * @param userId
	 * @param courseCode
	 * @param orderNum
	 * @return
	 */
	public String getPeixunTicket(Long userId,String courseCode,String orderNum);
	
	/**
	 * 
	 */
    public List<PartnerPeixunDto> queryBatchOnlinePeixunProcess(Long userId, List<String> courseCodes);
	
    public List<PartnerPeixunStatusCountDto> queryPeixunCountByCondition(
			PartnerPeixunQueryCondition condition);
    
    public PageDto<PartnerPeixunListDetailDto> queryPeixunList(PartnerPeixunQueryCondition condition);
    
    public String  commitRefund(Long taobaoUserId,String refundReason,String operator,Long applyOrg);
    
    public void refundAuditExecute(Long id,boolean auditResult);
    
    /**
	 * 处理退款完成消息
	 * @param strMessage
	 * @param ob
	 */
	public void handleRefundFinishSucess(ObjectMessage objMessage);
	
	public PartnerPeixunDto queryPeixunRecordById(Long id);
	
	public void validateQuitable(Long taobaoUserId);
}
