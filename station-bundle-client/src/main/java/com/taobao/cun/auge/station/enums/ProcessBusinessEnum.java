package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProcessBusinessEnum implements Serializable {

	private static final long serialVersionUID = -3645487560971801482L;

	private static final Map<String, ProcessBusinessEnum> mappings = new HashMap<String, ProcessBusinessEnum>();

	private String code;
	private String desc;

	//FIXME 历史原因，没有办法大写，否则需要数据订正，适配已经内外已经存在的流程参数
	public static final ProcessBusinessEnum stationForcedClosure = new ProcessBusinessEnum("stationForcedClosure","申请停业");
	public static final ProcessBusinessEnum stationQuitRecord = new ProcessBusinessEnum("stationQuitRecord", "申请退出");
	public static final ProcessBusinessEnum SHUT_DOWN_STATION = new ProcessBusinessEnum("shutDownStation", "申请撤点");
	public static final ProcessBusinessEnum partnerInstanceLevelAudit = new ProcessBusinessEnum("partnerInstanceLevelAudit", "合伙人层级审批");
	public static final ProcessBusinessEnum TPV_CLOSE = new ProcessBusinessEnum("TPV_CLOSE", "申请停业");
	public static final ProcessBusinessEnum TPV_QUIT = new ProcessBusinessEnum("TPV_QUIT", "申请退出");
	public static final ProcessBusinessEnum peixunPurchase = new ProcessBusinessEnum("peixun_purchase", "培训集采审批");
	public static final ProcessBusinessEnum partnerFlowerNameApply = new ProcessBusinessEnum("partner_flower_name_apply", "花名申请");
	public static final ProcessBusinessEnum incentiveProgramAudit = new ProcessBusinessEnum("incentive_program_audit", "激励方案审批");
	public static final ProcessBusinessEnum assetTransfer = new ProcessBusinessEnum("assetTransfer", "资产转移审批");
	public static final ProcessBusinessEnum peixunRefund = new ProcessBusinessEnum("peixun_refund", "培训退款");
	
	public static final ProcessBusinessEnum partnerQualifyAudit = new ProcessBusinessEnum("partnerQualifyAudit", "招募资格认证");
	public static final ProcessBusinessEnum stationInfoApply = new ProcessBusinessEnum("stationInfoApply", "村站信息修改审批");
	public static final ProcessBusinessEnum decorationFeedback = new ProcessBusinessEnum("decorationFeedback", "装修反馈审批");
	public static final ProcessBusinessEnum decorationInfoDecision = new ProcessBusinessEnum("decorationInfoDecision", "装修图纸审批");
	public static final ProcessBusinessEnum addressInfoDecision = new ProcessBusinessEnum("addressInfoDecision", "选址审批");
	public static final ProcessBusinessEnum serviceAbilityDecision = new ProcessBusinessEnum("serviceAbilityDecision", "服务能力审批");
	public static final ProcessBusinessEnum decorationDesignAudit = new ProcessBusinessEnum("decorationDesignAudit", "装修设计图纸审批");
	public static final ProcessBusinessEnum decorationCheckAudit = new ProcessBusinessEnum("decorationCheckAudit", "装修完工图纸审批");
	public static final ProcessBusinessEnum serviceAbilitySHRHDecision = new ProcessBusinessEnum("serviceAbilitySHRHDecision", "送货入户服务审批");
	
	public static final ProcessBusinessEnum assetCheckCountyFollowTask = new ProcessBusinessEnum("assetCheckCountyFollowTask", "县跟踪盘点任务");
	public static final ProcessBusinessEnum assetCheckCountyTask = new ProcessBusinessEnum("assetCheckCountyTask", "县盘点任务");
	public static final ProcessBusinessEnum serviceTrainAudit = new ProcessBusinessEnum("serviceTrainAudit", "服务资质培训报名审批");

	public static final ProcessBusinessEnum stationTransHandOverInviteAudit=new ProcessBusinessEnum("stationTransHandOverInviteAudit","村点转型切换邀请审批");



	static {
		mappings.put("stationForcedClosure", stationForcedClosure);
		mappings.put("stationQuitRecord", stationQuitRecord);
		mappings.put("shutDownStation", SHUT_DOWN_STATION);
		mappings.put("partnerInstanceLevelAudit", partnerInstanceLevelAudit);
		mappings.put("TPV_CLOSE", TPV_CLOSE);
		mappings.put("TPV_QUIT", TPV_QUIT);
		mappings.put("peixunPurchase", peixunPurchase);
		mappings.put("partnerFlowerNameApply", partnerFlowerNameApply);
		mappings.put("incentiveProgramAudit", incentiveProgramAudit);
		mappings.put("assetTransfer", assetTransfer);
		mappings.put("peixunRefund", peixunRefund);
		mappings.put("partnerQualifyAudit", partnerQualifyAudit);
		mappings.put("stationInfoApply", stationInfoApply);
		mappings.put("decorationFeedback", decorationFeedback);
		mappings.put("decorationInfoDecision", decorationInfoDecision);
		mappings.put("addressInfoDecision", addressInfoDecision);
		mappings.put("serviceAbilityDecision", serviceAbilityDecision);
		mappings.put("decorationDesignAudit", decorationDesignAudit);
		mappings.put("decorationCheckAudit", decorationCheckAudit);
		mappings.put("serviceAbilitySHRHDecision", serviceAbilitySHRHDecision);
		mappings.put("serviceTrainAudit", serviceTrainAudit);
		mappings.put("stationTransHandOverInviteAudit", stationTransHandOverInviteAudit);

	}

	public ProcessBusinessEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public ProcessBusinessEnum() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static ProcessBusinessEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		ProcessBusinessEnum other = (ProcessBusinessEnum) obj;
		if (code == null) {
			if (other.code != null) {
                return false;
            }
		} else if (!code.equals(other.code)) {
            return false;
        }
		return true;
	}
}
