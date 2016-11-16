package com.taobao.cun.auge.station.condition;

import java.util.List;

import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;

public class PartnerPeixunQueryCondition extends PageQuery {

	private static final long serialVersionUID = 1L;
	private String orgIdPath;
	private String nickName;
	private String phoneNum;
	private String partnerName;
	private PartnerPeixunStatusEnum peixunStatus;
	private List<PartnerPeixunCourseTypeEnum> courseTypes;

	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public PartnerPeixunStatusEnum getPeixunStatus() {
		return peixunStatus;
	}

	public void setPeixunStatus(PartnerPeixunStatusEnum peixunStatus) {
		this.peixunStatus = peixunStatus;
	}

	public List<PartnerPeixunCourseTypeEnum> getCourseTypes() {
		return courseTypes;
	}

	public void setCourseTypes(List<PartnerPeixunCourseTypeEnum> courseTypes) {
		this.courseTypes = courseTypes;
	}


}
