package com.taobao.cun.auge.station.bo;

import java.util.Optional;

import com.taobao.cun.auge.dal.domain.PartnerTpg;

public interface PartnerTpgBO {

	public boolean addTpgTag(Long taobaoUserId);
	
	public boolean checkTag(Long taobaoUserId);
	
	public boolean removeTpgTag(Long taobaoUserId);
	
	Optional<PartnerTpg> queryByParnterInstanceId(Long partnerInstanceId);
	
	Long addPartnerTpg(PartnerTpg parnterTpg);
	
	void updatePartnerTpg(PartnerTpg parnterTpg);
	
	void deletePartnerTpg(Long id);
}
