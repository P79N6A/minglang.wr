package com.taobao.cun.auge.lx.dto;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

/**
 * 伙伴dto
 * @author quanzhu.wangqz
 *
 */
public class LxPartnerDto implements Serializable{
	
	private static final long serialVersionUID = 3886628346836925075L;

	/**
     * 淘宝nick
     */
    private String taobaoNick;
    
    /**
     * userid
     */
    private Long taobaoUserId;
	
	/**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 编号
     */
    private String num;
    
    /**
     * 状态
     */
    private PartnerInstanceStateEnum stateEnum;

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public PartnerInstanceStateEnum getStateEnum() {
		return stateEnum;
	}

	public void setStateEnum(PartnerInstanceStateEnum stateEnum) {
		this.stateEnum = stateEnum;
	}
    
    
}
