package com.taobao.cun.auge.company.bo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.taobao.cun.auge.company.ServiceVendorAndManagerInfo;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeType;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.company.dto.CuntaoVendorState;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRel;
import com.taobao.cun.auge.dal.domain.CuntaoEmployeeRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendorExample;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoEmployeeRelMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoServiceVendorMapper;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.OrgAddDto;
import com.taobao.cun.endor.base.dto.UserAddDto;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;
import com.taobao.tddl.client.sequence.impl.GroupSequence;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Component
public class VendorWriteBOImpl implements VendorWriteBO{

	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;
	
	@Autowired
	private UicReadServiceClient uicReadServiceClient;
	
	@Autowired
	private CuntaoServiceVendorMapper cuntaoServiceVendorMapper;
	
	@Autowired
	private CuntaoEmployeeMapper cuntaoEmployeeMapper;
	
	@Autowired
	private CuntaoEmployeeRelMapper cuntaoEmployeeRelMapper;
	
	@Autowired
	@Qualifier("storeEndorApiClient")
	private EndorApiClient storeEndorApiClient;
	
	@Autowired
	@Qualifier("storeEndorOrgIdSequence")
	private GroupSequence groupSequence;
	
	@Autowired
	private EmployeeWriteBO employeeWriteBO;
	
	@Override
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addVendor(CuntaoServiceVendorDto cuntaoServiceVendorDto) {
			ResultDO<BaseUserDO> vendorUserDOresult = uicReadServiceClient.getBaseUserByNick(cuntaoServiceVendorDto.getTaobaoNick());
			ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(vendorUserDOresult.getModule().getUserId());
			ServiceVendorAndManagerInfo serviceVendorAndManagerInfo = addVendorAndManager(cuntaoServiceVendorDto, vendorUserDOresult.getModule(), basePaymentAccountDOResult.getModule());
			createEndorOrgAndUser(serviceVendorAndManagerInfo);
			return cuntaoServiceVendorDto.getId();
	}

	private void createEndorOrgAndUser(ServiceVendorAndManagerInfo serviceVendorAndManagerInfo) {
		OrgAddDto orgAddDto = new OrgAddDto();
		orgAddDto.setCreator(serviceVendorAndManagerInfo.getCuntaoServiceVendor().getCreator());
		orgAddDto.setOrgId(serviceVendorAndManagerInfo.getCuntaoServiceVendor().getEndorOrgId());
		orgAddDto.setOrgName(serviceVendorAndManagerInfo.getCuntaoServiceVendor().getCompanyName());
		orgAddDto.setParentId(5l);
		storeEndorApiClient.getOrgServiceClient().insert(orgAddDto, null);
		
		UserAddDto userAddDto = new UserAddDto();
		userAddDto.setCreator(serviceVendorAndManagerInfo.getManager().getCreator());
		userAddDto.setUserId(serviceVendorAndManagerInfo.getManager().getTaobaoUserId()+"");
		userAddDto.setUserName(serviceVendorAndManagerInfo.getManager().getName());
		storeEndorApiClient.getUserServiceClient().addUser(userAddDto);
		
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setCreator(serviceVendorAndManagerInfo.getManager().getCreator());
		userRoleAddDto.setOrgId(serviceVendorAndManagerInfo.getCuntaoServiceVendor().getEndorOrgId());
		userRoleAddDto.setRoleName(CuntaoEmployeeIdentifier.VENDOR_MANAGER.name());
		userRoleAddDto.setUserId(serviceVendorAndManagerInfo.getManager().getTaobaoUserId()+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
		
		UserRoleAddDto userRoleAddDto2 = new UserRoleAddDto();
		userRoleAddDto2.setCreator(serviceVendorAndManagerInfo.getManager().getCreator());
		userRoleAddDto2.setOrgId(serviceVendorAndManagerInfo.getCuntaoServiceVendor().getEndorOrgId());
		userRoleAddDto2.setRoleName(CuntaoEmployeeIdentifier.VENDOR_DISTRIBUTOR.name());
		userRoleAddDto2.setUserId(serviceVendorAndManagerInfo.getManager().getTaobaoUserId()+"");
		storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto2, null);
	}
	
	private ServiceVendorAndManagerInfo addVendorAndManager(CuntaoServiceVendorDto cuntaoServiceVendorDto, BaseUserDO baseUserDO,
			BasePaymentAccountDO basePaymentAccountDO) {
		ServiceVendorAndManagerInfo cuntaoCompanyAndManagerInfo = new ServiceVendorAndManagerInfo();
		CuntaoServiceVendor cuntaoServiceVendor = convert2CuntaoVendor(cuntaoServiceVendorDto, baseUserDO,basePaymentAccountDO);
		cuntaoServiceVendorMapper.insertSelective(cuntaoServiceVendor);
		cuntaoServiceVendorDto.setId(cuntaoServiceVendor.getId());
		CuntaoEmployee manager = convert2CuntaoCompanyEmployee(cuntaoServiceVendorDto,baseUserDO);
		cuntaoEmployeeMapper.insertSelective(manager);
		CuntaoEmployeeRel cuntaoCompanyEmployee = new CuntaoEmployeeRel();
		cuntaoCompanyEmployee.setCreator(cuntaoServiceVendorDto.getOperator());
		cuntaoCompanyEmployee.setGmtCreate(new Date());
		cuntaoCompanyEmployee.setModifier(cuntaoServiceVendorDto.getOperator());
		cuntaoCompanyEmployee.setGmtModified(new Date());
		cuntaoCompanyEmployee.setIsDeleted("n");
		cuntaoCompanyEmployee.setOwnerId(cuntaoServiceVendor.getId());
		cuntaoCompanyEmployee.setEmployeeId(manager.getId());
		cuntaoCompanyEmployee.setState(CuntaoVendorEmployeeState.SERVICING.name());
		cuntaoCompanyEmployee.setType(CuntaoEmployeeType.vendor.name());
		cuntaoCompanyEmployee.setIdentifier(CuntaoEmployeeIdentifier.VENDOR_MANAGER.name());
		cuntaoEmployeeRelMapper.insertSelective(cuntaoCompanyEmployee);
		cuntaoCompanyAndManagerInfo.setCuntaoServiceVendor(cuntaoServiceVendor);
		cuntaoCompanyAndManagerInfo.setManager(manager);
		cuntaoCompanyAndManagerInfo.setId(cuntaoCompanyEmployee.getId());
		cuntaoCompanyAndManagerInfo.setState(CuntaoVendorEmployeeState.valueOf(cuntaoCompanyEmployee.getState()));
		return cuntaoCompanyAndManagerInfo;
	}
	
