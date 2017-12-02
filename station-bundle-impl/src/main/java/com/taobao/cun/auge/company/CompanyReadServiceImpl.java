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
import com.taobao.cun.auge.company.dto.CompanyQueryPageCondition;
import com.taobao.cun.auge.company.dto.CuntaoCompanyDto;
import com.taobao.cun.auge.dal.domain.CuntaoCompany;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyExample;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("companyReadServiceImpl")
@HSFProvider(serviceInterface = CompanyReadService.class)
public class CompanyReadServiceImpl implements CompanyReadService{

	@Autowired
	private CuntaoCompanyMapper cuntaoCompanyMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyReadServiceImpl.class);
	@Override
	public Result<PageDto<CuntaoCompanyDto>> queryCompanyByPage(CompanyQueryPageCondition companyQueryPageCondition) {
		PageHelper.startPage(companyQueryPageCondition.getPageNum(), companyQueryPageCondition.getPageSize());
		CuntaoCompanyExample example = new CuntaoCompanyExample();
		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
		if(StringUtils.isNotEmpty(companyQueryPageCondition.getCompanyName())){
			criteria.andCompanyNameLike("%"+companyQueryPageCondition.getCompanyName()+"%");
		}
		if(StringUtils.isNotEmpty(companyQueryPageCondition.getTaobaoNick())){
			criteria.andTaobaoNickEqualTo(companyQueryPageCondition.getTaobaoNick());
		}
		if(StringUtils.isNotEmpty(companyQueryPageCondition.getMobile())){
			criteria.andMobileEqualTo(companyQueryPageCondition.getMobile());
		}
		if(StringUtils.isNotEmpty(companyQueryPageCondition.getState())){
			criteria.andStateEqualTo(companyQueryPageCondition.getState());
		}
		try {
			Page<CuntaoCompany> cuntaoCompanys =  (Page<CuntaoCompany>)cuntaoCompanyMapper.selectByExample(example);
			PageDto<CuntaoCompanyDto> success =  PageDtoUtil.success(cuntaoCompanys, CompanyConverter.convert2CuntaoCompanyDtoList(cuntaoCompanys));
			return Result.of(success);
		} catch (Exception e) {
			logger.error("queryCompanyByPage error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<CuntaoCompanyDto> queryCompanyByID(Long id) {
		try {
			CuntaoCompany company = cuntaoCompanyMapper.selectByPrimaryKey(id);
			if(company != null){
				CuntaoCompanyDto companyDto = CompanyConverter.convert2CuntaoCompanyDto(company);
				return Result.of(companyDto);
			}else{
				return Result.of(ErrorInfo.of(AugeErrorCodes.COMPANY_DATA_NOT_EXISTS_ERROR_CODE, null, "公司不存在"));
			}
		} catch (Exception e) {
			logger.error("queryCompanyByID error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

	@Override
	public Result<List<CuntaoCompanyDto>> queryCompanyByIDS(List<Long> ids) {
		try {
			CuntaoCompanyExample example = new CuntaoCompanyExample();
			Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
			criteria.andIdIn(ids);
			List<CuntaoCompany> companys = cuntaoCompanyMapper.selectByExample(example);
			return Result.of(CompanyConverter.convert2CuntaoCompanyDtoList(companys));
		} catch (Exception e) {
			logger.error("queryCompanyByIDS error!",e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

}
