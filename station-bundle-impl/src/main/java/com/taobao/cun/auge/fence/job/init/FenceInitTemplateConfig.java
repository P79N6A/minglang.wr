package com.taobao.cun.auge.fence.job.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

/**
 * 默认围栏模板配置
 * 
 * @author chengyu.zhoucy
 *
 */
@Configuration
public class FenceInitTemplateConfig {
	/**
	 * 收费围栏 - 镇级服务站默认收费围栏
	 */
	@Value("${fence.templateid.fee.town}")
	private Long templateIdFeeTown;
	
	/**
	 * 收费围栏 - 村级服务站默认收费围栏
	 */
	@Value("${fence.templateid.fee.village}")
	private Long templateIdFeeVillage;
	
	/**
	 * 售卖围栏 - 优品体验店默认售卖围栏
	 */
	@Value("${fence.templateid.sell.store.town}")
	private Long templateIdSellStoreTown;
	
	/**
	 * 售卖围栏 - 镇服务站默认售卖围栏
	 */
	@Value("${fence.templateid.sell.town}")
	private Long templateIdSellTown;
	
	/**
	 * 售卖围栏 - 村服务站默认售卖围栏
	 */
	@Value("${fence.templateid.sell.village}")
	private Long templateIdSellVillage;

	public Long getTemplateIdFeeTown() {
		return templateIdFeeTown;
	}

	public void setTemplateIdFeeTown(Long templateIdFeeTown) {
		this.templateIdFeeTown = templateIdFeeTown;
	}

	public Long getTemplateIdFeeVillage() {
		return templateIdFeeVillage;
	}

	public void setTemplateIdFeeVillage(Long templateIdFeeVillage) {
		this.templateIdFeeVillage = templateIdFeeVillage;
	}

	public Long getTemplateIdSellStoreTown() {
		return templateIdSellStoreTown;
	}

	public void setTemplateIdSellStoreTown(Long templateIdSellStoreTown) {
		this.templateIdSellStoreTown = templateIdSellStoreTown;
	}

	public List<Long> getTownTemplates(){
		return Lists.newArrayList(
				templateIdFeeTown, 
				//templateIdLogisticsTownAtuoselected,
				//templateIdLogisticsTown,
				templateIdSellStoreTown,
				templateIdSellTown
				);
	}
	
	public List<Long> getVillageTemplates(){
		return Lists.newArrayList(
				templateIdFeeVillage,
				//templateIdLogisticsVillageAtuoselected,
				templateIdSellVillage
				);
	}
	
	public List<Long> getStoreTemplates(){
		return Lists.newArrayList(
				templateIdSellStoreTown
				);
	}
	
	public List<Long> getTPTemplates(){
		return Lists.newArrayList(
				templateIdFeeTown, 
				templateIdSellTown,
				templateIdFeeVillage,
				templateIdSellVillage
				);
	}
	
	public boolean isStoreTemplate(Long templateId) {
		return getStoreTemplates().contains(templateId);
	}
	
	public boolean isTownTemplate(Long templateId) {
		return getTownTemplates().contains(templateId);
	}
	
	public boolean isVillageTemplate(Long templateId) {
		return getVillageTemplates().contains(templateId);
	}

	public Long getTemplateIdSellTown() {
		return templateIdSellTown;
	}

	public void setTemplateIdSellTown(Long templateIdSellTown) {
		this.templateIdSellTown = templateIdSellTown;
	}

	public Long getTemplateIdSellVillage() {
		return templateIdSellVillage;
	}

	public void setTemplateIdSellVillage(Long templateIdSellVillage) {
		this.templateIdSellVillage = templateIdSellVillage;
	}
}
