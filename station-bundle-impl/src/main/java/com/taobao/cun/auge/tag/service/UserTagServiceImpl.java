package com.taobao.cun.auge.tag.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.tag.UserTag;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userdata.client.UicTagServiceClient;

@Service("userTagService")
@HSFProvider(serviceInterface = UserTagService.class)
public class UserTagServiceImpl implements UserTagService {

	private static final Logger logger = LoggerFactory.getLogger(UserTagServiceImpl.class);
	
	@Autowired
	private UicTagServiceClient uicTagServiceClient;
	
	
	@Override
	public boolean addTag(Long taobaoUserId, String tag) {
		assertTag(tag);
		ResultDO<Boolean> addTagResult =  uicTagServiceClient.addUicTag(taobaoUserId, tag);
		if(!addTagResult.isSuccess()){
			logger.error("addUserTagError:ResultCode["+addTagResult.getRetCode().getCode()+"]errMsg["+addTagResult.getErrMsg()+"] errTrace["+addTagResult.getErrTrace()+"]");
		}
		return addTagResult.isSuccess()?addTagResult.getModule():false;
	}

	@Override
	public boolean removeTag(Long taobaoUserId, String tag) {
		assertTag(tag);
		ResultDO<Boolean> removeTagResult = uicTagServiceClient.removeUicTag(taobaoUserId, tag);
		if(!removeTagResult.isSuccess()){
			logger.error("removeTagResultError:ResultCode["+removeTagResult.getRetCode().getCode()+"]errMsg["+removeTagResult.getErrMsg()+"] errTrace["+removeTagResult.getErrTrace()+"]");
		}
		return removeTagResult.isSuccess()?removeTagResult.getModule():false;
	}

	@Override
	public boolean hasTag(Long taobaoUserId, String tag) {
		assertTag(tag);
		ResultDO<Boolean> checkTagResult = uicTagServiceClient.checkTag(taobaoUserId, tag);
		if(!checkTagResult.isSuccess()){
			logger.error("checkTagResult["+checkTagResult.getRetCode().getCode()+"]errMsg["+checkTagResult.getErrMsg()+"] errTrace["+checkTagResult.getErrTrace()+"]");
		}
		return checkTagResult.isSuccess()?checkTagResult.getModule():false;
	}

	
	private void assertTag(String tag) {
		UserTag userTag = UserTag.valueOfTag(tag);
		if(userTag == null)throw new IllegalArgumentException("illeage userTag["+tag+"]");
	}
}
