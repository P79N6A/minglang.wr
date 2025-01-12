package com.taobao.cun.auge.org.bo.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.domain.CuntaoOrgExample;
import com.taobao.cun.auge.dal.domain.CuntaoOrgExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
@Component("cuntaoOrgBO")
public class CuntaoOrgBOImpl implements CuntaoOrgBO {
	@Autowired
	CuntaoOrgMapper cuntaoOrgMapper;

	@Override
    public Long addOrg(CuntaoOrg org, String operator) {
		CuntaoOrgExample example = new CuntaoOrgExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andIdEqualTo(org.getParentId());
		List<CuntaoOrg> list = cuntaoOrgMapper.selectByExample(example);
		if (list.size() == 0) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"can not find org "
					+ org.getParentId());
		}
		CuntaoOrg parentOrg = list.get(0);
		CuntaoOrg sod = new CuntaoOrg();
		sod.setCreator(operator);
		sod.setModifier(operator);
		sod.setDeptPro(org.getDeptPro());
		sod.setGmtCreate(new Date());
		sod.setGmtModified(new Date());
		sod.setIsDeleted("n");
		sod.setFullNamePath(parentOrg.getFullNamePath() + "/" + org.getName());
		sod.setFullOrderPath(parentOrg.getFullOrderPath() + "/0");
		sod.setOrderLevel(0);
		sod.setOrgRangeType(org.getOrgRangeType());
		sod.setTempParentId(org.getTempParentId());
		sod.setIsLeaf("y");
		sod.setIsShow("y");
		sod.setName(org.getName());
		sod.setOrderPro(0);
		sod.setOrgType(org.getOrgType());
		sod.setParentId(org.getParentId());
		sod.setDingtalkDeptId(0L);
		cuntaoOrgMapper.insert(sod);
		CuntaoOrg tempCuntaoOrg = cuntaoOrgMapper.selectByPrimaryKey(org
				.getTempParentId());
		sod.setFullIdPath(parentOrg.getFullIdPath() + "/" + sod.getId());
		sod.setTempFullIdPath(tempCuntaoOrg.getTempFullIdPath() + "/"
				+ sod.getId());
		sod.setTempFullNamePath(tempCuntaoOrg.getTempFullNamePath() + "/"
				+ sod.getName());
		cuntaoOrgMapper.updateByPrimaryKey(sod);
		parentOrg.setIsLeaf("n");
		cuntaoOrgMapper.updateByPrimaryKey(parentOrg);
		return sod.getId();
	}

	@Override
	public void updateParent(Long id, Long parentId) {
		CuntaoOrg parentOrg = cuntaoOrgMapper.selectByPrimaryKey(parentId);
		CuntaoOrg org = cuntaoOrgMapper.selectByPrimaryKey(id);
		org.setTempParentId(parentId);
		org.setParentId(parentId);
		org.setTempFullIdPath(parentOrg.getTempFullIdPath() + "/" + id);
		org.setFullIdPath(parentOrg.getTempFullIdPath() + "/" + id);
		org.setTempFullNamePath(parentOrg.getTempFullNamePath() + "/" + org.getName());
		org.setFullNamePath(parentOrg.getTempFullNamePath() + "/" + org.getName());
		org.setGmtModified(new Date());
		cuntaoOrgMapper.updateByPrimaryKey(org);
	}
}
