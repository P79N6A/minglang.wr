package com.taobao.cun.auge.company;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.bo.VendorWriteBO;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("vendorWriteService")
@HSFProvider(serviceInterface = VendorWriteService.class)
public class VendorWriteServiceImpl implements VendorWriteService {

	private static final int ALIPAY_ENTERPRICE_PROMOTED_TYPE = 4;
	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	
	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;
	
	private static final Logger logger = LoggerFactory.getLogger(VendorWriteServiceImpl.class);
	
	@Autowired
	private VendorWriteBO vendorWriteBO;
	
	@SuppressWarnings("static-access")
	@Override
	public Result<Long> addVendor(CuntaoServiceVendorDto cuntaoServiceVendorDto) {
		Result<Long> result = null;
		ErrorInfo errorInfo = checkAddCuntaoVendorDto(cuntaoServiceVendorDto);
		//TODO 效验规则细化
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo = checkTaobaoAndAliPayInfo(cuntaoServiceVendorDto.getTaobaoNick());
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		if(StringUtils.isNotEmpty(cuntaoServiceVendorDto.getMobile())){
			errorInfo =  checkMobileExists(cuntaoServiceVendorDto.getMobile(),"公司手机号已存在!");
			if(errorInfo != null){
				return Result.of(errorInfo);
			}
		}
		try {
			result = result.of(vendorWriteBO.addVendor(cuntaoServiceVendorDto));
			return result;
		} catch (Exception e) {
			logger.error("addVendor error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
		
	}

	private ErrorInfo checkMobileExists(String mobile, String errorMessage) {
		CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andMobileEqualTo(mobile);
		List<CuntaoServiceVendor> result = cuntaoServiceVendorMapper.selectByExample(example);
		if(result != null && !result.isEmpty()){
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, errorMessage);
		}
		return null;
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
		if(diamondConfiguredProperties.isCheckVendorAlipayAccount()){
			errorInfo = checkPromotedType(baseUserDO.getPromotedType(),"服务商淘宝账号绑定的支付宝账号未做企业实名认证;请联系申请人,在支付宝平台完成企业实名认证操作!");
			if(errorInfo != null){
				return errorInfo;
			}
		}
		return null;
	}
	
	private ErrorInfo checkAddCuntaoVendorDto(CuntaoServiceVendorDto cuntaoVendorDto){
		try {
			BeanValidator.validateWithThrowable(cuntaoVendorDto);
		} catch (AugeBusinessException e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andMobileEqualTo(cuntaoVendorDto.getMobile());
		List<CuntaoServiceVendor> vendors = cuntaoServiceVendorMapper.selectByExample(example);
		if(vendors != null && !vendors.isEmpty()){
			return  ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, "服务商手机号已存在!");
		}
		example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCompanyNameEqualTo(cuntaoVendorDto.getCompanyName());
		vendors = cuntaoServiceVendorMapper.selectByExample(example);
		if(vendors != null && !vendors.isEmpty()){
			return  ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, "服务商公司名称已存在!");
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
	
	
	@Override
	public Result<Boolean> removeVendor(Long companyId) {
		return null;
	}

	@Override
	public Result<Boolean> updateVendor(CuntaoServiceVendorDto cuntaoVendorDto) {
		Result<Boolean> result = null;
		ErrorInfo errorInfo = checkUpdateCuntaoVendorDto(cuntaoVendorDto);
		if (errorInfo != null) {
			return Result.of(errorInfo);
		}
		CuntaoServiceVendor vendor = cuntaoServiceVendorMapper.selectByPrimaryKey(cuntaoVendorDto.getId());
		errorInfo = ErrorInfo.of(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, null, "指定ID服务商不存在");
		if (vendor == null) {
			if (errorInfo != null) {
				return Result.of(errorInfo);
			}
		}
		try {
			result = Result.of(vendorWriteBO.updateVendor(cuntaoVendorDto));
			return result;
		} catch (Exception e) {
			logger.error("update vendor error!", e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}

	}

	private ErrorInfo checkUpdateCuntaoVendorDto(CuntaoServiceVendorDto cuntaoServiceVendorDto){
		try {
			Assert.notNull(cuntaoServiceVendorDto.getId(),"服务商ID不能为空");
			Assert.notNull(cuntaoServiceVendorDto.getOperator(),"操作人员不能为空");
		} catch (Exception e) {
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, e.getMessage());
		}
		return null;
	}
	
}
