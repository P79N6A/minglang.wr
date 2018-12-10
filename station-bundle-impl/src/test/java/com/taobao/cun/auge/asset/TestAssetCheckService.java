package com.taobao.cun.auge.asset;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.service.AssetCheckService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.goods.CuntaoGoodsService;
import com.vividsolutions.jts.util.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestAssetCheckService {

	@Autowired
	private AssetCheckService assetCheckService;
	
	@Test
	public void testListInfoForOrg(){
		AssetCheckInfoCondition condition = new AssetCheckInfoCondition();
		condition.setCheckerAreaType("COUNTY");
		condition.setOrgId(1111L);
		PageDto<AssetCheckInfoDto> pageDto = assetCheckService.listInfoForOrg(condition);
		List<AssetCheckInfoDto> items = pageDto.getItems();
		System.out.println(items);
	}
	
	

}
