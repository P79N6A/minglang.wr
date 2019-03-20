package com.taobao.cun.auge.cuncounty.utils;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CainiaoCountyEditDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContactAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContractEditDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyOfficeEditDto;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContact;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContract;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovProtocol;
import com.taobao.cun.auge.dal.domain.CuntaoCountyOffice;

/**
 * 对象转换
 * 
 * @author chengyu.zhoucy
 *
 */
public class BeanConvertUtils {
	static <T> T createBean(Class<T> targetClass, Object sourceBean) {
		T bean = BeanCopy.copy(targetClass, sourceBean);
		try {
			String operator = BeanUtils.getProperty(sourceBean, "operator");
			BeanUtils.setProperty(bean, "creator", operator);
			BeanUtils.setProperty(bean, "modifier", operator);
			BeanUtils.setProperty(bean, "gmtCreate", new Date());
			BeanUtils.setProperty(bean, "gmtModified", new Date());
			BeanUtils.setProperty(bean, "isDeleted", "n");
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return bean;
	}
	/**
	 * 县服务中心
	 * @param cuntaoCountyAddDto
	 * @param orgId
	 * @param cuntaoCountyWhitenameDto
	 * @return
	 */
	public static CuntaoCounty convert(CuntaoCountyAddDto cuntaoCountyAddDto, Long orgId, CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto) {
		CuntaoCounty cuntaoCounty = createBean(CuntaoCounty.class, cuntaoCountyWhitenameDto);
		cuntaoCounty.setName(cuntaoCountyAddDto.getName());
		cuntaoCounty.setOrgId(orgId);
		cuntaoCounty.setState(CuntaoCountyStateEnum.PLANNING.getCode());
		return cuntaoCounty;
	}
	
	/**
	 * 政府联系人
	 * @param cuntaoCountyGovContactAddDto
	 * @return
	 */
	public static CuntaoCountyGovContact convert(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		return createBean(CuntaoCountyGovContact.class, cuntaoCountyGovContactAddDto);
	}
	
	/**
	 * 办公场地
	 * @param cuntaoCountyOfficeEditDto
	 * @return
	 */
	public static CuntaoCountyOffice convert(CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto) {
		return createBean(CuntaoCountyOffice.class, cuntaoCountyOfficeEditDto);
	}
	
	/**
	 * 政府签约信息
	 * @param cuntaoCountyGovContractEditDto
	 * @return
	 */
	public static CuntaoCountyGovContract convert(CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		return createBean(CuntaoCountyGovContract.class, cuntaoCountyGovContractEditDto);
	}
	
	/**
	 * 政府协议
	 * @param cuntaoCountyGovProtocolAddDto
	 * @return
	 */
	public static CuntaoCountyGovProtocol convert(CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto) {
		CuntaoCountyGovProtocol protocol = createBean(CuntaoCountyGovProtocol.class, cuntaoCountyGovProtocolAddDto);
		protocol.setState("valid");
		return protocol;
	}
	
	/**
	 * 菜鸟县仓
	 * @param cainiaoCountyEditDto
	 * @return
	 */
	public static CainiaoCounty convert(CainiaoCountyEditDto cainiaoCountyEditDto) {
		CainiaoCounty cainiaoCounty = createBean(CainiaoCounty.class, cainiaoCountyEditDto);
		cainiaoCounty.setState("new");
		return cainiaoCounty;
	}
	
	public static <T, S> List<T> listConvert(Class<T> targetClass, List<S> sources){
		return BeanCopy.copyList(targetClass, sources);
	}
	
	public static <T, S> T convert(Class<T> targetClass, S source){
		return BeanCopy.copy(targetClass, source);
	}
}
