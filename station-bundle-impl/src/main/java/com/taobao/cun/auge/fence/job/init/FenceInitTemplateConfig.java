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
	 * 物流围栏 - 服务站推荐围栏
	 */
	@Value("${fence.templateid.logistics}")
	private Long templateIdLogistics;
	/**
	 * 物流围栏 - 镇级服务站默认勾选围栏
	 */
	@Value("${fence.templateid.logistics.town.atuoselected}")
	private Long templateIdLogisticsTownAtuoselected;
	
	/**
	 * 物流围栏 - 绑定同镇推荐围栏
	 */
	@Value("${fence.templateid.logistics.town}")
	private Long templateIdLogisticsTown;
	
	/**
	 * 物流围栏 - 村级服务站默认勾选围栏
	 */
	@Value("${fence.templateid.logistics.village.atuoselected}")
	private Long templateIdLogisticsVillageAtuoselected;
	
	/**
	 * 售卖围栏 - 体验店优品售卖围栏
	 */
	@Value("${fence.templateid.sell.store.town}")
	private Long templateIdSellStoreTown;
	
	/**
	 * 售卖围栏 - 镇类型服务站优品售卖围栏
	 */
	@Value("${fence.templateid.sell.youpin.town}")
	private Long templateIdSellYoupinTown;
	
	/**
	 * 售卖围栏 - 村类型服务站优品售卖围栏
	 */
	@Value("${fence.templateid.sell.youpin.village}")
	private Long templateIdSellYoupinVillage;

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

	public Long getTemplateIdLogistics() {
		return templateIdLogistics;
	}

	public void setTemplateIdLogistics(Long templateIdLogistics) {
		this.templateIdLogistics = templateIdLogistics;
	}

	public Long getTemplateIdLogisticsTownAtuoselected() {
		return templateIdLogisticsTownAtuoselected;
	}

	public void setTemplateIdLogisticsTownAtuoselected(Long templateIdLogisticsTownAtuoselected) {
		this.templateIdLogisticsTownAtuoselected = templateIdLogisticsTownAtuoselected;
	}

	public Long getTemplateIdLogisticsTown() {
		return templateIdLogisticsTown;
	}

	public void setTemplateIdLogisticsTown(Long templateIdLogisticsTown) {
		this.templateIdLogisticsTown = templateIdLogisticsTown;
	}

	public Long getTemplateIdLogisticsVillageAtuoselected() {
		return templateIdLogisticsVillageAtuoselected;
	}

	public void setTemplateIdLogisticsVillageAtuoselected(Long templateIdLogisticsVillageAtuoselected) {
		this.templateIdLogisticsVillageAtuoselected = templateIdLogisticsVillageAtuoselected;
	}

	public Long getTemplateIdSellStoreTown() {
		return templateIdSellStoreTown;
	}

	public void setTemplateIdSellStoreTown(Long templateIdSellStoreTown) {
		this.templateIdSellStoreTown = templateIdSellStoreTown;
	}

	public Long getTemplateIdSellYoupinTown() {
		return templateIdSellYoupinTown;
	}

	public void setTemplateIdSellYoupinTown(Long templateIdSellYoupinTown) {
		this.templateIdSellYoupinTown = templateIdSellYoupinTown;
	}

	public Long getTemplateIdSellYoupinVillage() {
		return templateIdSellYoupinVillage;
	}

	public void setTemplateIdSellYoupinVillage(Long templateIdSellYoupinVillage) {
		this.templateIdSellYoupinVillage = templateIdSellYoupinVillage;
	}
	
	public List<Long> getTownTemplates(){
		return Lists.newArrayList(
				templateIdFeeTown, 
				//templateIdLogisticsTownAtuoselected,
				//templateIdLogisticsTown,
				templateIdSellStoreTown,
				templateIdSellYoupinTown
				);
	}
	
	public List<Long> getVillageTemplates(){
		return Lists.newArrayList(
				templateIdFeeVillage,
				//templateIdLogisticsVillageAtuoselected,
				templateIdSellYoupinVillage
				);
	}
	
	public boolean isTownTemplate(Long templateId) {
		return getTownTemplates().contains(templateId);
	}
	
	public boolean isVillageTemplate(Long templateId) {
		return getVillageTemplates().contains(templateId);
	}
}
