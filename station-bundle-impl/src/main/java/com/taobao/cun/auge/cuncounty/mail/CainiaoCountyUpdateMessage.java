package com.taobao.cun.auge.cuncounty.mail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.msg.dto.MailSendDto;
import com.taobao.cun.auge.msg.service.MessageService;

/**
 * 菜鸟县仓发生变化时发送邮件
 * @author chengyu.zhoucy
 *
 */
@Component
public class CainiaoCountyUpdateMessage {
	private static final String TEMPLATE_ID = "580107779";
    private static final String SOURCE_ID = "cuntao_org*edit_addr";
    private static final String MESSAGE_TYPE_ID = "120975556";
	@Resource
	private MessageService messageService;
	@Resource
	private AppResourceService appResourceService;
	@Resource
	private EnhancedUserQueryService enhancedUserQueryService;
	@Value("${station.address.change.receivers}")
	private String countyAddressChangeReceivers;
	
	private List<String> getEmailAddress(){
		Map<String, EnhancedUser> emps = null;
		try {
			emps = enhancedUserQueryService.findUsers(Splitter.on(",").splitToList(countyAddressChangeReceivers));
		} catch (BucException e) {
			throw new RuntimeException(e);
		}
		List<String> emails = Lists.newArrayList();
		emps.values().forEach(e->emails.add(e.getEmailAddr()));
		return emails;
	}
	
	public void sendEmail(CuntaoCountyDto cuntaoCounty, CainiaoCountyDto oldCainiaoCounty, CainiaoCountyDto newCainiaoCounty) {
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("originAddress", oldCainiaoCounty.toAdressDetail());
        params.put("curAddress", newCainiaoCounty.toAdressDetail());
        params.put("orgName", cuntaoCounty.getName());
        params.put("orgType", "县服务中心");
        MailSendDto mailSendDto =new MailSendDto();
        mailSendDto.setMailAddress(getEmailAddress());
        mailSendDto.setMessageType(MESSAGE_TYPE_ID);  
        mailSendDto.setSourceId(SOURCE_ID);
        mailSendDto.setTemplateId(TEMPLATE_ID);
        mailSendDto.setContentMap(params);
        messageService.sendMail(mailSendDto);
    }
}
