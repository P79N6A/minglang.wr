package com.taobao.cun.auge.asset;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckInfoDto;
import com.taobao.cun.auge.asset.service.AssetCheckService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.goods.CuntaoGoodsService;
import com.taobao.cun.auge.task.dto.TaskInteractionDto;
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

		TaskInteractionDto taskInteractionDto = new TaskInteractionDto();
		taskInteractionDto.setTaskId(265L);
		taskInteractionDto.setUserId("3840659724");
		//taskInteractionDto.setBusiType("ASSET_CHECK");
		taskInteractionDto.setIdentifier("30002268");
		String  str = "{\"identifier\":\"30002268\",\"taskId\":265,\"busiType\":\"ASSET_CHECK\",\"userId\":\"3840659724\"}";

		assetCheckService.initTaskForStation("COUNTY_CHECK",str,3840659724L);
		System.out.println("success======================");
	}

	public static void main(String[] args) {
		TaskInteractionDto taskInteractionDto = new TaskInteractionDto();
		taskInteractionDto.setTaskId(265L);
		taskInteractionDto.setUserId("3840659724");
		//taskInteractionDto.setBusiType("ASSET_CHECK");
		taskInteractionDto.setIdentifier("30002268");
		System.out.println(JSON.toJSONString(taskInteractionDto));
	}
	
	

}
