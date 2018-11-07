package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StationTransInfoTypeEnum implements Serializable {

	private static final long serialVersionUID = -3896079358826754530L;
	// 变更类型
	private TypeEnum type;
	// 描述
	private String description;
	// 起始业务态
	private StationBizTypeEnum fromBizType;
	// 目标业务态
	private StationBizTypeEnum toBizType;

	public enum TypeEnum {
		STATION_TO_YOUPIN, STATION_TO_YOUPIN_ELEC, STATION_TO_TPS_ELEC, YOUPIN_TO_YOUPIN_ELEC, YOUPIN_TO_TPS_ELEC, YOUPIN_ELEC_TO_TPS_ELEC
	}

	public static final StationTransInfoTypeEnum STATION_TO_YOUPIN = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_YOUPIN, "服务站转型天猫优品", StationBizTypeEnum.STATION, StationBizTypeEnum.YOUPIN);

	public static final StationTransInfoTypeEnum STATION_TO_YOUPIN_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_YOUPIN_ELEC, "服务站转型电器合作店", StationBizTypeEnum.STATION, StationBizTypeEnum.YOUPIN_ELEC);

	public static final StationTransInfoTypeEnum STATION_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_TPS_ELEC, "服务站转型电器体验店", StationBizTypeEnum.STATION, StationBizTypeEnum.TPS_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_TO_YOUPIN_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_TO_YOUPIN_ELEC, "天猫优品转型电器合作店", StationBizTypeEnum.YOUPIN, StationBizTypeEnum.YOUPIN_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_TO_TPS_ELEC, "天猫优品转型电器体验店", StationBizTypeEnum.YOUPIN, StationBizTypeEnum.TPS_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_ELEC_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_ELEC_TO_TPS_ELEC, "电器合作店转型电器体验店", StationBizTypeEnum.YOUPIN_ELEC,
			StationBizTypeEnum.TPS_ELEC);

	private static final Map<TypeEnum, StationTransInfoTypeEnum> MAPPINGS = new HashMap<TypeEnum, StationTransInfoTypeEnum>();
	static {
		MAPPINGS.put(TypeEnum.STATION_TO_YOUPIN, STATION_TO_YOUPIN);
		MAPPINGS.put(TypeEnum.STATION_TO_YOUPIN_ELEC, STATION_TO_YOUPIN_ELEC);
		MAPPINGS.put(TypeEnum.STATION_TO_TPS_ELEC, STATION_TO_TPS_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_TO_YOUPIN_ELEC, YOUPIN_TO_YOUPIN_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_TO_TPS_ELEC, YOUPIN_TO_TPS_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_ELEC_TO_TPS_ELEC, YOUPIN_ELEC_TO_TPS_ELEC);

	}

	public static TypeEnum getTypeEnumByBizType(StationBizTypeEnum fromBizType, StationBizTypeEnum toBizType) {
		StringBuilder sb = new StringBuilder();
		if (fromBizType != null) {
			sb.append(fromBizType.getCode());
		}
		sb.append("_TO_");
		if (toBizType != null) {
			sb.append(toBizType.getCode());
		}
		String code = sb.toString();

		for (TypeEnum typeEnum : TypeEnum.values()) {
			if (code.equals(typeEnum.name())) {
				return typeEnum;
			}
		}
		return null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StationBizTypeEnum getFromBizType() {
		return fromBizType;
	}

	public void setFromBizType(StationBizTypeEnum fromBizType) {
		this.fromBizType = fromBizType;
	}

	public StationBizTypeEnum getToBizType() {
		return toBizType;
	}

	public void setToBizType(StationBizTypeEnum toBizType) {
		this.toBizType = toBizType;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
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
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StationTransInfoTypeEnum)) {
			return false;
		}
		StationTransInfoTypeEnum objType = (StationTransInfoTypeEnum) obj;
		return objType.getType().equals(this.getType());
	}

	public StationTransInfoTypeEnum(TypeEnum type, String description, StationBizTypeEnum fromBizType,
			StationBizTypeEnum toBizType) {
		this.type = type;
		this.description = description;
		this.toBizType = toBizType;
		this.fromBizType = fromBizType;
	}

	public static StationTransInfoTypeEnum valueof(TypeEnum type) {
		if (type == null) {
			return null;
		}
		return MAPPINGS.get(type);
	}

	public StationTransInfoTypeEnum() {

	}
}
