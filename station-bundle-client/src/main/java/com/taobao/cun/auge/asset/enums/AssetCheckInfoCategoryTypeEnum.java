package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetCheckInfoCategoryTypeEnum implements Serializable {

	private static final Map<String, AssetCheckInfoCategoryTypeEnum> MAPPINGS = new HashMap<String, AssetCheckInfoCategoryTypeEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;


	/**
	 * 行政资产
	 */
	public static final AssetCheckInfoCategoryTypeEnum SHAFA = new AssetCheckInfoCategoryTypeEnum("SHAFA", "沙发");
	public static final AssetCheckInfoCategoryTypeEnum KONGTIAO = new AssetCheckInfoCategoryTypeEnum("KONGTIAO", "空调");
	public static final AssetCheckInfoCategoryTypeEnum ZUHEYINXIANG = new AssetCheckInfoCategoryTypeEnum("ZUHEYINXIANG", "组合音响");
	public static final AssetCheckInfoCategoryTypeEnum WEIBOLU = new AssetCheckInfoCategoryTypeEnum("WEIBOLU", "微波炉");
	public static final AssetCheckInfoCategoryTypeEnum BINGXIANG = new AssetCheckInfoCategoryTypeEnum("BINGXIANG", "冰箱");
	public static final AssetCheckInfoCategoryTypeEnum XUNLUOJI = new AssetCheckInfoCategoryTypeEnum("XUNLUOJI", "巡逻机");
	public static final AssetCheckInfoCategoryTypeEnum YINXIANG = new AssetCheckInfoCategoryTypeEnum("YINXIANG", "音响");
	
	/**
	 * it资产
	 */
	public static final AssetCheckInfoCategoryTypeEnum PRINTER = new AssetCheckInfoCategoryTypeEnum("PRINTER", "打印机");
	public static final AssetCheckInfoCategoryTypeEnum TV = new AssetCheckInfoCategoryTypeEnum("TV", "电视机");
	public static final AssetCheckInfoCategoryTypeEnum PROJECTOR = new AssetCheckInfoCategoryTypeEnum("PROJECTOR", "投影仪");
	public static final AssetCheckInfoCategoryTypeEnum DISPLAY = new AssetCheckInfoCategoryTypeEnum("DISPLAY", "显示器");
	public static final AssetCheckInfoCategoryTypeEnum AIO = new AssetCheckInfoCategoryTypeEnum("AIO", "一体机");
	public static final AssetCheckInfoCategoryTypeEnum MAIN = new AssetCheckInfoCategoryTypeEnum("MAIN", "主机");


	

	static {
		MAPPINGS.put("SHAFA", SHAFA);
		MAPPINGS.put("KONGTIAO", KONGTIAO);
		MAPPINGS.put("ZUHEYINXIANG", ZUHEYINXIANG);
		MAPPINGS.put("WEIBOLU", WEIBOLU);
		MAPPINGS.put("BINGXIANG", BINGXIANG);
		MAPPINGS.put("XUNLUOJI", XUNLUOJI);
		MAPPINGS.put("YINXIANG", YINXIANG);
		
		MAPPINGS.put("PRINTER", PRINTER);
		MAPPINGS.put("TV", TV);
		MAPPINGS.put("PROJECTOR", PROJECTOR);
		MAPPINGS.put("DISPLAY", DISPLAY);
		MAPPINGS.put("AIO", AIO);
		MAPPINGS.put("MAIN", MAIN);
	}
	
	public static String getAssetType(String category){
		List<String> itList = new ArrayList<String>();
		itList.add(AssetCheckInfoCategoryTypeEnum.SHAFA.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.KONGTIAO.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.ZUHEYINXIANG.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.WEIBOLU.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.BINGXIANG.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.XUNLUOJI.getCode());
		itList.add(AssetCheckInfoCategoryTypeEnum.YINXIANG.getCode());
		
		List<String> adminList = new ArrayList<String>();
		adminList.add(AssetCheckInfoCategoryTypeEnum.PRINTER.getCode());
		adminList.add(AssetCheckInfoCategoryTypeEnum.TV.getCode());
		adminList.add(AssetCheckInfoCategoryTypeEnum.PROJECTOR.getCode());
		adminList.add(AssetCheckInfoCategoryTypeEnum.DISPLAY.getCode());
		adminList.add(AssetCheckInfoCategoryTypeEnum.AIO.getCode());
		adminList.add(AssetCheckInfoCategoryTypeEnum.MAIN.getCode());
		if (itList.contains(category)) {
			return 
		}
	}
	
	public AssetCheckInfoCategoryTypeEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckInfoCategoryTypeEnum() {

	    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static AssetCheckInfoCategoryTypeEnum valueof(String code) {
		if (code == null) {
			return null;
		}
		return MAPPINGS.get(code);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AssetCheckInfoCategoryTypeEnum other = (AssetCheckInfoCategoryTypeEnum) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}
}
