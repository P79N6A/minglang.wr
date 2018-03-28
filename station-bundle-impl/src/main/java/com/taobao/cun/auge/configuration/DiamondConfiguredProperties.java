package com.taobao.cun.auge.configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 16/11/30.
 */
@Component
@RefreshScope
public class DiamondConfiguredProperties {

    @Value("${wisdom.audit.apply}")
    private String apply;

    @Value("${wisdom.audit.pass}")
    private String pass;

    @Value("${wisdom.audit.fail}")
    private String fail;

    @Value("${wisdom.audit.mobile}")
    private String mobile;

    @Value("${train.supplier}")
    private String supplier;

    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${train.purchase}')}")
    private Map<String, String> purchaseMap;

    @Value("${asset.assetOrg}")
    private String assetOrg;
    
    @Value("${asset.assetUse}")
    private String assetUse;
    
    @Value("${asset.acceptanceStandard}")
    private String acceptanceStandard;

    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${asset.category}')}")
    private Map<String, String> categoryMap;

    @Value("#{'${asset.can.buy.station}'.split(',')}")
    private List<Long> canBuyStationList;

    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${asset.value.station}')}")
    private Map<Long, Long> stationValueMap;

    @Value("${inSure.switch}")
    private String inSureSwitch;
    
    @Value("#{'${com.taobao.cun.admin.alipay.whitelist}'.split(',')}")
    private List<Long> insureWhiteListConfig;
    
    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${train.purchase.province}')}")
    private Map<String, String> purchaseProvinceMap;

    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${asset.transfer.error}')}")
    private Map<String, String> assetTransferErrorMap;
    
    @Value("${store.categoryId}")
    private Integer storeCategoryId;
    
    @Value("${store.storeMainUserId}")
    private Long storeMainUserId;

    @Value("${store.cuntaoStoreTag}")
    private Integer storeTag;
    
    @Value("${paymentSignReturnUrl}")
    private String paymentSignReturnUrl;
    
    
    @Value("${alipay.provideHostName:none}")
    private String alipayProvideHostName;
    
    @Value("${isCheckVendorAlipayAccount}")
    private boolean isCheckVendorAlipayAccount;
    
    @Value("${replenishOrderUrl}")
    private String replenishOrderUrl;
    
    @Value("${replenishFrozenUrl}")
    private String replenishFrozenUrl;
    
    @Value("#{'${ignoreSupplyStoreTownList.whitelist}'.split(',')}")
    private List<Long> ignoreSupplyStoreTownList;
    
    @Value("#{ T(com.alibaba.fastjson.JSON).parseObject('${station.name.map}')}")
    private Map<String, String> stationNameMap;
    
    @Value("#{'${stationNameSuffix}'.split(',')}")
    private List<String> stationNameSuffix;
    
    @Value("#{'${can.confirm.stationOpeningProtocolList}'.split(',')}")
    private List<Long> canConfirmStationOpeningProtocolList;
    
    @Value("${storeImagePerfix}")
    private String storeImagePerfix;
    
    
    public String getReplenishFrozenUrl() {
		return replenishFrozenUrl;
	}

	public void setReplenishFrozenUrl(String replenishFrozenUrl) {
		this.replenishFrozenUrl = replenishFrozenUrl;
	}

	public String getReplenishOrderUrl() {
		return replenishOrderUrl;
	}

	public void setReplenishOrderUrl(String replenishOrderUrl) {
		this.replenishOrderUrl = replenishOrderUrl;
	}
    public String getApply() {
        return apply;
    }

    public String getPass() {
        return pass;
    }

    public String getFail() {
        return fail;
    }

    public String getMobile() {
        return mobile;
    }

    public Map<String, String> getSupplierMap() {
        return JSON.parseObject(supplier, new TypeReference<LinkedHashMap<String, String>>(){});
    }

    public Map<String, String> getPurchaseMap() {
        return purchaseMap;
    }

	public String getAssetOrg() {
		return assetOrg;
	}

	public String getAssetUse() {
		return assetUse;
	}

	public String getAcceptanceStandard() {
		return acceptanceStandard;
	}

    public Map<String, String> getCategoryMap() {
        return categoryMap;
    }

    public List<Long> getCanBuyStationList() {
        return canBuyStationList;
    }

    public String getInSureSwitch() {
        return inSureSwitch;
    }

    public List<Long> getInsureWhiteListConfig() {
        return insureWhiteListConfig;
    }
    
    public Map<String, String> getPurchaseProvinceMap() {
        return purchaseProvinceMap;
    }
    
    public Map<Long, Long> getStationValueMap() {
        return stationValueMap;
    }

    public Map<String, String> getAssetTransferErrorMap() {
        return assetTransferErrorMap;
    }

	public Integer getStoreCategoryId() {
		return storeCategoryId;
	}

	public void setStoreCategoryId(Integer storeCategoryId) {
		this.storeCategoryId = storeCategoryId;
	}

	public Long getStoreMainUserId() {
		return storeMainUserId;
	}

	public void setStoreMainUserId(Long storeMainUserId) {
		this.storeMainUserId = storeMainUserId;
	}

	public Integer getStoreTag() {
		return storeTag;
	}

	public void setStoreTag(Integer storeTag) {
		this.storeTag = storeTag;
	}

	public String getPaymentSignReturnUrl() {
		return paymentSignReturnUrl;
	}

	public void setPaymentSignReturnUrl(String paymentSignReturnUrl) {
		this.paymentSignReturnUrl = paymentSignReturnUrl;
	}

	public String getAlipayProvideHostName() {
		return alipayProvideHostName;
	}

	public void setAlipayProvideHostName(String alipayProvideHostName) {
		this.alipayProvideHostName = alipayProvideHostName;
	}

	public boolean isCheckVendorAlipayAccount() {
		return isCheckVendorAlipayAccount;
	}

	public void setCheckVendorAlipayAccount(boolean isCheckVendorAlipayAccount) {
		this.isCheckVendorAlipayAccount = isCheckVendorAlipayAccount;
	}

	public List<Long> getIgnoreSupplyStoreTownList() {
		return ignoreSupplyStoreTownList;
	}

	public void setIgnoreSupplyStoreTownList(List<Long> ignoreSupplyStoreTownList) {
		this.ignoreSupplyStoreTownList = ignoreSupplyStoreTownList;
	}

    public Map<String, String> getStationNameMap() {
        return stationNameMap;
    }

    public void setStationNameMap(Map<String, String> stationNameMap) {
        this.stationNameMap = stationNameMap;
    }

    public List<String> getStationNameSuffix() {
        return stationNameSuffix;
    }

    public void setStationNameSuffix(List<String> stationNameSuffix) {
        this.stationNameSuffix = stationNameSuffix;
    }

	public List<Long> getCanConfirmStationOpeningProtocolList() {
		return canConfirmStationOpeningProtocolList;
	}

	public void setCanConfirmStationOpeningProtocolList(List<Long> canConfirmStationOpeningProtocolList) {
		this.canConfirmStationOpeningProtocolList = canConfirmStationOpeningProtocolList;
	}

	public String getStoreImagePerfix() {
		return storeImagePerfix;
	}

	public void setStoreImagePerfix(String storeImagePerfix) {
		this.storeImagePerfix = storeImagePerfix;
	}
}
