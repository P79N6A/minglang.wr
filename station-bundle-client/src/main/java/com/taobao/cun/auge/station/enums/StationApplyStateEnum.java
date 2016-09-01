package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务点申请单状态枚举
 * 
 * @author wangbowangb
 * 
 */
public class StationApplyStateEnum implements Serializable {
	private static final long serialVersionUID = -1197977282708260567L;
		public static final StationApplyStateEnum TEMP = new StationApplyStateEnum(
				"TEMP", "暂存");
		public static final StationApplyStateEnum SUMITTED = new StationApplyStateEnum(
				"SUMITTED", "待签约人确认");
		public static final StationApplyStateEnum CONFIRMED = new StationApplyStateEnum(
				"CONFIRMED", "已确认处理中");
		public static final StationApplyStateEnum FROZEN = new StationApplyStateEnum(
				"FROZEN", "已冻结待处理");
		//当冻结资金完成之后，村点申请单状态变为装修中，当装修完成之后，需要小二点击开业，才进入服务中
		public static final StationApplyStateEnum  DECORATING = new StationApplyStateEnum("DECORATING", "装修中");
		
		public static final StationApplyStateEnum SERVICING = new StationApplyStateEnum(
				"SERVICING", "服务中");
		public static final StationApplyStateEnum CLOSED = new StationApplyStateEnum(
				"CLOSED", "关闭");
	    public static final StationApplyStateEnum CLOSED_WAIT_THAW = new StationApplyStateEnum("CLOSED_WAIT_THAW", "退出待解冻");
	    public static final StationApplyStateEnum CLOSED_THAWED = new StationApplyStateEnum("CLOSED_THAWED", "退出已解冻");
		public static final StationApplyStateEnum CANCEL = new StationApplyStateEnum("CANCEL", "做废");
		
		//代购员对服务中的村点申请单提交退出之后，变为停业申请中状态，当小二确认停业申请之后，变为停业中状态
		public static final StationApplyStateEnum QUIT_APPLYING = new StationApplyStateEnum("QUIT_APPLYING", "停业申请中");
		
		//小二对代购员的申请单已经确认，代购单的状态变为已停业，这时候uic标以及旺旺标，佣金结算等都会关闭
		public static final StationApplyStateEnum QUIT_APPLY_CONFIRMED = new StationApplyStateEnum("QUIT_APPLY_CONFIRMED", "已停业");
		
		//小二完成资产、贷款情况的确认，提交之后，变为退出呆审批状态
		public static final StationApplyStateEnum  QUITAUDITING = new StationApplyStateEnum("QUITAUDITING", "退出待审批");
		
		//当小二确认完资产，到退出待审批状态，在这个状态上会有区域经理和省长来进行相关的审批，当省长完成审批之后，变为已退出状态，表示该申请单已经完成。
		public static final StationApplyStateEnum  QUIT = new StationApplyStateEnum("QUIT", "已退出");
		
		//维修状态
		public static final StationApplyStateEnum UNPAY_DECORATE=new StationApplyStateEnum("UNPAY_DECORATE","待缴纳装修基金");
		public static final StationApplyStateEnum DEC_WAIT_AUDIT=new StationApplyStateEnum("DEC_WAIT_AUDIT","装修反馈待审核");
		
		//培训状态
		public static final StationApplyStateEnum UNPAY_COURSE=new StationApplyStateEnum("UNPAY_COURSE","待购买培训基金");
		public static final StationApplyStateEnum UNSIGNED=new StationApplyStateEnum("UNSIGNED","待培训签到");
		
		/**
		 * 淘帮手状态生命周期
		 */
		//淘帮手由合伙人新增TEMP状态
		public static final StationApplyStateEnum  TPA_TEMP = new StationApplyStateEnum("TPA_TEMP", "审批待新增");
		//淘帮手审核不通过
		public static final StationApplyStateEnum  TPA_AUDIT_FAIL = new StationApplyStateEnum("TPA_AUDIT_FAIL", "审核不通过");
		//淘帮手营业中
		public static final StationApplyStateEnum  TPA_SERVICING = new StationApplyStateEnum("TPA_SERVICING", "淘帮手营业中");

		public static final StationApplyStateEnum TO_AUDIT = new StationApplyStateEnum("TO_AUDIT", "待审批");
		
		public static final StationApplyStateEnum TO_LOGISTICS_AUDIT  = new StationApplyStateEnum("TO_LOGISTICS_AUDIT", "待物流审批");

	    public static final StationApplyStateEnum AUDIT_FAIL = new StationApplyStateEnum("AUDIT_FAIL", "审核不通过");
		
