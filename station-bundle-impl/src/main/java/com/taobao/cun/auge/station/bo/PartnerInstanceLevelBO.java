package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;

/**
 * Created by jingxiao.gjx on 2016/7/29.
 */
public interface PartnerInstanceLevelBO {
	/**
	 * 查询给定partnerInstanceId的合伙人层级
	 *
	 * @param partnerInstanceId 合伙人实例Id
	 * @return 给定partnerInstanceId的合伙人层级
	 */
	PartnerInstanceLevel getPartnerInstanceLevelByPartnerInstanceId(Long partnerInstanceId);


	/**
	 * 新增合伙人层级
	 *
	 * @param partnerInstanceLevelDto 合伙人层级Dto
	 */
	void addPartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto);

	/**
	 * 根据taobao_user_id和station_id失效以前的评级(is_valid＝'n')
	 *
	 * @param partnerInstanceLevelDto 合伙人层级Dto
	 */
	void invalidatePartnerInstanceLevelBefore(PartnerInstanceLevelDto partnerInstanceLevelDto);


	void updatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto);
}
