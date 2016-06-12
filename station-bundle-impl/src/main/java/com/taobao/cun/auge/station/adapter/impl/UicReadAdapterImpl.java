package com.taobao.cun.auge.station.adapter.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Component("uicReadAdapter")
public class UicReadAdapterImpl implements UicReadAdapter{

	@Resource
	private UicReadServiceClient uicReadServiceClient;

	@Override
	public String getFullName(Long taobaoUserId) {
		if (taobaoUserId == null) {
	         return "";
		}
		ResultDO<BaseUserDO> rstDo = uicReadServiceClient.getBaseUserByUserId(taobaoUserId);

		if (rstDo == null || rstDo.getModule() == null || !rstDo.isSuccess()) {
			return "";
		} else {
			return rstDo.getModule().getFullname();
		}
	}

	@Override
	public Long getTaobaoUserIdByTaobaoNick(String taobaoNick) {
		if (StringUtils.isEmpty(taobaoNick)) {
			return null;
		}
			 
		ResultDO<Long>  res = uicReadServiceClient.getUserIdByNick(taobaoNick);
		if (res == null || res.getModule() == null) {
			return null;
		}
		return res.getModule();
	}

	@Override
	public String getTaobaoNickByTaobaoUserId(Long taobaoUserId) {
		if (taobaoUserId == null) {
	         return "";
		}
		ResultDO<BaseUserDO> rstDo = uicReadServiceClient.getBaseUserByUserId(taobaoUserId);

		if (rstDo == null || rstDo.getModule() == null || !rstDo.isSuccess()) {
			return "";
		} else {
			return rstDo.getModule().getNick();
		}
	}

}
