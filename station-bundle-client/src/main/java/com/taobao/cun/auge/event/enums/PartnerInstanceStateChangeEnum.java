package com.taobao.cun.auge.event.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

public class PartnerInstanceStateChangeEnum implements Serializable {

	private static final long serialVersionUID = -3896079358826754530L;
	// 状态变更类型
	private ChangeEnum type;
	// 描述
	private String description;
	// 变更后合伙人实例状态
	private PartnerInstanceStateEnum partnerInstanceState;
	// 变更前合伙人实例状态
	private PartnerInstanceStateEnum prePartnerInstanceState;

	public enum ChangeEnum {
		START_SETTLING, SETTLING_REFUSED, START_DECORATING, START_SERVICING, START_CLOSING, CLOSING_REFUSED, CLOSED, START_QUITTING, QUITTING_REFUSED, QUIT, CLOSE_TO_SERVICE,DECORATING_CLOSING
	}

	public static final PartnerInstanceStateChangeEnum START_SETTLING = new PartnerInstanceStateChangeEnum(ChangeEnum.START_SETTLING,
			"正式提交: '暂存'-> '入驻中'", PartnerInstanceStateEnum.SETTLING, PartnerInstanceStateEnum.TEMP);

	public static final PartnerInstanceStateChangeEnum SETTLING_REFUSED = new PartnerInstanceStateChangeEnum(ChangeEnum.SETTLING_REFUSED,
			"入驻失败: '入驻中' -> '入驻失败'", PartnerInstanceStateEnum.SETTLE_FAIL, PartnerInstanceStateEnum.SETTLING);

	public static final PartnerInstanceStateChangeEnum START_DECORATING = new PartnerInstanceStateChangeEnum(ChangeEnum.START_DECORATING,
			"进入装修中 : '入驻中'-> '装修中'", PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SETTLING);

	public static final PartnerInstanceStateChangeEnum START_SERVICING = new PartnerInstanceStateChangeEnum(ChangeEnum.START_SERVICING,
			"进入服务中 : '装修中' -> '服务中'", PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.DECORATING);

	public static final PartnerInstanceStateChangeEnum START_CLOSING = new PartnerInstanceStateChangeEnum(ChangeEnum.START_CLOSING,
			"申请停业: '服务中' -> '停业申请中'", PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING);
	
	public static final PartnerInstanceStateChangeEnum DECORATING_CLOSING = new PartnerInstanceStateChangeEnum(ChangeEnum.DECORATING_CLOSING,
			"申请停业: '装修中' -> '停业申请中'", PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.DECORATING);

	public static final PartnerInstanceStateChangeEnum CLOSING_REFUSED = new PartnerInstanceStateChangeEnum(ChangeEnum.CLOSING_REFUSED,
			"停业申请打回:'停业申请中' ->'服务中'", PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSING);

	public static final PartnerInstanceStateChangeEnum CLOSED = new PartnerInstanceStateChangeEnum(ChangeEnum.CLOSED,
			"停业 : '停业申请中' -> '已停业'", PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.CLOSING);

	public static final PartnerInstanceStateChangeEnum START_QUITTING = new PartnerInstanceStateChangeEnum(ChangeEnum.START_QUITTING,
			"申请退出 :'已停业' -> '退出申请中'", PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED);

	public static final PartnerInstanceStateChangeEnum QUITTING_REFUSED = new PartnerInstanceStateChangeEnum(ChangeEnum.QUITTING_REFUSED,
			"退出申请打回 : '退出申请中' -> '已停业'", PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING);

	public static final PartnerInstanceStateChangeEnum QUIT = new PartnerInstanceStateChangeEnum(ChangeEnum.QUIT, "已退出 :'退出申请中'->'已退出'",
			PartnerInstanceStateEnum.QUIT, PartnerInstanceStateEnum.QUITING);

	public static final PartnerInstanceStateChangeEnum CLOSE_TO_SERVICE = new PartnerInstanceStateChangeEnum(ChangeEnum.CLOSE_TO_SERVICE, "已停业恢复服务 :'已停业'->'服务中'",
			PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSED);
	
	private static final Map<ChangeEnum, PartnerInstanceStateChangeEnum> mappings = new HashMap<ChangeEnum, PartnerInstanceStateChangeEnum>();
	static {
		mappings.put(ChangeEnum.START_SETTLING, START_SETTLING);
		mappings.put(ChangeEnum.SETTLING_REFUSED, SETTLING_REFUSED);
		mappings.put(ChangeEnum.START_DECORATING, START_DECORATING);
		mappings.put(ChangeEnum.START_SERVICING, START_SERVICING);
		mappings.put(ChangeEnum.START_CLOSING, START_CLOSING);
		mappings.put(ChangeEnum.DECORATING_CLOSING, DECORATING_CLOSING);
		mappings.put(ChangeEnum.CLOSING_REFUSED, CLOSING_REFUSED);
		mappings.put(ChangeEnum.CLOSED, CLOSED);
		mappings.put(ChangeEnum.START_QUITTING, START_QUITTING);
		mappings.put(ChangeEnum.QUITTING_REFUSED, QUITTING_REFUSED);
		mappings.put(ChangeEnum.QUIT, QUIT);
		mappings.put(ChangeEnum.CLOSE_TO_SERVICE, CLOSE_TO_SERVICE);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PartnerInstanceStateEnum getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(PartnerInstanceStateEnum partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public PartnerInstanceStateEnum getPrePartnerInstanceState() {
		return prePartnerInstanceState;
	}

	public void setPrePartnerInstanceState(PartnerInstanceStateEnum prePartnerInstanceState) {
		this.prePartnerInstanceState = prePartnerInstanceState;
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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof PartnerInstanceStateChangeEnum)) {
            return false;
        }
		PartnerInstanceStateChangeEnum objType = (PartnerInstanceStateChangeEnum) obj;
		return objType.getType().equals(this.getType());
	}

	public PartnerInstanceStateChangeEnum(ChangeEnum type, String description, PartnerInstanceStateEnum partnerInstanceState,
			PartnerInstanceStateEnum prePartnerInstanceState) {
		this.type = type;
		this.description = description;
		this.partnerInstanceState = partnerInstanceState;
		this.prePartnerInstanceState = prePartnerInstanceState;
	}

	public static PartnerInstanceStateChangeEnum valueof(ChangeEnum type) {
		if (type == null) {
            return null;
        }
		return mappings.get(type);
	}

	public PartnerInstanceStateChangeEnum() {

	}
}
