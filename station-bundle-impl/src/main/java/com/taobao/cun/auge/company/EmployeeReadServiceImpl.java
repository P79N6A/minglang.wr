package com.taobao.cun.auge.company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeInfoDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.EmployeeQueryPageCondition;
import com.taobao.cun.auge.company.enums.CuntaoEmployeeIdentifierEnum;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.UserRoleDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

import jersey.repackaged.com.google.common.collect.Lists;

@Service("employeeReadServiceImpl")
@HSFProvider(serviceInterface = EmployeeReadService.class)
public class EmployeeReadServiceImpl implements EmployeeReadService{

	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeReadServiceImpl.class);

	@Override
	public Result<PageDto<CuntaoEmployeeDto>> queryVendorEmployeeByPage(
			EmployeeQueryPageCondition employeeQueryPageCondition) {
		try {
			if(employeeQueryPageCondition.getVendorId() == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "服务商ID不存在");
				return Result.of(errorInfo);
			}
			CuntaoEmployeeRelExample cuntaoVendorEmployeeExample = new CuntaoEmployeeRelExample();
			cuntaoVendorEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
					.andOwnerIdEqualTo(employeeQueryPageCondition.getVendorId()).andTypeEqualTo(CuntaoEmployeeType.vendor.name());
			List<CuntaoEmployeeRel> cuntaoVendorEmployees = cuntaoEmployeeRelMapper
					.selectByExample(cuntaoVendorEmployeeExample);
			List<Long> employeeIds = cuntaoVendorEmployees.stream()
					.map(cuntaoVendorEmployee -> cuntaoVendorEmployee.getEmployeeId()).collect(Collectors.toList());
			if (employeeIds != null && !employeeIds.isEmpty()) {
				PageHelper.startPage(employeeQueryPageCondition.getPageNum(), employeeQueryPageCondition.getPageSize());
					CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
				Criteria criteria = cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andTypeEqualTo(CuntaoEmployeeType.vendor.name())
						.andIdIn(employeeIds);
				if (StringUtils.isNotEmpty(employeeQueryPageCondition.getName())) {
					criteria.andNameEqualTo(employeeQueryPageCondition.getName());
				}
				if (StringUtils.isNotEmpty(employeeQueryPageCondition.getMobile())) {
					criteria.andMobileEqualTo(employeeQueryPageCondition.getMobile());
				}
				if (StringUtils.isNotEmpty(employeeQueryPageCondition.getTaobaoNick())) {
					criteria.andTaobaoNickEqualTo(employeeQueryPageCondition.getTaobaoNick());
				}
				Page<CuntaoEmployee> employees = (Page<CuntaoEmployee>) cuntaoEmployeeMapper
						.selectByExample(cuntaoEmployeeExample);
				PageDto<CuntaoEmployeeDto> success = PageDtoUtil.success(employees,
						EmployeeConverter.convert2CuntaoEmployeeDtoList(employees));
				return Result.of(success);
			}
			return Result.of(Lists.newArrayList());
		} catch (Exception e) {
			logger.error("queryEmployeeByPage error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
	}

	@Override
	public Result<CuntaoEmployeeDto> queryVendorEmployeeByID(Long id) {
		try {
			CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(id);
			if(employee == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "员工不存在");
				return Result.of(errorInfo);
			}
			return Result.of(EmployeeConverter.convert2CuntaoEmployeeDto(employee));
		} catch (Exception e) {
			logger.error("queryEmployeeByID error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<List<CuntaoEmployeeDto>> queryVendorEmployeeByIDS(List<Long> ids) {
		try {
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
					.andIdIn(ids).andTypeEqualTo(CuntaoEmployeeType.vendor.name());
			List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			return Result.of(EmployeeConverter.convert2CuntaoEmployeeDtoList(employees));
		} catch (Exception e) {
			logger.error("queryEmployeeByIDS error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<List<CuntaoEmployeeDto>> queryStoreEmployeeByIdentifier(Long stationId,
			CuntaoEmployeeIdentifier identifier) {
		try {
			CuntaoEmployeeRelExample cuntaoVendorEmployeeExample = new CuntaoEmployeeRelExample();
			cuntaoVendorEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
					.andOwnerIdEqualTo(stationId).andTypeEqualTo(CuntaoEmployeeType.store.name());
			List<CuntaoEmployeeRel> cuntaoVendorEmployees = cuntaoEmployeeRelMapper
					.selectByExample(cuntaoVendorEmployeeExample);
			List<Long> employeeIds = cuntaoVendorEmployees.stream()
					.map(cuntaoVendorEmployee -> cuntaoVendorEmployee.getEmployeeId()).collect(Collectors.toList());
			if (employeeIds != null && !employeeIds.isEmpty()) {
				CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
				cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andTypeEqualTo(CuntaoEmployeeType.store.name()).andIdIn(employeeIds);
				List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
				List<CuntaoEmployee> employeeList = Lists.newArrayList();
				if(employees !=null){
					for(CuntaoEmployee employee : employees){
						List<UserRoleDto>  userRoles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(employee.getTaobaoUserId()+"");
						if(userRoles != null && !userRoles.isEmpty()){
							for(UserRoleDto userRole : userRoles){
								if(userRole != null && userRole.getRoleName().equals(identifier.name())){
									employeeList.add(employee);
								}
							}
						}
					}
				}
				return Result.of(EmployeeConverter.convert2CuntaoEmployeeDtoList(employeeList));
			}
			return Result.of(true);
		} catch (Exception e) {
			logger.error("queryStoreEmployeeByIdentifier error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
		
	}

	@Override
	public Result<CuntaoEmployeeDto> queryVendorEmployeeByTaobaoUserID(Long taobaoUserId) {
		try {
			CuntaoEmployeeExample example = new CuntaoEmployeeExample();
			example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId).andTypeEqualTo(CuntaoEmployeeType.vendor.name());
			List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(example);
			if(employees != null && !employees.isEmpty()){
				return Result.of(EmployeeConverter.convert2CuntaoEmployeeDto(employees.iterator().next()));
			}else{
				return Result.of(true);
			}
		} catch (Exception e) {
			logger.error("queryVendorEmployeeByTaobaoUserID error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<List<CuntaoEmployeeDto>> queryVendorEmployeeByTaobaoUserIDS(List<Long> taobaoUserIds) {
		try {
			CuntaoEmployeeExample example = new CuntaoEmployeeExample();
			example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdIn(taobaoUserIds).andTypeEqualTo(CuntaoEmployeeType.vendor.name());
			List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(example);
			if(employees != null && !employees.isEmpty()){
				return Result.of(EmployeeConverter.convert2CuntaoEmployeeDtoList(employees));
			}else{
				return Result.of(true);
			}
		} catch (Exception e) {
			logger.error("queryVendorEmployeeByTaobaoUserIDS error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}
	
	@Override
	public Result<List<CuntaoEmployeeInfoDto>> queryVendorEmployeeByTaobaoUserIdForBx(Long taobaoUserId) {
		List<CuntaoEmployeeInfoDto> result = new ArrayList<CuntaoEmployeeInfoDto>();
		//根据村小二taobaoUserId取服务商
		CuntaoServiceVendorExample  example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId);
		List<CuntaoServiceVendor> vendors = cuntaoServiceVendorMapper.selectByExample(example);
		
		if(CollectionUtils.isEmpty(vendors)){
			return Result.of(result);
		}
		//根据服务商ID取工人id和身份(送货员，安装员)，is_deleted=‘y’的也要取
		List<String> identifiers = new ArrayList<String>();
		identifiers.add(CuntaoEmployeeIdentifierEnum.VENDOR_DISTRIBUTOR.getCode());
		identifiers.add(CuntaoEmployeeIdentifierEnum.VENDOR_INSTALLER.getCode());
		CuntaoEmployeeRelExample cuntaoVendorEmployeeExample = new CuntaoEmployeeRelExample();
		cuntaoVendorEmployeeExample.createCriteria().andOwnerIdEqualTo(vendors.get(0).getId()).andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIdentifierIn(identifiers);
		List<CuntaoEmployeeRel> reLs = cuntaoEmployeeRelMapper.selectByExample(cuntaoVendorEmployeeExample);
		List<Long> employeeIds = reLs.stream().map(CuntaoEmployeeRel::getEmployeeId).collect(Collectors.toList());
		//根据工人ID，取工人基本信息
		if (CollectionUtils.isNotEmpty(employeeIds)) {
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIdIn(employeeIds);
			List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			if(CollectionUtils.isNotEmpty(employees)){
				for(CuntaoEmployee e : employees){
					CuntaoEmployeeInfoDto dto = new CuntaoEmployeeInfoDto();
					ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(e.getTaobaoUserId());
					dto.setAlipayAccount(basePaymentAccountDOResult.getModule().getOutUser());
			        ResultDO<BaseUserDO> baseUserDOresult = uicReadServiceClient.getBaseUserByUserId(e.getTaobaoUserId());
			        BaseUserDO baseUserDO = baseUserDOresult.getModule();
					dto.setCertNo(baseUserDO.getIdCardNumber());
					dto.setEmployeeId(e.getId());
					if ("y".equals(e.getIsDeleted())) {
						dto.setLeaveJobTime(e.getGmtModified());
					}
					dto.setMobile(e.getMobile());
					dto.setName(e.getName());
					dto.setStartJobTime(e.getGmtCreate());
					dto.setTaobaoNick(e.getTaobaoNick());
					dto.setIdentifier(buildIdentifier(e.getId(),reLs));
					dto.setTaobaoUserId(e.getTaobaoUserId());
					result.add(dto);
				}
			}
		}
		return Result.of(result);
	}
	
	private static List<CuntaoEmployeeIdentifierEnum> buildIdentifier(Long employeeId,List<CuntaoEmployeeRel> reLs){
		if (CollectionUtils.isNotEmpty(reLs)) {
			Set<String> s = new HashSet<String>();
			for(CuntaoEmployeeRel e : reLs){
				if (employeeId.equals(e.getEmployeeId())) {
					s.add(e.getIdentifier());
				}
			}
			return s.stream().map(employee -> CuntaoEmployeeIdentifierEnum.valueof(employee)).collect(Collectors.toList());
		}
		return null;
	}

}
