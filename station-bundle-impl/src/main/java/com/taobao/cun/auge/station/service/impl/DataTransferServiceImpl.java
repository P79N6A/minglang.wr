package com.taobao.cun.auge.station.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
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
import org.esb.finance.service.audit.EsbFinanceAuditAdapter;
import org.esb.finance.service.contract.EsbFinanceContractAdapter;
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
import com.alibaba.ivy.common.PageDTO;
import com.alibaba.ivy.common.ResultDTO;
import com.alibaba.ivy.enums.TicketStatus;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.ivy.service.user.dto.TrainingRecordDTO;
import com.alibaba.ivy.service.user.dto.TrainingTicketDTO;
import com.alibaba.ivy.service.user.query.TrainingRecordQueryDTO;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerCourseRecordDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.DataTransferService;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.cun.crius.exam.dto.ExamInstanceDto;
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
	
	@Autowired
	PartnerPeixunService partnerPeixunService;
	
	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	
	@Autowired
	EsbFinanceContractAdapter esbFinanceContractAdapter;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	@Autowired 
	TrainingRecordServiceFacade trainingRecordServiceFacade;
	
	
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
		// 判断是否已经初始化过新培训记录
		PartnerCourseRecord pcr = partnerPeixunBO.queryOfflinePeixunRecord(dto
				.getPartnerUserId(), PartnerPeixunCourseTypeEnum.APPLY_IN,
				appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE",
						"APPLY_IN"));
		if (pcr == null) {
			// 初始化 新培训记录
			partnerPeixunBO.initPeixunRecord(dto.getPartnerUserId(),
					PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceBO
							.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE",
									"APPLY_IN"));
			partnerPeixunBO.initPeixunRecord(dto.getPartnerUserId(),
					PartnerPeixunCourseTypeEnum.UPGRADE, appResourceBO
							.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE",
									"UPGRADE"));
		}
		if (dto.getStatus().equals(PartnerPeixunStatusEnum.NEW.getCode())) {
			// 判断老订单是否下过单
			List<TrainingRecordDTO> trains = getRecordFromPeixun(
					dto.getCourseCode(), dto.getPartnerUserId());
			if (trains.size() == 0) {
				return true;
			}
		}
		// 判断是否已经下过订单，若下过，则直接返回true，若未下单，则下单
		String courseCode = appResourceBO.queryAppValueNotAllowNull(
				"PARTNER_PEIXUN_CODE", "APPLY_IN");
		Long userId = dto.getPartnerUserId();
		List<String> courseCodes = new ArrayList<String>();
		courseCodes.add(courseCode);
		List<FuwuOrderDto> orders = fuwuOrderService
				.queryOrdersByUserIdAndCode(userId, courseCodes, null);
		if (orders.size() > 0) {
			return true;
		} else {
			String mkey = appResourceBO.queryAppValueNotAllowNull(
					"CRM_ORDER_PARAM", "MKEY");
			fuwuOrderService.createOrderByPolicyId(userId, mkey, "0.0.0.0");
		}
		return true;
	}

	@Override
	public Boolean transferMoney(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())&&!record.getStatus().equals(PartnerPeixunStatusEnum.PAY.getCode())){
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
		auditDto.setBizObjectId("cuntao" + userId+System.currentTimeMillis());// 转款任务id，标示一次转款
		auditDto.setBizObject("CUNTAO_SHIFT_DRAFT");// 批示类型
		auditDto.setDomainFrom(BaseDto.BOSS);// 域
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
		toDto1.setDomainFrom(BaseDto.BOSS);
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
				auditDto, "cuntao" + userId+System.currentTimeMillis());
		if(!result.isSuccessed()){
			throw new AugeServiceException("transfer error "+result.getExceptionDesc());
		}
		//关闭财务订单
		ContractDto closeDto=new ContractDto();
		closeDto.setContractNo(oldOrderNum);
		closeDto.setStatus("E");
		closeDto.setDomainFrom(BaseDto.PALOS);
		closeDto.setDomainTo(BaseDto.FINANCE);
		closeDto.setExecAmount(new BigDecimal(0));
		ContractDto[] contractDtos=new ContractDto[1];
		contractDtos[0]=closeDto;
		EsbResultModel  closeResult= esbFinanceContractAdapter.modifyFinContractStatusAndServiceDate(contractDtos);
		if(!closeResult.isSuccessed()){
			throw new AugeServiceException("close Order error "+result.getExceptionDesc());
		}
		//更新财务订单执行金额
		EsbResultModel  exeResult= esbFinanceContractAdapter.modifyFinContractExecAmount(contractDtos);
		if(!exeResult.isSuccessed()){
			throw new AugeServiceException("update ExecuteAmont error "+result.getExceptionDesc());
		}
	}

	@Override
	public Boolean sign(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())){
			throw new AugeServiceException("培训记录状态不正确 "+String.valueOf(record.getPartnerUserId()));
		}
		//判断新的培训记录是不是已付款状态,若是，则获取签到码
		PartnerPeixunDto peixun=partnerPeixunService.queryOfflinePeixunProcess(dto.getPartnerUserId(), appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN_CODE", "APPLY_IN"), PartnerPeixunCourseTypeEnum.APPLY_IN);
        if(peixun!=null&&peixun.getStatus().equals(PartnerPeixunStatusEnum.PAY.getCode())){
        	String ticketNo=peixun.getTicketNo();
        	sign(ticketNo);
        }
		return null;
	}

	private List<TrainingRecordDTO> getRecordFromPeixun(String code, Long userId) {
	      AppAuthDTO auth = new AppAuthDTO();
	      auth.setAuthkey(peixunClientKey);
	      auth.setCode(peixunClientCode);
	      TrainingRecordQueryDTO query = new TrainingRecordQueryDTO();
	      query.addCourseCode(code);
	      query.addTrainee(String.valueOf(userId));
//	      query.addStatus(TrainStatus.NotEffect.value());
	      try {
	         ResultDTO<PageDTO<TrainingRecordDTO>> result = trainingRecordServiceFacade
	               .find(auth, query, 100, 1);
	         if (result.isSuccess()) {
	            return result.getData().getRows()==null?Lists.newArrayList():result.getData().getRows();
	         } else {
	            throw new RuntimeException("query record error,"
	                  + result.getMsg());
	         }
	      } catch (Exception e) {
	         throw new RuntimeException(e);
	      }
	   }
	
	private void sign(String ticketNo){
		 AppAuthDTO auth = new AppAuthDTO();
	      auth.setAuthkey(peixunClientKey);
	      auth.setCode(peixunClientCode);
		ResultDTO<TrainingTicketDTO> resultDTO = trainingTicketServiceFacade.getByTicketNo(auth, ticketNo);
	    if(!resultDTO.isSuccess() || resultDTO.getData() == null){
	        throw new IllegalStateException("券号无效");
	    }
	    TrainingTicketDTO trainingTicketDTO = resultDTO.getData();
	    if(trainingTicketDTO.getTicketStatus()!=TicketStatus.UnUsed){
	       return;
	    }
	    //修改ticket状态
	    trainingTicketDTO.setTicketStatus(TicketStatus.Used);
	    trainingTicketDTO.setSignDate(new Date());
	    ResultDTO<Boolean> resultDTOVar2 = trainingTicketServiceFacade.edit(auth, "system-SignIn", trainingTicketDTO);
	    if(!resultDTOVar2.isSuccess()){
	        throw new IllegalStateException("系统异常,签到失败,稍后重试"+resultDTOVar2.getMsg());
	    }
	}

	public static void main(String[] args) throws Exception{
		 BufferedReader b = new BufferedReader(new FileReader("D://shujuqianyi.txt"));
		 String l=null;
		 while((l=b.readLine())!=null){
		 String queryString = ("ticket="+l+"&&code=bc471");
         String lisReq = "http://cunxuexi.taobao.com/user/sign/signin.json"+"?"+queryString;
         HttpClient httpClient = new HttpClient();
         HttpMethod method = new GetMethod(lisReq);
         HttpClientParams params = new HttpClientParams();
         params.setConnectionManagerTimeout(3000);
         httpClient.setParams(params);
         try {
             httpClient.executeMethod(method);
             if(method.getStatusCode() == HttpStatus.SC_OK) {
            	 System.out.println("sign ok");
            	 System.out.println(method.getResponseBodyAsString());
             } else {
            	 System.out.println("error");
             }
         } catch (Exception e) {
        	 System.out.println("error");
         }
		 }
	}

	@Override
	public Long queryInstances(Long id,Long detailId) {
		if(id==null){
			return null;
		}
		Long returnLong=null;
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("id", id);
		param.put("detailId", detailId);
		List<ExamInstanceDto> instances=partnerCourseRecordMapper.queryExamInstanceList(param);
		if(instances.size()==0){
			return null;
		}else{
			for(ExamInstanceDto dto:instances){
				returnLong=dto.getId();
				param.clear();
				param.put("userId", dto.getUserId());
				param.put("status", dto.getUserType());
				param.put("point", dto.getPoint());
				partnerCourseRecordMapper.updateApplyExamPoint(param);
			}
		}
		return returnLong;
//		List<PartnerApply> applys=partnerCourseRecordMapper.testPartnerApplyQuery(null);
//		for(PartnerApply apply:applys){
//			int type=(int) (1+Math.random()*(5));
//			List<Map<String,Object>> params=getInit(apply.getTaobaoUserId(),type);
//			for(Map<String,Object> param:params){
//				try{
//				partnerCourseRecordMapper.testInsertExamInstance(param);
//				}catch(Exception e){
//					System.out.println(e);
//				}
//			}
//		}
//		return null;
	}
	
	private List<Map<String,Object>> getInit(Long userId,int type){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		if(type==1){
			//单个招募试卷试卷 三条考试记录，一条有效
			Map<String,Object> map1=getDefaultMap(userId);
			map1.put("paperId", "123");
		    map1.put("point", "50");
		    map1.put("status", "failed");
		    map1.put("bizType", "partner_apply");
		    map1.put("valid", "n");
		    result.add(map1);
		    Map<String,Object> map2=getDefaultMap(userId);
			map2.put("paperId", "123");
		    map2.put("point", "98");
		    map2.put("status", "pass");
		    map2.put("bizType", "partner_apply");
		    map2.put("valid", "n");
		    result.add(map2);
		    Map<String,Object> map3=getDefaultMap(userId);
			map3.put("paperId", "123");
		    map3.put("point", "100");
		    map3.put("status", "pass");
		    map3.put("bizType", "partner_apply");
		    result.add(map3);
		}else if(type==2){
			//两个招募试卷试卷 各三条考试记录，一条有效
			Map<String,Object> map1=getDefaultMap(userId);
			map1.put("paperId", "123");
		    map1.put("point", "50");
		    map1.put("status", "failed");
		    map1.put("bizType", "partner_apply");
		    map1.put("valid", "n");
		    result.add(map1);
		    Map<String,Object> map2=getDefaultMap(userId);
			map2.put("paperId", "123");
		    map2.put("point", "98");
		    map2.put("status", "pass");
		    map2.put("bizType", "partner_apply");
		    map2.put("valid", "n");
		    result.add(map2);
		    Map<String,Object> map3=getDefaultMap(userId);
			map3.put("paperId", "123");
		    map3.put("point", "100");
		    map3.put("status", "pass");
		    map3.put("bizType", "partner_apply");
		    result.add(map3);
		    Map<String,Object> map4=getDefaultMap(userId);
			map4.put("paperId", "1");
		    map4.put("point", "50");
		    map4.put("status", "failed");
		    map4.put("bizType", "partner_apply");
		    map4.put("valid", "n");
		    result.add(map4);
		    Map<String,Object> map5=getDefaultMap(userId);
			map5.put("paperId", "1");
		    map5.put("point", "98");
		    map5.put("status", "pass");
		    map5.put("bizType", "partner_apply");
		    map5.put("valid", "n");
		    result.add(map5);
		    Map<String,Object> map6=getDefaultMap(userId);
			map6.put("paperId", "1");
		    map6.put("point", "100");
		    map6.put("status", "pass");
		    map6.put("bizType", "partner_apply");
		    map6.put("valid", "n");
		    result.add(map6);
		}else if(type==3){
			//单个招募试卷，+活动试卷，一条有效
			Map<String,Object> map1=getDefaultMap(userId);
			map1.put("paperId", "123");
		    map1.put("point", "50");
		    map1.put("status", "failed");
		    map1.put("bizType", "partner_apply");
		    map1.put("valid", "n");
		    result.add(map1);
		    Map<String,Object> map2=getDefaultMap(userId);
			map2.put("paperId", "123");
		    map2.put("point", "98");
		    map2.put("status", "pass");
		    map2.put("bizType", "partner_apply");
		    map2.put("valid", "n");
		    result.add(map2);
		    Map<String,Object> map3=getDefaultMap(userId);
			map3.put("paperId", "123");
		    map3.put("point", "100");
		    map3.put("status", "pass");
		    map3.put("bizType", "partner_apply");
		    result.add(map3);
		    Map<String,Object> map4=getDefaultMap(userId);
			map4.put("paperId", "122");
		    map4.put("point", "50");
		    map4.put("status", "failed");
		    map4.put("bizType", "partner_activity");
		    map4.put("valid", "n");
		    result.add(map4);
		    Map<String,Object> map5=getDefaultMap(userId);
			map5.put("paperId", "122");
		    map5.put("point", "98");
		    map5.put("status", "pass");
		    map5.put("bizType", "partner_activity");
		    map5.put("valid", "n");
		    result.add(map5);
		    Map<String,Object> map6=getDefaultMap(userId);
			map6.put("paperId", "122");
		    map6.put("point", "100");
		    map6.put("status", "pass");
		    map6.put("bizType", "partner_activity");
		    result.add(map6);
		}else if(type==4){
			//无招募试卷，只有活动试卷，一条有效
			Map<String,Object> map4=getDefaultMap(userId);
			map4.put("paperId", "122");
		    map4.put("point", "50");
		    map4.put("status", "failed");
		    map4.put("bizType", "partner_activity");
		    map4.put("valid", "n");
		    result.add(map4);
		    Map<String,Object> map5=getDefaultMap(userId);
			map5.put("paperId", "122");
		    map5.put("point", "98");
		    map5.put("status", "pass");
		    map5.put("bizType", "partner_activity");
		    map5.put("valid", "n");
		    result.add(map5);
		    Map<String,Object> map6=getDefaultMap(userId);
			map6.put("paperId", "122");
		    map6.put("point", "100");
		    map6.put("status", "pass");
		    map6.put("bizType", "partner_activity");
		    result.add(map6);
		}else{
			//无考试记录
			return result;
		}
		return result;
	}
	
	private Map<String,Object> getDefaultMap(Long userId){
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("gmtCreate", new Date());
		result.put("gmtModified", new Date());
		result.put("creator", "test");
		result.put("modifier", "test");
		result.put("isDeleted", "n");
		result.put("taobaoNick","h" );
		result.put("userId", userId);
		result.put("valid", "y");
		return result;
	}
