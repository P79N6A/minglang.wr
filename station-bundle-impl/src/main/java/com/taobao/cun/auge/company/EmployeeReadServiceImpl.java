package com.taobao.cun.auge.company;

import java.util.List;
import java.util.stream.Collectors;

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
import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.EmployeeQueryPageCondition;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoCompanyEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoCompanyMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import jersey.repackaged.com.google.common.collect.Lists;

@Service("employeeReadServiceImpl")
@HSFProvider(serviceInterface = EmployeeReadServiceImpl.class)
public class EmployeeReadServiceImpl implements EmployeeReadService{

	@Autowired
	private CuntaoCompanyMapper cuntaoCompanyMapper;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoCompanyEmployeeMapper cuntaoCompanyEmployeeMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeReadServiceImpl.class);

	@Override
	public Result<PageDto<CuntaoEmployeeDto>> queryEmployeeByPage(
			EmployeeQueryPageCondition employeeQueryPageCondition) {
		try {
			CuntaoCompanyEmployeeExample cuntaoCompanyEmployeeExample = new CuntaoCompanyEmployeeExample();
			cuntaoCompanyEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
					.andCompanyIdEqualTo(employeeQueryPageCondition.getCompanyId());
			List<CuntaoCompanyEmployee> cuntaoCompanyEmployees = cuntaoCompanyEmployeeMapper
					.selectByExample(cuntaoCompanyEmployeeExample);
			List<Long> employeeIds = cuntaoCompanyEmployees.stream()
					.map(cuntaoCompanyEmployee -> cuntaoCompanyEmployee.getEmployeeId()).collect(Collectors.toList());
			if (employeeIds != null && !employeeIds.isEmpty()) {
				PageHelper.startPage(employeeQueryPageCondition.getPageNum(), employeeQueryPageCondition.getPageSize());
				CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
				Criteria criteria = cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
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
	public Result<CuntaoEmployeeDto> queryEmployeeByID(Long id) {
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
	public Result<List<CuntaoEmployeeDto>> queryEmployeeByIDS(List<Long> ids) {
		try {
			CuntaoEmployeeExample cuntaoEmployeeExample = new CuntaoEmployeeExample();
			cuntaoEmployeeExample.createCriteria().andIsDeletedEqualTo("n")
					.andIdIn(ids);
			List<CuntaoEmployee> employees = cuntaoEmployeeMapper.selectByExample(cuntaoEmployeeExample);
			if(employees == null){
				ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, null, "员工不存在");
				return Result.of(errorInfo);
			}
			return Result.of(EmployeeConverter.convert2CuntaoEmployeeDtoList(employees));
		} catch (Exception e) {
			logger.error("queryEmployeeByIDS error!", e);
			ErrorInfo errorInfo = ErrorInfo.of(AugeErrorCodes.SYSTEM_ERROR_CODE, null, "系统异常");
			return Result.of(errorInfo);
		}
	}

}
