package com.taobao.cun.auge.tag.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
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
	
	@Autowired
	private PartnerStationRelMapper partnerStationRelMapper;
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
		if(userTag == null) {
            throw new IllegalArgumentException("illeage userTag[" + tag + "]");
        }
	}

	@Override
	public boolean initTPTag() {
		PartnerStationRelExample example = new PartnerStationRelExample();
		example.createCriteria().andIsCurrentEqualTo("y").andTypeEqualTo("TP").andIsDeletedEqualTo("n");
		List<PartnerStationRel> rels = partnerStationRelMapper.selectByExample(example);
		for(PartnerStationRel rel : rels){
			try {
				if(this.hasTag(rel.getTaobaoUserId(), UserTag.TP_USER_TAG.getTag()) && !this.hasTag(rel.getTaobaoUserId(), UserTag.TP_USER_TAG2.getTag())){
					this.addTag(rel.getTaobaoUserId(), UserTag.TP_USER_TAG2.getTag());
					logger.info("add UserTag["+rel.getTaobaoUserId()+"]");
				}
			} catch (Exception e) {
				logger.error("initTPTag error",e);
			}
		}
		logger.info("finish add UserTag");
		return true;
	}

	@Override
	public boolean batchAddTag(List<Long> taobaoUserIds, String userTag) {
		for (Long taobaoUserId : taobaoUserIds) {
			try {
				if (!this.hasTag(taobaoUserId, userTag)) {
					this.addTag(taobaoUserId, userTag);
				}
			} catch (Exception e) {
				logger.error("batchAddTag error[" + taobaoUserId + "]", e);
			}
		}
		return true;
	}
}