//	public static void main(String[] args) throws Exception{
//		 BufferedReader b = new BufferedReader(new FileReader("D://shujushengji1.txt"));
//		 PrintWriter pw1=new PrintWriter(new File("D://shujushengji2.txt"));
//		 String l=null;
//		 while((l=b.readLine())!=null){
//			 String[] temps=l.split(",");
//			 pw1.println("insert into partner_course_record (gmt_create, gmt_modified, creator,  modifier, is_deleted, course_type, course_code, status, partner_user_id)values(now(),now(),'datatransfer','datatransfer','n','APPLY_IN','bc471','NEW','"+temps[0]+"');");
//			 pw1.println("insert into partner_course_record (gmt_create, gmt_modified, creator,  modifier, is_deleted, course_type, course_code, status, partner_user_id)values(now(),now(),'datatransfer','datatransfer','n','UPGRADE','bc467','NEW','"+temps[0]+"');");
//             String sell=null;
//             if(temps[2].contains("500003")||temps[2].contains("500002")){
//            	 sell="2927051613";
//             }else{
//            	 sell="795246961";
//             }
//			 pw1.println("insert into station_decorate (gmt_create, gmt_modified, creator, modifier, is_deleted, station_id,  partner_user_id, seller_taobao_user_id, status,is_valid,payment_type,decorate_type)values (now(),now(),'yi.shaoy','yi.shaoy','n','"+temps[1]+"','"+temps[0]+"', '"+sell+"','UNDECORATE','Y','SELF','NEW');");
//		     pw1.flush();
//		 }
//	}
}
