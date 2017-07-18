package com.taobao.cun.auge.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ali.com.google.common.collect.Maps;
import com.alibaba.ceres.service.Result;
import com.alibaba.ceres.service.catalog.ProductService;
import com.alibaba.ceres.service.catalog.model.CatalogProductDto;
import com.alibaba.ceres.service.catalog.model.CatalogSkuDto;
import com.alibaba.ceres.service.category.CategoryService;
import com.alibaba.ceres.service.category.model.CategoryUseDto;
import com.alibaba.ceres.service.pr.PrService;
import com.alibaba.ceres.service.pr.model.PrDto;
import com.alibaba.ceres.service.pr.model.PrLineDto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlow;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowDetail;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowDetailExample;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowDetailForExcel;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowExtExample;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetFlowDetailMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetFlowExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetFlowMapper;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.domain.CuntaoFlowRecordEvent;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.crius.bpm.dto.CuntaoTaskExecuteDto;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.NodeActionEnum;
import com.taobao.cun.crius.bpm.enums.NodeTypeEnum;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetFlowService")
@HSFProvider(serviceInterface = AssetFlowService.class)

public class AssetFlowServiceImpl implements AssetFlowService{

    private static final Logger logger = LoggerFactory.getLogger(AssetFlowServiceImpl.class);

	@Autowired
	private CuntaoAssetFlowExtMapper cuntaoAssetFlowExtMapper;
	
	@Autowired
	private CuntaoAssetFlowMapper cuntaoAssetFlowMapper;
	
	@Autowired
	private CuntaoAssetFlowDetailMapper cuntaoAssetFlowDetailMapper;
	
	@Autowired
	private Emp360Adapter emp360Adapter;
	
	@Resource
	private ProductService ceresProductService;
	
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;
    
    @Autowired
    private CuntaoWorkFlowService cuntaoWorkFlowService;
    
	@Resource
	private PrService prService;
	
    @Autowired
    private CuntaoAssetExtMapper cuntaoAssetExtMapper;
    
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    
    @Autowired
    private CategoryService ceresCategoryService;
    
    @Value("${asset.assetDefaultAddressId}")
    private Long assetDefaultAddressId;
    @Value("${asset.receiverId}")
	private String receiverId; 
	@Override
	public PageDto<CuntaoAssetFlowDto> queryByPage(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto) {
			CuntaoAssetFlowExtExample example = new CuntaoAssetFlowExtExample();
			com.taobao.cun.auge.dal.domain.CuntaoAssetFlowExtExample.Criteria cri = example.createCriteria();
			PageHelper.startPage(cuntaoAssetFlowQueryDto.getPageNum(), cuntaoAssetFlowQueryDto.getPageSize());
			if(StringUtils.isNotEmpty(cuntaoAssetFlowQueryDto.getApplier())){
				cri.andApplierEqualTo(cuntaoAssetFlowQueryDto.getApplier());
			}
			if(StringUtils.isNotEmpty(cuntaoAssetFlowQueryDto.getApplierNo())){
				cri.andApplierNoEqualTo(cuntaoAssetFlowQueryDto.getApplierNo());
			}
			if(StringUtils.isNotEmpty(cuntaoAssetFlowQueryDto.getFullIdPath())){
				example.setFullIdPath(cuntaoAssetFlowQueryDto.getFullIdPath());
			}
			if(cuntaoAssetFlowQueryDto.getPurchaseStatus() != null){
				cri.andPurchaseStatusEqualTo(cuntaoAssetFlowQueryDto.getPurchaseStatus().getCode());
			}
			if(cuntaoAssetFlowQueryDto.getApplyStatus() != null){
				cri.andApplyStatusEqualTo(cuntaoAssetFlowQueryDto.getApplyStatus().getCode());
			}
			if(cuntaoAssetFlowQueryDto.getPlanReceiveTimeBegin() != null){
				cri.andPlanReceiveTimeGreaterThanOrEqualTo(cuntaoAssetFlowQueryDto.getPlanReceiveTimeBegin());
			}
			if(cuntaoAssetFlowQueryDto.getPlanReceiveTimeEnd() != null){
				cri.andPlanReceiveTimeLessThanOrEqualTo(cuntaoAssetFlowQueryDto.getPlanReceiveTimeEnd());
			}
			example.setOrderByClause("apply_time DESC");
			Page<CuntaoAssetFlow> page =  (Page<CuntaoAssetFlow>)cuntaoAssetFlowExtMapper.selectByExample(example);
			
			List<CuntaoAssetFlowDto> targetList = page.getResult().stream().map(source -> convertToflowDto(source)).collect(Collectors.toList());
			PageDto<CuntaoAssetFlowDto> result = PageDtoUtil.success(page, targetList);
			return result;
	}

