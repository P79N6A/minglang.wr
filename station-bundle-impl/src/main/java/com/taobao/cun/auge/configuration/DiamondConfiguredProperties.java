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

    @Value("${refactor.sync.station.apply.key}")
    private String isSync;

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

    @Value("#{'${insurance.cp.codes}'.split(',')}")
    private List<String> insureCpCodes;

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


    @Value("${com.taobao.cun.auge.store.mainImage}")
    private String storeMainImage;

    @Value("${com.taobao.cun.auge.store.subImage}")
    private String storeSubImage;

    @Value("${com.taobao.cun.auge.store.minApp.bizCode}")
    private String minAppBizCode;

    @Value("${com.taobao.cun.auge.store.minApp.icon.prefix}")
    private String minAppIconPreFix;

    @Value("${com.taobao.cun.auge.store.minApp.templateid}")
    private String minAppTemplateId;

    @Value("${com.taobao.cun.auge.store.minApp.version}")
    private String minAppVersion;

    @Value("${com.taobao.cun.auge.store.minApp.getlastversion}")
    private String minAppGetLastVersion;

    @Value("${checkOrderFinish}")
    private boolean checkOrderFinish;

    @Value("${checkStoreStock}")
    private boolean checkStoreStock;

    @Value("${decorateCountyAuditActivityId}")
    private String decorateCountyAuditActivityId;

    @Value("${decorate.design.county.audit.activity.id}")
    private String decorateDesignCountyAuditActivityId;

    @Value("${decorate.feedback.route.url}")
    private String decorateFeedbackRouteUrl;

    public String getMinAppGetLastVersion() {
        return minAppGetLastVersion;
    }

    public void setMinAppGetLastVersion(String minAppGetLastVersion) {
        this.minAppGetLastVersion = minAppGetLastVersion;
    }

    public String getMinAppTemplateId() {
        return minAppTemplateId;
    }

    public void setMinAppTemplateId(String minAppTemplateId) {
        this.minAppTemplateId = minAppTemplateId;
    }

    public String getMinAppVersion() {
        return minAppVersion;
    }

    public void setMinAppVersion(String minAppVersion) {
        this.minAppVersion = minAppVersion;
    }

    public String getMinAppBizCode() {
        return minAppBizCode;
    }

    public void setMinAppBizCode(String minAppBizCode) {
        this.minAppBizCode = minAppBizCode;
    }

    public String getMinAppIconPreFix() {
        return minAppIconPreFix;
    }

    public void setMinAppIconPreFix(String minAppIconPreFix) {
        this.minAppIconPreFix = minAppIconPreFix;
    }

    public String getDecorateFeedbackRouteUrl() {
        return decorateFeedbackRouteUrl;
    }

    public void setDecorateFeedbackRouteUrl(String decorateFeedbackRouteUrl) {
        this.decorateFeedbackRouteUrl = decorateFeedbackRouteUrl;
    }

    //供应商Id,村淘自营门店对应uic的userId为3405569954
    @Value("${supplierTbId}")
    private Long supplierTbId;

    @Value("${serviceAbilitySHRHCountyAuditActivityId}")
    private String serviceAbilitySHRHCountyAuditActivityId;


    @Value("${cainiao.switch}")
    private String cainiaoSwitch;

    @Value("${station.new.customer.rate.time}")
    private Integer stationNewCustomerRateTime;

    @Value("${batch.close.or.quit.um.max.num}")
    private Integer batchCloseOrQuitUmNum;

    @Value("${com.taobao.cun.auge.createSellerBizScene}")
    private String createSellerBizScene;

    public String getCreateSellerBizScene() {
        return createSellerBizScene;
    }

    public void setCreateSellerBizScene(String createSellerBizScene) {
        this.createSellerBizScene = createSellerBizScene;
    }

    public String getStoreMainImage() {
        return storeMainImage;
    }

    public void setStoreMainImage(String storeMainImage) {
        this.storeMainImage = storeMainImage;
    }

    public String getStoreSubImage() {
        return storeSubImage;
    }

    public void setStoreSubImage(String storeSubImage) {
        this.storeSubImage = storeSubImage;
    }

    public String getCainiaoSwitch() {
		return cainiaoSwitch;
	}

	public void setCainiaoSwitch(String cainiaoSwitch) {
		this.cainiaoSwitch = cainiaoSwitch;
	}

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

    public String getDecorateDesignCountyAuditActivityId() {
        return decorateDesignCountyAuditActivityId;
    }

    public void setDecorateDesignCountyAuditActivityId(String decorateDesignCountyAuditActivityId) {
        this.decorateDesignCountyAuditActivityId = decorateDesignCountyAuditActivityId;
    }

    public String getStoreImagePerfix() {
		return storeImagePerfix;
	}

	public void setStoreImagePerfix(String storeImagePerfix) {
		this.storeImagePerfix = storeImagePerfix;
	}

	public boolean isCheckOrderFinish() {
		return checkOrderFinish;
	}

	public void setCheckOrderFinish(boolean checkOrderFinish) {
		this.checkOrderFinish = checkOrderFinish;
	}
    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

	public boolean isCheckStoreStock() {
		return checkStoreStock;
	}

	public void setCheckStoreStock(boolean checkStoreStock) {
		this.checkStoreStock = checkStoreStock;
	}

	public String getDecorateCountyAuditActivityId() {
		return decorateCountyAuditActivityId;
	}

	public void setDecorateCountyAuditActivityId(String decorateCountyAuditActivityId) {
		this.decorateCountyAuditActivityId = decorateCountyAuditActivityId;
	}

	public Long getSupplierTbId() {
		return supplierTbId;
	}

	public void setSupplierTbId(Long supplierTbId) {
		this.supplierTbId = supplierTbId;
	}

	public String getServiceAbilitySHRHCountyAuditActivityId() {
		return serviceAbilitySHRHCountyAuditActivityId;
	}

	public void setServiceAbilitySHRHCountyAuditActivityId(String serviceAbilitySHRHCountyAuditActivityId) {
		this.serviceAbilitySHRHCountyAuditActivityId = serviceAbilitySHRHCountyAuditActivityId;
	}

    public List<String> getInsureCpCodes() {
        return insureCpCodes;
    }

    public Integer getStationNewCustomerRateTime() {
        return stationNewCustomerRateTime;
    }

    public void setStationNewCustomerRateTime(Integer stationNewCustomerRateTime) {
        this.stationNewCustomerRateTime = stationNewCustomerRateTime;
    }

    public Integer getBatchCloseOrQuitUmNum() {
        return batchCloseOrQuitUmNum;
    }

    public void setBatchCloseOrQuitUmNum(Integer batchCloseOrQuitUmNum) {
        this.batchCloseOrQuitUmNum = batchCloseOrQuitUmNum;
    }
}
