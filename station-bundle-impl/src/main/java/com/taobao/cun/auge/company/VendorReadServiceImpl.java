package com.taobao.cun.auge.company;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.VendorQueryPageCondition;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("vendorReadService")
@HSFProvider(serviceInterface = VendorReadService.class)
public class VendorReadServiceImpl implements VendorReadService{

	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(VendorReadServiceImpl.class);
	@Override
	public Result<PageDto<CuntaoServiceVendorDto>> queryVendorByPage(VendorQueryPageCondition vendorQueryPageCondition) {
		PageHelper.startPage(vendorQueryPageCondition.getPageNum(), vendorQueryPageCondition.getPageSize());
		CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
		if(StringUtils.isNotEmpty(vendorQueryPageCondition.getCompanyName())){
			criteria.andCompanyNameLike("%"+vendorQueryPageCondition.getCompanyName()+"%");
		}
		if(StringUtils.isNotEmpty(vendorQueryPageCondition.getTaobaoNick())){
			criteria.andTaobaoNickEqualTo(vendorQueryPageCondition.getTaobaoNick());
		}
		if(StringUtils.isNotEmpty(vendorQueryPageCondition.getMobile())){
			criteria.andMobileEqualTo(vendorQueryPageCondition.getMobile());
		}
		if(StringUtils.isNotEmpty(vendorQueryPageCondition.getState())){
			criteria.andStateEqualTo(vendorQueryPageCondition.getState());
		}
		try {
			Page<CuntaoServiceVendor> cuntaoVendors =  (Page<CuntaoServiceVendor>)cuntaoServiceVendorMapper.selectByExample(example);
			PageDto<CuntaoServiceVendorDto> success =  PageDtoUtil.success(cuntaoVendors, VendorConverter.convert2CuntaoVendorDtoList(cuntaoVendors));
			return Result.of(success);
		} catch (Exception e) {
			logger.error("queryVendorByPage error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<CuntaoServiceVendorDto> queryVendorByID(Long id) {
		try {
			CuntaoServiceVendor vendor = cuntaoServiceVendorMapper.selectByPrimaryKey(id);
			if(vendor != null){
				CuntaoServiceVendorDto vendorDto = VendorConverter.convert2CuntaoVendorDto(vendor);
				return Result.of(vendorDto);
			}else{
				return Result.of(ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "服务商不存在"));
			}
		} catch (Exception e) {
			logger.error("queryVendorByID error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<List<CuntaoServiceVendorDto>> queryVendorByIDS(List<Long> ids) {
		try {
			CuntaoServiceVendorExample example = new CuntaoServiceVendorExample();
			Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
			criteria.andIdIn(ids);
			List<CuntaoServiceVendor> vendors = cuntaoServiceVendorMapper.selectByExample(example);
			return Result.of(VendorConverter.convert2CuntaoVendorDtoList(vendors));
		} catch (Exception e) {
			logger.error("queryVendorByIDS error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

}