	private CuntaoAssetFlowDto convertToflowDto(CuntaoAssetFlow fd) {
		CuntaoAssetFlowDto dto = new CuntaoAssetFlowDto();
		if (fd != null) {
			dto.setApplier(fd.getApplier());
			dto.setApplierNo(fd.getApplierNo());
			dto.setApplyOrg(fd.getApplyOrg());
			dto.setApplyOrgDesc(fd.getApplyOrgDesc());
			dto.setApplyStatus(AssetFlowApplyStatusEnum.valueof(fd.getApplyStatus()));
			dto.setApplyTime(fd.getApplyTime());
			dto.setAssetFlowId(fd.getId());
			dto.setAssetOwner(fd.getAssetOwner());
			dto.setAssetOwnerNo(fd.getAssetOwnerNo());
			//dto.setAssetSituation(fd.getAssetSituation());
			dto.setMobile(fd.getMobile());
			dto.setPlanReceiveTime(fd.getPlanReceiveTime());
			dto.setPurchaseStatus(AssetFlowPurchaseStatusEnum.valueof(fd.getPurchaseStatus()));
			dto.setReceiveAddress(fd.getReceiveAddress());
			dto.setReceiver(fd.getReceiver());
			dto.setRemark(fd.getRemark());
		}
		return dto;
	}
	
	private CuntaoAssetFlow convertToCuntaoAssetFlow(CuntaoAssetFlowDto dto,String operator) {
		CuntaoAssetFlow fd = new CuntaoAssetFlow();
		if (dto != null) {
			//自动设置
			fd.setApplierNo(operator);
			fd.setApplier(emp360Adapter.getName(operator));
			Date date =new Date();
			fd.setGmtCreate(date);
			fd.setApplyTime(date);
			fd.setIsDeleted("n");
			fd.setGmtModified(date);
			fd.setCreator(operator);
			fd.setModifier(operator);
			fd.setApplyStatus(AssetFlowApplyStatusEnum.AUDITING.getCode());
			fd.setPurchaseStatus(AssetFlowPurchaseStatusEnum.NO_ORDER.getCode());
			
			//参数获取
			fd.setApplyOrg(dto.getApplyOrg());
			fd.setApplyOrgDesc(dto.getApplyOrgDesc());
			
			if (dto.getAssetOwnerNo() != null && dto.getAssetOwner() == null) {
				fd.setAssetOwner(emp360Adapter.getName(dto.getAssetOwnerNo()));
			}else {
				fd.setAssetOwner(dto.getAssetOwner());
			}
			fd.setAssetOwnerNo(dto.getAssetOwnerNo());
			//fd.setAssetSituation(dto.getAssetSituation());
			fd.setMobile(dto.getMobile());
			fd.setPlanReceiveTime(dto.getPlanReceiveTime());
			fd.setReceiveAddress(dto.getReceiveAddress());
			fd.setReceiver(dto.getReceiver());
			fd.setRemark(dto.getRemark());

		}
		return fd;
	}
	
	@Override
	public CuntaoAssetFlowDto getFlowById(Long assetFlowId) {
			Assert.notNull(assetFlowId);
			CuntaoAssetFlow flow= cuntaoAssetFlowMapper.selectByPrimaryKey(assetFlowId);
			CuntaoAssetFlowDetailExample example = new CuntaoAssetFlowDetailExample();
			example.createCriteria().andApplyIdEqualTo(assetFlowId);
			List<CuntaoAssetFlowDetail> details= cuntaoAssetFlowDetailMapper.selectByExample(example);
			
			CuntaoAssetFlowDto convertToflowDto =this.convertToflowDto(flow);
			List<CuntaoAssetFlowDetailDto> detailDtoList =	details.stream().map(souce -> convertToDetailDto(souce)).collect(Collectors.toList());
			convertToflowDto.setAssetFlowDetailList(detailDtoList);
			return convertToflowDto;
	}

