package com.taobao.cun.auge.company;

import java.util.List;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.company.bo.VendorWriteBO;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorType;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service("vendorWriteService")
@HSFProvider(serviceInterface = VendorWriteService.class)
public class VendorWriteServiceImpl implements VendorWriteService {

	private static final int ALIPAY_ENTERPRICE_PROMOTED_TYPE = 4;

	private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;


	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	@Autowired
	private UicReadServiceClient uicReadServiceClient;

	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;

	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;

	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;

	@Autowired
	private VendorReadService vendorReadService;

	private static final Logger logger = LoggerFactory.getLogger(VendorWriteServiceImpl.class);

	@Autowired
	private VendorWriteBO vendorWriteBO;

    @Autowired
    private StoreReadBO storeReadBO;

	@Override
	public Result<Long> addNewVendor(CuntaoServiceVendorDto cuntaoServiceVendorDto) {
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
		errorInfo = checkTaobaoNickExists(cuntaoServiceVendorDto.getTaobaoNick(),"淘宝账号已存在!");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo = checkCompanyExists(cuntaoServiceVendorDto.getCompanyName(),"公司名称已存在!");
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
			result = Result.of(vendorWriteBO.addNewVendor(cuntaoServiceVendorDto));
			return result;
		} catch (Exception e) {
			logger.error("addNewVendor error!",e);
			errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

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
		errorInfo = checkTaobaoNickExists(cuntaoServiceVendorDto.getTaobaoNick(),"淘宝账号已存在!");
		if(errorInfo != null){
			return Result.of(errorInfo);
		}
		errorInfo = checkCompanyExists(cuntaoServiceVendorDto.getCompanyName(),"公司名称已存在!");
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
			result = Result.of(vendorWriteBO.addVendor(cuntaoServiceVendorDto));
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

	private ErrorInfo checkCompanyExists(String companyName, String errorMessage) {
		CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCompanyNameEqualTo(companyName);
		List<CuntaoServiceVendor> result = cuntaoServiceVendorMapper.selectByExample(example);
		if(result != null && !result.isEmpty()){
			return ErrorInfo.of(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, null, errorMessage);
		}
		return null;
	}


	private ErrorInfo checkTaobaoNickExists(String taobaoNick, String errorMessage) {
		CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
		example.createCriteria().andIsDeletedEqualTo("n").andTaobaoNickEqualTo(taobaoNick);
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
			errorInfo = checkEnterprisePromotedType(baseUserDO.getPromotedType(),"服务商淘宝账号绑定的支付宝账号未做企业实名认证;请联系申请人,在支付宝平台完成企业实名认证操作!");
			if(errorInfo != null){
				return errorInfo;
			}
		}
		else{
			errorInfo = checkPersonOrEnterprisePromotedType(baseUserDO.getPromotedType(),"账号未做实名认证");
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

	private ErrorInfo checkEnterprisePromotedType(int promotedType,String errorMessage){
		if (((promotedType & ALIPAY_ENTERPRICE_PROMOTED_TYPE) != ALIPAY_ENTERPRICE_PROMOTED_TYPE)) {
			return ErrorInfo.of(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE, null, errorMessage);
		}
		return null;
	}

	private ErrorInfo checkPersonOrEnterprisePromotedType(int promotedType,String errorMessage){
		if (((promotedType & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE)&&((promotedType & ALIPAY_ENTERPRICE_PROMOTED_TYPE) != ALIPAY_ENTERPRICE_PROMOTED_TYPE)) {
			return ErrorInfo.of(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE, null, errorMessage);
		}
		return null;
	}



	@Override
	public Result<Boolean> removeVendor(Long companyId,String operator) {
		Result<Boolean> result = null;
		try {
			Assert.notNull(companyId,"公司ID不能为空");
			Assert.notNull(operator,"操作人不能为空");
			result = Result.of(vendorWriteBO.removeVendor(companyId,operator));
			return result;
		} catch (Exception e) {
			logger.error("update vendor error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<Boolean> removeNewVendor(Long companyId, String operator) {
		Result<Boolean> result = null;
		try {
			Assert.notNull(companyId,"公司ID不能为空");
			Assert.notNull(operator,"操作人不能为空");
			result = Result.of(vendorWriteBO.removeNewVendor(companyId,operator));
			return result;
		} catch (Exception e) {
			logger.error("remove new vendor error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
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


	@Override
	public Result<Boolean> confirmVendorProtocol(Long taobaoUserId, ProtocolTypeEnum protocol) {
		try {
			Result<Boolean> isConfirmResult = isConfirmVendorProtocol(taobaoUserId,protocol);
			if(isConfirmResult.isSuccess() && isConfirmResult.getModule()){
				return isConfirmResult;
			}
			boolean comfirmResult = confirmProtocol(taobaoUserId,protocol);
			Result<Boolean> result = Result.of(true);
			result.setModule(comfirmResult);
			return result;
		} catch (Exception e) {
			logger.error("confirmVendorProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}


	@Override
	public Result<Boolean> isConfirmVendorProtocol(Long taobaoUserId, ProtocolTypeEnum protocol) {
		try {
			boolean isConfirmed = isConfirmProtocol(taobaoUserId,protocol);
			Result<Boolean> result = Result.of(true);
			result.setModule(isConfirmed);
			return result;
		} catch (Exception e) {
			logger.error("isConfirmVendorProtocol["+taobaoUserId+"]",e);
			return Result.of(ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "系统异常"));
		}
	}

	private Boolean isConfirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		Assert.notNull(taobaoUserId);
		Assert.notNull(protcolType);
		Result<CuntaoServiceVendorDto> result = vendorReadService.queryVendorByTaobaoUserID(taobaoUserId);
		if(result.getModule() == null){
			return false;
			//throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"服务商不存在");
		}
		PartnerProtocolRelDto partnerProtocolRelDto = partnerProtocolRelBO.getPartnerProtocolRelDto(protcolType, result.getModule().getId(), PartnerProtocolRelTargetTypeEnum.VENDOR);
		if(partnerProtocolRelDto != null){
			return true;
		}
		return false;
	}


	private Boolean confirmProtocol(Long taobaoUserId,ProtocolTypeEnum protcolType){
		try {
			Assert.notNull(taobaoUserId);
			Assert.notNull(protcolType);
			Result<CuntaoServiceVendorDto> result = vendorReadService.queryVendorByTaobaoUserID(taobaoUserId);

			if(result.getModule() == null){
                /*
                 * 中小件送货入户,家电送装,家电送货,家居送装服务场景下,存在服务商未成功创建bug,阻断签约
                 * 全链路不清楚还有哪些地方依赖,稳妥起见,暂在末端fix BY憨实
                 */
				if (protcolType.equals(ProtocolTypeEnum.DELIVERY_GOODS_AGREEMENT) ||
                        protcolType.equals(ProtocolTypeEnum.VENDOR_INSTALLMENT_AGREEMENT) ||
						protcolType.equals(ProtocolTypeEnum.VENDOR_DISTRIBUTE_AGREEMENT) ||
						protcolType.equals(ProtocolTypeEnum.CTS_JZ_SZYT_AGREEMENT)) {
                    result = createVendor4Service(taobaoUserId);
				}

				// 仍为空,follow原逻辑
                if(result.getModule() == null){
                    return false;
                    //throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"服务商不存在");
                }
			}
			partnerProtocolRelBO.signProtocol(taobaoUserId,protcolType, result.getModule().getId(), PartnerProtocolRelTargetTypeEnum.VENDOR);
			return true;
		} catch (Exception e) {
			logger.error("confirmProtocol["+taobaoUserId+"]",e);
			throw new AugeBusinessException(e);
		}
	}

    /**
     * 服务创建服务商,仍不能100%创建成功,需要迭代 BY憨实
     *
     * @param userId
     * @return
     */
	private Result<CuntaoServiceVendorDto> createVendor4Service(Long userId) {
        StoreDto storeDTO = storeReadBO.getStoreDtoByTaobaoUserId(userId);
        if(null == storeDTO) {
            return null;
        }
        CuntaoServiceVendorDto vendorDTO = new CuntaoServiceVendorDto();
        vendorDTO.setCompanyName(storeDTO.getName() + "[门店提供New]");
        vendorDTO.setMobile(storeDTO.getMobile());
        vendorDTO.setOperator(storeDTO.getMobile());
        vendorDTO.setTaobaoNick(storeDTO.getTaobaoNick());
        vendorDTO.setTaobaoUserId(userId);
        vendorDTO.setType(CuntaoVendorType.SERVICE_VENDOR);
        vendorDTO.setRemark("SERVICE_ADMIN");
        addNewVendor(vendorDTO);
	    return vendorReadService.queryVendorByTaobaoUserID(userId);
    }

}
