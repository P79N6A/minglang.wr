package com.taobao.cun.auge.test.init;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.domain.CuntaoOrgExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.log.ExtAppBizLog;
import com.taobao.cun.auge.log.LogContent;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.org.service.ExtDeptOrgClient;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.cun.crius.oss.client.FileStoreService;
import com.taobao.cun.dto.UserTypeEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = OrgInitService.class)
public class OrgInitServiceImpl implements OrgInitService {
	@Resource
	private CuntaoOrgMapper cuntaoOrgMapper;
	@Resource
	private FileStoreService fileStoreService;
	@Resource
	private AppBizLogBo appBizLogBo;
	@Resource
	private ExtDeptOrgClient extDeptOrgClient;
	@Resource
	private CuntaoOrgBO cuntaoOrgBO;
	@Resource
	private CuntaoUserOrgService cuntaoUserOrgService;
	@Resource
	private StationMapper stationMapper;
	@Resource
	private CountyStationMapper countyStationMapper;
	
	@Override
	public void createVirtualTeam() {
		CuntaoOrgExample example = new CuntaoOrgExample();
		example.createCriteria().andOrgRangeTypeEqualTo("province");
		List<CuntaoOrg> cuntaoOrgs = cuntaoOrgMapper.selectByExample(example);
		for(CuntaoOrg cuntaoOrg : cuntaoOrgs) {
			CuntaoOrg team = getCuntaoOrgByName(cuntaoOrg.getTempFullNamePath() + "/拓展战队");
			if(team == null) {
				createOrg(cuntaoOrg, "拓展特战队", "special_team", "特战队");
			}
		}
	}
	
	private CuntaoOrg createOrg(CuntaoOrg cuntaoOrg, String name, String orgRangeType, String deptPro) {
		CuntaoOrg team = new CuntaoOrg();
		team.setCreator("init-virtual");
		team.setModifier("init-virtual");
		team.setGmtCreate(new Date());
		team.setGmtModified(new Date());
		team.setDeptPro(deptPro);
		team.setTempParentId(cuntaoOrg.getId());
		team.setParentId(cuntaoOrg.getId());
		team.setDingtalkDeptId(0L);
		
		team.setName(name);
		team.setFullIdPath("/");
		team.setFullNamePath(cuntaoOrg.getTempFullNamePath() + "/" + name);
		team.setTempFullIdPath("/");
		team.setTempFullNamePath(cuntaoOrg.getTempFullNamePath() + "/" + name);
		team.setFullOrderPath("/0/0/0");
		team.setIsDeleted("n");
		team.setIsLeaf("n");
		team.setIsShow("y");
		team.setOrderLevel(0);
		team.setOrderPro(0);
		team.setOrgRangeType(orgRangeType);
		team.setOrgType("selfSupport");
		team.setParentId(cuntaoOrg.getId());
		cuntaoOrgMapper.insert(team);
		
		team.setFullIdPath(cuntaoOrg.getTempFullIdPath() + "/" + team.getId());
		team.setTempFullIdPath(cuntaoOrg.getTempFullIdPath() + "/" + team.getId());
		cuntaoOrgMapper.updateByPrimaryKey(team);
		return team;
	}

	private CuntaoOrg getCuntaoOrgByName(String name) {
		CuntaoOrgExample example = new CuntaoOrgExample();
		example.createCriteria().andTempFullNamePathEqualTo(name);
		List<CuntaoOrg> cuntaoOrgs = cuntaoOrgMapper.selectByExample(example);
		if(cuntaoOrgs.size() > 0) {
			return cuntaoOrgs.get(0);
		}
		
		return null;
	}

	@Override
	public void initCounty() {
		ExtAppBizLog extAppBizLog = new ExtAppBizLog();
		extAppBizLog.setBizType("init-county");
		extAppBizLog.setBizKey(0L);
		extAppBizLog.setCreator("system");
		extAppBizLog.setState("error");
		extAppBizLog.setLogContents(doInitCounty());
		appBizLogBo.addLog(extAppBizLog);
	}
	
	private List<LogContent> doInitCounty() {
		List<LogContent> logContents = Lists.newArrayList();
		List<String> lines = null;
		try {
			lines = IOUtils.readLines(fileStoreService.read("ext-county.txt"));
		}catch(Throwable t) {
			LogContent logContent = new LogContent();
			logContent.setException(t);
			logContents.add(logContent);
			return logContents;
		}
		for(String line : lines) {
			String[] array = line.split(",");
			Long provinceOrgId = Long.parseLong(array[0]);
			Long countyOrgId = Long.parseLong(array[1]);
			String loginId = array[2];
			String loginName = array[3];
			Optional<CuntaoOrgDto> optional = extDeptOrgClient.getExtTeamByProvinceOrg(provinceOrgId);
			if(optional.isPresent()) {
				cuntaoOrgBO.updateParent(countyOrgId, optional.get().getId());
				List<CuntaoUserOrgVO> cuntaoUserOrgVOs = Lists.newArrayList();
				CuntaoUserOrgVO cuntaoUserOrgVO = new CuntaoUserOrgVO();
				cuntaoUserOrgVO.setLoginId(loginId);
				cuntaoUserOrgVO.setOrgId(countyOrgId);
				cuntaoUserOrgVO.setModifier("init");
				cuntaoUserOrgVO.setCreator("init");
				cuntaoUserOrgVO.setUserRoleEnum(UserRoleEnum.EXT_COUNTY_LEADER);
				cuntaoUserOrgVO.setUserName(loginName);
				cuntaoUserOrgVO.setUserType(UserTypeEnum.BUC.getCode());
				cuntaoUserOrgVOs.add(cuntaoUserOrgVO);
				cuntaoUserOrgService.assignLeaders(countyOrgId, "EXT_COUNTY_LEADER", cuntaoUserOrgVOs);
			}else {
				LogContent logContent = new LogContent();
				logContent.addError("没有拓展战队");
				logContent.setParams(line);
				logContents.add(logContent);
			}
		}
		
		return logContents;
	}

	@Override
	public void initState() {
		List<Station> stations = stationMapper.selectByExample(new StationExample());
		for(Station station : stations) {
			Station s = stationMapper.selectByPrimaryKey(station.getId());
			s.setTransferState(TransferState.FINISHED.name());
			s.setOwnDept(OrgDeptType.opdept.name());
			stationMapper.updateByPrimaryKey(s);
		}
		
		List<CountyStation> countyStations = countyStationMapper.selectByExample(new CountyStationExample());
		for(CountyStation countyStation : countyStations) {
			CountyStation s = countyStationMapper.selectByPrimaryKey(countyStation.getId());
			s.setTransferState(TransferState.FINISHED.name());
			s.setOwnDept(OrgDeptType.opdept.name());
			countyStationMapper.updateByPrimaryKey(s);
		}
	}
}