	private static final Map<String, StationApplyStateEnum> mappings = new HashMap<String, StationApplyStateEnum>();
	static {
		mappings.put("TEMP", TEMP);
		mappings.put("SUMITTED", SUMITTED);
		mappings.put("CONFIRMED", CONFIRMED);
		mappings.put("FROZEN", FROZEN);
		mappings.put("DECORATING", DECORATING);
		mappings.put("SERVICING", SERVICING);
		mappings.put("CLOSED", CLOSED);
		mappings.put("CLOSED_WAIT_THAW", CLOSED_WAIT_THAW);
		mappings.put("CLOSED_THAWED", CLOSED_THAWED);
		mappings.put("CANCEL", CANCEL);
		mappings.put("QUIT_APPLYING", QUIT_APPLYING);
		mappings.put("QUIT_APPLY_CONFIRMED", QUIT_APPLY_CONFIRMED);
		mappings.put("QUITAUDITING", QUITAUDITING);
		mappings.put("QUIT", QUIT);
		mappings.put("TPA_TEMP", TPA_TEMP);
		mappings.put("TPA_AUDIT_FAIL", TPA_AUDIT_FAIL);
		mappings.put("TPA_SERVICING", TPA_SERVICING);
		mappings.put("TO_AUDIT", TO_AUDIT);
		mappings.put("TO_LOGISTICS_AUDIT", TO_LOGISTICS_AUDIT);
		mappings.put("AUDIT_FAIL", AUDIT_FAIL);
		mappings.put("UNPAY_DECORATE", UNPAY_DECORATE);
		mappings.put("DEC_WAIT_AUDIT", DEC_WAIT_AUDIT);
		mappings.put("UNPAY_COURSE", UNPAY_COURSE);
		mappings.put("UNSIGNED", UNSIGNED);

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
		if (!(obj instanceof StationApplyStateEnum))
			return false;
		StationApplyStateEnum objType = (StationApplyStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationApplyStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationApplyStateEnum valueof(String code) {
		if (code==null)
			return null;
		return mappings.get(code);
	}
	/**
	 * 获取有效的村点申请单状态，
	 * @return
	 */
	public static String[]  getValidStatusArray(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(SUMITTED.getCode());
		listValidStatus.add(CONFIRMED.getCode());
		listValidStatus.add(DECORATING.getCode());
		listValidStatus.add(SERVICING.getCode());
		listValidStatus.add(QUIT_APPLYING.getCode());
		listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		listValidStatus.add(QUITAUDITING.getCode());	
		listValidStatus.add(TPA_SERVICING.getCode());
		listValidStatus.add(TO_AUDIT.getCode());
		listValidStatus.add(TO_LOGISTICS_AUDIT.getCode());
		return listValidStatus.toArray(new String[0]);
	}	
	
	/**
	 * 获取有效的淘帮手村点申请单状态，
	 * @return
	 */
	public static String[]  getValidTpaStatusArray(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(TEMP.getCode());
		listValidStatus.add(TPA_TEMP.getCode());
		listValidStatus.add(SUMITTED.getCode());
		listValidStatus.add(CONFIRMED.getCode());
		listValidStatus.add(TPA_SERVICING.getCode());
//		listValidStatus.add(CLOSED_WAIT_THAW.getCode() );
//		listValidStatus.add(CLOSED_THAWED.getCode() );
		listValidStatus.add(QUIT_APPLYING.getCode());
	    listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		listValidStatus.add(QUITAUDITING.getCode() );
		return listValidStatus.toArray(new String[0]);
	}	
	
	/**
	 * 获取有效的淘帮手村点申请单状态，合伙人停业使用
	 * @return
	 */
	public static String[]  getValidTpaStatusArrayForQuitConfirm(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(TEMP.getCode());
		listValidStatus.add(TPA_TEMP.getCode());
		listValidStatus.add(SUMITTED.getCode());
		listValidStatus.add(CONFIRMED.getCode());
		listValidStatus.add(TPA_SERVICING.getCode());
		listValidStatus.add(QUIT_APPLYING.getCode());
		//listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		//listValidStatus.add(QUITAUDITING.getCode() );
		return listValidStatus.toArray(new String[0]);
	}	
	
	/**
	 * 获取有效的村点申请单状态，
	 * @return
	 */
	public static List<StationApplyStateEnum>  getValidStatusList(){
		List<StationApplyStateEnum> listValidStatus = new ArrayList<StationApplyStateEnum>();
		listValidStatus.add(SUMITTED);
		listValidStatus.add(CONFIRMED);
		listValidStatus.add(DECORATING);
		listValidStatus.add(SERVICING);
		listValidStatus.add(QUIT_APPLYING);
		listValidStatus.add(QUIT_APPLY_CONFIRMED);
		listValidStatus.add(QUITAUDITING);	
		listValidStatus.add(TPA_SERVICING);
		return listValidStatus;
	}
	
	/**
	 * 获取村点开始服务之后的有效状态，
	 * @return
	 */
	public static List<StationApplyStateEnum>  getInServiceStatusList(){
		List<StationApplyStateEnum> listValidStatus = new ArrayList<StationApplyStateEnum>();
		listValidStatus.add(DECORATING);
		listValidStatus.add(SERVICING);
		listValidStatus.add(QUIT_APPLYING);
		listValidStatus.add(QUIT_APPLY_CONFIRMED);
		listValidStatus.add(QUITAUDITING);	
		listValidStatus.add(TPA_SERVICING);
		return listValidStatus;
	}	
	
	
	/**
	 * 获取村点开始服务之后的有效状态
	 * @return
	 */
	public static String[]  getInServiceStatusArray(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(DECORATING.getCode());
		listValidStatus.add(SERVICING.getCode());
		listValidStatus.add(QUIT_APPLYING.getCode());
		listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		listValidStatus.add(QUITAUDITING.getCode());	
		listValidStatus.add(TPA_SERVICING.getCode());
		return listValidStatus.toArray(new String[0]);
	}


	public static String[] getCheckStationNumArray(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(TEMP.getCode());
		listValidStatus.add(SUMITTED.getCode());
		listValidStatus.add(CONFIRMED.getCode());
		listValidStatus.add(DECORATING.getCode());
		listValidStatus.add(SERVICING.getCode());
		listValidStatus.add(CLOSED.getCode());
		listValidStatus.add(CANCEL.getCode());
		listValidStatus.add(CLOSED_THAWED.getCode());
		listValidStatus.add(QUIT_APPLYING.getCode());
		listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		listValidStatus.add(QUITAUDITING.getCode());
		listValidStatus.add(AUDIT_FAIL.getCode());
		listValidStatus.add(TO_AUDIT.getCode());
		listValidStatus.add(TO_LOGISTICS_AUDIT.getCode());

		return listValidStatus.toArray(new String[0]);
	}

	
	
	/**
	 * 获取所有的状态
	 * 
	 * @return
	 */
	public static String[]  getAllStatusArray(){
		ArrayList<String> listValidStatus = new ArrayList<String>();
		listValidStatus.add(TEMP.getCode());
		listValidStatus.add(SUMITTED.getCode());
		listValidStatus.add(CONFIRMED.getCode());
		listValidStatus.add(DECORATING.getCode());
		listValidStatus.add(SERVICING.getCode());
		listValidStatus.add(CLOSED.getCode());
		listValidStatus.add(CANCEL.getCode());
		listValidStatus.add(CLOSED_WAIT_THAW.getCode());
		listValidStatus.add(CLOSED_THAWED.getCode());
		listValidStatus.add(QUIT_APPLYING.getCode());
		listValidStatus.add(QUIT_APPLY_CONFIRMED.getCode());
		listValidStatus.add(QUITAUDITING.getCode());	
		listValidStatus.add(QUIT.getCode());
		
		return listValidStatus.toArray(new String[0]);
	}
	
	/**
	 * 获取所有的状态
	 * 
	 * @return
	 */
	public static List<StationApplyStateEnum>  getAllStatusList(){
		ArrayList<StationApplyStateEnum> listValidStatus = new ArrayList<StationApplyStateEnum>();
		listValidStatus.add(TEMP );
		listValidStatus.add(SUMITTED );
		listValidStatus.add(CONFIRMED );
		listValidStatus.add(DECORATING );
		listValidStatus.add(SERVICING );
		listValidStatus.add(CLOSED );
		listValidStatus.add(CANCEL );
		listValidStatus.add(CLOSED_WAIT_THAW );
		listValidStatus.add(CLOSED_THAWED );
		listValidStatus.add(QUIT_APPLYING );
		listValidStatus.add(QUIT_APPLY_CONFIRMED );
		listValidStatus.add(QUITAUDITING );	
		listValidStatus.add(QUIT );
		
		return listValidStatus;
	}

	public static List<StationApplyStateEnum>  getAllTaoHelperStatusList(){
		ArrayList<StationApplyStateEnum> listValidStatus = new ArrayList<StationApplyStateEnum>();
		listValidStatus.add(TEMP);
		listValidStatus.add(TPA_TEMP );
		listValidStatus.add(TPA_AUDIT_FAIL );
		listValidStatus.add(SUMITTED );
		listValidStatus.add(CONFIRMED );
		listValidStatus.add(TPA_SERVICING );

		listValidStatus.add(CLOSED );
		listValidStatus.add(CANCEL );
		listValidStatus.add(CLOSED_WAIT_THAW );
		listValidStatus.add(CLOSED_THAWED );
		listValidStatus.add(QUIT_APPLYING );
		listValidStatus.add(QUIT_APPLY_CONFIRMED );
		listValidStatus.add(QUITAUDITING );
		listValidStatus.add(QUIT );

		return listValidStatus;
	}
	

	@SuppressWarnings("unused")
	private StationApplyStateEnum() {

	}

}