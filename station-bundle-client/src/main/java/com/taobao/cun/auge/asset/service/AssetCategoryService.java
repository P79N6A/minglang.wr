package com.taobao.cun.auge.asset.service;

import java.util.List;


public interface AssetCategoryService {

	public List<CuntaoAssetCategoryDto> getAllList();
	
	public void updateCategory(CuntaoAssetCategoryDto cuntaoAssetCategoryDto,String operator);
	
	public void addCategory(CuntaoAssetCategoryDto cuntaoAssetCategoryDto,String operator);
	
	public void deleteCategory(Long categoryId,String operator);
	
	public Integer getCountByName(String name);
}
