package com.taobao.cun.auge.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
}
