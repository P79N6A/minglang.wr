package com.taobao.cun.auge.lx.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 伙伴列表dto
 * @author quanzhu.wangqz
 *
 */
public class LxPartnerListDto implements Serializable {
	
	private static final long serialVersionUID = -7361654339368666823L;

	/**
	 * 拉新伙伴最大数量
	 */
	private Integer maxCount;
	
	/**
	 * 已增加的伙伴列表
	 */
	private List<LxPartnerDto> lxPartners;

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public List<LxPartnerDto> getLxPartners() {
		return lxPartners;
	}

	public void setLxPartners(List<LxPartnerDto> lxPartners) {
		this.lxPartners = lxPartners;
	}
	
	
}
