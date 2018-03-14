package com.taobao.cun.auge.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrg;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrgExample;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrgExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoUserRoleLog;
import com.taobao.cun.auge.dal.mapper.CuntaoUserOrgMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoUserRoleLogMapper;
import com.taobao.cun.auge.org.dto.CuntaoUserRole;
import com.taobao.cun.auge.user.dto.CuntaoBucUserOrgCreateDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgListDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgQueryDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.CuntaoUserStausEnum;
import com.taobao.cun.auge.user.dto.CuntaoUserTypeEnum;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.cun.common.exception.ParamException;
import com.taobao.cun.common.util.BeanCopy;
import com.taobao.cun.endor.dto.BizUserRole;
import com.taobao.cun.endor.dto.User;
import com.taobao.cun.endor.service.UserRoleService;
import com.taobao.cun.endor.service.UserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CollectionUtil;

@Service("CuntaoUserOrgService")
@HSFProvider(serviceInterface = CuntaoUserOrgService.class)
public class CuntaoUserOrgServiceImpl implements CuntaoUserOrgService{

	@Autowired
	CuntaoUserRoleService cuntaoUserRoleService;
	
	@Autowired
	CuntaoUserOrgMapper cuntaoUserOrgMapper;
	
	@Resource
	CuntaoUserRoleLogMapper cuntaoUserRoleLogMapper;
	
	@Resource
	UserRoleService userRoleService;
	
	@Resource
	UserService userService;
	
	@Override
    public Boolean checkOrg(String empId, String cuntaoFullIdPath) {
		if(StringUtils.isEmpty(empId)||StringUtils.isEmpty(cuntaoFullIdPath)){
			return false;
		}
		List<CuntaoUserRole> userRoles=cuntaoUserRoleService.getCuntaoUserRoles(empId);
		if(userRoles==null||userRoles.size()==0){
			return false;
		}
		for(CuntaoUserRole userRole:userRoles){
			if(cuntaoFullIdPath.equals(userRole.getFullIdPath())){
				return true;
			}
		}
		return false;
	}

	@Override
	public PageDto<CuntaoUserOrgListDto> queryUsersByPage(CuntaoUserOrgQueryDto queryCondition) {
		if (queryCondition.getUserType() != null
				&& queryCondition.getUserType().getCode()
						.equals(CuntaoUserTypeEnum.BUC.getCode())) {
			if (CollectionUtil.isEmpty(queryCondition.getOrgIds())) {
				throw new RuntimeException(
						"cuntaoUserOrgQueryVo.orgids is null!");
			}
		}
		if (queryCondition.getPage() < 0) {
            queryCondition.setPage(0);
        }
        if (queryCondition.getPageSize() <= 0) {
            queryCondition.setPageSize(20);
        }
        if (queryCondition.getPageSize() > 100) {
            queryCondition.setPageSize(100);
        }
		PageDto<CuntaoUserOrgListDto> returnModel= new PageDto<CuntaoUserOrgListDto>();
		CuntaoUserOrgExample example=new CuntaoUserOrgExample();
		Criteria c = example.createCriteria().andIsDeletedEqualTo("n");
		if(StringUtils.isNotEmpty(queryCondition.getLoginId())){
			c.andLoginIdEqualTo(queryCondition.getLoginId());
		}
		if(StringUtils.isNotEmpty(queryCondition.getUserName())){
			c.andUserNameEqualTo(queryCondition.getUserName());
		}
		if(queryCondition.getUserType()!=null){
			c.andUserTypeEqualTo(queryCondition.getUserType().getCode());
		}
		if(queryCondition.getUserStatuses()!=null){
			List<String> status=new ArrayList<String>();
			for(CuntaoUserStausEnum en:queryCondition.getUserStatuses()){
				status.add(en.getCode());
			}
			c.andStatusIn(status);
		}
		int total=cuntaoUserOrgMapper.countByExample(example);
		example.setOrderByClause("gmt_modified desc");
        PageHelper.startPage(queryCondition.getPage(), queryCondition.getPageSize());
		List<CuntaoUserOrg> userOrgs = cuntaoUserOrgMapper.selectByExample(example);
		List<CuntaoUserOrgListDto> rst=new ArrayList<CuntaoUserOrgListDto>();
		for(CuntaoUserOrg userOrg:userOrgs){
			rst.add(convert(userOrg));
		}
	    returnModel.setItems(rst);
		returnModel.setTotal(new Long(total));
		return returnModel;
	}

