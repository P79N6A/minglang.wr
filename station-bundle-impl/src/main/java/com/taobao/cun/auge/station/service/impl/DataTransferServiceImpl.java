package com.taobao.cun.auge.station.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.esb.finance.service.audit.EsbFinanceAuditAdapter;
import org.esb.finance.service.contract.EsbFinanceContractAdapter;
import org.mule.esb.model.tcc.result.EsbResultModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ali.dowjones.service.constants.OrderItemBizStatus;
import com.alibaba.china.member.service.MemberReadService;
import com.alibaba.china.member.service.models.MemberModel;
import com.alibaba.crm.finance.dataobject.BaseDto;
import com.alibaba.crm.finance.dataobject.RefundShiftApplyType;
import com.alibaba.crm.finance.dataobject.audit.AuditDto;
import com.alibaba.crm.finance.dataobject.contract.ContractDto;
import com.alibaba.crm.finance.dataobject.draft.RefundOrShiftDraftMaterialDto;
import com.alibaba.crm.finance.dataobject.draft.ShiftDraftMaterialDetailDto;
import com.alibaba.fastjson.JSON;
import com.alibaba.ivy.common.AppAuthDTO;
import com.alibaba.ivy.common.PageDTO;
import com.alibaba.ivy.common.ResultDTO;
import com.alibaba.ivy.enums.TicketStatus;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.ivy.service.user.dto.TrainingRecordDTO;
import com.alibaba.ivy.service.user.dto.TrainingTicketDTO;
import com.alibaba.ivy.service.user.query.TrainingRecordQueryDTO;
import com.alibaba.organization.api.orgstruct.enums.OrgStructStatus;
import com.alibaba.organization.api.orgstruct.model.OrgStructModel;
import com.alibaba.organization.api.orgstruct.param.OrgStructBaseParam;
import com.alibaba.organization.api.orgstruct.param.OrgStructPostParam;
import com.alibaba.organization.api.orgstruct.param.QueryOrgStructParam;
import com.alibaba.organization.api.orgstruct.service.OrgStructReadService;
import com.alibaba.organization.api.orgstruct.service.OrgStructWriteService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.fuwu.dto.FuwuOrderDto;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerCourseRecordDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.DataTransferService;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.notify.remotingclient.NotifyManagerBean;



/**
 * 
 * 此类为项目数据初始化临时类，在预发环境执行
 *
 */
@Service("dataTransferService")
@HSFProvider(serviceInterface = DataTransferService.class)
public class DataTransferServiceImpl implements DataTransferService{

	@Autowired
	PartnerCourseRecordMapper partnerCourseRecordMapper;
	
	@Autowired
	private AppResourceService appResourceService;
	
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
	OrgStructWriteService orgStructWriteService;
	@Autowired
	MemberReadService memberReadService;

	@Value("${cbu.market.parent_code}")
	private Long parentId;
	@Autowired
	OrgStructReadService orgStructReadService;
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired 
	TrainingRecordServiceFacade trainingRecordServiceFacade;
	
    @Autowired
    PartnerStationRelMapper partnerStationRelMapper;
	
    @Autowired
    PartnerProtocolRelMapper partnerProtocolRelMapper;
    
