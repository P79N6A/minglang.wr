package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.asset.enums.AssetCheckStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetDetailDto implements Serializable{

    private static final long serialVersionUID = 7100435184292730173L;
    /**
     * 资产主键id
     */
    private Long id;
    /**
     * 大阿里编号
     */
    private String aliNo;
    /**
     * 序列号
     */
    private String serialNo;
    
    /**
     * po编号
     */
    private String poNo;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 资产类型
     */
    private String category;
    /**
     * 资产类型名称
     */
    private String categoryName;
    /**
     * 用户区域名称
     */
    private String useArea;
    /**
     * 用户名字
     */
    private String userName;
    /**
     * 责任人区域名称
     */
    private String ownerArea;
    /**
     * 责任人名字
     */
    private String owner;
    /**
     * 资产状态
     */
    private AssetStatusEnum status;
    /**
     * 盘点状态
     */
    private AssetCheckStatusEnum checkStatus;
    /**
     *  回收标示
     */
    private String recycle;

    /**
     * 赔付时使用
     */
    private String payment;
    /**
     * 责任人组织id
     */
    private Long ownerOrgId;
    /**
     * 责任人工号
     */
    private String ownerWorkno;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户区域id
     */
    private Long useAreaId;
    /**
     * 区域类型
     */
    private AssetUseAreaTypeEnum areaType;
    /**
     * 盘点时间
     */
    private Date checkTime;
    
    public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOwnerArea() {
        return ownerArea;
    }

    public void setOwnerArea(String ownerArea) {
        this.ownerArea = ownerArea;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public AssetStatusEnum getStatus() {
        return status;
    }

    public void setStatus(AssetStatusEnum status) {
        this.status = status;
    }

    
    public AssetCheckStatusEnum getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(AssetCheckStatusEnum checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getRecycle() {
        return recycle;
    }

    public void setRecycle(String recycle) {
        this.recycle = recycle;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

	public Long getOwnerOrgId() {
		return ownerOrgId;
	}

	public void setOwnerOrgId(Long ownerOrgId) {
		this.ownerOrgId = ownerOrgId;
	}

	public String getOwnerWorkno() {
		return ownerWorkno;
	}

	public void setOwnerWorkno(String ownerWorkno) {
		this.ownerWorkno = ownerWorkno;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getUseAreaId() {
		return useAreaId;
	}

	public void setUseAreaId(Long useAreaId) {
		this.useAreaId = useAreaId;
	}

	public AssetUseAreaTypeEnum getAreaType() {
		return areaType;
	}

	public void setAreaType(AssetUseAreaTypeEnum areaType) {
		this.areaType = areaType;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
}
