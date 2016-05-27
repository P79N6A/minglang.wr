package com.taobao.cun.auge.dal.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;

import tk.mybatis.mapper.common.Mapper;

public interface PartnerStationRelMapper extends Mapper<PartnerStationRel> {
	
	@Select("select instance.*,p.name as partner_name,p.alipay_account,p.taobao_user_id,p.taobao_nick,p.iden_num,p.mobile,p.email,p.business_type,p.description as partner_description,p.state as partner_state,s.name as station_name,s.description as station_description,s.province,s.city,s.county,s.town,s.province_detail,s.city_detail,s.county_detail,s.town_detail,s.address,s.apply_org,s.station_num,s.lng,s.lat,s.village,s.village_detail,s.covered,s.products,s.logistics_state,s.format,s.area_type,s.manager_id,s.provider_id,s.feature,s.status,s.fixed_type from partner_station_rel instance,partner p,station s where instance.partner_id = p.id and instance.station_id=s.id and instance.is_deleted = 'n' and s.is_deleted='n' and p.is_deleted='n'")
	@ResultMap("PartnerInstanceMap")
	Page<PartnerInstance> findPartnerInstances();
	
}