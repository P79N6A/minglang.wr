package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.Partner;

public interface ExPartnerMapper extends PartnerMapper {
	public Partner selectByAlilangUserId(String alilangUserId);
}
