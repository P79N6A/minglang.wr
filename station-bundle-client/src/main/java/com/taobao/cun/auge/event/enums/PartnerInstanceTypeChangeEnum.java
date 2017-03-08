package com.taobao.cun.auge.event.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

public class PartnerInstanceTypeChangeEnum implements Serializable {

	private static final long serialVersionUID = -3896079358826754530L;
	// 状态变更类型
	private ChangeEnum type;
	// 描述
	private String description;
	// 变更后合伙人类型
	private PartnerInstanceTypeEnum partnerInstanceType;
	// 变更前合伙人类型
	private PartnerInstanceTypeEnum prePartnerInstanceType;

	public enum ChangeEnum {
		TP_DEGREE_2_TPA,TPA_UPGRADE_2_TP,CANCEL_TPA_UPGRADE_2_TP
	}

	public static final PartnerInstanceTypeChangeEnum TP_DEGREE_2_TPA = new PartnerInstanceTypeChangeEnum(ChangeEnum.TP_DEGREE_2_TPA,
			"合伙人降级: '合伙人'-> '淘帮手'", PartnerInstanceTypeEnum.TP, PartnerInstanceTypeEnum.TPA);
	
	public static final PartnerInstanceTypeChangeEnum TPA_UPGRADE_2_TP = new PartnerInstanceTypeChangeEnum(ChangeEnum.TPA_UPGRADE_2_TP,
            "淘帮手升级: '淘帮手'-> '合伙人'", PartnerInstanceTypeEnum.TP, PartnerInstanceTypeEnum.TPA);
	
	public static final PartnerInstanceTypeChangeEnum CANCEL_TPA_UPGRADE_2_TP = new PartnerInstanceTypeChangeEnum(ChangeEnum.CANCEL_TPA_UPGRADE_2_TP,
            "撤销淘帮手升级: '合伙人'-> '淘帮手'", PartnerInstanceTypeEnum.TPA, PartnerInstanceTypeEnum.TP);

	private static final Map<ChangeEnum, PartnerInstanceTypeChangeEnum> mappings = new HashMap<ChangeEnum, PartnerInstanceTypeChangeEnum>();
	static {
		mappings.put(ChangeEnum.TP_DEGREE_2_TPA, TP_DEGREE_2_TPA);
		mappings.put(ChangeEnum.TPA_UPGRADE_2_TP, TPA_UPGRADE_2_TP);
		mappings.put(ChangeEnum.CANCEL_TPA_UPGRADE_2_TP, CANCEL_TPA_UPGRADE_2_TP);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PartnerInstanceTypeEnum getPartnerInstanceType() {
		return partnerInstanceType;
	}

	public void setPartnerInstanceType(PartnerInstanceTypeEnum partnerInstanceType) {
		this.partnerInstanceType = partnerInstanceType;
	}

	public PartnerInstanceTypeEnum getPrePartnerInstanceType() {
		return prePartnerInstanceType;
	}

	public void setPrePartnerInstanceType(PartnerInstanceTypeEnum prePartnerInstanceType) {
		this.prePartnerInstanceType = prePartnerInstanceType;
	}

	public ChangeEnum getType() {
		return type;
	}

	public void setType(ChangeEnum type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof PartnerInstanceTypeChangeEnum))
			return false;
		PartnerInstanceTypeChangeEnum objType = (PartnerInstanceTypeChangeEnum) obj;
		return objType.getType().equals(this.getType());
	}

	public PartnerInstanceTypeChangeEnum(ChangeEnum type, String description, PartnerInstanceTypeEnum partnerInstanceType,
			PartnerInstanceTypeEnum prePartnerInstanceType) {
		this.type = type;
		this.description = description;
		this.partnerInstanceType = partnerInstanceType;
		this.prePartnerInstanceType = prePartnerInstanceType;
	}

	public static PartnerInstanceTypeChangeEnum valueof(ChangeEnum type) {
		if (type == null)
			return null;
		return mappings.get(type);
	}

	public PartnerInstanceTypeChangeEnum() {

	}
}
