package com.taobao.cun.auge.company;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.taobao.cun.auge.company.dto.CuntaoCompanyDto;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeState;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoCompanyState;
import com.taobao.cun.auge.dal.domain.CuntaoCompany;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("companyWriteService")
@HSFProvider(serviceInterface = CompanyWriteService.class)
public class CompanyWriteServiceImpl implements CompanyWriteService {

	private static final int ALIPAY_ENTERPRICE_PROMOTED_TYPE = 4;
	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	@Autowired
	private CuntaoCompanyMapper cuntaoCompanyMapper;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoCompanyEmployeeMapper cuntaoCompanyEmployeeMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyWriteServiceImpl.class);
	
	@SuppressWarnings("static-access")
	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Result<Long> addCompany(CuntaoCompanyDto cuntaoCompanyDto) {
		Result<Long> result = null;
		ErrorInfo errorInfo = checkAddCuntaoCompanyDto(cuntaoCompanyDto);
		//TODO 效验规则细化
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo = checkTaobaoAndAliPayInfo(cuntaoCompanyDto.getTaobaoNick());
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		//TODO 检查手机号重复
		try {
			ResultDO<BaseUserDO> companyUserDOresult = uicReadServiceClient.getBaseUserByNick(cuntaoCompanyDto.getTaobaoNick());
			ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(companyUserDOresult.getModule().getUserId());
			CompanyAndManagerInfo cuntaoCompanyAndManagerInfo = addCompanyAndManager(cuntaoCompanyDto, companyUserDOresult.getModule(), basePaymentAccountDOResult.getModule());
			//调用endor创建组织和管理员，分配管理员角色
			result = result.of(cuntaoCompanyDto.getId());
			return result;
		} catch (Exception e) {
			logger.error("add company error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
	}

	private ErrorInfo checkTaobaoAndAliPayInfo(String taobaoNick){
		ResultDO<BaseUserDO> companyUserDOresult = uicReadServiceClient.getBaseUserByNick(taobaoNick);
		ErrorInfo errorInfo = checkTaobaoNick(companyUserDOresult,"服务商淘宝账号不存在或状态异常!");
		if(errorInfo != null){
			return errorInfo;
		}
		BaseUserDO baseUserDO = companyUserDOresult.getModule();
		ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(baseUserDO.getUserId());
		errorInfo = checkAlipayAccount(basePaymentAccountDOResult,"服务商淘宝账号尚未完成支付宝绑定操作，请联系申请人，先在淘宝->账号管理中，完成支付宝账号的绑定，并在支付宝平台完成实名认证操作!");
		if(errorInfo != null){
			return errorInfo;
		}
		
		errorInfo = checkPromotedType(baseUserDO.getPromotedType(),"服务商淘宝账号绑定的支付宝账号未做企业实名认证;请联系申请人,在支付宝平台完成企业实名认证操作!");
		if(errorInfo != null){
			return errorInfo;
		}
		return null;
	}
	
	
	private CompanyAndManagerInfo addCompanyAndManager(CuntaoCompanyDto cuntaoCompanyDto, BaseUserDO baseUserDO,
			BasePaymentAccountDO basePaymentAccountDO) {
		CompanyAndManagerInfo cuntaoCompanyAndManagerInfo = new CompanyAndManagerInfo();
		CuntaoCompany cuntaoCompany = convert2CuntaoCompany(cuntaoCompanyDto, baseUserDO,basePaymentAccountDO);
		cuntaoCompanyMapper.insertSelective(cuntaoCompany);
		cuntaoCompanyDto.setId(cuntaoCompany.getId());
		CuntaoEmployee manager = convert2CuntaoCompanyEmployee(cuntaoCompanyDto,baseUserDO);
		cuntaoEmployeeMapper.insertSelective(manager);
		CuntaoCompanyEmployee cuntaoCompanyEmployee = new CuntaoCompanyEmployee();
		cuntaoCompanyEmployee.setCreator(cuntaoCompanyDto.getOperator());
		cuntaoCompanyEmployee.setGmtCreate(new Date());
		cuntaoCompanyEmployee.setModifier(cuntaoCompanyDto.getOperator());
		cuntaoCompanyEmployee.setGmtModified(new Date());
		cuntaoCompanyEmployee.setIsDeleted("n");
		cuntaoCompanyEmployee.setCompanyId(cuntaoCompany.getId());
		cuntaoCompanyEmployee.setEmployeeId(manager.getId());
		cuntaoCompanyEmployee.setState(CuntaoCompanyEmployeeState.SERVICING.name());
		cuntaoCompanyEmployee.setType(CuntaoCompanyEmployeeType.MANAGER.name());
		cuntaoCompanyEmployeeMapper.insertSelective(cuntaoCompanyEmployee);
		cuntaoCompanyAndManagerInfo.setCuntaoCompany(cuntaoCompany);
		cuntaoCompanyAndManagerInfo.setManager(manager);
		cuntaoCompanyAndManagerInfo.setId(cuntaoCompanyEmployee.getId());
		cuntaoCompanyAndManagerInfo.setState(CuntaoCompanyEmployeeState.valueOf(cuntaoCompanyEmployee.getState()));
		cuntaoCompanyAndManagerInfo.setType(CuntaoCompanyEmployeeType.valueOf(cuntaoCompanyEmployee.getType()));
		return cuntaoCompanyAndManagerInfo;
	}

	private CuntaoEmployee convert2CuntaoCompanyEmployee(CuntaoCompanyDto cuntaoCompanyDto,BaseUserDO baseUserDO){
		CuntaoEmployee cuntaoEmployee = new CuntaoEmployee();
		cuntaoEmployee.setCreator(baseUserDO.getUserId()+"");
		cuntaoEmployee.setGmtCreate(new Date());
		cuntaoEmployee.setModifier(baseUserDO.getUserId()+"");
		cuntaoEmployee.setGmtModified(new Date());
		cuntaoEmployee.setIsDeleted("n");
		cuntaoEmployee.setName(baseUserDO.getFullname());
		cuntaoEmployee.setMobile(cuntaoCompanyDto.getMobile());
		cuntaoEmployee.setTaobaoNick(baseUserDO.getNick());
		cuntaoEmployee.setTaobaoUserId(baseUserDO.getUserId());
		return cuntaoEmployee;
	}
	
	
	private ErrorInfo checkAddCuntaoCompanyDto(CuntaoCompanyDto cuntaoCompanyDto){
		try {
			BeanValidator.validateWithThrowable(cuntaoCompanyDto);
		} catch (AugeBusinessException e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
	
	private ErrorInfo checkTaobaoNick(ResultDO<BaseUserDO> baseUserDOresult,String errorMessage){
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	private ErrorInfo checkAlipayAccount(ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult,String errorMessage){
		if (basePaymentAccountDOResult == null || !basePaymentAccountDOResult.isSuccess() || basePaymentAccountDOResult.getModule() == null) {
			return ErrorInfo.of(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	private ErrorInfo checkPromotedType(int promotedType,String errorMessage){
		if (((promotedType & ALIPAY_ENTERPRICE_PROMOTED_TYPE) != ALIPAY_ENTERPRICE_PROMOTED_TYPE)) {
			return ErrorInfo.of(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE, null, errorMessage);
		}
		return null;
	}
	
	
	private CuntaoCompany convert2CuntaoCompany(CuntaoCompanyDto cuntaoCompanyDto, BaseUserDO baseUserDO,BasePaymentAccountDO basePaymentAccountDO) {
		CuntaoCompany cuntaoCompany = new CuntaoCompany();
		cuntaoCompany.setCreator(baseUserDO.getUserId()+"");
		cuntaoCompany.setGmtCreate(new Date());
		cuntaoCompany.setModifier(baseUserDO.getUserId()+"");
		cuntaoCompany.setGmtModified(new Date());
		cuntaoCompany.setIsDeleted("n");
		cuntaoCompany.setCompanyName(cuntaoCompanyDto.getCompanyName());
		cuntaoCompany.setMobile(cuntaoCompany.getMobile());
		cuntaoCompany.setAlipayAccountNo(basePaymentAccountDO.getAccountNo());
		cuntaoCompany.setAlipayOutUser(basePaymentAccountDO.getOutUser());
		cuntaoCompany.setTaobaoNick(baseUserDO.getNick());
		cuntaoCompany.setTaobaoUserId(baseUserDO.getUserId());
		cuntaoCompany.setType(cuntaoCompanyDto.getType().name());
		cuntaoCompany.setState(CuntaoCompanyState.SERVICING.name());
		cuntaoCompany.setRemark(cuntaoCompanyDto.getRemark());
		return cuntaoCompany;
	}

	
	
	@Override
	public Result<Boolean> removeCompany(Long companyId) {
		return null;
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Result<Boolean> updateCompany(CuntaoCompanyDto cuntaoCompanyDto) {
		Result<Boolean> result = null;
		ErrorInfo errorInfo = checkUpdateCuntaoCompanyDto(cuntaoCompanyDto);
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		CuntaoCompany company = cuntaoCompanyMapper.selectByPrimaryKey(cuntaoCompanyDto.getId());
		errorInfo = ErrorInfo.of(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, null, "指定ID公司不存在");
		if(company == null){
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
		}
		try {
			CuntaoEmployee manager = getCompanyManager(cuntaoCompanyDto.getId());
			
			if(StringUtils.isNotEmpty(cuntaoCompanyDto.getTaobaoNick()) && !cuntaoCompanyDto.getTaobaoNick().equals(company.getTaobaoNick())){
				errorInfo = checkTaobaoAndAliPayInfo(cuntaoCompanyDto.getTaobaoNick());
				if(errorInfo != null){
					return Result.of(errorInfo);
				}
				ResultDO<BaseUserDO> companyUserDOresult = uicReadServiceClient.getBaseUserByNick(cuntaoCompanyDto.getTaobaoNick());
				ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(companyUserDOresult.getModule().getUserId());
				company.setTaobaoNick(companyUserDOresult.getModule().getNick());
				company.setTaobaoUserId(companyUserDOresult.getModule().getUserId());
				company.setAlipayOutUser(basePaymentAccountDOResult.getModule().getOutUser());
				company.setAlipayAccountNo(basePaymentAccountDOResult.getModule().getAccountNo());
				
				if(manager != null){
					manager.setTaobaoNick(companyUserDOresult.getModule().getNick());
					manager.setTaobaoUserId(companyUserDOresult.getModule().getUserId());
					manager.setName(companyUserDOresult.getModule().getFullname());
					manager.setGmtModified(new Date());
					manager.setModifier(cuntaoCompanyDto.getOperator());
					if(StringUtils.isNotEmpty(cuntaoCompanyDto.getMobile())){
						manager.setMobile(cuntaoCompanyDto.getMobile());
					}
					cuntaoEmployeeMapper.updateByPrimaryKeySelective(manager);
				}
			}
			if(StringUtils.isNotEmpty(cuntaoCompanyDto.getMobile())){
				company.setMobile(cuntaoCompanyDto.getMobile());
			}
			if(cuntaoCompanyDto.getType() != null){
				company.setType(cuntaoCompanyDto.getType().name());
			}
			if(StringUtils.isNotEmpty(cuntaoCompanyDto.getRemark())){
				company.setRemark(cuntaoCompanyDto.getRemark());
			}
			company.setGmtModified(new Date());
			company.setModifier(cuntaoCompanyDto.getOperator());
			cuntaoCompanyMapper.updateByPrimaryKeySelective(company);
			 result = Result.of(Boolean.TRUE);
			 return result;
		} catch (Exception e) {
			logger.error("update company error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
	}

	private CuntaoEmployee getCompanyManager(Long companyId){
		CuntaoCompanyEmployeeExample example = new CuntaoCompanyEmployeeExample();
		example.createCriteria().andCompanyIdEqualTo(companyId).andTypeEqualTo(CuntaoCompanyEmployeeType.MANAGER.name()).andIsDeletedEqualTo("n");
		List<CuntaoCompanyEmployee> cuntaoCompanyEmployees = cuntaoCompanyEmployeeMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(cuntaoCompanyEmployees)){
			CuntaoCompanyEmployee cuntaoCompanyEmployee =  cuntaoCompanyEmployees.iterator().next();
			return cuntaoEmployeeMapper.selectByPrimaryKey(cuntaoCompanyEmployee.getEmployeeId());
		}
		return null;
	} 
	
	
	private ErrorInfo checkUpdateCuntaoCompanyDto(CuntaoCompanyDto cuntaoCompanyDto){
		try {
			Assert.notNull(cuntaoCompanyDto.getId(),"公司ID不能为空");
			Assert.notNull(cuntaoCompanyDto.getOperator(),"操作人员不能为空");
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
}
