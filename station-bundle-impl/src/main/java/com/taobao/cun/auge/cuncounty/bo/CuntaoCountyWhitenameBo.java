package com.taobao.cun.auge.cuncounty.bo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitename;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitenameExample;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyWhitenameMapper;

/**
 * 开县白名单
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyWhitenameBo {
	@Resource
	private CuntaoCountyWhitenameMapper cuntaoCountyWhitenameMapper;
	/**
	 * 获取可开县的白名单
	 * 
	 * @return
	 */
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenames(){
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCountyIdEqualTo(0L);
		return BeanConvertUtils.listConvert(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitenameMapper.selectByExample(example));
	}
	
	/**
	 * 按县CODE获取白名单
	 * @param countyCode
	 * @return
	 */
	public Optional<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenameByCountyCode(String countyCode){
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCountyCodeEqualTo(countyCode);
		List<CuntaoCountyWhitename> cuntaoCountyWhitenames = cuntaoCountyWhitenameMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoCountyWhitenames)) {
			return Optional.empty();
		}else {
			return Optional.of(BeanConvertUtils.convert(cuntaoCountyWhitenames.get(0)));
		}
	}
	
	/**
	 * 把县点ID更新到白名单上
	 * 
	 * @param id
	 * @param countyId
	 */
	public void updateCountyId(Long id, Long countyId) {
		CuntaoCountyWhitename cuntaoCountyWhitename = cuntaoCountyWhitenameMapper.selectByPrimaryKey(id);
		cuntaoCountyWhitename.setGmtModified(new Date());
		cuntaoCountyWhitename.setCountyId(countyId);
		cuntaoCountyWhitenameMapper.updateByPrimaryKey(cuntaoCountyWhitename);
	}
}
