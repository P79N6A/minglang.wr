package com.taobao.cun.auge.event.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.StationStatusEnum;

public class StationStatusChangeEnum implements Serializable{

	private static final long serialVersionUID = -5332635806448596297L;
	
	// 状态变更类型
	private ChangeEnum type;
	// 描述
	private String description;
	// 变更后村点状态
	private StationStatusEnum stationStatus;
	// 变更前村点状态
	private StationStatusEnum preStationStatus;

	public enum ChangeEnum {
		NEW, INVALID, START_DECORATING, START_SERVICING, START_CLOSING, CLOSING_REFUSED, CLOSED, START_QUITTING, QUITTING_REFUSED, QUIT
	}

	public static final StationStatusChangeEnum NEW = new StationStatusChangeEnum(ChangeEnum.NEW,
			"正式提交: '暂存'-> '新'", StationStatusEnum.NEW, StationStatusEnum.TEMP);

	public static final StationStatusChangeEnum INVALID = new StationStatusChangeEnum(ChangeEnum.INVALID,
			"入驻失败: '新' -> '无效'", StationStatusEnum.INVALID, StationStatusEnum.NEW);

	public static final StationStatusChangeEnum START_DECORATING = new StationStatusChangeEnum(ChangeEnum.START_DECORATING,
			"进入装修中 : '新'-> '装修中'", StationStatusEnum.DECORATING, StationStatusEnum.NEW);

	public static final StationStatusChangeEnum START_SERVICING = new StationStatusChangeEnum(ChangeEnum.START_SERVICING,
			"进入服务中 : '装修中' -> '服务中'", StationStatusEnum.SERVICING, StationStatusEnum.DECORATING);

	public static final StationStatusChangeEnum START_CLOSING = new StationStatusChangeEnum(ChangeEnum.START_CLOSING,
			"申请停业: '服务中' -> '停业申请中'", StationStatusEnum.CLOSING, StationStatusEnum.SERVICING);

	public static final StationStatusChangeEnum CLOSING_REFUSED = new StationStatusChangeEnum(ChangeEnum.CLOSING_REFUSED,
			"停业申请打回:'停业申请中' ->'服务中'", StationStatusEnum.SERVICING, StationStatusEnum.CLOSING);

	public static final StationStatusChangeEnum CLOSED = new StationStatusChangeEnum(ChangeEnum.CLOSED,
			"停业 : '停业申请中' -> '已停业'", StationStatusEnum.CLOSED, StationStatusEnum.CLOSING);

	public static final StationStatusChangeEnum START_QUITTING = new StationStatusChangeEnum(ChangeEnum.START_QUITTING,
			"申请撤点 :'已停业' -> '撤点申请中'", StationStatusEnum.QUITING, StationStatusEnum.CLOSED);

	public static final StationStatusChangeEnum QUITTING_REFUSED = new StationStatusChangeEnum(ChangeEnum.QUITTING_REFUSED,
			"撤点申请打回 : '撤点申请中' -> '已停业'", StationStatusEnum.CLOSED, StationStatusEnum.QUITING);

	public static final StationStatusChangeEnum QUIT = new StationStatusChangeEnum(ChangeEnum.QUIT, "已撤点 :'撤点申请中'->'已撤点'",
			StationStatusEnum.QUIT, StationStatusEnum.QUITING);
	
	private static final Map<ChangeEnum, StationStatusChangeEnum> mappings = new HashMap<ChangeEnum, StationStatusChangeEnum>();
	static {
		mappings.put(ChangeEnum.NEW, NEW);
		mappings.put(ChangeEnum.INVALID, INVALID);
		mappings.put(ChangeEnum.START_DECORATING, START_DECORATING);
		mappings.put(ChangeEnum.START_SERVICING, START_SERVICING);
		mappings.put(ChangeEnum.START_CLOSING, START_CLOSING);
		mappings.put(ChangeEnum.CLOSING_REFUSED, CLOSING_REFUSED);
		mappings.put(ChangeEnum.CLOSED, CLOSED);
		mappings.put(ChangeEnum.START_QUITTING, START_QUITTING);
		mappings.put(ChangeEnum.QUITTING_REFUSED, QUITTING_REFUSED);
		mappings.put(ChangeEnum.QUIT, QUIT);

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StationStatusEnum getStationStatus() {
		return stationStatus;
	}

	public void setStationStatus(StationStatusEnum stationStatus) {
		this.stationStatus = stationStatus;
	}

	public StationStatusEnum getPreStationStatus() {
		return preStationStatus;
	}

	public void setPreStationStatus(StationStatusEnum preStationStatus) {
		this.preStationStatus = preStationStatus;
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
		if (!(obj instanceof StationStatusChangeEnum))
			return false;
		StationStatusChangeEnum objType = (StationStatusChangeEnum) obj;
		return objType.getType().equals(this.getType());
	}

	public StationStatusChangeEnum(ChangeEnum type, String description, StationStatusEnum stationStatus,
			StationStatusEnum preStationStatus) {
		this.type = type;
		this.description = description;
		this.stationStatus = stationStatus;
		this.preStationStatus = preStationStatus;
	}

	public static StationStatusChangeEnum valueof(ChangeEnum type) {
		if (type == null)
			return null;
		return mappings.get(type);
	}

	public StationStatusChangeEnum() {

	}
}
