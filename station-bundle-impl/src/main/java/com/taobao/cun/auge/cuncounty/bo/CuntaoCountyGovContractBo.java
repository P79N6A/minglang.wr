package com.taobao.cun.auge.cuncounty.bo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.annotation.Resource;

import com.google.common.base.Strings;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyProtocolRiskEnum;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContractDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContractEditDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovProtocolAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContract;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovContractMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 政府签约信息
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyGovContractBo {
	@Resource
	private CuntaoCountyGovProtocolBo cuntaoCountyGovProtocolBo;
	@Resource
	private CuntaoCountyGovContractMapper cuntaoCountyGovContractMapper;
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	
	@Transactional(rollbackFor=Throwable.class)
	public void save(CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		CuntaoCountyGovProtocolAddDto cuntaoCountyGovProtocolAddDto = BeanConvertUtils.convert(CuntaoCountyGovProtocolAddDto.class, cuntaoCountyGovContractEditDto);
		cuntaoCountyGovProtocolBo.save(cuntaoCountyGovProtocolAddDto);
		
		CuntaoCountyGovContract cuntaoCountyGovContract = cuntaoCountyExtMapper.getCuntaoCountyGovContract(cuntaoCountyGovContractEditDto.getCountyId());
		if(null == cuntaoCountyGovContract) {
			insert(cuntaoCountyGovContractEditDto);
		}else {
			update(cuntaoCountyGovContract, cuntaoCountyGovContractEditDto);
		}
	}
	
	private void insert(CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		cuntaoCountyGovContractMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovContractEditDto));
	}

	private void update(CuntaoCountyGovContract cuntaoCountyGovContract, CuntaoCountyGovContractEditDto cuntaoCountyGovContractEditDto) {
		CuntaoCountyGovContract newCuntaoCountyGovContract = BeanConvertUtils.convert(cuntaoCountyGovContractEditDto);
		newCuntaoCountyGovContract.setId(cuntaoCountyGovContract.getId());
		newCuntaoCountyGovContract.setGmtModified(new Date());
		newCuntaoCountyGovContract.setCreator(cuntaoCountyGovContract.getCreator());
		cuntaoCountyGovContractMapper.updateByPrimaryKey(newCuntaoCountyGovContract);
	}
	
	CuntaoCountyGovContractDto getCuntaoCountyGovContract(Long countyId){
		CuntaoCountyGovContractDto cuntaoCountyGovContractDto = BeanConvertUtils.convert(CuntaoCountyGovContractDto.class,cuntaoCountyExtMapper.getCuntaoCountyGovContract(countyId));
		if(cuntaoCountyGovContractDto != null) {
			cuntaoCountyGovContractDto.setAttachmentVOList(BeanConvertUtils.convertAttachmentVO(cuntaoCountyGovContractDto.getAttachments()));
		}
		return cuntaoCountyGovContractDto;
	}

	/**
	 * 检查县点协议风险
	 * @param countyId
	 * @return
	 */
	public CuntaoCountyProtocolRiskEnum checkContractRisk(Long countyId){
		CuntaoCountyGovContractDto cuntaoCountyGovContractDto = getCuntaoCountyGovContract(countyId);
		if(cuntaoCountyGovContractDto != null){
			if(!Strings.isNullOrEmpty(cuntaoCountyGovContractDto.getSerialNum())){
				if(isProtocolExpire(cuntaoCountyGovContractDto)) {
					return CuntaoCountyProtocolRiskEnum.protocolExpire;
				}else if(isProtocolWillExpire(cuntaoCountyGovContractDto)) {
					return CuntaoCountyProtocolRiskEnum.protocolWillExpire;
				}else{
					return null;
				}
			}else{
				return CuntaoCountyProtocolRiskEnum.protocolMaybeNotExists;
			}
		}else{
			return CuntaoCountyProtocolRiskEnum.protocolNotExists;
		}

	}

	private boolean isProtocolExpire(CuntaoCountyGovContractDto cuntaoCountyGovContractDto){
		LocalDate today = LocalDate.now();
		LocalDate protocolLocalDate = LocalDate.parse(cuntaoCountyGovContractDto.getProtocolEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return today.isAfter(protocolLocalDate);
	}

	private boolean isProtocolWillExpire(CuntaoCountyGovContractDto cuntaoCountyGovContractDto){
		LocalDate date = LocalDate.now().plusDays(90);
		LocalDate protocolLocalDate = LocalDate.parse(cuntaoCountyGovContractDto.getProtocolEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return date.isAfter(protocolLocalDate);
	}
}
