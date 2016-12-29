package com.taobao.cun.auge.station.bo;

import com.alibaba.fastjson.JSONObject;
import com.taobao.notify.message.StringMessage;

public interface PartnerExamBO {

	public void handleExamFinish(StringMessage strMessage, JSONObject ob);
}
