package com.taobao.cun.auge.company;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.bo.EmployeeWriteBO;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.UserRoleDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("employeeWriteService")
@HSFProvider(serviceInterface = EmployeeWriteService.class)
public class EmployeeWriteServiceImpl implements EmployeeWriteService{

	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;

	@Autowired
	private EmployeeWriteBO employeeWriteBO;
	
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeWriteServiceImpl.class);
 
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	@Autowired
	private CuntaoStoreMapper cuntaoStoreMapper;
	
	@Override
	
	public Result<Long> addVendorEmployee(Long vendorId,CuntaoEmployeeDto employeeDto,CuntaoEmployeeIdentifier identifier) {
		ErrorInfo errorInfo = null;
		CuntaoEmployeeExample example = new CuntaoEmployeeExample();
		example.createCriteria().andTaobaoNickEqualTo(employeeDto.getTaobaoNick()).andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIsDeletedEqualTo("n");
		
		List<CuntaoEmployee>  employees = cuntaoEmployeeMapper.selectByExample(example);
	
		if(employees == null||employees.isEmpty()){
			errorInfo = checkAddEmployee(vendorId,employeeDto,identifier);
			//TODO 效验规则细化
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			CuntaoServiceVendor  cuntaoServiceVendor = cuntaoServiceVendorMapper.selectByPrimaryKey(vendorId);
			if(cuntaoServiceVendor == null){
				errorInfo = ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "公司不存在");
				return Result.of(errorInfo);
			}
			
			ResultDO<BaseUserDO> employeeUserDOresult = uicReadServiceClient.getBaseUserByNick(employeeDto.getTaobaoNick());
			errorInfo = checkTaobaoNick(employeeUserDOresult,"员工淘宝账号不存在或状态异常!");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			
			errorInfo =  checkPromotedType(employeeUserDOresult.getModule().getPromotedType(),"员工淘宝账号绑定支付宝未做个人实名认证");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			/**if(StringUtils.isNotEmpty(employeeDto.getMobile())){
				errorInfo =  checkMobileExists(employeeDto.getMobile(),CuntaoEmployeeType.vendor.name(),"员工手机号已存在!");
				if(errorInfo != null){
					return Result.of(errorInfo);
				}
			}**/
			employeeDto.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
		}else{
			Long employeeId = employees.iterator().next().getId();
			errorInfo=  checkEmployeeRel(vendorId,employeeId,identifier.name(),CuntaoEmployeeType.vendor.name(),"员工只能归属一个服务商且角色不能重复");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			employeeDto.setId(employees.iterator().next().getId());
		}
		try {
			return Result.of(employeeWriteBO.addVendorEmployee(vendorId,employeeDto,identifier));
		} catch (Exception e) {
			logger.error("addCompanyEmployee company error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	private ErrorInfo checkEmployeeRel(Long ownerId,Long employeeId,String identifier,String type,String errorMessage){
		CuntaoEmployeeRelExample cuntaoEmployeeRelExample = new CuntaoEmployeeRelExample();
		cuntaoEmployeeRelExample.createCriteria().andIsDeletedEqualTo("n").andEmployeeIdEqualTo(employeeId).andTypeEqualTo(type);
		List<CuntaoEmployeeRel> cuntaoEmployeeRels = cuntaoEmployeeRelMapper.selectByExample(cuntaoEmployeeRelExample);
		if(cuntaoEmployeeRels == null || cuntaoEmployeeRels.isEmpty()){
			return null;
		}else{
			//员工只能归属于一个服务商
			if(!cuntaoEmployeeRels.stream().allMatch(rel ->ownerId.equals(rel.getOwnerId()))){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
				return errorInfo;
			}
			//且角色不能重复
			if(cuntaoEmployeeRels.stream().anyMatch(rel ->identifier.equals(rel.getIdentifier()))){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
				return errorInfo;
			}
		}
		return null;
	}
	
	
	
	
	
	
	@Override
	public Result<Long> addVendorEmployeeWithIdentifers(Long vendorId, CuntaoEmployeeDto employeeDto,
			List<CuntaoEmployeeIdentifier> identifiers) {
		Result<Long> result = null;
		if(identifiers!= null &&  !identifiers.isEmpty()){
			CuntaoEmployeeIdentifier identifier = identifiers.iterator().next();
			result = this.addVendorEmployee(vendorId, employeeDto, identifier);
			Long employeeId = result.getModule();
			identifiers.remove(identifier);
			for(CuntaoEmployeeIdentifier ident : identifiers){
				this.addVendorEmployeeByEmployeeId(vendorId, employeeId, ident);
			}
		}
		return result;
	}

	
	
	private ErrorInfo checkTaobaoNick(ResultDO<BaseUserDO> baseUserDOresult,String errorMessage){
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	private ErrorInfo checkPromotedType(int promotedType,String errorMessage){
		if (((promotedType & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE)) {
			return ErrorInfo.of(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	private ErrorInfo checkTaobaoNickExists(Long vendorId,String taobaoNick,String type,CuntaoEmployeeIdentifier identifier,String errorMessage){
		CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
		cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andTaobaoNickEqualTo(taobaoNick).andTypeEqualTo(type);
		List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
		if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	private ErrorInfo checkTaobaoNickExists(String taobaoNick,String type,String errorMessage){
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andTaobaoNickEqualTo(taobaoNick).andTypeEqualTo(type);;
			List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
				return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
			}
		return null;
	}
	
	private ErrorInfo checkMobileExists(String mobile,String type,String errorMessage){
		CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
		cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andMobileEqualTo(mobile).andTypeEqualTo(type);
		List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
		if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
		}
	return null;
}
	
	
	private ErrorInfo checkAddEmployee(Long ownerId,CuntaoEmployeeDto employeeDto,CuntaoEmployeeIdentifier identifier){
		try {
			Assert.notNull(ownerId,"公司ID不能为空");
			Assert.notNull(identifier,"员工身份不能为空");
			BeanValidator.validateWithThrowable(employeeDto);
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	

	private ErrorInfo checkUpdateEmployee(CuntaoEmployeeDto employeeDto){
		try {
			Assert.notNull(employeeDto.getId(),"员工ID不能为空");
			Assert.notNull(employeeDto.getOperator(),"操作人员不能为空");
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
	
	@Override
	public Result<Boolean> updateVendorEmployee(CuntaoEmployeeDto updateCuntaoEmployeeDto) {
		ErrorInfo errorInfo = null;
		errorInfo = checkUpdateEmployee(updateCuntaoEmployeeDto);
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(updateCuntaoEmployeeDto.getId());
		if(employee == null){
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "待更新的员工不存在"));
		}
		ResultDO<BaseUserDO> employeeUserDOresult = uicReadServiceClient.getBaseUserByNick(updateCuntaoEmployeeDto.getTaobaoNick());
		errorInfo = checkTaobaoNick(employeeUserDOresult,"员工淘宝账号不存在或状态异常!");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		if(!employee.getTaobaoNick().equals(updateCuntaoEmployeeDto.getTaobaoNick())){
			errorInfo = checkTaobaoNickExists(updateCuntaoEmployeeDto.getTaobaoNick(),CuntaoEmployeeType.vendor.name(),"员工淘宝账号已存在!");
			return Result.of(errorInfo);
		}
		if(StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getMobile())){
			errorInfo =  checkMobileExists(updateCuntaoEmployeeDto.getMobile(),CuntaoEmployeeType.vendor.name(),"员工手机号已存在!");
			return Result.of(errorInfo);
		}
		try {
			employeeWriteBO.updateVendorEmployee(updateCuntaoEmployeeDto);
		} catch (Exception e) {
			logger.error("updateCompanyEmployee error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
		return  Result.of(Boolean.TRUE);
	}

	

	@Override
	public Result<Boolean> removeVendorEmployee(Long employeeId,Long taobaoUserId,String operator) {
		try {
			Assert.notNull(employeeId, "员工ID不能为空");
			Assert.notNull(operator, "操作人不能为空");
			
			List<UserRoleDto> roles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(taobaoUserId+"");
			
			CuntaoServiceVendor cuntaoServiceVendor = getVendorByEmployeeId(employeeId);
			
			if(cuntaoServiceVendor == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该员工不属于任何供应商");
				return Result.of(errorInfo);
			}
			
			
			Optional<UserRoleDto> userRole = roles.stream().filter(role -> CuntaoEmployeeIdentifier.VENDOR_MANAGER.name().equals(role.getRoleName()))
					.filter(role -> role.getOrgId().equals(cuntaoServiceVendor.getEndorOrgId()))
					.findFirst();
			if(userRole.isPresent()){
				return Result.of(employeeWriteBO.removeVendorEmployee(employeeId,operator));
			}
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该用户没有权限删除");
			return Result.of(errorInfo);
		} catch (Exception e) {
			logger.error("removeVendorEmployee error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}




	private CuntaoServiceVendor getVendorByEmployeeId(Long employeeId) {
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andEmployeeIdEqualTo(employeeId);
		List<CuntaoEmployeeRel> employeeRels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(employeeRels == null || employeeRels.isEmpty()){
			return null;
		}
		CuntaoEmployeeRel rel = employeeRels.iterator().next();
		
		CuntaoServiceVendor cuntaoServiceVendor = cuntaoServiceVendorMapper.selectByPrimaryKey(rel.getOwnerId());
		return cuntaoServiceVendor;
	}

	@Override
	public Result<Long> addStoreEmployee(Long stationId, CuntaoEmployeeDto storeEmployee, CuntaoEmployeeIdentifier identifier) {
		ErrorInfo errorInfo = null;
		CuntaoEmployeeExample example = new CuntaoEmployeeExample();
		example.createCriteria().andTaobaoNickEqualTo(storeEmployee.getTaobaoNick()).andTypeEqualTo(CuntaoEmployeeType.store.name()).andIsDeletedEqualTo("n");
		
		List<CuntaoEmployee>  employees = cuntaoEmployeeMapper.selectByExample(example);
		
		if(employees == null||employees.isEmpty()){
			errorInfo = checkAddEmployee(stationId,storeEmployee,identifier);
			//TODO 效验规则细化
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			
			ResultDO<BaseUserDO> employeeUserDOresult = uicReadServiceClient.getBaseUserByNick(storeEmployee.getTaobaoNick());
			errorInfo = checkTaobaoNick(employeeUserDOresult,"员工淘宝账号不存在或状态异常!");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			errorInfo =  checkPromotedType(employeeUserDOresult.getModule().getPromotedType(),"员工淘宝账号绑定支付宝未做个人实名认证");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			errorInfo = checkTaobaoNickExists(stationId,storeEmployee.getTaobaoNick(),CuntaoEmployeeType.store.name(),identifier,"员工淘宝账号已存在");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			if(StringUtils.isNotEmpty(storeEmployee.getMobile())){
				//errorInfo =  checkMobileExists(storeEmployee.getMobile(),CuntaoEmployeeType.store.name(),"员工手机号已存在!");
				//if(errorInfo != null){
				//	return Result.of(errorInfo);
			//}
			}
			storeEmployee.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
		}else{
			Long employeeId = employees.iterator().next().getId();
			errorInfo=  checkEmployeeRel(stationId,employeeId,identifier.name(),CuntaoEmployeeType.store.name(),"员工只能归属一个门店且角色不能重复");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
			storeEmployee.setId(employeeId);
		}
		try {
			return Result.of(employeeWriteBO.addStoreEmployee(stationId, storeEmployee, identifier));
		} catch (Exception e) {
			logger.error("addStoreEmployee  error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}


	private ErrorInfo checkAddEmployeeByEmployeeId(Long ownerId,Long  employeeId,CuntaoEmployeeIdentifier identifier){
		try {
			Assert.notNull(ownerId,"公司ID不能为空");
			Assert.notNull(identifier,"员工身份不能为空");
			Assert.notNull(employeeId,"员工ID不能为空");
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}

	@Override
	public Result<Long> addVendorEmployeeByEmployeeId(Long vendorId, Long employeeId,
			CuntaoEmployeeIdentifier identifier) {
		ErrorInfo errorInfo = null;
		errorInfo = checkAddEmployeeByEmployeeId(vendorId,employeeId,identifier);
		//TODO 效验规则细化
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo=  checkEmployeeRel(vendorId,employeeId,identifier.name(),CuntaoEmployeeType.vendor.name(),"员工只能归属一个服务商且角色不能重复");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		CuntaoServiceVendor  cuntaoServiceVendor = cuntaoServiceVendorMapper.selectByPrimaryKey(vendorId);
		if(cuntaoServiceVendor == null){
			errorInfo = ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "公司不存在");
			return Result.of(errorInfo);
		}
		
		try {
			return Result.of(employeeWriteBO.addVendorEmployeeByEmployeeId(vendorId,employeeId,identifier));
		} catch (Exception e) {
			logger.error("addVendorEmployeeByEmployeeId  error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	




	@Override
	public Result<Boolean> removeStoreEmployee(Long employeeId,Long taobaoUserId, String operator) {
		try {
			Assert.notNull(employeeId, "员工ID不能为空");
			Assert.notNull(operator, "操作人不能为空");
			List<UserRoleDto> roles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(taobaoUserId+"");
			CuntaoStore store = getStoreByEmployeeId(employeeId);
			if(store == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该员工没有门店");
				return Result.of(errorInfo);
			}
			Optional<UserRoleDto> userRole = roles.stream().filter(role -> CuntaoEmployeeIdentifier.STORE_MANAGER.name().equals(role.getRoleName()))
					.filter(role -> role.getOrgId().equals(store.getEndorOrgId()))
					.findFirst();
			if(userRole.isPresent()){
				return Result.of(employeeWriteBO.removeStoreEmployee(employeeId, operator));
			}
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该用户没有权限删除");
			return Result.of(errorInfo);
		} catch (Exception e) {
			logger.error("removeStoreEmployee error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}




	@Override
	public Result<Boolean> removeStoreEmployeeRole(Long employeeId,Long taobaoUserId, String operator, CuntaoEmployeeIdentifier cuntaoEmployeeIdentifier) {
		try {
			Assert.notNull(employeeId, "员工ID不能为空");
			Assert.notNull(operator, "操作人不能为空");
			Assert.notNull(cuntaoEmployeeIdentifier, "操作角色不能为空");
			
			List<UserRoleDto> roles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(taobaoUserId+"");
			CuntaoStore store = getStoreByEmployeeId(employeeId);
			if(store == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该员工没有门店");
				return Result.of(errorInfo);
			}
			Optional<UserRoleDto> userRole = roles.stream().filter(role -> CuntaoEmployeeIdentifier.STORE_MANAGER.name().equals(role.getRoleName()))
					.filter(role -> role.getOrgId().equals(store.getEndorOrgId()))
					.findFirst();
			if(userRole.isPresent()){
				return Result.of(employeeWriteBO.removeStoreEmployeeRole(employeeId, operator,cuntaoEmployeeIdentifier));
			}
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "该用户没有权限删除");
			return Result.of(errorInfo);
		} catch (Exception e) {
			logger.error("removeStoreEmployeeRole error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}




	private CuntaoStore getStoreByEmployeeId(Long employeeId) {
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andEmployeeIdEqualTo(employeeId);
		List<CuntaoEmployeeRel> employeeRels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(employeeRels == null || employeeRels.isEmpty()){
			return null;
		}
		CuntaoEmployeeRel rel = employeeRels.iterator().next();
		
		CuntaoStoreExample storeExample = new CuntaoStoreExample();
		storeExample.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(rel.getOwnerId());
		List<CuntaoStore> stores = cuntaoStoreMapper.selectByExample(storeExample);
		if(stores == null || stores.isEmpty()){
			return null;
		}
		CuntaoStore store = stores.iterator().next();
		return store;
	}




	
}
