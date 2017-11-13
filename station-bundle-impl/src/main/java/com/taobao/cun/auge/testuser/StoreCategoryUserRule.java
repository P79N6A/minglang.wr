package com.taobao.cun.auge.testuser;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;

@Component("testStoreCategory")
public class StoreCategoryUserRule implements TestUserRule{

	@Autowired
	private StoreReadBO storeReadBO;
	
	@Override
	public boolean checkTestUser(Long taobaoUserId, String config) {
	
		if(StringUtils.isEmpty(config)){
			return false;
		}
		StoreDto store = storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
		if(store == null){
			return false;
		}
		return Stream.of(config.split(",")).anyMatch(category -> store.getStoreCategory().getCategory().equals(category));
	}

}
