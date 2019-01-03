package com.taobao.cun.auge.company.bo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
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
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.UserAddDto;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;
import com.taobao.cun.endor.base.dto.UserRoleDto;
import com.taobao.cun.endor.base.dto.UserUpdateDto;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;

	private static final int ALIPAY_ENTER_PROMOTED_TYPE = 4;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addVendorEmployee(Long vendorId, CuntaoEmployeeDto employeeDto, CuntaoEmployeeIdentifier identifier) {
		//CuntaoEmployeeExample example = new CuntaoEmployeeExample();
		//example.createCriteria().andTaobaoNickEqualTo(employeeDto.getTaobaoNick()).andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIsDeletedEqualTo("n");
		Long employeeId = null;
		CuntaoEmployee employee=null;
		if(employeeDto.getId() == null){
			employee = new CuntaoEmployee();
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
			employeeId = employee.getId();
		}else{
			employeeId = employeeDto.getId();
			employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		}
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andOwnerIdEqualTo(vendorId)
		.andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIdentifierEqualTo(identifier.name())
		.andEmployeeIdEqualTo(employeeId);
		List<CuntaoEmployeeRel> rels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(!CollectionUtils.isNotEmpty(rels)){
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
		}
		return employeeId;
	}

	public Long addVendorEmployee(Long vendorId, CuntaoEmployee employee, CuntaoEmployeeIdentifier identifier) {
		Long employeeId = employee.getId();
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andOwnerIdEqualTo(vendorId)
		.andTypeEqualTo(CuntaoEmployeeType.vendor.name()).andIdentifierEqualTo(identifier.name())
		.andEmployeeIdEqualTo(employeeId);
		List<CuntaoEmployeeRel> rels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(!CollectionUtils.isNotEmpty(rels)){
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
		}
		
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
	public Boolean removeVendorEmployee(Long employeeId,String operator) {
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		
		removeEmployee(employeeId,CuntaoEmployeeType.vendor,operator);
		//删除关系
		List<UserRoleDto> userRoles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(employee.getTaobaoUserId()+"");
		if(userRoles != null){
			userRoles = userRoles.stream().filter(role -> (CuntaoEmployeeIdentifier.VENDOR_MANAGER.name().equals(role.getRoleName())
					||CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR.name().equals(role.getRoleName()))).collect(Collectors.toList());
			for(UserRoleDto useRole : userRoles){
				storeEndorApiClient.getUserRoleServiceClient().deleteUserRole(useRole.getId(), null);
			}
		}
		//删除用户角色
		return true;
	}

	private void removeEmployee(Long employeeId,CuntaoEmployeeType type,String operator) {
		CuntaoEmployee record = new CuntaoEmployee();
		record.setId(employeeId);
		record.setGmtModified(new Date());
		record.setModifier(operator);
		record.setIsDeleted("y");
		cuntaoEmployeeMapper.updateByPrimaryKeySelective(record);
		//删除员工
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andEmployeeIdEqualTo(employeeId).andIsDeletedEqualTo("n").andTypeEqualTo(type.name());
		CuntaoEmployeeRel relRecord = new CuntaoEmployeeRel();
		relRecord.setIsDeleted("y");
		relRecord.setModifier(operator);
		relRecord.setGmtModified(new Date());
		cuntaoEmployeeRelMapper.updateByExampleSelective(relRecord, example);
		//删除关系
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
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andOwnerIdEqualTo(stationId)
		.andTypeEqualTo(CuntaoEmployeeType.store.name()).andIdentifierEqualTo(identifier.name())
		.andEmployeeIdEqualTo(employeeId);
		List<CuntaoEmployeeRel> rels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(!CollectionUtils.isNotEmpty(rels)){
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
		}
		return employeeId;
	}

	@Override
	public void createStoreEndorUser(Long stationId, CuntaoEmployee employee) {
		checkTaobaoNick(employee.getTaobaoNick());
		createStoreEndorUser(stationId, employee, CuntaoEmployeeIdentifier.STORE_MANAGER);
		createStoreEndorUser(stationId, employee, CuntaoEmployeeIdentifier.STORE_PICKER);
		createStoreEndorUser(stationId, employee, CuntaoEmployeeIdentifier.SONGZHUANG_BULU);
	}

	private void checkTaobaoNick(String taobaoNick) {
		ResultDO<BaseUserDO> resultDO = uicReadServiceClient.getBaseUserByNick(taobaoNick);
		if (resultDO == null || !resultDO.isSuccess() || resultDO.getModule() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "员工淘宝账号不存在或状态异常!");
		}
		if (((resultDO.getModule().getPromotedType() & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE)&&((resultDO.getModule().getPromotedType() & ALIPAY_ENTER_PROMOTED_TYPE) != ALIPAY_ENTER_PROMOTED_TYPE)) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "员工淘宝账号绑定支付宝未做个人实名认证!");
		}
	}

	private void createStoreEndorUser(Long stationId,CuntaoEmployee employee,CuntaoEmployeeIdentifier identifier){
		CuntaoStoreExample example = new CuntaoStoreExample();
		example.createCriteria().andIsDeletedEqualTo("n").andStationIdEqualTo(stationId);
		List<CuntaoStore> stores = cuntaoStoreMapper.selectByExample(example);
		if(stores == null || stores.isEmpty()){
			logger.error("store not exists stationId["+stationId+"]");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "门店不存在");
		}
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator(employee.getCreator());
		userAddDto.setUserId(employee.getTaobaoUserId()+"");
		userAddDto.setUserName(employee.getName());
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator(employee.getCreator());
		userRoleAddDto.setOrgId(3L);
		userRoleAddDto.setRoleName(identifier.name());
		userRoleAddDto.setUserId(employee.getTaobaoUserId()+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}

	@Override
	public void addEndorUserRole(Long taobaoUserId,String roleName,Long orgId){
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator(taobaoUserId+"");
		userRoleAddDto.setOrgId(orgId);
		userRoleAddDto.setRoleName(roleName);
		userRoleAddDto.setUserId(taobaoUserId+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
	}
	
	@Override
	public Long addVendorEmployeeByEmployeeId(Long vendorId, Long employeeId, CuntaoEmployeeIdentifier identifier) {
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		return addVendorEmployee(vendorId,employee,identifier);
	}

	@Override
	public Boolean removeStoreEmployee(Long employeeId,String operator) {
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		
		removeEmployee(employeeId,CuntaoEmployeeType.store,operator);
		
		List<UserRoleDto> userRoles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(employee.getTaobaoUserId()+"");
		if(userRoles != null){
			for(UserRoleDto useRole : userRoles){
				storeEndorApiClient.getUserRoleServiceClient().deleteUserRole(useRole.getId(), null);
			}
		}
		return true;
	}

	@Override
	public Boolean removeStoreEmployeeRole(Long employeeId, String operator,
			CuntaoEmployeeIdentifier cuntaoEmployeeIdentifier) {
		CuntaoEmployee employee = cuntaoEmployeeMapper.selectByPrimaryKey(employeeId);
		List<UserRoleDto> userRoles = storeEndorApiClient.getUserRoleServiceClient().getUserRoles(employee.getTaobaoUserId()+"");
		if(userRoles != null){
			for(UserRoleDto useRole : userRoles){
				if(cuntaoEmployeeIdentifier.name().equals(useRole.getRoleName())){
					storeEndorApiClient.getUserRoleServiceClient().deleteUserRole(useRole.getId(), null);
				}
			}
		}
		return true;
	}
}
