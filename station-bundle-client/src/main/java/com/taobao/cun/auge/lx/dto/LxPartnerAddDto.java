package com.taobao.cun.auge.lx.dto;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 新增伙伴dto
 * @author quanzhu.wangqz
 *
 */
public class LxPartnerAddDto  extends OperatorDto{

	private static final long serialVersionUID = -5724166639845726392L;

	/**
     * 淘宝nick
     */
    private String taobaoNick;
	
	/**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 所属村小二淘宝userId
     */
    private Long  pTaobaoUserId;
    
	public Long getpTaobaoUserId() {
		return pTaobaoUserId;
	}

	public void setpTaobaoUserId(Long pTaobaoUserId) {
		this.pTaobaoUserId = pTaobaoUserId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
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
}
