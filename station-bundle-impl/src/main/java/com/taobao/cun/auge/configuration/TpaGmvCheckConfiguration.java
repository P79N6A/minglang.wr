package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class TpaGmvCheckConfiguration {

	// =========奖励tp淘帮手名额===========

	// 绩效奖励： 根据最近2个月绩效，奖励合伙人淘帮手名额
	@Value("${last.months.4.auto.close}")
	private Integer lastMonths4TpaPerform = 2;

	// 绩效奖励：排名前20%
	@Value("${scale.4.tpa.perform}")
	private Double scale4TpaPerform = 0.2;

	// 绩效奖励：每次奖励tp名额数
	@Value("${reward.tpa.num.4.tpa.perform}")
	private Integer rewardTpaNum4TpaPerform = 2;

	// tp最多tpa名额
	@Value("${max.tpa.num.4.tp}")
	private Integer maxTpaNum4Tp = 10;

	// tp默认tpa名额
	@Value("${default.tpa.num.4.tp}")
	private Integer defaultTpaNum4Tp = 3;

	// =========tpa 直升 奖励tp淘帮手名额===========

	// tpa 直升：奖励父合伙人2个名额
	@Value("${reward.tpa.num.4.tpa.upgrade}")
	private Integer rewardTpaNum4TpaUpgrade = 2;

	// =========tpa自动停业===========

	// 淘帮手自动停业: 淘帮手业绩不达标，减少父合伙人1个名额
	@Value("${reduce.tpa.num.4.auto.close}")
	private Integer reduceTpaNum4AutoClose = 1;

	// 淘帮手自动停业: 根据最近2个月绩效
	@Value("${last.months.4.auto.close}")
	private Integer lastMonths4AutoClose = 2;

	// 淘帮手自动停业:GMV限制
	@Value("${gmv.limit.4.auto.close}")
	private Double gmvLimit4AutoClose = 1000d;

	// 淘帮手自动停业:订单限制
	@Value("${order.limit.4.auto.close}")
	private Long orderLimit4AutoClose = 10l;

	public Long getOrderLimit4AutoClose() {
		return orderLimit4AutoClose;
	}

	public void setOrderLimit4AutoClose(Long orderLimit4AutoClose) {
		this.orderLimit4AutoClose = orderLimit4AutoClose;
	}

	public Integer getReduceTpaNum4AutoClose() {
		return reduceTpaNum4AutoClose;
	}

	public void setReduceTpaNum4AutoClose(Integer reduceTpaNum4AutoClose) {
		this.reduceTpaNum4AutoClose = reduceTpaNum4AutoClose;
	}

	public Integer getLastMonths4AutoClose() {
		return lastMonths4AutoClose;
	}

	public void setLastMonths4AutoClose(Integer lastMonths4AutoClose) {
		this.lastMonths4AutoClose = lastMonths4AutoClose;
	}

	public Double getGmvLimit4AutoClose() {
		return gmvLimit4AutoClose;
	}

	public void setGmvLimit4AutoClose(Double gmvLimit4AutoClose) {
		this.gmvLimit4AutoClose = gmvLimit4AutoClose;
	}

	public Integer getLastMonths4TpaPerform() {
		return lastMonths4TpaPerform;
	}

	public void setLastMonths4TpaPerform(Integer lastMonths4TpaPerform) {
		this.lastMonths4TpaPerform = lastMonths4TpaPerform;
	}

	public Double getScale4TpaPerform() {
		return scale4TpaPerform;
	}

	public void setScale4TpaPerform(Double scale4TpaPerform) {
		this.scale4TpaPerform = scale4TpaPerform;
	}

	public Integer getRewardTpaNum4TpaPerform() {
		return rewardTpaNum4TpaPerform;
	}

	public void setRewardTpaNum4TpaPerform(Integer rewardTpaNum4TpaPerform) {
		this.rewardTpaNum4TpaPerform = rewardTpaNum4TpaPerform;
	}

	public Integer getMaxTpaNum4Tp() {
		return maxTpaNum4Tp;
	}

	public void setMaxTpaNum4Tp(Integer maxTpaNum4Tp) {
		this.maxTpaNum4Tp = maxTpaNum4Tp;
	}

	public Integer getDefaultTpaNum4Tp() {
		return defaultTpaNum4Tp;
	}

	public void setDefaultTpaNum4Tp(Integer defaultTpaNum4Tp) {
		this.defaultTpaNum4Tp = defaultTpaNum4Tp;
	}

	public Integer getRewardTpaNum4TpaUpgrade() {
		return rewardTpaNum4TpaUpgrade;
	}

	public void setRewardTpaNum4TpaUpgrade(Integer rewardTpaNum4TpaUpgrade) {
		this.rewardTpaNum4TpaUpgrade = rewardTpaNum4TpaUpgrade;
	}
}
