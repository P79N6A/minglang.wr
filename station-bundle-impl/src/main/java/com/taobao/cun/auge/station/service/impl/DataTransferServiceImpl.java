package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.esb.finance.service.audit.EsbFinanceAuditAdapter;
import org.mule.esb.model.tcc.result.EsbResultModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ali.dowjones.service.constants.OrderItemBizStatus;
import com.alibaba.crm.finance.dataobject.BaseDto;
import com.alibaba.crm.finance.dataobject.RefundShiftApplyType;
import com.alibaba.crm.finance.dataobject.audit.AuditDto;
import com.alibaba.crm.finance.dataobject.contract.ContractDto;
import com.alibaba.crm.finance.dataobject.draft.RefundOrShiftDraftMaterialDto;
import com.alibaba.crm.finance.dataobject.draft.ShiftDraftMaterialDetailDto;
import com.alibaba.ivy.common.AppAuthDTO;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.dto.PartnerCourseRecordDto;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.DataTransferService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("dataTransferService")
@HSFProvider(serviceInterface = DataTransferService.class)
public class DataTransferServiceImpl implements DataTransferService{

	@Autowired
	PartnerCourseRecordMapper partnerCourseRecordMapper;
	
	@Autowired
	AppResourceBO appResourceBO;
	
	@Autowired
	FuwuOrderService fuwuOrderService;
	
	@Autowired
	EsbFinanceAuditAdapter esbFinanceAuditAdapter;
	
	@Autowired
	TrainingTicketServiceFacade trainingTicketServiceFacade;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	
	@Override
	public List<PartnerCourseRecordDto> getAllRecords(String status,String courseCode) {
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		criteria.andCourseCodeEqualTo(courseCode);
		criteria.andStatusEqualTo(status);
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		List<PartnerCourseRecordDto> result=new ArrayList<PartnerCourseRecordDto>();
		for(PartnerCourseRecord record:records){
			PartnerCourseRecordDto dto=new PartnerCourseRecordDto();
			BeanUtils.copyProperties(record, dto);
			result.add(dto);
		}
		return result;
	}

	@Override
	public Boolean createOrder(PartnerCourseRecordDto dto) {
		//判断是否已经下过订单，若下过，则直接返回true，若未下单，则下单
		String courseCode=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE", "APPLY_IN");
		Long userId=dto.getPartnerUserId();
		List<String> courseCodes=new ArrayList<String>();
		courseCodes.add(courseCode);
		List<FuwuOrderDto> orders=fuwuOrderService.queryOrdersByUserIdAndCode(userId, courseCodes, null);
		if(orders.size()>0){
			return true;
		}else{
			String mkey=appResourceBO.queryAppValueNotAllowNull("CRM_ORDER_PARAM", "MKEY");
			fuwuOrderService.createOrderByPolicyId(userId, mkey,"0.0.0.0");
		}
		return true;
	}

	@Override
	public Boolean transferMoney(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())||!record.getStatus().equals(PartnerPeixunStatusEnum.PAY.getCode())){
			throw new AugeServiceException("培训记录状态不正确 "+String.valueOf(record.getPartnerUserId()));
		}
		//判断新订单是否已经创建
		String courseCodeNew=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE", "APPLY_IN");
		Long userId=dto.getPartnerUserId();
		List<String> courseCodes=new ArrayList<String>();
		courseCodes.add(courseCodeNew);
		Set<String> orderStatus=new HashSet<String>();
		orderStatus.add(OrderItemBizStatus.Before.getValue());
		List<FuwuOrderDto> orders=fuwuOrderService.queryOrdersByUserIdAndCode(userId, courseCodes, orderStatus);
		if(orders.size()==0){
			throw new AugeServiceException("新订单未创建"+String.valueOf(record.getPartnerUserId()));
		}
		FuwuOrderDto order=orders.get(0);
		transfer(dto.getOrderNum()+"_1",order.getOrderItemNo(),dto.getPartnerUserId());
		return null;
	}
	
	private void transfer(String oldOrderNum,String newOrderNum,Long userId){
		AuditDto auditDto = new AuditDto();
		auditDto.setBizObjectId("cuntao" + userId);// 转款任务id，标示一次转款
		auditDto.setBizObject("EXECUTE_SHIFT_NO_AUDIT");// 批示类型
		auditDto.setDomainFrom(BaseDto.MARTINI);// 域
		auditDto.setApplyDate(new Date());// 申请时间
		auditDto.setApplier("cuntao");// 申请人

		RefundOrShiftDraftMaterialDto shiftDraftMaterialDto = new RefundOrShiftDraftMaterialDto();
		shiftDraftMaterialDto.setComments("村淘启航班转款");
		shiftDraftMaterialDto.setApplyType(RefundShiftApplyType.SHIFT_DRAFT
				.toString());
		List<ShiftDraftMaterialDetailDto> dtoList = new ArrayList<ShiftDraftMaterialDetailDto>();
		ShiftDraftMaterialDetailDto dto1 = new ShiftDraftMaterialDetailDto();
		dto1.setType("contractShift");
		ContractDto fromDto1 = new ContractDto();
		fromDto1.setContractNo(oldOrderNum);
		fromDto1.setDomainFrom(BaseDto.PALOS);
		dto1.setFromContract(fromDto1);
		ContractDto toDto1 = new ContractDto();
		toDto1.setContractNo(newOrderNum);
		toDto1.setDomainFrom(BaseDto.MARTINI);
		List<ContractDto> contractList = new ArrayList<ContractDto>();
		contractList.add(toDto1);
		dto1.setToContracts(contractList);
		List<BigDecimal> amounts = new ArrayList<BigDecimal>();
		amounts.add(new BigDecimal(1500));
		dto1.setAmounts(amounts);
		dtoList.add(dto1);
		shiftDraftMaterialDto.setShiftDraftMaterialDetailDtoList(dtoList);
		List<RefundOrShiftDraftMaterialDto> refundShiftList = new ArrayList<RefundOrShiftDraftMaterialDto>();
		refundShiftList.add(shiftDraftMaterialDto);
		auditDto.setRefundOrShiftDraftMaterialDtoList(refundShiftList);
		EsbResultModel result = esbFinanceAuditAdapter.addAuditAndMaterial(
				auditDto, "cuntao" + userId);
		if(!result.isSuccessed()){
			throw new AugeServiceException("transfer error "+result.getExceptionDesc());
		}
	}

	@Override
	public Boolean sign(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())){
			throw new AugeServiceException("培训记录状态不正确 "+String.valueOf(record.getPartnerUserId()));
		}
		//判断新的培训记录是不是已付款状态
		String courseCode=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE", "APPLY_IN");
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		criteria.andCourseCodeEqualTo(courseCode);
		criteria.andStatusEqualTo("PAY");
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records==null||records.size()==0){
			throw new AugeServiceException("未找到已付款的新培训记录  "+String.valueOf(dto.getPartnerUserId()));
		}
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
//		ResultDTO<Boolean> result=trainingTicketServiceFacade.edit(auth, "cuntao_trans", TrainingTicketDTO trainingTicket);
		return null;
	}


}
