package com.taobao.cun.auge.asset.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.service.CuntaoAssetCategoryDto.Sku;
import com.taobao.cun.auge.dal.domain.CuntaoAssetCategory;
import com.taobao.cun.auge.dal.domain.CuntaoAssetCategoryExample;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetCategoryMapper;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("assetCategoryService")
@HSFProvider(serviceInterface = AssetCategoryService.class)
public class AssetCategoryServiceImpl implements AssetCategoryService{

	@Autowired
	private CuntaoAssetCategoryMapper cuntaoAssetCategoryMapper;
	
	private static final String SKU_SEPARATOR= "#";

	private CuntaoAssetCategoryDto convertToDto(CuntaoAssetCategory cuntaoAssetCategory) {
		CuntaoAssetCategoryDto dto = new CuntaoAssetCategoryDto();
		if (cuntaoAssetCategory != null) {
			dto.setCategoryId(cuntaoAssetCategory.getId());
			dto.setName(cuntaoAssetCategory.getName());
			dto.setRemark(cuntaoAssetCategory.getRemark());
			dto.setType(cuntaoAssetCategory.getType());
			if (cuntaoAssetCategory.getSku() !=null) {
				try {
					JSONArray jsonArr = JSON.parseArray(cuntaoAssetCategory.getSku());
					List<Sku> skulist = new ArrayList<Sku>();
					for(int i = 0;i < jsonArr.size();i++) {
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						Sku sku = dto.new Sku();
						sku.setId(jsonObj.getLong("id"));
						sku.setName(jsonObj.getString("name"));
						skulist.add(sku);
					}
					dto.setSkuObjects(skulist);
				} catch(Exception e) {
					String[] skuArray = cuntaoAssetCategory.getSku().split(SKU_SEPARATOR);
					dto.setSku(Arrays.asList(skuArray));
				}
			}
			dto.setPoNo(cuntaoAssetCategory.getPoNo());
		}
		return dto;
	}

	private CuntaoAssetCategory convertToDO(CuntaoAssetCategoryDto dto) {
		CuntaoAssetCategory cuntaoAssetCategory = new CuntaoAssetCategory();
		if (dto != null) {
			cuntaoAssetCategory.setId(dto.getCategoryId());
			if(StringUtils.isNotEmpty(dto.getName())) {
				cuntaoAssetCategory.setName(dto.getName());
			}
			if(StringUtils.isNotEmpty(dto.getRemark())) {
				cuntaoAssetCategory.setRemark(dto.getRemark());
			}
			if(StringUtils.isNotEmpty(dto.getType())) {
				cuntaoAssetCategory.setType(dto.getType());
			}
			if(dto.getSkuObjects() == null || dto.getSkuObjects().size() == 0) {
				if(dto.getSku() != null && dto.getSku().size() > 0) {
					cuntaoAssetCategory.setSku(ListToString(dto.getSku()));
				}
			} else {
				cuntaoAssetCategory.setSku(listToString(dto.getSkuObjects()));
			}
			cuntaoAssetCategory.setPoNo(dto.getPoNo());
		}
		return cuntaoAssetCategory;
	}

	public String listToString(List<Sku> skuList) {
		return JSON.toJSONString(skuList);
	}
	
	public static String ListToString(List<?> list) {
		StringBuffer sb = new StringBuffer();
		String rs = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null || "".equals(String.valueOf(list.get(i)))) {
					continue;
				}
				sb.append(list.get(i));
				sb.append(SKU_SEPARATOR);
			}
			rs = sb.toString();
			rs = rs.substring(0, rs.length() - 1);
		}
		return rs;
	} 
	
	@Override
	public List<CuntaoAssetCategoryDto> getAllList() {
		CuntaoAssetCategoryExample example = new CuntaoAssetCategoryExample();
		example.createCriteria().andIsDeletedEqualTo("n");
		example.setOrderByClause("gmt_create desc");
		List<CuntaoAssetCategory> cates = cuntaoAssetCategoryMapper.selectByExample(example);
		return cates.stream().map(cate -> this.convertToDto(cate)).collect(Collectors.toList());
	}

	@Override
	public void updateCategory(CuntaoAssetCategoryDto cuntaoAssetCategoryDto,String operator) {
		Assert.notNull(cuntaoAssetCategoryDto);
		CuntaoAssetCategory cuntaoAssetCategory = this.convertToDO(cuntaoAssetCategoryDto);
		cuntaoAssetCategory.setGmtModified(new Date());
		cuntaoAssetCategory.setModifier(operator==null?"system":operator);
		cuntaoAssetCategoryMapper.updateByPrimaryKeySelective(cuntaoAssetCategory);
		
		
	}

	@Override
	public void addCategory(CuntaoAssetCategoryDto cuntaoAssetCategoryDto,String operator) {
		Assert.notNull(cuntaoAssetCategoryDto);
		CuntaoAssetCategory cuntaoAssetCategory = this.convertToDO(cuntaoAssetCategoryDto);
		cuntaoAssetCategory.setCreator(operator==null?"system":operator);
		cuntaoAssetCategory.setModifier(operator==null?"system":operator);
		cuntaoAssetCategory.setGmtCreate(new Date());
		cuntaoAssetCategory.setGmtModified(new Date());
		cuntaoAssetCategory.setIsDeleted("n");
		cuntaoAssetCategoryMapper.insertSelective(cuntaoAssetCategory);
	}

	@Override
	public void deleteCategory(Long categoryId,String operator) {
		CuntaoAssetCategory cuntaoAssetCategory  = new CuntaoAssetCategory();
		cuntaoAssetCategory.setId(categoryId);
		cuntaoAssetCategory.setIsDeleted("y");
		cuntaoAssetCategory.setGmtModified(new Date());
		cuntaoAssetCategory.setModifier(operator==null?"system":operator);
		cuntaoAssetCategoryMapper.updateByPrimaryKeySelective(cuntaoAssetCategory);
	}

	@Override
	public Integer getCountByName(String name) {
		CuntaoAssetCategoryExample example = new CuntaoAssetCategoryExample();
		example.createCriteria().andNameEqualTo(name).andIsDeletedEqualTo("n");
		return cuntaoAssetCategoryMapper.countByExample(example);
	}

}