	private CuntaoUserOrgListDto convert(CuntaoUserOrg userOrg) {
		CuntaoUserOrgListDto cuntaoUserOrgVo = new CuntaoUserOrgListDto();
		cuntaoUserOrgVo.setUserName(userOrg.getUserName());
		cuntaoUserOrgVo.setId(userOrg.getId());
		cuntaoUserOrgVo.setOrgId(userOrg.getOrgId());
		cuntaoUserOrgVo.setStartTime(userOrg.getStartTime());
		cuntaoUserOrgVo.setEndTime(userOrg.getEndTime());
		cuntaoUserOrgVo.setId(userOrg.getId());
		cuntaoUserOrgVo.setUserStatus(CuntaoUserStausEnum.valueof(userOrg
				.getStatus()));
		cuntaoUserOrgVo.setWorkNo(userOrg.getLoginId());
		cuntaoUserOrgVo.setMobile(toMap(userOrg.getFeature()).get("mobile"));
		cuntaoUserOrgVo.setLoginId(userOrg.getLoginId());
		String divisionId = toMap(userOrg.getFeature()).get("divisionId");
		if (StringUtils.isNotEmpty(divisionId)) {
			cuntaoUserOrgVo.setDivisionId(new Long(divisionId));
		}
		return cuntaoUserOrgVo;
	}
	
	private Map<String,String> toMap(String feature){
		Map<String,String> valueMap=new HashMap<String, String>();
		if(!StringUtils.isBlank(feature)){
            String[] values=feature.split(";");
            if(values.length>0){
            	for(String value:values){
            		String[] keyValue=value.split(":");
            		if(keyValue.length==2){
                		valueMap.put(keyValue[0],keyValue[1]);
            		}
            	}
            }
        }
		return valueMap;
	}

	@Override
	public Boolean addBucUsers(
			List<CuntaoBucUserOrgCreateDto> cuntaoBucUserOrgCreateDtoList) {
		for (CuntaoBucUserOrgCreateDto vo : cuntaoBucUserOrgCreateDtoList) {
			validate(vo);
			String loginId;
			if (vo.getUserType().getCode()
					.equals(CuntaoUserTypeEnum.BUC.getCode())) {
				loginId = vo.getWorkNo().replaceFirst("^0+", "");
			} else {
				loginId = vo.getWorkNo();
			}
			CuntaoUserOrg cuntaoUserOrgDO = new CuntaoUserOrg();
			Date now = new Date();
			cuntaoUserOrgDO.setUserType(vo.getUserType().getCode());
			cuntaoUserOrgDO.setStatus(CuntaoUserStausEnum.VALID.getCode());
			cuntaoUserOrgDO.setModifier(vo.getOperator());
			cuntaoUserOrgDO.setLoginId(loginId);
			cuntaoUserOrgDO.setCreator(vo.getOperator());
			cuntaoUserOrgDO.setGmtCreate(now);
			cuntaoUserOrgDO.setGmtModified(now);
			cuntaoUserOrgDO.setStartTime(now);
			cuntaoUserOrgDO.setOrgId(vo.getOrgId());
			cuntaoUserOrgDO.setUserName(vo.getUserName());
			cuntaoUserOrgDO.setIsDeleted("n");
			cuntaoUserOrgDO.setFeature(getFeatureString(vo.getMobile(),
					vo.getDivisionId()));
			cuntaoUserOrgDO.setFeatureCc(1);
			judgeExist(vo.getOrgId(), loginId);
			cuntaoUserOrgMapper.insert(cuntaoUserOrgDO);
		}
		return true;
	}
	
	private void judgeExist(Long orgId,String loginId){
		CuntaoUserOrgExample example=new CuntaoUserOrgExample();
		Criteria c = example.createCriteria().andIsDeletedEqualTo("n");
		c.andOrgIdEqualTo(orgId);
		c.andLoginIdEqualTo(loginId);
		c.andStatusEqualTo(CuntaoUserStausEnum.VALID.getCode());
		int count=cuntaoUserOrgMapper.countByExample(example);
		if(count>0){
			 throw new RuntimeException("该同学已经在组织中了哦![" + orgId + "]");
		}
	}
	
