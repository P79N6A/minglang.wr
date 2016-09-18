package com.taobao.cun.auge.fuwu.dto;

import java.io.Serializable;

public class FuwuProductDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private String                  mkey;
	    private String                  key;
	    private String                  groupKey;
	    private String                  name;
	    private String                  icon;
	    private String                  iconMiddle;
	    private String                  iconSmall;
	    private String                  iconLarge;
	    private String                  agreement;
	    private String                  agreementMobile;
		public String getMkey() {
			return mkey;
		}
		public void setMkey(String mkey) {
			this.mkey = mkey;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getGroupKey() {
			return groupKey;
		}
		public void setGroupKey(String groupKey) {
			this.groupKey = groupKey;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getIconMiddle() {
			return iconMiddle;
		}
		public void setIconMiddle(String iconMiddle) {
			this.iconMiddle = iconMiddle;
		}
		public String getIconSmall() {
			return iconSmall;
		}
		public void setIconSmall(String iconSmall) {
			this.iconSmall = iconSmall;
		}
		public String getIconLarge() {
			return iconLarge;
		}
		public void setIconLarge(String iconLarge) {
			this.iconLarge = iconLarge;
		}
		public String getAgreement() {
			return agreement;
		}
		public void setAgreement(String agreement) {
			this.agreement = agreement;
		}
		public String getAgreementMobile() {
			return agreementMobile;
		}
		public void setAgreementMobile(String agreementMobile) {
			this.agreementMobile = agreementMobile;
		}
	    
}
