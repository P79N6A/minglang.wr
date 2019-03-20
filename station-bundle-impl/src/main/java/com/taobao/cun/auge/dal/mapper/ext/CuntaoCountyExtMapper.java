package com.taobao.cun.auge.dal.mapper.ext;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContact;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContract;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;
import com.taobao.cun.auge.dal.domain.CuntaoCountyOffice;

/**
 * 县服务中心
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyExtMapper {
	/**
	 * 获取政府签约信息
	 * @return
	 */
	CuntaoCountyGovContract getCuntaoCountyGovContract(@Param("countyId") Long countyId);
	
	/**
	 * 获取上传的有效协议
	 * @param countyId
	 * @return
	 */
	CuntaoCountyGovProtocol getCuntaoCountyGovProtocol(@Param("countyId") Long countyId);
	
	/**
	 * 获取政府联系人
	 * @param countyId
	 * @return
	 */
	List<CuntaoCountyGovContact> getCuntaoCountyGovContacts(@Param("countyId") Long countyId);
	
	/**
	 * 失效掉协议
	 */
	void invalidProtocols(@Param("countyId") Long countyId, @Param("operator")String operator);
	
	/**
	 * 获取办公场地信息
	 * @param countyId
	 * @return
	 */
	CuntaoCountyOffice getCuntaoCountyOffice(@Param("countyId") Long countyId);
	
	/**
	 * 删除办公场地信息
	 * @param countyId
	 */
	void deleteCuntaoCountyOffice(@Param("countyId") Long countyId, @Param("operator")String operator);
}
