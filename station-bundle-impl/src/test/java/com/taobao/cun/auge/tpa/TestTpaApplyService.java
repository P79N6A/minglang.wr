package com.taobao.cun.auge.tpa;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.station.dto.TpaApplyInfoDto;
import com.taobao.cun.auge.station.request.CheckTpaApplyRequest;
import com.taobao.cun.auge.station.response.CheckTpaApplyResponse;
import com.taobao.cun.auge.station.response.TpaApplyResponse;
import com.taobao.cun.auge.station.tpa.TpaApplyService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestTpaApplyService {
	
	@Autowired
	private TpaApplyService tpaApplyService;
	
	@Test
	public void testCheckTpa(){
		CheckTpaApplyRequest request = new CheckTpaApplyRequest();
		request.setPartnerStationId(3731943L);
		request.setTaobaoNick("gytest604");
		CheckTpaApplyResponse response = tpaApplyService.checkTpaApply(request);
		System.out.println(response);
	}
	
	@Test
	public void testApplyTpa() throws IOException{
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpaApplyRequest.json"));
		TpaApplyInfoDto request = JSON.parseObject(requestJSON, TpaApplyInfoDto.class);
		TpaApplyResponse reponse = tpaApplyService.applyTpa(request);
	}

	
	@Test
	public void testUpdateTpa() throws IOException{
		String requestJSON = IOUtils.toString(TestTpaApplyService.class.getClassLoader().getResourceAsStream("tpaApplyRequest.json"));
		TpaApplyInfoDto request = JSON.parseObject(requestJSON, TpaApplyInfoDto.class);
		request.setEmail("test@126.com");
		request.getAddress().setAddressDetail("testAddressDetail");
		request.setStationId(3733326L);
		TpaApplyResponse reponse = tpaApplyService.updateTpa(request);
	}
	
	@Test
	public void testGetTpaApplyInfo(){
		TpaApplyInfoDto info = tpaApplyService.getTpaApplyInfo("gytest604", 3731943L);
		System.out.println(info);
	}
	
	@Test
	public void testGetTpaInfo(){
		TpaApplyInfoDto info = tpaApplyService.getTpaInfo(3733326L);
		System.out.println(info);
	}
}
