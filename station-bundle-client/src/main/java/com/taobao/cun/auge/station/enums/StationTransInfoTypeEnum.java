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
	private StationTransInfoBizTypeEnum fromBizType;
	// 目标业务态
	private StationTransInfoBizTypeEnum toBizType;

	public enum TypeEnum {
		STATION_TO_YOUPIN, STATION_TO_YOUPIN_ELEC, STATION_TO_TPS_ELEC, YOUPIN_TO_YOUPIN_ELEC, YOUPIN_TO_TPS_ELEC, YOUPIN_ELEC_TO_TPS_ELEC
	}

	public static final StationTransInfoTypeEnum STATION_TO_YOUPIN = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_YOUPIN, "服务站转型天猫优品", StationTransInfoBizTypeEnum.STATION,
			StationTransInfoBizTypeEnum.YOUPIN);

	public static final StationTransInfoTypeEnum STATION_TO_YOUPIN_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_YOUPIN_ELEC, "服务站转型电器合作店", StationTransInfoBizTypeEnum.STATION,
			StationTransInfoBizTypeEnum.YOUPIN_ELEC);

	public static final StationTransInfoTypeEnum STATION_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.STATION_TO_TPS_ELEC, "服务站转型电器体验店", StationTransInfoBizTypeEnum.STATION,
			StationTransInfoBizTypeEnum.TPS_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_TO_YOUPIN_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_TO_YOUPIN_ELEC, "天猫优品转型电器合作店", StationTransInfoBizTypeEnum.YOUPIN,
			StationTransInfoBizTypeEnum.YOUPIN_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_TO_TPS_ELEC, "天猫优品转型电器体验店", StationTransInfoBizTypeEnum.YOUPIN,
			StationTransInfoBizTypeEnum.TPS_ELEC);

	public static final StationTransInfoTypeEnum YOUPIN_ELEC_TO_TPS_ELEC = new StationTransInfoTypeEnum(
			TypeEnum.YOUPIN_ELEC_TO_TPS_ELEC, "电器合作店转型电器体验店", StationTransInfoBizTypeEnum.YOUPIN_ELEC,
			StationTransInfoBizTypeEnum.TPS_ELEC);

	private static final Map<TypeEnum, StationTransInfoTypeEnum> MAPPINGS = new HashMap<TypeEnum, StationTransInfoTypeEnum>();
	static {
		MAPPINGS.put(TypeEnum.STATION_TO_YOUPIN, STATION_TO_YOUPIN);
		MAPPINGS.put(TypeEnum.STATION_TO_YOUPIN_ELEC, STATION_TO_YOUPIN_ELEC);
		MAPPINGS.put(TypeEnum.STATION_TO_TPS_ELEC, STATION_TO_TPS_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_TO_YOUPIN_ELEC, YOUPIN_TO_YOUPIN_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_TO_TPS_ELEC, YOUPIN_TO_TPS_ELEC);
		MAPPINGS.put(TypeEnum.YOUPIN_ELEC_TO_TPS_ELEC, YOUPIN_ELEC_TO_TPS_ELEC);

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StationTransInfoBizTypeEnum getFromBizType() {
		return fromBizType;
	}

	public void setFromBizType(StationTransInfoBizTypeEnum fromBizType) {
		this.fromBizType = fromBizType;
	}

	public StationTransInfoBizTypeEnum getToBizType() {
		return toBizType;
	}

	public void setToBizType(StationTransInfoBizTypeEnum toBizType) {
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

	public StationTransInfoTypeEnum(TypeEnum type, String description, StationTransInfoBizTypeEnum fromBizType,
			StationTransInfoBizTypeEnum toBizType) {
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
