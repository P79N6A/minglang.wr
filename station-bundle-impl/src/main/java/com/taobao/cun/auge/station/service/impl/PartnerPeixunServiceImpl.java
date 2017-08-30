package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ali.dowjones.service.constants.OrderItemBizStatus;
import com.alibaba.fastjson.JSON;
import com.taobao.common.category.util.StringUtil;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseSchedule;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.FuwuProductService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.fuwu.dto.FuwuProductDto;
import com.taobao.cun.auge.station.bo.PartnerCourseScheduleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.condition.PartnerPeixunQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunStatusCountDto;
import com.taobao.cun.auge.station.enums.PartnerOnlinePeixunStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunRefundStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamInstanceDto;
import com.taobao.cun.crius.exam.dto.UserDispatchDto;
import com.taobao.cun.crius.exam.enums.ExamInstanceStatusEnum;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 
 * @author yi.shaoy
 *
 */
@Service("partnerPeixunService")
@HSFProvider(serviceInterface = PartnerPeixunService.class)
public class PartnerPeixunServiceImpl implements PartnerPeixunService{
	private static final Logger logger = LoggerFactory.getLogger(PartnerPeixunService.class);

	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	
	@Autowired
	AppResourceService appResourceService;
	
	@Autowired
	ExamInstanceService examInstanceService;
	
	@Autowired
	FuwuProductService fuwuProductService;
	
	@Autowired
	FuwuOrderService fuwuOrderService;
	
	@Autowired
	ExamUserDispatchService examUserDispatchService;
	@Autowired
	PartnerInstanceQueryService partnerInstanceQueryService;
	@Autowired
	PartnerCourseScheduleBO partnerCourseScheduleBO;
	
