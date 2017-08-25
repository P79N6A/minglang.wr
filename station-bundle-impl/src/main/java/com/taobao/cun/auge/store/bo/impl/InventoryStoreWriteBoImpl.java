package com.taobao.cun.auge.store.bo.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.taobao.cun.auge.store.bo.InventoryStoreWriteBo;
import com.taobao.cun.auge.store.dto.InventoryStoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.inventory.sic.constants.SicConstant.StoreInfoOperateType;
import com.taobao.inventory.sic.constants.SicConstant.StoreType;
import com.taobao.inventory.sic.model.dto.request.StoreSaveDTO;
import com.taobao.inventory.sic.model.dto.response.BaseResult;
import com.taobao.inventory.sic.model.dto.store.StoreInfoDTO;
import com.taobao.inventory.sic.service.client.store.StoreInfoServiceClient;
import com.taobao.tddl.client.sequence.impl.GroupSequence;

@Component
public class InventoryStoreWriteBoImpl implements InventoryStoreWriteBo {
	@Resource
	private StoreInfoServiceClient storeInfoServiceClient;
	@Resource
	private GroupSequence inventoryStoreCodeSequence;
	
	@Override
	public String create(InventoryStoreCreateDto inventoryStoreCreateDto) throws StoreException{
		BeanValidator.validateWithThrowable(inventoryStoreCreateDto);
		if(Strings.isNullOrEmpty(inventoryStoreCreateDto.getCode())){
			inventoryStoreCreateDto.setCode(newCode());
		}
		StoreInfoDTO data = new StoreInfoDTO();
		data.setStoreAliasName(inventoryStoreCreateDto.getAlias());
		data.setStoreName(inventoryStoreCreateDto.getName());
		data.setStoreCode(inventoryStoreCreateDto.getCode());
		data.setPriority(inventoryStoreCreateDto.getPriority());
		data.setType(StoreType.MERCHANT_WAREHOUSE);
		data.setAreaId(inventoryStoreCreateDto.getAreaId());
		data.setUserId(inventoryStoreCreateDto.getUserId());
		
		StoreSaveDTO saveDTO = new StoreSaveDTO.Builder().
				setOperateType(StoreInfoOperateType.CREATE_IF_NOT_EXIST).
				setMerchantStoreDTO(data).
				build();
		
		saveDTO.setOperateType(StoreInfoOperateType.CREATE_IF_NOT_EXIST);
		BaseResult<Void> result = storeInfoServiceClient.saveStore(saveDTO);
		if(!result.isSuccess()){
			throw new StoreException(result.getErrorInfo());
		}else{
			return inventoryStoreCreateDto.getCode();
		}
	}

	private String newCode(){
		String value = String.valueOf(inventoryStoreCodeSequence.nextValue());
		String code = "";
		for(char c : value.toCharArray()){
			code += (char)(c + 'A');
		}
		return "CUNTAO_" + code.toUpperCase();
	}
}