	private String getFeatureString(String mobile,String division){
		 Map<String,String> featureMap=new LinkedHashMap<>();
			if(division==null){
				featureMap.put("divisionId", "");
			}else{
				featureMap.put("divisionId", division);
			}
			if(mobile==null){
				featureMap.put("mobile", "");
			}else{
				featureMap.put("mobile", mobile);
			}
			return toString(featureMap);
		
	}
	private String toString(Map<String, String> featureMap){
		StringBuilder feature=new StringBuilder();
		if(featureMap!=null&&!featureMap.isEmpty()){
			for(Entry<String,String> entry:featureMap.entrySet()){
				feature.append(";").append(entry.getKey()).append(":").append(entry.getValue());
			}
			return feature.substring(1);
		}
		return feature.toString();
	}
	
	 private void validate(CuntaoBucUserOrgCreateDto vo) {
	      if(vo==null) {
              throw new ParamException("CuntaoBucUserOrgCreateVo is null");
          }
	      if(vo.getOrgId()==null) {
              throw new ParamException("CuntaoBucUserOrgCreateVo.orgid is null");
          }
	      if(vo.getUserType().getCode().equals(CuntaoUserTypeEnum.BUC.getCode())){
	    	  if(StringUtils.isBlank(vo.getUserName())) {
                  throw new RuntimeException("CuntaoBucUserOrgCreateVo.username is null");
              }
	      }            
	      if(StringUtils.isBlank(vo.getWorkNo())) {
			  throw new RuntimeException("CuntaoBucUserOrgCreateVo.workno is null");
		  }
	   }

	@Override
	public Boolean invalidBucUsers(String workNo, Long orgId) {
		if(StringUtils.isEmpty(workNo)||orgId==null){
			throw new RuntimeException("param is null");
		}
		CuntaoUserOrgExample example=new CuntaoUserOrgExample();
		Criteria c = example.createCriteria().andIsDeletedEqualTo("n");
		c.andOrgIdEqualTo(orgId);
		c.andLoginIdEqualTo(workNo);
		c.andStatusEqualTo(CuntaoUserStausEnum.VALID.getCode());
		List<CuntaoUserOrg> userOrg=cuntaoUserOrgMapper.selectByExample(example);
		for(CuntaoUserOrg user:userOrg){
			user.setModifier(workNo);
			user.setGmtModified(new Date());
			user.setEndTime(new Date());
			user.setStatus(CuntaoUserStausEnum.INVALID.getCode());
			cuntaoUserOrgMapper.updateByPrimaryKey(user);
		}
		return true;
	}
	
	@Override
	public List<CuntaoUserOrgVO> getCuntaoOrgUsers(List<Long> orgIds, List<String> roles){
		CuntaoUserOrgExample example = new CuntaoUserOrgExample();
		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n").andStatusEqualTo("VALID").andUserTypeEqualTo("BUC");
		criteria.andOrgIdIn(orgIds).andRoleIn(roles);
		List<CuntaoUserOrg> cuntaoUserOrgs = cuntaoUserOrgMapper.selectByExample(example);
		if(Iterables.isEmpty(cuntaoUserOrgs)){
			return Lists.newArrayList();
		}
		
		return Lists.transform(cuntaoUserOrgs, new Function<CuntaoUserOrg, CuntaoUserOrgVO>(){
			@Override
			public CuntaoUserOrgVO apply(CuntaoUserOrg input) {
				CuntaoUserOrgVO cuntaoUserOrgVO = BeanCopy.copy(CuntaoUserOrgVO.class, input);
				cuntaoUserOrgVO.setUserRoleEnum(UserRoleEnum.valueof(input.getRole()));
				return cuntaoUserOrgVO;
			}});
	}
	
