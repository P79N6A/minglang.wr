package com.taobao.cun.auge.fence.instance.rule;

import java.util.List;

/**
 * 商品规则
 * 
 * @author chengyu.zhoucy
 *
 */
public class CommodityFenceRule extends FenceRule {
	/**
	 * 商品标
	 */
	private List<String> tags;
	
	/**
	 * 商品类目
	 */
	private List<String> categories;
	
	/**
	 * 行业
	 */
	private List<String> industries;

	public List<String> getIndustries() {
		return industries;
	}

	public void setIndustries(List<String> industries) {
		this.industries = industries;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
}