    @Autowired
    StationMapper stationMapper;
    
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
				appResourceService.queryAppResourceValue("PARTNER_PEIXUN_CODE",
						"APPLY_IN"));
		if (pcr == null) {
			// 初始化 新培训记录
			partnerPeixunBO.initPeixunRecord(dto.getPartnerUserId(),
					PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
							.queryAppResourceValue("PARTNER_PEIXUN_CODE",
									"APPLY_IN"));
			partnerPeixunBO.initPeixunRecord(dto.getPartnerUserId(),
					PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
							.queryAppResourceValue("PARTNER_PEIXUN_CODE",
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
		String courseCode = appResourceService.queryAppResourceValue(
				"PARTNER_PEIXUN_CODE", "APPLY_IN");
		Long userId = dto.getPartnerUserId();
		List<String> courseCodes = new ArrayList<String>();
		courseCodes.add(courseCode);
		List<FuwuOrderDto> orders = fuwuOrderService
				.queryOrdersByUserIdAndCode(userId, courseCodes, null);
		if (orders.size() > 0) {
			return true;
		} else {
			String mkey = appResourceService.queryAppResourceValue(
					"CRM_ORDER_PARAM", "MKEY");
			fuwuOrderService.createOrderByPolicyId(userId, mkey, "0.0.0.0");
		}
		return true;
	}

	@Override
	public Boolean transferMoney(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())&&!record.getStatus().equals(PartnerPeixunStatusEnum.PAY.getCode())){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_BUSINESS_CHECK_ERROR_CODE,"培训记录状态不正确 "+String.valueOf(record.getPartnerUserId()));
		}
		//判断新订单是否已经创建
		String courseCodeNew=appResourceService.queryAppResourceValue("PARTNER_PEIXUN_CODE", "APPLY_IN");
		Long userId=dto.getPartnerUserId();
		List<String> courseCodes=new ArrayList<String>();
		courseCodes.add(courseCodeNew);
		Set<String> orderStatus=new HashSet<String>();
		orderStatus.add(OrderItemBizStatus.Before.getValue());
		List<FuwuOrderDto> orders=fuwuOrderService.queryOrdersByUserIdAndCode(userId, courseCodes, orderStatus);
		if(orders.size()==0){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"新订单未创建"+String.valueOf(record.getPartnerUserId()));
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
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"transfer error "+result.getExceptionDesc());
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
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"close Order error "+result.getExceptionDesc());
		}
		//更新财务订单执行金额
		EsbResultModel  exeResult= esbFinanceContractAdapter.modifyFinContractExecAmount(contractDtos);
		if(!exeResult.isSuccessed()){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"update ExecuteAmont error "+result.getExceptionDesc());
		}
	}

	@Override
	public Boolean sign(PartnerCourseRecordDto dto) {
		PartnerCourseRecord record=partnerCourseRecordMapper.selectByPrimaryKey(dto.getId());
		if(!record.getStatus().equals(PartnerPeixunStatusEnum.DONE.getCode())){
			throw new AugeBusinessException(AugeErrorCodes.PEIXUN_BUSINESS_CHECK_ERROR_CODE,"培训记录状态不正确 "+String.valueOf(record.getPartnerUserId()));
		}
		//判断新的培训记录是不是已付款状态,若是，则获取签到码
		PartnerPeixunDto peixun=partnerPeixunService.queryOfflinePeixunProcess(dto.getPartnerUserId(), appResourceService.queryAppResourceValue("PARTNER_PEIXUN_CODE", "APPLY_IN"), PartnerPeixunCourseTypeEnum.APPLY_IN);
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
	         ResultDTO<PageDTO<TrainingRecordDTO>> result = trainingRecordServiceFacade
	               .find(auth, query, 100, 1);
	         if (result.isSuccess()) {
	            return result.getData().getRows()==null?Lists.newArrayList():result.getData().getRows();
	         } else {
	            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"query record error,"
	                  + result.getMsg());
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
	        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"系统异常,签到失败,稍后重试"+resultDTOVar2.getMsg());
	    }
	}

	public static void main(String[] args) throws Exception{
		 BufferedReader b = new BufferedReader(new FileReader("D://shujuqianyi.txt"));
		 String l=null;
		 while((l=b.readLine())!=null){
		 String queryString = ("ticket=123123&&code=bc471");
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
            	 Map<String,Object> json= (Map<String, Object>) JSON.parse(method.getResponseBodyAsString());
            	 System.out.println(json.get("success"));
            	 Map<String,Object> json1=(Map<String, Object>) json.get("content");
            	 System.out.println(json1);
             } else {
            	 System.out.println("error");
             }
         } catch (Exception e) {
        	 System.out.println("error");
         }
		 }
	}

	
	@Value("${notify.alilang.topic}")
	protected String topic;
	@Value("${notify.alilang.messageType}")
	protected String messageType;
	@Value("${alilang.orgId}")
	protected long alilangOrgId;
	@Autowired
    private NotifyManagerBean notifyPublisherManagerBean;
	
	@Override
	public Long queryInstances(Long id,Long detailId) {
//		if(id==null){
//			return null;
//		}
//		Long returnLong=null;
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("id", id);
//		param.put("detailId", detailId);
//		List<PartnerDto> instances=partnerCourseRecordMapper.queryPartnerIden(param);
//		if(instances.size()==0){
//			return null;
//		}else{
//			for(PartnerDto dto:instances){
//				returnLong=dto.getId();
//				param.clear();
//				if(StringUtils.isNoneEmpty(dto.getIdenNum())&&dto.getIdenNum().length()==18){
//					DateFormat format = new SimpleDateFormat("yyyyMMdd");  
//					try {
//						param.put("birthday", format.parse(dto.getIdenNum().substring(6, 14)));
//					} catch (Exception e) {
//						// 暂时不影响正常保存
//					}
//				}
//				param.put("id", dto.getId());
//				partnerCourseRecordMapper.updatePartnerBirth(param);
//			}
//		}
		//阿里郎同步
//		if(id==null){
//			return null;
//		}
//		Long returnLong=null;
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("id", id);
//		param.put("detailId", detailId);
//		List<PartnerDto> instances=partnerCourseRecordMapper.queryAlilangPartner(param);
//		if(instances.size()==0){
//			return null;
//		}else{
//			for(PartnerDto dto:instances){
//				try{
//				returnLong=dto.getId();
//				PartnerMessage partnerMessage = new PartnerMessage();
//				partnerMessage.setTaobaoUserId(dto.getTaobaoUserId());
//				partnerMessage.setMobile(dto.getMobile());
//				partnerMessage.setAction("new");
//				partnerMessage.setEmail(dto.getEmail());
//				partnerMessage.setName(dto.getName());
//				partnerMessage.setAlilangOrgId(alilangOrgId);
//				String str = JSONObject.toJSONString(partnerMessage);
//				StringMessage stringMessage = new StringMessage();
//				stringMessage.setBody(str);
//				stringMessage.setTopic(topic);
//				stringMessage.setMessageType(messageType);
//				SendResult sendResult = notifyPublisherManagerBean.sendMessage(stringMessage);
//				}catch(Exception e){
//					//
//				}
//			}
//		}
//		return returnLong;
		
		//考试答卷初始化
//		if(id==null){
//			return null;
//		}
//		try{
//		Map<String,Object> param=new HashMap<String,Object>();
//		param.put("idmin", id);
//		param.put("idmax", id+500);
//		param.put("detailId", detailId);
//		List<ExamInstanceItemDto> instances=partnerCourseRecordMapper.queryExamInstanceItemList(param);
//		if(instances.size()==0){
//			return null;
//		}else{
//			Map<Long,List<ExamInstanceItemDto>> map=new HashMap<Long,List<ExamInstanceItemDto>>();
//			for(ExamInstanceItemDto dto:instances){
//				if(map.get(dto.getInstanceId())==null){
//					map.put(dto.getInstanceId(), new ArrayList<ExamInstanceItemDto>());
//				}
//				map.get(dto.getInstanceId()).add(dto);
//			}
//			for(Long temp:map.keySet()){
//				Map<String,Object> insertMap=new HashMap<String,Object>();
//				insertMap.put("instanceId", temp);
//				int count=partnerCourseRecordMapper.queryExamInstanceAnswer(insertMap);
//				if(count>=1){
//					continue;
//				}
//				String answer=JSON.toJSONString(map.get(temp));
//				insertMap.put("answer", answer);
//				partnerCourseRecordMapper.insertExamAnswer(insertMap);
//			}
//		}
//		}catch(Exception e){
//			return id;
//		}
//		return id+500;
		PartnerInstanceDto partnerInstanceDto=partnerInstanceBO.getPartnerInstanceById(id);
		String taobaoNick=partnerInstanceDto.getPartnerDto().getTaobaoNick();
		MemberModel memberModel = memberReadService.findMemberByLoginId(taobaoNick);
		if(memberModel==null||StringUtils.isEmpty(memberModel.getMemberId())){
			throw new AugeBusinessException(AugeErrorCodes.MEMBER_ID_GET_ERROR,
					"memberid获取失败"+partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
		try{
			String memberId = memberModel.getMemberId();
			QueryOrgStructParam queryparam = new QueryOrgStructParam();
			queryparam.setMemberId(memberModel.getMemberId());
			queryparam.setStatuses(OrgStructStatus.getEffectiveStatus());
			List<OrgStructModel> modelList = orgStructReadService
					.queryOrgStructs(queryparam);
			if (modelList != null && modelList.size() > 0) {
				return null;
			}
			OrgStructPostParam param = new OrgStructPostParam();
			param.setCreatorMemberId(memberId);
			param.setCreatorUserId(partnerInstanceDto.getTaobaoUserId());
			param.setMemberId(memberId);
			param.setParentId(parentId);
			Long structId=orgStructWriteService.postOrgStruct(param);
			OrgStructBaseParam pa = new OrgStructBaseParam();
			pa.setOrgStructId(structId);
			pa.setNewStatus(OrgStructStatus.success.getValue());
			orgStructWriteService.modifyBaseInfo(pa);
		}catch(Exception e){
			throw new AugeBusinessException(AugeErrorCodes.CBU_MARKET_ACCESS_ERROR,
					"1688商城授权失败"+partnerInstanceDto.getPartnerDto().getTaobaoNick());
		}
		return null;
	}

	/*
	 * 初始化站点和人村关系的模式类型
	 * 
	 */
	@Override
	public Boolean initSatationAndPartnerMod() {
		PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTypeEqualTo(PartnerInstanceTypeEnum.TP.getCode());
        int pageCount = partnerStationRelMapper.countByExample(example);
        
        for(int i=1;i<=pageCount;i++) {
        	PageHelper.startPage(i, 500);
        	List<PartnerStationRel> partnerStationRelList = partnerStationRelMapper.selectByExample(example);
        	
        	for(PartnerStationRel partnerStationRel:partnerStationRelList) {
        		initMod(partnerStationRel);
        	}
        }

		return null;
	}
	
	private void initMod(PartnerStationRel partnerStationRel) {
		PartnerProtocolRelExample nomalExample = new PartnerProtocolRelExample();
		PartnerProtocolRelExample c2bExample = new PartnerProtocolRelExample();
		//以下是几个历史版本的入驻协议，理论上一个合伙人实例对应一个入驻协议数据，代码按照这个逻辑写，发现29条异常数据需要根锁解决
		Long[] protocolIdArrayNomal = new Long[] {6L,9L,10L,13L};
		//查询普通入驻协议的example
		nomalExample.createCriteria().andIsDeletedEqualTo("n").andTargetTypeEqualTo("PARTNER_INSTANCE")
				.andObjectIdEqualTo(partnerStationRel.getId()).andProtocolIdIn(Arrays.asList(protocolIdArrayNomal));
		//查询C2B协议的example
		c2bExample.createCriteria().andIsDeletedEqualTo("n").andTargetTypeEqualTo("PARTNER_INSTANCE")
		.andObjectIdEqualTo(partnerStationRel.getId()).andProtocolIdEqualTo(12l);
		
		List<PartnerProtocolRel> nomalList = partnerProtocolRelMapper.selectByExample(nomalExample);
		//如果仅仅签约过C2B，认为是V3
		if(CollectionUtils.isEmpty(nomalList)) {
			List c2bList = partnerProtocolRelMapper.selectByExample(c2bExample);
			if(CollectionUtils.isNotEmpty(c2bList)) {
				updateMode(partnerStationRel,"v3");
			}
			return;
		}
		
		PartnerProtocolRel partnerProtocolRel = nomalList.get(0);
		updateMode(partnerStationRel,modeMatch(partnerProtocolRel.getProtocolId()));
	}
	
	private void updateMode(PartnerStationRel partnerStationRel,String version) {
		//先更新rel
        PartnerStationRel updateInstance = new PartnerStationRel();
        updateInstance.setId(partnerStationRel.getId());
        updateInstance.setMode(version);
        DomainUtils.beforeUpdate(updateInstance, "4.0init");
        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
        
		//再更新station
        if("y".equals(partnerStationRel.getIsCurrent())) {
    		Station station = new Station();
    		station.setId(partnerStationRel.getParentStationId());
    		station.setMode(version);
            DomainUtils.beforeUpdate(station, "4.0init");
            stationMapper.updateByPrimaryKeySelective(station);
        }
	}
	
	private String modeMatch(Long protocolId) {
		String mode= null;
		switch(protocolId.toString()) {
		case "6":	
			mode = "v1";
			break;
		case "9":	
			mode = "v2";
			break;
		case "10":	
			mode = "v3";
			break;
		case "13":	
			mode = "v4";
		}
		
		return mode;
	}
}
