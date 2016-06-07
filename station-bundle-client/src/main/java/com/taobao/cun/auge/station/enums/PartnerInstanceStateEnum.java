package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合伙人实例表 状态枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceStateEnum  implements Serializable {

	private static final long serialVersionUID = -2698634278143088180L;
	
	// 暂存
	public static final PartnerInstanceStateEnum TEMP = new PartnerInstanceStateEnum("TEMP", "暂存");
	// 入驻中
	public static final PartnerInstanceStateEnum SETTLING  = new PartnerInstanceStateEnum("SETTLING", "入驻中");
	// 入驻失败
	public static final PartnerInstanceStateEnum SETTLE_FAIL  = new PartnerInstanceStateEnum("SETTLE_FAIL", "入驻失败");
	// 装修中
	public static final PartnerInstanceStateEnum DECORATING = new PartnerInstanceStateEnum("DECORATING", "装修中");
	// 服务中
	public static final PartnerInstanceStateEnum SERVICING = new PartnerInstanceStateEnum("SERVICING", "服务中");
	// 停业申请中
	public static final PartnerInstanceStateEnum CLOSING = new PartnerInstanceStateEnum("CLOSING", "停业申请中");
	// 已停业
	public static final PartnerInstanceStateEnum CLOSED = new PartnerInstanceStateEnum("CLOSED", "已停业");
	// 退出申请中
	public static final PartnerInstanceStateEnum QUITING = new PartnerInstanceStateEnum("QUITING", "退出申请中");
	// 已退出
	public static final PartnerInstanceStateEnum QUIT = new PartnerInstanceStateEnum("QUIT", "已退出");
	

	private static final Map<String, PartnerInstanceStateEnum> mappings = new HashMap<String, PartnerInstanceStateEnum>();
	static {
		mappings.put("TEMP", TEMP);
		mappings.put("SETTLE_FAIL", SETTLE_FAIL);
		mappings.put("SETTLING", SETTLING);
		mappings.put("DECORATING", DECORATING);
		mappings.put("SERVICING", SERVICING);
		mappings.put("CLOSING", CLOSING);
		mappings.put("CLOSED", CLOSED);
		mappings.put("QUITING", QUITING);
		mappings.put("QUIT", QUIT);
	}

	private String code;
	private String desc;

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
		if (obj == null)
			return false;
		if (!(obj instanceof PartnerInstanceStateEnum))
			return false;
		PartnerInstanceStateEnum objType = (PartnerInstanceStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerInstanceStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerInstanceStateEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}
	
	/**
	 * 有效的下一级合伙人状态
	 * 
	 * @return
	 */
	public static List<PartnerInstanceStateEnum>  getValidChildPartnersStatus(){
		ArrayList<PartnerInstanceStateEnum > listValidStatus = new ArrayList<PartnerInstanceStateEnum >();
		listValidStatus.add(PartnerInstanceStateEnum.TEMP);
		listValidStatus.add(PartnerInstanceStateEnum.SETTLING);
		listValidStatus.add(PartnerInstanceStateEnum.SERVICING);
		listValidStatus.add(PartnerInstanceStateEnum.CLOSING);
		return listValidStatus;
	}
	
	/**
	 *不能再次入驻的 实例状态
	 * quiting中，待保证金冻结的子状态除外
	 * 
	 * @return
	 */
	public static List<String>  unReSettlableStatusCodeList(){
		List<String > listValidStatus = new ArrayList<String >();
		listValidStatus.add(PartnerInstanceStateEnum.SETTLING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.DECORATING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.SERVICING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSED.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.QUITING.getCode());
		return listValidStatus;
	}
	
	/**
	 * 可以降级的合伙人状态
	 * @return
	 */
	public static List<String> canDegradeStateCodeList() {
		List<String > listValidStatus = new ArrayList<String >();
		listValidStatus.add(PartnerInstanceStateEnum.DECORATING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.SERVICING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSED.getCode());
		return listValidStatus;
	}
	
	/**
	 * 统计合伙人管理几个淘帮手时，使用， quiting中，待保证金冻结的子状态除外
	 * @return
	 */
	public static List<String>  getValidTpaStatusArray(){
		List<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(PartnerInstanceStateEnum.TEMP.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.SETTLING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.SERVICING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSING.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.CLOSED.getCode());
		listValidStatus.add(PartnerInstanceStateEnum.QUITING.getCode());
		return listValidStatus;
	}	
	
	
	

	@SuppressWarnings("unused")
	private PartnerInstanceStateEnum() {

	}

}