	@Value("${partner.peixun.sign.url}")
	private String peixunSignUrl;
	@Override
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds,String courseType,String courseCode) {
		return partnerPeixunBO.queryBatchPeixunRecord(userIds,courseType,courseCode);
	}

	@Override
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId,String courseType) {
		Assert.notNull(userId);
		String code=appResourceService.queryAppResourceValue(courseType, "COURSE_CODE");
		String examId=appResourceService.queryAppResourceValue(courseType, "EXAM_ID");
		String onlineCourseUrl=appResourceService.queryAppResourceValue("PARTNER_PEIXUN", "ONLINE_COURSE_URL");
		String examUrl=appResourceService.queryAppResourceValue("PARTNER_PEIXUN", "EXAM_URL");
		PartnerOnlinePeixunDto result=new PartnerOnlinePeixunDto();
		result.setCourseUrl(onlineCourseUrl+code);
		result.setTaobaoUserId(userId);
		result.setCourseCode(code);
		result.setExamUrl(examUrl+examId);
		//针对2.0用户特殊判断，若没有分发试卷，则返回null
		if(StringUtil.isNotEmpty(examId)){
			//判断是否分发过试卷
			ResultModel<UserDispatchDto> dp=examUserDispatchService.queryExamUserDispatch(new Long(examId), userId);
			if(dp.isSuccess()&&dp.getResult()==null){
				return null;
			}
		}
		// 查询在线培训记录;
		PartnerPeixunDto peixunDto=partnerPeixunBO.queryOnlineCourseRecord(userId,code);
		if(peixunDto==null){
			result.setStatus(PartnerOnlinePeixunStatusEnum.WAIT_PEIXUN);
		}else if(StringUtils.isEmpty(examId)){
			//不需要融入考试状态
			result.setStatus(PartnerOnlinePeixunStatusEnum.DONE);
		}else{
			// 查询考试成绩
			ResultModel<ExamInstanceDto> examResult = examInstanceService
					.queryValidInstance(userId, new Long(examId));
			if (examResult.isSuccess() && examResult.getResult() != null
					&& ExamInstanceStatusEnum.PASS.getCode().equals(examResult.getResult().getStatus().getCode())) {
				result.setStatus(PartnerOnlinePeixunStatusEnum.DONE);
			} else {
				result.setStatus(PartnerOnlinePeixunStatusEnum.WAIT_EXAM);
			}
		}
		return result;
	}

	@Override
	public PartnerPeixunDto queryOfflinePeixunProcess(Long userId,
			String courseCode, PartnerPeixunCourseTypeEnum courseType) {
		PartnerCourseRecord record=partnerPeixunBO.queryOfflinePeixunRecord(userId,courseType,courseCode);
		if(record==null){
			return null;
		}
		PartnerPeixunDto result = new PartnerPeixunDto();
		result.setUserId(userId);
		// 获取课程信息
		FuwuProductDto product=getCourseDetail(courseCode);
		result.setCourseName(product.getName());
		result.setCourseAmount(new BigDecimal(product.getPrice()));
		result.setCourseCode(courseCode);
		result.setLogo(product.getIcon());
		result.setStatus(record.getStatus());
		result.setStatusDesc(PartnerPeixunStatusEnum.valueof(record.getStatus()).getDesc());
		result.setRefundStatus(record.getRefundStatus());
		if(StringUtils.isNotEmpty(record.getRefundStatus())){
		 result.setRefundStatusDesc(PartnerPeixunRefundStatusEnum.valueof(record.getRefundStatus()).getDesc());
		}
		result.setRefundNo(record.getRefundNo());
		result.setRefundReason(record.getRefundReason());
		result.setId(record.getId());
		if (!PartnerPeixunStatusEnum.NEW.getCode().equals(record.getStatus())) {
			List<FuwuOrderDto> orders = getCourseOrders(userId, courseCode,
					null);
			// 获得订单售卖价格
			for (FuwuOrderDto order : orders) {
				if (order.getOrderItemNo().equals(record.getOrderNum())) {
					result.setCourseAmount(order.getExecutePrice());
					break;
				}
			}
			result.setGmtDone(record.getGmtDone());
			result.setOrderNum(record.getOrderNum());
			result.setGmtOrder(record.getGmtCreate());
			// 获取签到码
			result.setTicketNo(partnerPeixunBO.getPeixunTicket(userId,courseCode, record.getOrderNum()));
		}else{
			//查询有没有未付款订单信息
			Set<String> orderStatus=new HashSet<String>();
			orderStatus.add(OrderItemBizStatus.Before.getValue());
			List<FuwuOrderDto> orders=getCourseOrders(userId,courseCode,orderStatus);
			if(orders.size()>0){
				result.setOrderNum(orders.get(0).getOrderNo());
				result.setStatus(PartnerPeixunStatusEnum.WAIT_PAY.getCode());
				result.setStatusDesc(PartnerPeixunStatusEnum.WAIT_PAY.getDesc());
				result.setGmtOrder(orders.get(0).getGmtOrder());
				result.setCourseAmount(orders.get(0).getExecutePrice());
				result.setPayUrl(orders.get(0).getPayUrl());
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		if(result.getGmtDone()!=null){
			result.setGmtDoneDesc(sdf.format(result.getGmtDone()));
		}
		if(result.getGmtOrder()!=null){
			result.setGmtOrderDesc(sdf.format(result.getGmtOrder()));
		}
		//组装下单地址
		result.setCourseDetailUrl(appResourceService.queryAppResourceValue("PARTNER_PEIXUN", "APPLY_COURSE_BUY_URL"));
		//组装退款地址
		result.setRefundUrl(appResourceService.queryAppResourceValue("PARTNER_PEIXUN", "ACCOUNT_REFUND_URL")+"?ali_id="+record.getPartnerUserId()+"&site_id=CN&system=CUNTAO");
		return result;
	}
	
	private FuwuProductDto getCourseDetail(String productCode){
		FuwuProductDto product=fuwuProductService.queryProductByCode(productCode);
		if(product==null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"course not find :"+productCode);
		}
		return product;
	}
	
	private List<FuwuOrderDto> getCourseOrders(Long userId,String productCode,Set<String> orderStatus){
		List<String> productCodes=new ArrayList<String>();
		productCodes.add(productCode);
		return fuwuOrderService.queryOrdersByUserIdAndCode(userId, productCodes, orderStatus);
	}

	@Override
	public Map<String, PartnerOnlinePeixunDto> queryBatchOnlinePeixunProcess(
			Long userId, List<String> courseCodes) {
		Assert.notNull(userId);
		Assert.notNull(courseCodes);
		List<PartnerPeixunDto> dtos= partnerPeixunBO.queryBatchOnlinePeixunProcess(userId, courseCodes);
		Map<String,PartnerOnlinePeixunDto> result=new HashMap<String,PartnerOnlinePeixunDto>();
		l1:for(String code:courseCodes){
			if(result.get(code)!=null){
				continue;
			}
			PartnerOnlinePeixunDto re=new PartnerOnlinePeixunDto();
			re.setCourseCode(code);
			for(PartnerPeixunDto dto:dtos){
				if(dto.getCourseCode().equals(code)){
					re.setStatus(PartnerOnlinePeixunStatusEnum.DONE);
					result.put(code, re);
					continue l1;
				}
			}
			re.setStatus(PartnerOnlinePeixunStatusEnum.WAIT_PEIXUN);
			result.put(code, re);
		}
		return result;
	}

	@Override
	public List<PartnerPeixunStatusCountDto> queryPeixunCountByCondition(
			PartnerPeixunQueryCondition condition) {
		return partnerPeixunBO.queryPeixunCountByCondition(condition);
	}

	@Override
	public PageDto<PartnerPeixunListDetailDto> queryPeixunList(
			PartnerPeixunQueryCondition condition) {
		try{
			return partnerPeixunBO.queryPeixunList(condition);
		}catch(Exception e){
			return null;
		}
		
	}

	@Override
	public Boolean checkCourseViewPermission(Long taobaoUserId,
			String courseCode) {
		Assert.notNull(taobaoUserId);
		// 判断是否合伙人身份
		PartnerInstanceDto dto = partnerInstanceQueryService
				.getActivePartnerInstance(taobaoUserId);
		if (dto == null) {
			return false;
		}
		if (StringUtils.isNotEmpty(courseCode)) {
			// 判断是否课程表可播放时间段
			List<PartnerCourseSchedule> schedules = partnerCourseScheduleBO
					.getScheduleByCourseCode(courseCode);
			if (schedules == null || schedules.size() == 0) {
				return true;
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String nowDate = dateFormat.format(new Date());
				for (PartnerCourseSchedule schedule : schedules) {
					if (dateFormat.format(schedule.getGmtCourse()).equals(
							nowDate)) {
						if (new Date().before(schedule.getGmtEnd())
								&& new Date().after(schedule.getGmtStart())) {
							return true;
						}
					}
				}
				return false;
			}
		}
		return true;
	}

	@Override
    public String  commitRefund(Long taobaoUserId, String refundReason, String operator, Long applyOrg){
		Assert.notNull(taobaoUserId);
		Assert.notNull(refundReason);
		Assert.notNull(operator);
		Assert.notNull(applyOrg);
		return partnerPeixunBO.commitRefund(taobaoUserId,refundReason,operator,applyOrg);
	}

	@Override
	public PartnerPeixunDto queryPeixunRecordById(Long id){
		Assert.notNull(id);
		return partnerPeixunBO.queryPeixunRecordById(id);
	}
	
	public void sign(String ticketNo,String courseType,Long loginUserId,String poNo){
		String courseCode=validateSignAndGetCourseCode(ticketNo,courseType,loginUserId,poNo);
		if(StringUtils.isEmpty(courseCode)){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"未找到匹配的培训订单");
		}
		//调用crm进行签到
		signToCrm(ticketNo,courseCode,poNo);
	}
	
	@SuppressWarnings("unchecked")
	private void signToCrm(String ticketNo,String courseCode,String poNo){
		 String queryString = ("ticket="+ticketNo+"&code="+courseCode+"&rtnMsg="+poNo);
//         String lisReq = "http://cunxuexi.taobao.com/user/sign/signin.json"+"?"+queryString;
		 String signUrl=peixunSignUrl+"?"+queryString;
         HttpClient httpClient = new HttpClient();
         HttpMethod method = new GetMethod(signUrl);
         HttpClientParams params = new HttpClientParams();
         params.setConnectionManagerTimeout(3000);
         httpClient.setParams(params);
         try {
             httpClient.executeMethod(method);
             if(method.getStatusCode() == HttpStatus.SC_OK) {
            	 Map<String,Object> json= (Map<String, Object>) JSON.parse(method.getResponseBodyAsString());
            	 Map<String,Object> json1=(Map<String, Object>) json.get("content");
            	 if(!(Boolean)json1.get("success")){
            		 String errMsg="签到失败";
            		 String errorDate=String.valueOf(json1.get("data"));
            		 if("INVALID_TICKET".equals(errorDate)){
            			 errMsg="无效券号";
            		 }else if("HAVE_SIGNED".equals(errorDate)){
            			 errMsg="已经签到";
            		 }else if("INVALID_CODE".equals(errorDate)){
            			 errMsg="无效课程code";
            		 }
                 	throw new AugeBusinessException(AugeErrorCodes.PEIXUN_SIGN_BUSINESS_CHECK_ERROR_CODE,"签到失败:"+errMsg);
            	 }
             } else {
            	throw new AugeBusinessException(AugeErrorCodes.PEIXUN_SIGN_BUSINESS_CHECK_ERROR_CODE,"签到失败");
             }
         } catch (Exception e) {
        	logger.error("signToCrm error", e);
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_SIGN_BUSINESS_CHECK_ERROR_CODE,e.getMessage());
         }
    }
	
	private String validateSignAndGetCourseCode(String ticketNo,String courseType,Long loginUserId,String poNo){
		if(StringUtils.isEmpty(ticketNo)){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"签到码不能为空");
		}
		if(StringUtils.isEmpty(poNo)){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"该签到页面已过期,请访问最新签到页面");
		}
		if(loginUserId==null){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"用户信息获取失败");
		}
		List<AppResourceDto> apps=appResourceService.queryAppResourceList("PARTNER_PEIXUN_CODE");
		for(AppResourceDto app:apps){
			if(app.getName().equals(courseType)){
				String courseCode=app.getValue();
				PartnerCourseRecord record=partnerPeixunBO.queryOfflinePeixunRecord(loginUserId, PartnerPeixunCourseTypeEnum.valueof(courseType), courseCode);
				if(record==null){
					return null;
				}
				if(PartnerPeixunStatusEnum.DONE.getCode().equals(record.getStatus())){
					throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"已经签到，请勿重复签到");
				}else if(!PartnerPeixunStatusEnum.PAY.getCode().equals(record.getStatus())){
					throw new AugeBusinessException(AugeErrorCodes.PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE,"订单状态不正确，无法签到");
				}
				return record.getCourseCode();
			}
		}
		return null;
	}


}
