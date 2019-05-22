package com.taobao.cun.auge.dal.mapper.ext;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.vo.CuntaoCountyListItemVO;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContact;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContract;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;
import com.taobao.cun.auge.dal.domain.CuntaoCountyOffice;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitename;

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
	 * 获取办公场地信息
	 * @param countyId
	 * @return
	 */
	List<CuntaoCountyOffice> getCuntaoCountyOffices(@Param("countyIds") List<Long> countyIds);
	
	/**
	 * 删除办公场地信息
	 * @param countyId
	 */
	void deleteCuntaoCountyOffice(@Param("countyId") Long countyId, @Param("operator")String operator);
	
	/**
	 * 获取菜鸟县仓
	 * @param countyId
	 * @return
	 */
	CainiaoCounty getCainiaoCounty(@Param("countyId") Long countyId);
	
	/**
	 * 分页查询
	 * @param condition
	 * @return
	 */
	List<CuntaoCountyListItemVO> query(@Param("condition") CuntaoCountyCondition condition);
	
	/**
	 * 分页查询总数
	 * @param condition
	 * @return
	 */
	int count(@Param("condition") CuntaoCountyCondition condition);
	
	/**
	 * 按状态统计县点数
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> groupByState(@Param("condition") CuntaoCountyCondition condition);
	
	/**
	 * 根据组织ID获取县点
	 * @param orgId
	 * @return
	 */
	CuntaoCounty getCuntaoCountyByOrgId(@Param("orgId") Long orgId);
	
	/**
	 * 更新菜鸟ID
	 * @param countyId
	 * @param caiNiaostationId
	 */
	void updateCainiaoCountyId(@Param("countyId") Long countyId, @Param("caiNiaostationId") Long caiNiaostationId);

	/**
	 * 更新菜鸟县仓类型
	 * @param id
	 * @param storeType
	 * @param operator
	 */
	void updateCainiaoCountyStoreType(@Param("id") Long id, @Param("storeType") String storeType, @Param("operator") String operator);
	
	/**
	 * 按code（县 or 市）查询白名单
	 * @param codes
	 * @return
	 */
	List<CuntaoCountyWhitename> getCuntaoCountyWhitenamesByCodes(List<String> codes);
}
