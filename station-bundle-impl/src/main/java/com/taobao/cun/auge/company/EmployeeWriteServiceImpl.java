package com.taobao.cun.auge.company;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.validator.BeanValidator;
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
 
	@Override
	
	public Result<Long> addVendorEmployee(Long vendorId,CuntaoEmployeeDto employeeDto,CuntaoEmployeeIdentifier identifier) {
		ErrorInfo errorInfo = null;
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
		
		errorInfo = checkTaobaoNickExists(vendorId,employeeDto.getTaobaoNick(),CuntaoEmployeeType.vendor.name(),identifier,"员工淘宝账号已存在");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		if(StringUtils.isNotEmpty(employeeDto.getMobile())){
			errorInfo =  checkMobileExists(employeeDto.getMobile(),CuntaoEmployeeType.vendor.name(),"员工手机号已存在!");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
		}
		try {
			employeeDto.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
			return Result.of(employeeWriteBO.addVendorEmployee(vendorId,employeeDto,identifier));
		} catch (Exception e) {
			logger.error("addCompanyEmployee company error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
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
	public Result<Boolean> removeVendorEmployee(Long employeeId) {
		//CuntaoEmployee employee = new CuntaoEmployee();
		//employee.setId(employeeId);
		//employee.setGmtCreate(new Date());
		//employee.setModifier(modifier);
		//employee.setIsDeleted("y");
		//cuntaoEmployeeMapper.updateByPrimaryKeySelective(employee);
		return null;
	}

	@Override
	public Result<Long> addStoreEmployee(Long stationId, CuntaoEmployeeDto storeEmployee, CuntaoEmployeeIdentifier identifier) {
		ErrorInfo errorInfo = null;
		if(storeEmployee.getId() == null){
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
				errorInfo =  checkMobileExists(storeEmployee.getMobile(),CuntaoEmployeeType.store.name(),"员工手机号已存在!");
				if(errorInfo != null){
					return Result.of(errorInfo);
				}
			}
			storeEmployee.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
		}
		try {
			return Result.of(employeeWriteBO.addStoreEmployee(stationId, storeEmployee, identifier));
		} catch (Exception e) {
			logger.error("addCompanyEmployee company error!",e);
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
		CuntaoServiceVendor  cuntaoServiceVendor = cuntaoServiceVendorMapper.selectByPrimaryKey(vendorId);
		if(cuntaoServiceVendor == null){
			errorInfo = ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "公司不存在");
			return Result.of(errorInfo);
		}
		
		errorInfo = checkEmployeeExists(vendorId,employeeId,CuntaoEmployeeType.vendor.name(),identifier,"员工已存在");
		if(errorInfo != null){
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

	private ErrorInfo checkEmployeeExists(Long vendorId,Long employeeId,String type,CuntaoEmployeeIdentifier identifier,String errorMessage){
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andOwnerIdEqualTo(vendorId).andTypeEqualTo(type).andIdentifierEqualTo(identifier.name());
		List<CuntaoEmployeeRel> cuntaoCompanyEmployees = cuntaoEmployeeRelMapper.selectByExample(example);
		if(cuntaoCompanyEmployees != null  && !cuntaoCompanyEmployees.isEmpty()){
			List<Long>  employeeIds = cuntaoCompanyEmployees.stream().map(employee -> employee.getEmployeeId()).collect(Collectors.toList());
			if(employeeIds.contains(employeeId)){
				return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
			}
		}
		return null;
	}

}
