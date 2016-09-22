package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.support.Assert;

import com.ali.dowjones.service.constants.OrderItemBizStatus;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.FuwuProductService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.fuwu.dto.FuwuProductDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.PartnerOnlinePeixunStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamInstanceDto;
import com.taobao.cun.crius.exam.enums.ExamInstanceStatusEnum;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
/**
 * 
 * @author yi.shaoy
 *
 */
@Service("partnerPeixunService")
@HSFProvider(serviceInterface = PartnerPeixunService.class)
public class PartnerPeixunServiceImpl implements PartnerPeixunService{

	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	
	@Autowired
	AppResourceBO appResourceBO;
	
	@Autowired
	ExamInstanceService examInstanceService;
	
	@Autowired
	FuwuProductService fuwuProductService;
	
	@Autowired
	FuwuOrderService fuwuOrderService;
	
	@Autowired
	TrainingTicketServiceFacade trainingTicketServiceFacade;
	
	@Override
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds,String courseType,String courseCode) {
		return partnerPeixunBO.queryBatchPeixunRecord(userIds,courseType,courseCode);
	}

	@Override
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId,String courseType) {
		Assert.notNull(userId);
		String code=appResourceBO.queryAppValueNotAllowNull(courseType, "COURSE_CODE");
		String examId=appResourceBO.queryAppResourceValue(courseType, "EXAM_ID");
		String onlineCourseUrl=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN", "ONLINE_COURSE_URL");
		String examUrl=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN", "EXAM_URL");
		PartnerOnlinePeixunDto result=new PartnerOnlinePeixunDto();
		result.setCourseUrl(onlineCourseUrl+code);
		result.setTaobaoUserId(userId);
		result.setCourseCode(code);
		result.setExamUrl(examUrl+examId);
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
					&& ExamInstanceStatusEnum.PASS.getCode().equals(examResult.getResult().getStatus())) {
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
		if (!PartnerPeixunStatusEnum.NEW.getCode().equals(record.getStatus())) {
				result.setGmtDone(record.getGmtDone());
				result.setOrderNum(record.getOrderNum());
				result.setGmtOrder(record.getGmtCreate());
				//获取签到码
				result.setTicketNo(partnerPeixunBO.getPeixunTicket(userId,courseCode,record.getOrderNum()));
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
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		if(result.getGmtDone()!=null){
			result.setGmtDoneDesc(sdf.format(result.getGmtDone()));
		}
		if(result.getGmtOrder()!=null){
			result.setGmtOrderDesc(sdf.format(result.getGmtOrder()));
		}
		result.setCourseDetailUrl(appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN", "APPLY_COURSE_BUY_URL"));
		return result;
	}
	
	private FuwuProductDto getCourseDetail(String productCode){
		FuwuProductDto product=fuwuProductService.queryProductByCode(productCode);
		if(product==null){
			throw new AugeServiceException("course not find :"+productCode);
		}
		return product;
	}
	
	private List<FuwuOrderDto> getCourseOrders(Long userId,String productCode,Set<String> orderStatus){
		List<String> productCodes=new ArrayList<String>();
		productCodes.addAll(productCodes);
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
	
}
