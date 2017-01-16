package com.taobao.cun.auge.station.bo.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.PartnerTpg;
import com.taobao.cun.auge.dal.domain.PartnerTpgExample;
import com.taobao.cun.auge.dal.mapper.PartnerTpgMapper;
import com.taobao.cun.auge.station.bo.PartnerTpgBO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userdata.client.UicTagServiceClient;

@Component("partnerTpgBO")
public class PartnerTpgBOImpl implements PartnerTpgBO {

	@Autowired
	private UicTagServiceClient uicTagServiceClient;
	
	public static final String TPG_TAG = "ct_partner_tpg";
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerTpgBOImpl.class);
	
	@Autowired
	private PartnerTpgMapper partnerTpgMapper;
	
	
	@Override
	public boolean addTpgTag(Long taobaoUserId) {
		ResultDO<Boolean> addTagResult =  uicTagServiceClient.addUicTag(taobaoUserId, TPG_TAG);
		if(!addTagResult.isSuccess()){
			logger.error("addTpgTagError:ResultCode["+addTagResult.getRetCode().getCode()+"]errMsg["+addTagResult.getErrMsg()+"] errTrace["+addTagResult.getErrTrace()+"]");
		}
		return addTagResult.isSuccess()?addTagResult.getModule():false;
	}

	@Override
	public boolean removeTpgTag(Long taobaoUserId) {
		ResultDO<Boolean> removeTagResult = uicTagServiceClient.removeUicTag(taobaoUserId, TPG_TAG);
		if(!removeTagResult.isSuccess()){
			logger.error("removeTagResult:ResultCode["+removeTagResult.getRetCode().getCode()+"]errMsg["+removeTagResult.getErrMsg()+"] errTrace["+removeTagResult.getErrTrace()+"]");
		}
		return removeTagResult.isSuccess()?removeTagResult.getModule():false;
	}

	@Override
	public boolean checkTag(Long taobaoUserId) {
		ResultDO<Boolean> checkTagResult = uicTagServiceClient.checkTag(taobaoUserId, TPG_TAG);
		if(!checkTagResult.isSuccess()){
			logger.error("checkTagResult["+checkTagResult.getRetCode().getCode()+"]errMsg["+checkTagResult.getErrMsg()+"] errTrace["+checkTagResult.getErrTrace()+"]");
		}
		return checkTagResult.isSuccess()?checkTagResult.getModule():false;
	}
	
	@Override
	public Optional<PartnerTpg> queryByParnterInstanceId(Long partnerInstanceId) {
		PartnerTpgExample example = new PartnerTpgExample();
		example.createCriteria().andIsDeletedEqualTo("n").andPartnerInstanceIdEqualTo(partnerInstanceId);
		List<PartnerTpg>  partnerTpgs = partnerTpgMapper.selectByExample(example);
		if(!partnerTpgs.isEmpty()){
			return Optional.ofNullable(partnerTpgs.iterator().next());
		}
		return  Optional.ofNullable(null);
	}

	@Override
	public Long addPartnerTpg(PartnerTpg parnterTpg) {
		partnerTpgMapper.insertSelective(parnterTpg);
		return parnterTpg.getId();
	}

	@Override
	public void deletePartnerTpg(Long id) {
		PartnerTpg partnerTpg = new PartnerTpg();
		partnerTpg.setIsDeleted("y");
		partnerTpg.setId(id);
		partnerTpgMapper.updateByPrimaryKeySelective(partnerTpg);
	}


	@Override
	public void updatePartnerTpg(PartnerTpg parnterTpg) {
		partnerTpgMapper.updateByPrimaryKeySelective(parnterTpg);
	}

}
