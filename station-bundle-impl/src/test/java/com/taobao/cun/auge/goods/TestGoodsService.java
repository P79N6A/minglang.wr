package com.taobao.cun.auge.goods;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.common.result.Result;
import com.vividsolutions.jts.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestGoodsService {

	@Autowired
	private CuntaoGoodsService cuntaoGoodsService;
	
	@Test
	public void testIsConfirmSampleGoodsProtocol(){
		Result<Boolean> result = cuntaoGoodsService.isConfirmSampleGoodsProtocol(3683820876l);
		Assert.isTrue(result.getModule());
	}
	
	
	@Test
	public void testConfirmSampleGoodsProtocol(){
		Result<Boolean> result = cuntaoGoodsService.confirmSampleGoodsProtocol(3683820876l);
		Assert.isTrue(result.getModule());
	}
	
	
	@Test
	public void testConfirmSupplyGoodsProtocol(){
		Result<Boolean> result = cuntaoGoodsService.confirmSupplyGoodsProtocol(3683820876l);
		Assert.isTrue(result.getModule());
	}
	
	
	@Test
	public void testIsConfirmSupplyGoodsProtocol(){
		Result<Boolean> result = cuntaoGoodsService.isConfirmSupplyGoodsProtocol(3683820876l);
		Assert.isTrue(result.getModule());
	}
}
