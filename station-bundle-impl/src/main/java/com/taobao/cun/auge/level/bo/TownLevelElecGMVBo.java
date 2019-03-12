package com.taobao.cun.auge.level.bo;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;

/**
 * 家电市场规模计算
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownLevelElecGMVBo {
	public long calc(TownLevelDto townLevelDto) {
		if(townLevelDto.getTaobaoGmv() == 0 || townLevelDto.getTownPopulation() == 0) {
			return 7999999L;
		}
		
		return Math.round(townLevelDto.getTownPopulation() * 300 * getMarketFactor(townLevelDto));
	}
	
	private float getMarketFactor(TownLevelDto townLevelDto) {
		//上一年度人均每月GMV
		float gmv = townLevelDto.getTaobaoGmv() / (12 * townLevelDto.getTownPopulation());
		
		if(gmv < 5) {
			return 0.5f;
		}else if(gmv < 10) {
			return 0.6f;
		}else if(gmv < 15) {
			return 0.7f;
		}else if(gmv < 20) {
			return 0.8f;
		}else if(gmv < 30) {
			return 0.9f;
		}else if(gmv < 40) {
			return 1.0f;
		}else if(gmv < 50) {
			return 1.1f;
		}else if(gmv < 50) {
			return 1.1f;
		}else if(gmv < 60) {
			return 1.2f;
		}else if(gmv < 70) {
			return 1.3f;
		}else if(gmv < 80) {
			return 1.4f;
		}else if(gmv < 90) {
			return 1.5f;
		}else if(gmv < 100) {
			return 1.6f;
		}else if(gmv < 200) {
			return 1.7f;
		}else if(gmv < 300) {
			return 1.8f;
		}else if(gmv < 500) {
			return 1.9f;
		}else {
			return 2.0f;
		}
	}
}