	private CuntaoAssetFlowDetailDto convertToDetailDto(CuntaoAssetFlowDetail flowDetailDO) {
		CuntaoAssetFlowDetailDto dto = new CuntaoAssetFlowDetailDto();
		if (flowDetailDO != null) {
			dto.setApplyId(flowDetailDO.getApplyId());
			dto.setApplyNum(flowDetailDO.getApplyNum());
			dto.setAssetFlowDetailId(flowDetailDO.getId());
			dto.setName(flowDetailDO.getName());
			dto.setSku(skuName(flowDetailDO));
			dto.setType(AssetFlowDetailAssetTypeEnum.valueof(flowDetailDO.getType()));
			dto.setPoNo(flowDetailDO.getPoNo());
		}
		return dto;
	}
	
	
	private String skuName(CuntaoAssetFlowDetail flowDetailDO) {
		Result<CatalogProductDto> result = ceresProductService.getProductInfoWithSku(flowDetailDO.getPoNo());
		if(result != null && result.isSuccess()) {
			List<CatalogSkuDto> skus = result.getValue().getSkuList();
			if(skus != null) {
				for(CatalogSkuDto sku : skus){
					if(flowDetailDO.getSku() != null && flowDetailDO.getSku().equals(sku.getSkuId() + "")) {
						JSONObject desJson = JSON.parseObject(sku.getSkuDesc());
						StringBuilder sb = new StringBuilder();
						for(Entry<String, Object> entry : desJson.entrySet()) {
							sb.append(entry.getKey() + ":" + entry.getValue());
							sb.append(";");
						}
						return sb.toString();
					}
				}
			}
		}
		return flowDetailDO.getSku();
	}
	
	@Override
	public void saveFlow(CuntaoAssetFlowDto cuntaoAssetFlowDto, String operator) {
			Assert.notNull(cuntaoAssetFlowDto);
			CuntaoAssetFlow cuntaoAssetFlow = this.convertToCuntaoAssetFlow(cuntaoAssetFlowDto, operator);
			this.cuntaoAssetFlowMapper.insertSelective(cuntaoAssetFlow);
			Long flowId = cuntaoAssetFlow.getId();
			List<CuntaoAssetFlowDetailDto> detailList = cuntaoAssetFlowDto.getAssetFlowDetailList();
			if(CollectionUtils.isNotEmpty(detailList)){
				for (CuntaoAssetFlowDetailDto detailDto : detailList) {
					detailDto.setApplyId(flowId);
					CuntaoAssetFlowDetail detailDo = convertToDetailDOForInsert(detailDto,operator);
					this.cuntaoAssetFlowDetailMapper.insertSelective(detailDo);
				}
			}
			/*CuntaoOrgDto org = cuntaoOrgServiceClient.getCuntaoOrg(Long.parseLong(cuntaoAssetFlowDto.getApplyOrg()));
			createTask(flowId,String.valueOf(org.getParentId()),operator);*/
			createTask(flowId,cuntaoAssetFlowDto.getApplyOrg(),operator);
	}

	
	private void createTask(Long applyId,String parentOrgId,String operator) {
		Map<String,String> initData = new HashMap<String, String>();
		initData.put("orgId", parentOrgId);

		StartProcessInstanceDto startDto = new StartProcessInstanceDto();

		startDto.setBusinessCode("assetApply");
		startDto.setBusinessId(String.valueOf(applyId));

		startDto.setApplierId(operator);
		startDto.setApplierUserType(UserTypeEnum.BUC);
		startDto.setInitData(initData);

		cuntaoWorkFlowService.startProcessInstance(startDto);
	}
	
	private CuntaoAssetFlowDetail convertToDetailDOForInsert(CuntaoAssetFlowDetailDto dto,String operator) {
		CuntaoAssetFlowDetail flowDetailDO = new CuntaoAssetFlowDetail();
		if (dto != null) {
			flowDetailDO.setGmtModified(new Date());
			flowDetailDO.setApplyId(dto.getApplyId());
			flowDetailDO.setGmtCreate(new Date());
			flowDetailDO.setApplyNum(dto.getApplyNum());
			flowDetailDO.setName(dto.getName());
			flowDetailDO.setIsDeleted("n");
			flowDetailDO.setSku(dto.getSku());
			flowDetailDO.setType(dto.getType()==null ? null:dto.getType().getCode());
			flowDetailDO.setCreator(operator);
			flowDetailDO.setModifier(operator);
			flowDetailDO.setPoNo(dto.getPoNo());
		}
		return flowDetailDO;
	}

