package com.taobao.cun.auge.county.dto;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;

public class CountyQueryResult extends CountyDto{
	private static final long serialVersionUID = 1L;

	/**
	 * 县负责人
	 */
	private List<CuntaoUserOrgVO> countyLeaders;
	
	/**
	 * 特战队长
	 */
	private List<CuntaoUserOrgVO> teamLeaders;
	
	/**
	 * 省负责人
	 */
	private List<CuntaoUserOrgVO> provinceLeaders;
	
	private boolean addWisdomCounty;

	//智慧县域报名
	private String wisdomCountyApplyState;

	private String wisdomCountyApplyStateDesc;

	public String getWisdomCountyApplyStateDesc() {
		return wisdomCountyApplyStateDesc;
	}

	public void setWisdomCountyApplyStateDesc(String wisdomCountyApplyStateDesc) {
		this.wisdomCountyApplyStateDesc = wisdomCountyApplyStateDesc;
	}

	public String getWisdomCountyApplyState() {
		return wisdomCountyApplyState;
	}

	public void setWisdomCountyApplyState(String wisdomCountyApplyState) {
		this.wisdomCountyApplyState = wisdomCountyApplyState;
	}

	public void addCountyLeader(CuntaoUserOrgVO cuntaoUserOrgVO){
		if(countyLeaders == null){
			countyLeaders = new ArrayList<CuntaoUserOrgVO>();
		}
		countyLeaders.add(cuntaoUserOrgVO);
	}
	public void addTeamLeader(CuntaoUserOrgVO cuntaoUserOrgVO){
		if(teamLeaders == null){
			teamLeaders = new ArrayList<CuntaoUserOrgVO>();
		}
		teamLeaders.add(cuntaoUserOrgVO);
	}
	public void addProvinceLeader(CuntaoUserOrgVO cuntaoUserOrgVO){
		if(provinceLeaders == null){
			provinceLeaders = new ArrayList<CuntaoUserOrgVO>();
		}
		provinceLeaders.add(cuntaoUserOrgVO);
	}


	public boolean isAddWisdomCounty() {
		return addWisdomCounty;
	}

	public void setAddWisdomCounty(boolean addWisdomCounty) {
		this.addWisdomCounty = addWisdomCounty;
	}

	public List<CuntaoUserOrgVO> getCountyLeaders() {
		return countyLeaders;
	}

	public void setCountyLeaders(List<CuntaoUserOrgVO> countyLeaders) {
		this.countyLeaders = countyLeaders;
	}

	public List<CuntaoUserOrgVO> getTeamLeaders() {
		return teamLeaders;
	}

	public void setTeamLeaders(List<CuntaoUserOrgVO> teamLeaders) {
		this.teamLeaders = teamLeaders;
	}

	public List<CuntaoUserOrgVO> getProvinceLeaders() {
		return provinceLeaders;
	}

	public void setProvinceLeaders(List<CuntaoUserOrgVO> provinceLeaders) {
		this.provinceLeaders = provinceLeaders;
	}
}
