package com.taobao.cun.auge.company;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeState;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.dal.domain.CuntaoCompany;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
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
	private CuntaoCompanyMapper cuntaoCompanyMapper;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoCompanyEmployeeMapper cuntaoCompanyEmployeeMapper;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeWriteServiceImpl.class);
 
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Result<Long> addCompanyEmployee(Long companyId,CuntaoEmployeeDto employeeDto,CuntaoCompanyEmployeeType type) {
		ErrorInfo errorInfo = null;
		errorInfo = checkAddCompanyEmployee(companyId,employeeDto,type);
		
		CuntaoCompany  cuntaoCompany = cuntaoCompanyMapper.selectByPrimaryKey(companyId);
		if(cuntaoCompany == null){
			errorInfo = ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "公司不存在");
			return Result.of(errorInfo);
		}
		//TODO 效验规则细化
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		ResultDO<BaseUserDO> employeeUserDOresult = uicReadServiceClient.getBaseUserByNick(employeeDto.getTaobaoNick());
		errorInfo = checkTaobaoNick(employeeUserDOresult,"员工淘宝账号不存在或状态异常!");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo = checkTaobaoNickExists(companyId,employeeDto.getTaobaoNick(),"员工淘宝账号已存在");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		if(StringUtils.isNotEmpty(employeeDto.getMobile())){
			errorInfo =  checkMobileExists(employeeDto.getMobile(),"员工手机号已存在!");
			return Result.of(errorInfo);
		}
		
		try {
			CuntaoEmployee employee = new CuntaoEmployee();
			employee.setCreator(employeeDto.getOperator());
			employee.setGmtCreate(new Date());
			employee.setModifier(employeeDto.getOperator());
			employee.setGmtModified(new Date());
			employee.setMobile(employeeDto.getMobile());
			employee.setName(employeeDto.getName());
			employee.setTaobaoNick(employeeDto.getTaobaoNick());
			employee.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
			cuntaoEmployeeMapper.insertSelective(employee);
			Long employeeId = employee.getId();
			CuntaoCompanyEmployee cuntaoCompanyEmployee = new CuntaoCompanyEmployee();
			cuntaoCompanyEmployee.setCreator(employeeDto.getOperator());
			cuntaoCompanyEmployee.setGmtCreate(new Date());
			cuntaoCompanyEmployee.setModifier(employeeDto.getOperator());
			cuntaoCompanyEmployee.setGmtModified(new Date());
			cuntaoCompanyEmployee.setCompanyId(companyId);
			cuntaoCompanyEmployee.setEmployeeId(employeeId);
			cuntaoCompanyEmployee.setState(CuntaoCompanyEmployeeState.SERVICING.name());
			cuntaoCompanyEmployee.setType(type.name());
			cuntaoCompanyEmployeeMapper.insertSelective(cuntaoCompanyEmployee);
			//TODO 添加Endor组织和人员，并分配角色
			return Result.of(employeeId);
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
	
	
	private ErrorInfo checkTaobaoNickExists(Long companyId,String taobaoNick,String errorMessage){
		CuntaoCompanyEmployeeExample example = new CuntaoCompanyEmployeeExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCompanyIdEqualTo(companyId);
		List<CuntaoCompanyEmployee> cuntaoCompanyEmployees = cuntaoCompanyEmployeeMapper.selectByExample(example);
		if(cuntaoCompanyEmployees != null  && !cuntaoCompanyEmployees.isEmpty()){
			List<Long>  employeeIds = cuntaoCompanyEmployees.stream().map(employee -> employee.getEmployeeId()).collect(Collectors.toList());
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(employeeIds).andTaobaoNickEqualTo(taobaoNick);
			List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
				return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
			}
		}
		return null;
	}
	
	private ErrorInfo checkTaobaoNickExists(String taobaoNick,String errorMessage){
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andTaobaoNickEqualTo(taobaoNick);
			List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
				return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
			}
		return null;
	}
	
	private ErrorInfo checkMobileExists(String mobile,String errorMessage){
		CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
		cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n").andMobileEqualTo(mobile);
		List<CuntaoEmployee> cuntaoEmployees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
		if(cuntaoEmployees != null && !cuntaoEmployees.isEmpty()){
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, errorMessage);
		}
	return null;
}
	
	
	private ErrorInfo checkAddCompanyEmployee(Long companyId,CuntaoEmployeeDto employeeDto,CuntaoCompanyEmployeeType type){
		try {
			Assert.notNull(companyId,"公司ID不能为空");
			Assert.notNull(type,"员工类型不能为空");
			BeanValidator.validateWithThrowable(employeeDto);
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private ErrorInfo checkUpdateCompanyEmployee(CuntaoEmployeeDto employeeDto){
		try {
			Assert.notNull(employeeDto.getId(),"员工ID不能为空");
			Assert.notNull(employeeDto.getOperator(),"操作人员不能为空");
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
	
	@Override
	public Result<Boolean> updateCompanyEmployee(CuntaoEmployeeDto updateCuntaoEmployeeDto) {
		ErrorInfo errorInfo = null;
		errorInfo = checkUpdateCompanyEmployee(updateCuntaoEmployeeDto);
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
			errorInfo = checkTaobaoNickExists(updateCuntaoEmployeeDto.getTaobaoNick(),"员工淘宝账号已存在!");
			return Result.of(errorInfo);
		}
		if(StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getMobile())){
			errorInfo =  checkMobileExists(updateCuntaoEmployeeDto.getMobile(),"员工手机号已存在!");
			return Result.of(errorInfo);
		}
		try {
				employee = new CuntaoEmployee();
				employee.setId(updateCuntaoEmployeeDto.getId());
				employee.setGmtModified(new Date());
				employee.setModifier(updateCuntaoEmployeeDto.getOperator());
			if(StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getTaobaoNick())){
				employee.setTaobaoNick(employeeUserDOresult.getModule().getNick());
				employee.setTaobaoUserId(employeeUserDOresult.getModule().getUserId());
			}
			if(StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getMobile())){
				employee.setMobile(updateCuntaoEmployeeDto.getMobile());
			}
			if(StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getName())){
				employee.setName(updateCuntaoEmployeeDto.getName());
			}
			cuntaoEmployeeMapper.updateByPrimaryKeySelective(employee);
		} catch (Exception e) {
			logger.error("updateCompanyEmployee error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
		return  Result.of(Boolean.TRUE);
	}

	@Override
	public Result<Boolean> removeCompanyEmployee(Long employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