	private CuntaoAssetFlowDetail convertToDetailDOForUpdate(CuntaoAssetFlowDetailDto dto,String operator) {
		CuntaoAssetFlowDetail flowDetailDO = new CuntaoAssetFlowDetail();
		if (dto != null) {
			flowDetailDO.setId(dto.getAssetFlowDetailId());
			flowDetailDO.setGmtModified(new Date());
			flowDetailDO.setApplyNum(dto.getApplyNum());
			flowDetailDO.setName(dto.getName());
			flowDetailDO.setSku(dto.getSku());
			flowDetailDO.setType(dto.getType()==null ? null:dto.getType().getCode());
			flowDetailDO.setModifier(operator);
			flowDetailDO.setPoNo(dto.getPoNo());
		}
		return flowDetailDO;
	}
	
	
	@Override
	public Map<String, List<CuntaoAssetSituationDto>> getAssetSituation(Long applyOrgId) {
			Assert.notNull(applyOrgId);
			Map<String, List<CuntaoAssetSituationDto>> res = new HashMap<String, List<CuntaoAssetSituationDto>>();
			List<Map<String,Object>> assetSituation = this.cuntaoAssetExtMapper.getAssetSituation(applyOrgId);
			//县库存资产
			if(CollectionUtils.isNotEmpty(assetSituation)){
				List<CuntaoAssetSituationDto> countyAssetDtoList = Lists.newArrayList();
				for (Map<String, Object> assetDo : assetSituation) {
					CuntaoAssetSituationDto countyasset = new CuntaoAssetSituationDto();
					countyasset.setCategory((String)assetDo.get("category"));
					countyasset.setCount((Long)assetDo.get("categoryCount"));
					countyAssetDtoList.add(countyasset);
				}
				res.put(CuntaoAssetSituationDto.COUNTY_HAS_ASSET, countyAssetDtoList);
			}
			Map<String,Object> param = Maps.newHashMap();
			param.put("applyOrg", String.valueOf(applyOrgId));
			param.put("applyStatus", AssetFlowApplyStatusEnum.AUDIT_PASS.getCode());
			List<Map<String,Object>> detailLists= this.cuntaoAssetFlowExtMapper.getApplyAssetCount(param);
			if(CollectionUtils.isNotEmpty(detailLists)){
				List<CuntaoAssetSituationDto> countyAssetDtoList2 = Lists.newArrayList();
				for (Map<String,Object> detailDo : detailLists) {
					CuntaoAssetSituationDto countyasset2 = new CuntaoAssetSituationDto();
					countyasset2.setCategory((String)detailDo.get("name"));
					countyasset2.setCount(((BigDecimal)detailDo.get("categoryCount")).longValue());
					countyAssetDtoList2.add(countyasset2);
				}
				res.put(CuntaoAssetSituationDto.COUNTY_APPLY__ASSET, countyAssetDtoList2);
			}
			return res;
	}

	@Override
	public void updateFlow(CuntaoAssetFlowDto cuntaoAssetFlowDto, String operator) {
			Assert.notNull(cuntaoAssetFlowDto);
			Assert.notNull(operator);
			
			CuntaoAssetFlow flow = convertToflowDOForUpdate(cuntaoAssetFlowDto,operator);
			
			this.cuntaoAssetFlowMapper.updateByPrimaryKeySelective(flow);
			
			if(cuntaoAssetFlowDto.getAssetFlowDetailList() !=null){
				for(CuntaoAssetFlowDetailDto flowDetail : cuntaoAssetFlowDto.getAssetFlowDetailList()) {
					
					CuntaoAssetFlowDetail detail = convertToDetailDOForUpdate(flowDetail, operator);
					this.cuntaoAssetFlowDetailMapper.updateByPrimaryKeySelective(detail);
				}
			}
			
			if(cuntaoAssetFlowDto.isApplyPr()) {
				syncAsset(operator, flow, flow.getId());
			}
	}

