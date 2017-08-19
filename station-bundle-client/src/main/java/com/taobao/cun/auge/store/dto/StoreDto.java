package com.taobao.cun.auge.store.dto;

import java.io.Serializable;

public class StoreDto implements Serializable{

	private static final long serialVersionUID = 3116443473224484893L;

	private Long id;
	/**
	 * 门店名称
	 */
	private String name;
	/**
	 * 共享门店ID
	 */
	private Long shareStoreId;
}
