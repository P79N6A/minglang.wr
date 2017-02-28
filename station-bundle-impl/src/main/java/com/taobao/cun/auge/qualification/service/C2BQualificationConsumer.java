package com.taobao.cun.auge.qualification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.QualiLifeCycleMessage;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.tc.domain.util.JavaSerializationUtil;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component
public class C2BQualificationConsumer {

	@Autowired
	private CuntaoQualificationService cuntaoQualificationService;
	
	@Autowired
	SellerQualiServiceAdapter sellerQualiServiceAdapter;
	 @ServiceActivator(inputChannel = "c2bquali")
     public void receiveMessage(Message<byte[]> rmessage) {
		 byte[]  message = (byte[])rmessage.getPayload();
		 try {
			QualiLifeCycleMessage qualiLifeCycleMessage = JavaSerializationUtil.deSerialize(message);
			EntityQuali quali = sellerQualiServiceAdapter.queryValidQualiById(qualiLifeCycleMessage.getQid(),qualiLifeCycleMessage.getEidType()).get();
			Assert.notNull(quali);
			ListHidByEidAndEidTypeResponse listHidByEidAndEidTypeResponse = sellerQualiServiceAdapter.queryHavanaIdByQuali(quali.getEid(), quali.getEidType()).get();
			Assert.notNull(listHidByEidAndEidTypeResponse);
			if(CollectionUtils.isNotEmpty(listHidByEidAndEidTypeResponse.getQualiBindHids())){
				listHidByEidAndEidTypeResponse.getQualiBindHids().stream().forEach(taobaoUserId -> cuntaoQualificationService.syncCuntaoQulification(taobaoUserId));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		 System.out.println(message);
     }
}