	private void syncAsset(String operator, CuntaoAssetFlow cuntaoAssetFlow, Long assetFlowId) {
		List<CuntaoAssetFlowDetail> detailList = getDetailList(cuntaoAssetFlow);
		CuntaoAssetFlowDto assetFlowDto = this.getFlowById(assetFlowId);
		PrDto prDto = new PrDto();
		prDto.setActualRequestor(assetFlowDto.getAssetOwnerNo());
		prDto.setApplicant(operator);
		prDto.setDescription(applyReason(assetFlowDto));
		String assetOrg = diamondConfiguredProperties.getAssetOrg();
		prDto.setOuCode(assetOrg != null ? assetOrg : "T62");
		List<PrLineDto> prLineList = preparePrLineList(operator, detailList, assetFlowDto);
		if(prLineList == null || prLineList.size() == 0) {
			return ;
		}
		prDto.setPrLineList(prLineList);
		
		Result<?> result = prService.submitPr(prDto);
		//this.updateAssetFlow(flowDto, contextDto);
		if(!result.isSuccess()) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"提交pr失败，失败原因：" + result.getMessage());
		}
	}
	
	private List<PrLineDto> preparePrLineList(String operator,List<CuntaoAssetFlowDetail> detailList, CuntaoAssetFlowDto assetFlowDto) {
		List<PrLineDto> prLineList = new ArrayList<PrLineDto>();
		if(detailList != null) {
			String assetUsesJsonString = diamondConfiguredProperties.getAssetUse();
			String acceptanceStandard = diamondConfiguredProperties.getAcceptanceStandard();
			JSONObject usesObj = JSON.parseObject(assetUsesJsonString);
			for(CuntaoAssetFlowDetail detail : detailList) {
				Result<CatalogProductDto> proResult = ceresProductService.getProductInfoWithSku(detail.getPoNo());
				if(proResult != null && proResult.isSuccess()) {
					String useCode = null;
					// 取diamond配置中的产品用途编码
					if(usesObj.containsKey(detail.getPoNo())) {
						useCode = usesObj.getString(detail.getPoNo());
					} else {
						List<CategoryUseDto> useList = ceresCategoryService.getCategoryUseList(proResult.getValue().getPurchaseCategoryId(), operator);
						if(useList != null && useList.size() > 0) {
							useCode = useList.get(0).getUseCode();
						}
					}
					if(detail.getApplyNum() != null && detail.getApplyNum() > 0) {
						PrLineDto prLine = new PrLineDto();
						prLine.setSkuId(detail.getSku());
						prLine.setCategoryUse(useCode);
						prLine.setNeedByDate(assetFlowDto.getPlanReceiveTime());
						prLine.setDeliveryAddressId(assetDefaultAddressId);
						prLine.setReceiver(receiverId + "");
						prLine.setQuantity(detail.getApplyNum());
						prLine.setRemark(getPrRemark(assetFlowDto));
						prLine.setAcceptanceStandard(acceptanceStandard);
						prLineList.add(prLine);
					}
				}
			}
		}
		return prLineList;
	}
	
	private String getPrRemark(CuntaoAssetFlowDto assetFlowDto) {
		String address = assetFlowDto.getReceiveAddress();
		String receiver = assetFlowDto.getReceiver();
		String receiverPhone = assetFlowDto.getMobile();
		return "实际收货人: " + receiver + "; " + receiverPhone + "; " + address;
	}
	
	
	private String applyReason(CuntaoAssetFlowDto assetFlowDto) {
		Map<String, List<CuntaoAssetSituationDto>> assetSituation = getAssetSituation(Long.parseLong(assetFlowDto.getApplyOrg()));
		List<CuntaoAssetSituationDto> hasAsset = assetSituation.get(CuntaoAssetSituationDto.COUNTY_HAS_ASSET);
		StringBuilder assetStr = new StringBuilder();
		if(hasAsset != null && hasAsset.size() > 0) {
			assetStr.append("库存");
			for(CuntaoAssetSituationDto situation : hasAsset) {
				assetStr.append(situation.getCategory() + situation.getCount() + "个, ");
			}
			int last = assetStr.lastIndexOf(", ");
			assetStr.delete(last, last + 2);
			assetStr.append(";");
		}
		String reason = assetFlowDto.getApplyOrgDesc()+"；计划开业村点"+getStationNumFromJson(assetFlowDto)+"个；"+assetStr+getDescriptionFromJson(assetFlowDto);
		return reason;
	}
	
	private String getDescriptionFromJson(CuntaoAssetFlowDto cuntaoAssetFlow) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(cuntaoAssetFlow.getRemark());
			return jsonObject.containsKey("remark") ? jsonObject.getString("remark") : "";
		} catch(Exception e) {
			return cuntaoAssetFlow.getRemark();
		}
	}
	
	private String getStationNumFromJson(CuntaoAssetFlowDto cuntaoAssetFlow) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(cuntaoAssetFlow.getRemark());
			return jsonObject.containsKey("stationNum") ? jsonObject.getString("stationNum") : "";
		} catch(Exception e) {
			return "";
		}
	}
	private List<CuntaoAssetFlowDetail> getDetailList(CuntaoAssetFlow cuntaoAssetFlow) {
		CuntaoAssetFlowDetailExample example = new CuntaoAssetFlowDetailExample();
		example.createCriteria().andApplyIdEqualTo(cuntaoAssetFlow.getId()).andIsDeletedEqualTo("n");
		return this.cuntaoAssetFlowDetailMapper.selectByExample(example);
	}
	
	
	private CuntaoAssetFlow convertToflowDOForUpdate(CuntaoAssetFlowDto dto,String operator) {
		CuntaoAssetFlow fd = new CuntaoAssetFlow();
		if (dto != null) {
			fd.setGmtModified(new Date());
			fd.setId(dto.getAssetFlowId());
			fd.setModifier(operator);
			if(dto.getApplyStatus() != null) {
				fd.setApplyStatus(dto.getApplyStatus().getCode());
			}
			if(dto.getPurchaseStatus() != null) {
				fd.setPurchaseStatus(dto.getPurchaseStatus().getCode());
			}
			if (dto.getApplyOrg() != null) {
				fd.setApplyOrg(dto.getApplyOrg());
			}
			if(dto.getApplyOrgDesc() != null) {
				fd.setApplyOrgDesc(dto.getApplyOrgDesc());
			}
			if (dto.getAssetOwnerNo() != null){
				fd.setAssetOwnerNo(dto.getAssetOwnerNo());
				if(dto.getAssetOwner() == null) {
					fd.setAssetOwner(this.emp360Adapter.getName(dto.getAssetOwnerNo()));
				}else {
					fd.setAssetOwner(dto.getAssetOwner());
				}
			}
			if (dto.getMobile() != null) {
				fd.setMobile(dto.getMobile());
			}
			if (dto.getPlanReceiveTime() !=null) {
				fd.setPlanReceiveTime(dto.getPlanReceiveTime());
			}
			if (dto.getReceiveAddress() != null) {
				fd.setReceiveAddress(dto.getReceiveAddress());
			}
			if (dto.getReceiver() != null) {
				fd.setReceiver(dto.getReceiver());
			}
			if (dto.getRemark() != null) {
				fd.setRemark(dto.getRemark());
			}
		}
		return fd;
	}
	
	
	@Override
	public void audit(CuntaoAssetFlowAuditDto cuntaoAssetFlowAuditDto, String operator) {
			Assert.notNull(cuntaoAssetFlowAuditDto);
			Assert.notNull(operator);
			CuntaoAssetFlow flow = new CuntaoAssetFlow();
			flow.setModifier(operator);
			flow.setGmtModified(new Date());
			flow.setId(cuntaoAssetFlowAuditDto.getAssetFlowId());
			flow.setApplyStatus(cuntaoAssetFlowAuditDto.getApplyStatus().getCode());
			Integer c = this.cuntaoAssetFlowMapper.updateByPrimaryKeySelective(flow);
			if(c != null && c > 0){
				finishTask(cuntaoAssetFlowAuditDto,operator);
			}
	}

	private void finishTask(CuntaoAssetFlowAuditDto auditDto, String  operator){
		CuntaoTaskExecuteDto executeDto = new CuntaoTaskExecuteDto();
		executeDto.setLoginId(operator);
		executeDto.setOrgId(String.valueOf(auditDto.getStationOrgId()));
		executeDto.setType(NodeTypeEnum.ORG);//从ORG系统来源

		executeDto.setTaskId(auditDto.getTaskId());
		executeDto.setRemark(auditDto.getWorkFLowRemarks());
		if (AssetFlowApplyStatusEnum.AUDIT_PASS.getCode().equals(auditDto.getApplyStatus().getCode())){
			executeDto.setAction(NodeActionEnum.AGREE);
			cuntaoWorkFlowService.executeTask(executeDto);
		}else {
			executeDto.setAction(NodeActionEnum.DISAGREE);
			cuntaoWorkFlowService.executeTask(executeDto);
		}
	}
	
	@Override
	public List<CuntaoAssetFlowDetailForExcelDto> getDetailForExcel(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto) {
			Assert.notNull(cuntaoAssetFlowQueryDto);
			List<CuntaoAssetFlowDetailForExcelDto>  resList = Lists.newArrayList();
			
			List<CuntaoAssetFlowDetailForExcel> excelDOList = this.cuntaoAssetFlowExtMapper.selectForExcel(buildParamForExcel(cuntaoAssetFlowQueryDto));
			if(CollectionUtils.isNotEmpty(excelDOList)){
				for (CuntaoAssetFlowDetailForExcel excelDo: excelDOList) {
					CuntaoAssetFlowDetailForExcelDto excelDto  = new CuntaoAssetFlowDetailForExcelDto();
					excelDto.setApplier(excelDo.getApplier());
					excelDto.setApplierNo(excelDo.getApplierNo());
					excelDto.setApplyId(excelDo.getApplyId());
					excelDto.setApplyNum(excelDo.getApplyNum());
					excelDto.setApplyOrgDesc(excelDo.getApplyOrgDesc());
					excelDto.setApplyStatusDesc(AssetFlowApplyStatusEnum.valueof(excelDo.getApplyStatus())==null?"":AssetFlowApplyStatusEnum.valueof(excelDo.getApplyStatus()).getDesc());
					excelDto.setApplyTime(DateUtil.formatTime(excelDo.getApplyTime()));
					excelDto.setAssetOwner(excelDo.getAssetOwner());
					excelDto.setAssetOwnerNo(excelDo.getAssetOwnerNo());
					excelDto.setMobile(excelDo.getMobile());
					excelDto.setName(excelDo.getName());
					excelDto.setPlanReceiveTime(DateUtil.format(excelDo.getPlanReceiveTime()));
					excelDto.setPurchaseStatusDesc(AssetFlowPurchaseStatusEnum.valueof(excelDo.getPurchaseStatus())==null?"":AssetFlowPurchaseStatusEnum.valueof(excelDo.getPurchaseStatus()).getDesc());
					excelDto.setReceiveAddress(excelDo.getReceiveAddress());
					excelDto.setReceiver(excelDo.getReceiver());
					excelDto.setRemark(excelDo.getRemark());
					excelDto.setSku(excelDo.getSku());
					excelDto.setTypeDesc(AssetFlowDetailAssetTypeEnum.valueof(excelDo.getType())==null?"":AssetFlowDetailAssetTypeEnum.valueof(excelDo.getType()).getDesc());
					resList.add(excelDto);
				}
			}
			return resList;
	}

	 private CuntaoAssetFlowDetailForExcel buildParamForExcel(CuntaoAssetFlowQueryDto queryDto) {
	    	CuntaoAssetFlowDetailForExcel excelDO = new CuntaoAssetFlowDetailForExcel();
	    	List<Long> applyIdList = queryDto.getApplyIdList();
			if (applyIdList != null && applyIdList.size() > 0) {
				excelDO.setApplyIdArray(applyIdList.toArray(new Long[applyIdList.size()]));
			}else {
				excelDO.setApplierNo(queryDto.getApplierNo());
				excelDO.setFullIdPath(queryDto.getFullIdPath());
				
				excelDO.setPurchaseStatus(queryDto.getPurchaseStatus()==null? null:queryDto.getPurchaseStatus().getCode());
				excelDO.setPlanReceiveTimeBegin(queryDto.getPlanReceiveTimeBegin());
				excelDO.setPlanReceiveTimeEnd(queryDto.getPlanReceiveTimeEnd());
			}
			return excelDO;
	    }
	 
	@Override
	public Integer getDetailCountForExcel(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto) {
			Assert.notNull(cuntaoAssetFlowQueryDto);
			return this.cuntaoAssetFlowExtMapper.selectCountForExcel(buildParamForExcel(cuntaoAssetFlowQueryDto));
	}

	@Override
	public void cancelFlow(Long assetFlowId, String operator) {
			Assert.notNull(assetFlowId);
			Assert.notNull(operator);
			CuntaoAssetFlow flow = this.cuntaoAssetFlowMapper.selectByPrimaryKey(assetFlowId);
			if( flow != null){
				if (!AssetFlowApplyStatusEnum.AUDITING.getCode().equals(flow.getApplyStatus())) {
					String applyStatusDesc = AssetFlowApplyStatusEnum.valueof(flow.getApplyStatus()).getDesc();
					throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"申请单状态为【"+applyStatusDesc+"】，不能取消申请！");
				}
			}else{
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"查询不到当前申请单");
			}
			
			cuntaoWorkFlowService.teminateProcessInstance(String.valueOf(assetFlowId), "assetApply", operator);
			CuntaoAssetFlow cuntaoAssetFlow = new CuntaoAssetFlow();
			cuntaoAssetFlow.setId(assetFlowId);
			cuntaoAssetFlow.setModifier(operator);
			cuntaoAssetFlow.setGmtModified(new Date());
			cuntaoAssetFlow.setApplyStatus(AssetFlowApplyStatusEnum.CANCEL.getCode());
			this.cuntaoAssetFlowMapper.updateByPrimaryKeySelective(cuntaoAssetFlow);
	}

	@Override
	public void deleteFlow(Long assetFlowId, String operator) {
			Assert.notNull(assetFlowId);
			Assert.notNull(operator);
			CuntaoAssetFlow cuntaoAssetFlow = new CuntaoAssetFlow();
			cuntaoAssetFlow.setId(assetFlowId);
			cuntaoAssetFlow.setModifier(operator);
			cuntaoAssetFlow.setGmtModified(new Date());
			cuntaoAssetFlow.setIsDeleted("y");
			this.cuntaoAssetFlowMapper.updateByPrimaryKeySelective(cuntaoAssetFlow);
	}

	@Override
	public void updateFlowDetails(List<CuntaoAssetFlowDetailDto> details, String operator) {
			Assert.notNull(details);
			Assert.notNull(operator);
			if(details !=null){
				CuntaoAssetFlowDetail detailDo = null;
				for(CuntaoAssetFlowDetailDto detailDto : details) {
					detailDo = convertFlowDetailDO(detailDto, operator);
					this.cuntaoAssetFlowDetailMapper.updateByPrimaryKeySelective(detailDo);
					sendQuitEvent(detailDo, operator);
				}
			}
	}

	private void sendQuitEvent(CuntaoAssetFlowDetail detailDo, String operator) {
		CuntaoFlowRecordEvent event1 = new CuntaoFlowRecordEvent();
		event1.setOperateTime(new Date());
		event1.setOperatorName(this.emp360Adapter.getName(operator));
		event1.setOperatorWorkid(operator);
		event1.setTargetId(detailDo.getId());
		event1.setNodeTitle("assetFlowEdit");
		event1.setTargetType("assetFlowEdit");
		event1.setRemarks(JSON.toJSONString(detailDo));
		EventDispatcherUtil.dispatch(EventConstant.ASSET_DETAIL_CHANGE_EVENT, event1);
	}
	
	private CuntaoAssetFlowDetail convertFlowDetailDO(CuntaoAssetFlowDetailDto detailDto, String operator) {
		CuntaoAssetFlowDetail detailDo = new CuntaoAssetFlowDetail();
		detailDo.setModifier(operator);
		detailDo.setGmtModified(new Date());
		if(detailDto.getApplyNum() != null) {
			detailDo.setApplyNum(detailDto.getApplyNum());
		}
		if(detailDto.getName() != null) {
			detailDo.setName(detailDto.getName());
		}
		if(detailDto.getPoNo() != null) {
			detailDo.setPoNo(detailDto.getPoNo());
		}
		if(detailDto.getSku() != null) {
			detailDo.setSku(detailDto.getSku());
		}
		if(detailDto.getType() != null) {
			detailDo.setType(detailDto.getType().getCode());
		}
		detailDo.setId(detailDto.getAssetFlowDetailId());
		return detailDo;
	}
	
}
