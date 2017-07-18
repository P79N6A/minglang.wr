package com.taobao.cun.auge.alilang;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.alilang.jingwei.PartnerMessage;
import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.notify.message.StringMessage;
import com.taobao.notify.remotingclient.NotifyManagerBean;
import com.taobao.notify.remotingclient.SendResult;

@HSFProvider(serviceInterface = AlilangUserInitService.class)
public class AlilangUserInitServiceImpl implements AlilangUserInitService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${notify.alilang.topic}")
	private String topic;
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
	private AccountMoneyMapper accountMoneyMapper;
	@Autowired
    private NotifyManagerBean notifyPublisherManagerBean;
	@Value("${alilang.orgId}")
	protected long alilangOrgId;
	
	@Override
	public void init() {
		List<AccountMoney> list = getAllAccountMoneys();
		for(AccountMoney accountMoney : list){
			PartnerInstanceDto partnerInstanceDto = null;
			try{
				partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(accountMoney.getObjectId());
			}catch(Exception e){
			}
			if(partnerInstanceDto != null && PartnerInstanceTypeEnum.TP.equals(partnerInstanceDto.getType())){
				try{
					newAlilangUser(partnerInstanceDto);
				}catch(Exception e){
					logger.error("init alilang user error. instance_id={}", partnerInstanceDto.getId(), e);
				}
			}
		}
	}

	@Override
	  public void testInit(Long id) {
	    PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(id);
	    if(partnerInstanceDto != null && PartnerInstanceTypeEnum.TP.equals(partnerInstanceDto.getType())){
	        newAlilangUser(partnerInstanceDto);
	    }
	  }
	
	private void newAlilangUser(PartnerInstanceDto partnerInstanceDto) {
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		PartnerMessage partnerMessage = new PartnerMessage();
		partnerMessage.setTaobaoUserId(partnerDto.getTaobaoUserId());
		partnerMessage.setMobile(partnerDto.getMobile());
		partnerMessage.setOmobile(partnerDto.getMobile());
		partnerMessage.setAction("update");
		partnerMessage.setEmail(partnerDto.getEmail());
		partnerMessage.setName(partnerDto.getName());
		//partnerMessage.setAlilangUserId((String) row.get("alilang_user_id"));
		partnerMessage.setAlilangOrgId(alilangOrgId);
		String str = JSONObject.toJSONString(partnerMessage);
		
		logger.info("init alilang user:{}", str);
		
		StringMessage stringMessage = new StringMessage();
		stringMessage.setBody(str);
		stringMessage.setTopic(topic);
		stringMessage.setMessageType("sync-alilang-user");
		SendResult sendResult = notifyPublisherManagerBean.sendMessage(stringMessage);
		if(sendResult.isSuccess()){
			logger.info("messageId:{}", sendResult.getMessageId());
		}else{
			logger.error("send message error.{}", sendResult.getErrorMessage());
		}
	}
	
	private List<AccountMoney> getAllAccountMoneys(){
		AccountMoneyExample example = new AccountMoneyExample();
		example.createCriteria()
			.andIsDeletedEqualTo("n")
			.andTargetTypeEqualTo(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE.getCode())
			.andThawTimeIsNull()
			.andStateEqualTo(AccountMoneyStateEnum.HAS_FROZEN.getCode());
		return accountMoneyMapper.selectByExample(example);
	}

}