	@Override
	public void assignLeaders(Long orgId, String leaderType, List<CuntaoUserOrgVO> cuntaoUserOrgVOs) {
		CuntaoUserOrgExample example = new CuntaoUserOrgExample();
		example.createCriteria()
			.andOrgIdEqualTo(orgId)
			.andRoleEqualTo(leaderType)
			.andIsDeletedEqualTo("n")
			.andStatusEqualTo("VALID");
		List<CuntaoUserOrg> cuntaoUserOrgs = cuntaoUserOrgMapper.selectByExample(example);
		
		List<String> oldLoginIds = null;
		if(!Iterables.isEmpty(cuntaoUserOrgs)){
			oldLoginIds = Lists.transform(cuntaoUserOrgs, new Function<CuntaoUserOrg, String>(){
				@Override
				public String apply(CuntaoUserOrg input) {
					return input.getLoginId();
				}});
		}else{
			oldLoginIds = Lists.newArrayList();
		}
		
		List<String> newLoginIds = Lists.transform(cuntaoUserOrgVOs, new Function<CuntaoUserOrgVO, String>(){
			@Override
			public String apply(CuntaoUserOrgVO input) {
				return input.getLoginId();
			}});
		
		//原来在cuntaoUserOrgs中,但不在cuntaoUserOrgVOs中，则取消掉负责人身份
		List<String> removeList = Lists.newArrayList(oldLoginIds);
		Iterables.removeAll(removeList, newLoginIds);
		if(!Iterables.isEmpty(removeList)){
			for(String loginId : removeList){
				unassignLeader(orgId, loginId, leaderType, cuntaoUserOrgVOs.get(0).getModifier());
			}
		}
		//不在原来列表中的则是新加的
		List<String> addList = Lists.newArrayList(newLoginIds);
		Iterables.removeAll(addList, oldLoginIds);
		if(!Iterables.isEmpty(addList)){
			for(String loginId : addList){
				for(CuntaoUserOrgVO cuntaoUserOrgVO : cuntaoUserOrgVOs){
					if(loginId.equals(cuntaoUserOrgVO.getLoginId())){
						assignLeader(cuntaoUserOrgVO);
						break;
					}
				}
			}
		}
	}
	
	private void assignLeader(CuntaoUserOrgVO cuntaoUserOrgVO) {
		CuntaoUserOrgExample example = new CuntaoUserOrgExample();
		example.createCriteria()
			.andLoginIdEqualTo(cuntaoUserOrgVO.getLoginId())
			.andOrgIdEqualTo(cuntaoUserOrgVO.getOrgId())
			.andIsDeletedEqualTo("n")
			.andStatusEqualTo("VALID");
		List<CuntaoUserOrg> cuntaoUserOrgs = cuntaoUserOrgMapper.selectByExample(example);
		
		CuntaoUserRoleLog userRoleLog = new CuntaoUserRoleLog();
		if(Iterables.isEmpty(cuntaoUserOrgs)){
			CuntaoUserOrg cuntaoUserOrg = new CuntaoUserOrg();
			cuntaoUserOrg.setCreator(cuntaoUserOrgVO.getCreator());
			cuntaoUserOrg.setModifier(cuntaoUserOrgVO.getModifier());
			cuntaoUserOrg.setGmtCreate(new Date());
			cuntaoUserOrg.setGmtModified(new Date());
			cuntaoUserOrg.setIsDeleted("n");
			cuntaoUserOrg.setOrgId(cuntaoUserOrgVO.getOrgId());
			cuntaoUserOrg.setRole(cuntaoUserOrgVO.getUserRoleEnum().getCode());
			cuntaoUserOrg.setLoginId(cuntaoUserOrgVO.getLoginId());
			cuntaoUserOrg.setUserName(cuntaoUserOrgVO.getUserName()); 
			cuntaoUserOrg.setUserType(cuntaoUserOrgVO.getUserType());
			cuntaoUserOrg.setStartTime(new Date());
			cuntaoUserOrg.setFeatureCc(0);
			cuntaoUserOrg.setStatus("VALID");
			cuntaoUserOrgMapper.insert(cuntaoUserOrg);
			
			User user = new User();
			user.setUserId(String.valueOf(cuntaoUserOrgVO.getUserId()));
			user.setUserName(cuntaoUserOrgVO.getUserName());
			user.setCreator(cuntaoUserOrgVO.getCreator());
			user.setModifier(cuntaoUserOrgVO.getModifier());
			user.setState("NORMAL");
			userService.save("cuntaobops", user);
			BizUserRole bizUserRole = new BizUserRole();
			bizUserRole.setBizOrgId(cuntaoUserOrgVO.getOrgId());
			bizUserRole.setBizUserId(String.valueOf(cuntaoUserOrgVO.getUserId()));
			bizUserRole.setCreator(cuntaoUserOrgVO.getCreator());
			bizUserRole.setModifier(cuntaoUserOrgVO.getModifier());
			bizUserRole.setRoleName(getRole(cuntaoUserOrgVO.getUserRoleEnum().getCode()));
			bizUserRole.setEndTime(DateUtils.addMonths(new Date(), 3));
			userRoleService.addBizUserRole("cuntaobops", bizUserRole);
		}else{
			CuntaoUserOrg cuntaoUserOrg = cuntaoUserOrgs.get(0);
			//如果角色没有发生变化，那么直接返回
			if(cuntaoUserOrgVO.getUserRoleEnum().getCode().equals(cuntaoUserOrg.getRole())){
				return;
			}
			cuntaoUserOrg.setModifier(cuntaoUserOrgVO.getModifier());
			cuntaoUserOrg.setGmtModified(new Date());
			userRoleLog.setOldRole(cuntaoUserOrg.getRole());
			cuntaoUserOrg.setRole(cuntaoUserOrgVO.getUserRoleEnum().getCode());
			cuntaoUserOrgMapper.updateByPrimaryKey(cuntaoUserOrg);
		}
		userRoleLog.setLoginId(cuntaoUserOrgVO.getLoginId());
		userRoleLog.setOrgId(cuntaoUserOrgVO.getOrgId());
		userRoleLog.setGmtCreate(new Date());
		userRoleLog.setGmtModified(new Date());
		userRoleLog.setCreator(cuntaoUserOrgVO.getCreator());
		userRoleLog.setModifier(cuntaoUserOrgVO.getModifier());
		userRoleLog.setNewRole(cuntaoUserOrgVO.getUserRoleEnum().getCode());
		cuntaoUserRoleLogMapper.insert(userRoleLog);
	}
	
