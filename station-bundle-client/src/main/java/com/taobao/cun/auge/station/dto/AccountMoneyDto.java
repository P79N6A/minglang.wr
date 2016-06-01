package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;

/**
 * 账号dto
 * @author quanzhu.wangqz
 *
 */
public class AccountMoneyDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 5957229362759722926L;


    /**
     * 淘宝userid
     */
    private String taobaoUserId;

    /**
     * 冻结时间
     */
    private Date frozenTime;

    /**
     * 解冻时间
     */
    private Date thawTime;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 类型
     */
    private AccountMoneyTypeEnum type;

    /**
     * 状态
     */
    private AccountMoneyStateEnum state;

    /**
     * 关联主键类型
     */
    private AccountMoneyTargetTypeEnum targetType;

    /**
     * 关联主键id
     */
    private Long objectId;

    /**
     * 支付宝账号
     */
    private String alipayAccount;


	public String getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(String taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Date getFrozenTime() {
		return frozenTime;
	}

	public void setFrozenTime(Date frozenTime) {
		this.frozenTime = frozenTime;
	}

	public Date getThawTime() {
		return thawTime;
	}

	public void setThawTime(Date thawTime) {
		this.thawTime = thawTime;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public AccountMoneyTypeEnum getType() {
		return type;
	}

	public void setType(AccountMoneyTypeEnum type) {
		this.type = type;
	}

	public AccountMoneyStateEnum getState() {
		return state;
	}

	public void setState(AccountMoneyStateEnum state) {
		this.state = state;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public AccountMoneyTargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(AccountMoneyTargetTypeEnum targetType) {
		this.targetType = targetType;
	}
	
}