	private CuntaoEmployee convert2CuntaoCompanyEmployee(CuntaoServiceVendorDto cuntaoCompanyDto,BaseUserDO baseUserDO){
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
		cuntaoEmployee.setType(CuntaoEmployeeType.vendor.name());
		return cuntaoEmployee;
	}
	
	private CuntaoServiceVendor convert2CuntaoVendor(CuntaoServiceVendorDto cuntaoVendorDto, BaseUserDO baseUserDO,BasePaymentAccountDO basePaymentAccountDO) {
		CuntaoServiceVendor cuntaoServiceVendor = new CuntaoServiceVendor();
		cuntaoServiceVendor.setCreator(baseUserDO.getUserId()+"");
		cuntaoServiceVendor.setGmtCreate(new Date());
		cuntaoServiceVendor.setModifier(baseUserDO.getUserId()+"");
		cuntaoServiceVendor.setGmtModified(new Date());
		cuntaoServiceVendor.setIsDeleted("n");
		cuntaoServiceVendor.setCompanyName(cuntaoVendorDto.getCompanyName());
		cuntaoServiceVendor.setMobile(cuntaoVendorDto.getMobile());
		cuntaoServiceVendor.setAlipayAccountNo(basePaymentAccountDO.getAccountNo());
		cuntaoServiceVendor.setAlipayOutUser(basePaymentAccountDO.getOutUser());
		cuntaoServiceVendor.setTaobaoNick(baseUserDO.getNick());
		cuntaoServiceVendor.setTaobaoUserId(baseUserDO.getUserId());
		cuntaoServiceVendor.setType(cuntaoVendorDto.getType().name());
		cuntaoServiceVendor.setState(CuntaoVendorState.SERVICING.name());
		cuntaoServiceVendor.setRemark(cuntaoVendorDto.getRemark());
		cuntaoServiceVendor.setEndorOrgId(groupSequence.nextValue());
		return cuntaoServiceVendor;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean updateVendor(CuntaoServiceVendorDto cuntaoVendorDto) {
		CuntaoServiceVendor vendor = cuntaoServiceVendorMapper.selectByPrimaryKey(cuntaoVendorDto.getId());
		if (StringUtils.isNotEmpty(cuntaoVendorDto.getMobile())) {
			vendor.setMobile(cuntaoVendorDto.getMobile());
		}
		vendor.setRemark(cuntaoVendorDto.getRemark());
		vendor.setGmtModified(new Date());
		vendor.setModifier(cuntaoVendorDto.getOperator());
		cuntaoServiceVendorMapper.updateByPrimaryKey(vendor);
		return Boolean.TRUE;
	}

	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean removeVendor(Long companyId,String operator) {
		//TODO 
		CuntaoServiceVendorExample vendorExample = new CuntaoServiceVendorExample();
		vendorExample.createCriteria().andIdEqualTo(companyId).andIsDeletedEqualTo("n");
		List<CuntaoServiceVendor> vendors = cuntaoServiceVendorMapper.selectByExample(vendorExample);
		if(vendors == null || vendors.isEmpty()){
			throw new AugeBusinessException("NOT_FIND_VENDOR","服务商["+companyId+"]不存在");
		}
		
		
		CuntaoEmployeeRelExample example = new CuntaoEmployeeRelExample();
		example.createCriteria().andIsDeletedEqualTo("n").andOwnerIdEqualTo(companyId).andTypeEqualTo(CuntaoEmployeeType.vendor.name());
		List<CuntaoEmployeeRel>  rels = cuntaoEmployeeRelMapper.selectByExample(example);
		if(rels != null){
			for(CuntaoEmployeeRel rel : rels){
				if(rel != null){
					employeeWriteBO.removeVendorEmployee(rel.getEmployeeId(),operator);
				}
			}
		}
		
		
		CuntaoServiceVendor record = new CuntaoServiceVendor();
		record.setId(companyId);
		record.setGmtModified(new Date());
		record.setModifier(operator);
		record.setIsDeleted("y");
		cuntaoServiceVendorMapper.updateByPrimaryKeySelective(record);
		
		//TODO删除组织
		storeEndorApiClient.getOrgServiceClient().delete(vendors.iterator().next().getEndorOrgId());
		return true;
	}

}