	private String getRole(String userRole) {
		if(userRole.equals(UserRoleEnum.TEAM_LEADER.getCode())) {
			return "SPECIAL_TEAM_LEADER";
		}
		
		if(userRole.equals(UserRoleEnum.PROVINCE_LEADER.getCode())) {
			return "PROVINCE_LEADER";
		}
		
		return "COUNTY_ADMIN";
	}
	
	private void unassignLeader(Long orgId, String loginId, String leaderType, String modifier) {
		CuntaoUserOrgExample example = new CuntaoUserOrgExample();
		example.createCriteria()
			.andLoginIdEqualTo(loginId)
			.andOrgIdEqualTo(orgId)
			.andIsDeletedEqualTo("n")
			.andRoleEqualTo(leaderType)
			.andStatusEqualTo("VALID");
		List<CuntaoUserOrg> cuntaoUserOrgs = cuntaoUserOrgMapper.selectByExample(example);
		if(!Iterables.isEmpty(cuntaoUserOrgs)){
			CuntaoUserRoleLog userRoleLog = new CuntaoUserRoleLog();
			CuntaoUserOrg cuntaoUserOrg = cuntaoUserOrgs.get(0);
			cuntaoUserOrg.setModifier(modifier);
			cuntaoUserOrg.setGmtModified(new Date());
			//以下两行顺序别搞反了，否则会出大事的！！！
			userRoleLog.setOldRole(cuntaoUserOrg.getRole());
			cuntaoUserOrg.setRole("");
			cuntaoUserOrgMapper.updateByPrimaryKey(cuntaoUserOrg);
			
			userRoleLog.setGmtCreate(new Date());
			userRoleLog.setGmtModified(new Date());
			userRoleLog.setNewRole("");
			userRoleLog.setCreator(modifier);
			userRoleLog.setModifier(modifier);
			userRoleLog.setLoginId(cuntaoUserOrg.getLoginId());
			userRoleLog.setOrgId(cuntaoUserOrg.getOrgId());
			cuntaoUserRoleLogMapper.insert(userRoleLog);
			userRoleService.deleteBizUserRole("cuntaobops", loginId, orgId, getRole(leaderType));
		}
	}

}
