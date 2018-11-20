package com.taobao.cun.auge.insurance.dto;

import java.io.Serializable;

/**
 * Created by xiao on 18/10/22.
 */
public class BusinessInfoDto implements Serializable {

    private static final long serialVersionUID = -3296803069491951653L;

    /**
     * 商家（企业）名称
     */
    private String name;

    /**
     * 商家（企业）类型,1代表个体工商户,2代表有限责任公司
     */
    private String type;

    /**
     * 商家（企业）联系地址
     */
    private String address;

    /**
     * 商家（企业）联系电话
     */
    private String mobile;

    /**
     * 商家（企业）证件类型,1代表营业执照
     */
    private String idType = "1";

    /**
     * 商家（企业）证件号
     */
    private String idenNum;

    /**
     * 法人身份证号
     */
    private String idenCard;

    /**
     * 商家（企业）法定代表人名称
     */
    private String legalPerson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIdenCard() {
        return idenCard;
    }

    public void setIdenCard(String idenCard) {
        this.idenCard = idenCard;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
}
