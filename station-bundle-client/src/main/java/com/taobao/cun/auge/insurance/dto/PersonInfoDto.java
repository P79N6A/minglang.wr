package com.taobao.cun.auge.insurance.dto;

import java.io.Serializable;

/**
 * Created by xiao on 18/10/22.
 */
public class PersonInfoDto implements Serializable {

    private static final long serialVersionUID = -3540587239300511424L;

    /**
     * 账户持有人名称类型,1代表真实名称
     */
    private String nameType = "1";

    /**
     * 账户持有人证件名称
     */
    private String name;

    /**
     * 账户持有人联系地址
     */
    private String address;

    /**
     * 账户持有人联系电话
     */
    private String mobile;

    /**
     * 账户持有人证件类型,100代表身份证
     */
    private String idType = "100";

    /**
     * 账户持有人证件号(身份证号)
     */
    private String idenNum;

    /**
     * 账户持有人支付宝帐号
     */
    private String aplipayAccount;

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdenNum() {
        return idenNum;
    }

    public void setIdenNum(String idenNum) {
        this.idenNum = idenNum;
    }

    public String getAplipayAccount() {
        return aplipayAccount;
    }

    public void setAplipayAccount(String aplipayAccount) {
        this.aplipayAccount = aplipayAccount;
    }
}
