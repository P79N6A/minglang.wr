package com.taobao.cun.auge.company.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeExample;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoStore;
import com.taobao.cun.auge.dal.domain.CuntaoStoreExample;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.UserAddDto;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;
import com.taobao.cun.endor.base.dto.UserUpdateDto;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Component
public class EmployeeWriteBOImpl implements EmployeeWriteBO{

	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	@Autowired
	private CuntaoStoreMapper cuntaoStoreMapper;
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeWriteBOImpl.class);
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addVendorEmployee(Long vendorId, CuntaoEmployeeDto employeeDto, CuntaoEmployeeIdentifier identifier) {
		CuntaoEmployeeExample example = new CuntaoEmployeeExample();
		example.createCriteria().andTaobaoNickEqualTo(employeeDto.getTaobaoNick()).andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIsDeletedEqualTo("n");
		CuntaoEmployee	employee = new CuntaoEmployee();
		employee.setCreator(employeeDto.getOperator());
		employee.setGmtCreate(new Date());
		employee.setModifier(employeeDto.getOperator());
		employee.setGmtModified(new Date());
		employee.setMobile(employeeDto.getMobile());
		employee.setIsDeleted("n");
		employee.setName(employeeDto.getName());
		employee.setTaobaoNick(employeeDto.getTaobaoNick());
		employee.setTaobaoUserId(employeeDto.getTaobaoUserId());
		employee.setType(CuntaoEmployeeType.vendor.name());
		cuntaoEmployeeMapper.insertSelective(employee);
		
		Long employeeId = employee.getId();
		CuntaoEmployeeRel cuntaoVendorEmployee = new CuntaoEmployeeRel();
		cuntaoVendorEmployee.setCreator(employeeDto.getOperator());
		cuntaoVendorEmployee.setGmtCreate(new Date());
		cuntaoVendorEmployee.setModifier(employeeDto.getOperator());
		cuntaoVendorEmployee.setGmtModified(new Date());
		cuntaoVendorEmployee.setIsDeleted("n");
		cuntaoVendorEmployee.setOwnerId(vendorId);
		cuntaoVendorEmployee.setEmployeeId(employeeId);
		cuntaoVendorEmployee.setState(CuntaoVendorEmployeeState.SERVICING.name());
		cuntaoVendorEmployee.setType(CuntaoEmployeeType.vendor.name());
		cuntaoVendorEmployee.setIdentifier(identifier.name());
		cuntaoEmployeeRelMapper.insertSelective(cuntaoVendorEmployee);
		
		createVendorEndorUser(vendorId,employee,identifier);
		return employeeId;
	}

	public Long addVendorEmployee(Long vendorId, CuntaoEmployee employee, CuntaoEmployeeIdentifier identifier) {
		Long employeeId = employee.getId();
		CuntaoEmployeeRel cuntaoVendorEmployee = new CuntaoEmployeeRel();
		cuntaoVendorEmployee.setCreator(employee.getCreator());
		cuntaoVendorEmployee.setGmtCreate(new Date());
		cuntaoVendorEmployee.setModifier(employee.getModifier());
		cuntaoVendorEmployee.setGmtModified(new Date());
		cuntaoVendorEmployee.setIsDeleted("n");
		cuntaoVendorEmployee.setOwnerId(vendorId);
		cuntaoVendorEmployee.setEmployeeId(employeeId);
		cuntaoVendorEmployee.setState(CuntaoVendorEmployeeState.SERVICING.name());
		cuntaoVendorEmployee.setType(CuntaoEmployeeType.vendor.name());
		cuntaoVendorEmployee.setIdentifier(identifier.name());
		cuntaoEmployeeRelMapper.insertSelective(cuntaoVendorEmployee);
		
		createVendorEndorUser(vendorId,employee,identifier);
		return employeeId;
	}

	
	
	private void createVendorEndorUser(Long vendorId,CuntaoEmployee employee,CuntaoEmployeeIdentifier identifier){
		CuntaoServiceVendor cuntaoServiceVendor = cuntaoServiceVendorMapper.selectByPrimaryKey(vendorId);
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator(employee.getCreator());
		userAddDto.setUserId(employee.getTaobaoUserId()+"");
		userAddDto.setUserName(employee.getName());
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator(employee.getCreator());
		userRoleAddDto.setOrgId(cuntaoServiceVendor.getEndorOrgId());
		userRoleAddDto.setRoleName(identifier.name());
		userRoleAddDto.setUserId(employee.getTaobaoUserId()+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean updateVendorEmployee(CuntaoEmployeeDto updateCuntaoEmployeeDto) {
		ResultDO<BaseUserDO> employeeUserDOresult = uicReadServiceClient.getBaseUserByNick(updateCuntaoEmployeeDto.getTaobaoNick());
		CuntaoEmployee employee = new CuntaoEmployee();
		employee.setId(updateCuntaoEmployeeDto.getId());
		employee.setGmtModified(new Date());
		employee.setModifier(updateCuntaoEmployeeDto.getOperator());
		if (StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getMobile())) {
			employee.setMobile(updateCuntaoEmployeeDto.getMobile());
		}
		if (StringUtils.isNotEmpty(updateCuntaoEmployeeDto.getName())) {
			employee.setName(updateCuntaoEmployeeDto.getName());
		}
		cuntaoEmployeeMapper.updateByPrimaryKeySelective(employee);

		updateEndorUser(updateCuntaoEmployeeDto, employeeUserDOresult);
		return null;
	}

	private void updateEndorUser(CuntaoEmployeeDto updateCuntaoEmployeeDto, ResultDO<BaseUserDO> employeeUserDOresult) {
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setUserId(employeeUserDOresult.getModule().getUserId()+"");
		userUpdateDto.setUserName(updateCuntaoEmployeeDto.getName());
		userUpdateDto.setModifier(updateCuntaoEmployeeDto.getOperator());
		storeEndorApiClient.getUserServiceClient().updateUser(userUpdateDto, null);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean removeVendorEmployee(Long employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addStoreEmployee(Long stationId, CuntaoEmployeeDto storeEmployee, CuntaoEmployeeIdentifier identifier) {
		Long employeeId = null;
		CuntaoEmployee employee=null;
		if(storeEmployee.getId() == null){
			employee = new CuntaoEmployee();
			employee.setCreator(storeEmployee.getOperator());
			employee.setGmtCreate(new Date());
			employee.setModifier(storeEmployee.getOperator());
			employee.setGmtModified(new Date());
			employee.setMobile(storeEmployee.getMobile());
			employee.setIsDeleted("n");
			employee.setName(storeEmployee.getName());
			employee.setTaobaoNick(storeEmployee.getTaobaoNick());
			employee.setTaobaoUserId(storeEmployee.getTaobaoUserId());
			employee.setType(CuntaoEmployeeType.store.name());
			cuntaoEmployeeMapper.insertSelective(employee);
			employeeId = employee.getId();
		}else{
			employeeId = storeEmployee.getId();
			employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		}
		CuntaoEmployeeRel cuntaoVendorEmployee = new CuntaoEmployeeRel();
		cuntaoVendorEmployee.setCreator(storeEmployee.getOperator());
		cuntaoVendorEmployee.setGmtCreate(new Date());
		cuntaoVendorEmployee.setModifier(storeEmployee.getOperator());
		cuntaoVendorEmployee.setGmtModified(new Date());
		cuntaoVendorEmployee.setIsDeleted("n");
		cuntaoVendorEmployee.setOwnerId(stationId);
		cuntaoVendorEmployee.setEmployeeId(employeeId);
		cuntaoVendorEmployee.setState(CuntaoVendorEmployeeState.SERVICING.name());
		cuntaoVendorEmployee.setType(CuntaoEmployeeType.store.name());
		cuntaoVendorEmployee.setIdentifier(identifier.name());
		cuntaoEmployeeRelMapper.insertSelective(cuntaoVendorEmployee);
		
		createStoreEndorUser(stationId,employee,identifier);
		return employeeId;
	}

	private void createStoreEndorUser(Long stationId,CuntaoEmployee employee,CuntaoEmployeeIdentifier identifier){
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
		List<CuntaoStore> stores = cuntaoStoreMapper.selectByExample(example);
		if(stores == null || stores.isEmpty()){
			logger.error("store not exists stationId["+stationId+"]");
			return;
		}
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator(employee.getCreator());
		userAddDto.setUserId(employee.getTaobaoUserId()+"");
		userAddDto.setUserName(employee.getName());
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator(employee.getCreator());
		userRoleAddDto.setOrgId(stores.iterator().next().getEndorOrgId());
		userRoleAddDto.setRoleName(identifier.name());
		userRoleAddDto.setUserId(employee.getTaobaoUserId()+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}

	@Override
	public Long addVendorEmployeeByEmployeeId(Long vendorId, Long employeeId, CuntaoEmployeeIdentifier identifier) {
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		return addVendorEmployee(vendorId,employee,identifier);
	}
}
