package com.taobao.cun.auge.testuser;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.log.bo.SyncLogBo;
import com.taobao.cun.endor.dto.BizUserRole;
import com.taobao.cun.endor.dto.User;
import com.taobao.cun.endor.service.UserRoleService;
import com.taobao.cun.endor.service.UserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HSFProvider(serviceInterface = PartnerSync.class)
public class PartnerSyncImpl implements PartnerSync {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private PartnerMapper partnerMapper;
	@Resource
	private SyncLogBo syncLogBo;
	private Long rootId = 2L;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private UserService userService;
	
	@Override
	public void sync() {
		List<PartnerRole> partners = partnerMapper.queryAll();
		partners.forEach(partner->{
			logger.info("name={}, taobaouserid={}", partner.getName(), partner.getUserid(), partner.getRole());
			SyncLog syncLog = new SyncLog();
			syncLog.setType("SYNC-PARTNER");
			syncLog.setState("NEW");
			syncLog.setContent(JSON.toJSONString(partner));
			syncLog = syncLogBo.addLog(syncLog);
			try{
				addUserRole(partner);
				syncLog.setState("success");
				syncLogBo.updateState(syncLog);
			}catch(Exception e){
				syncLog.setState("fail");
				syncLog.setErrorMsg(e.getMessage());
				syncLogBo.updateState(syncLog);
			}
		});
		
	}
	
	private void addUserRole(PartnerRole partnerRole) throws Exception{
		User user = new User();
		user.setUserId(partnerRole.getUserid());
		user.setUserName(partnerRole.getName());
		user.setCreator("sys");
		user.setModifier("sys");
		user.setState("Normal");
		userService.save("cuntaostore", user);
		BizUserRole bizUserRole = new BizUserRole();
		bizUserRole.setBizOrgId(rootId);//不区分组织，验权而已，并不关心组织
		bizUserRole.setBizUserId(partnerRole.getUserid());
		bizUserRole.setRoleName(partnerRole.getRole());
		bizUserRole.setCreator("sys");
		bizUserRole.setModifier("sys");
		userRoleService.addBizUserRole("cuntaostore", bizUserRole);
	}

}
