package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;

@Component
public class CuntaoCountyPredicate {
	@Resource
	private CuntaoCountyWhitenameBo cuntaoCountyWhitenameBo;
	
	void checkCreateCounty(String countyCode) {
		Optional<CuntaoCountyWhitenameDto> optional = cuntaoCountyWhitenameBo.getCuntaoCountyWhitenameByCountyCode(countyCode);
		if(optional.isPresent()) {
			CuntaoCountyWhitenameDto cuntaoCountyWhitenameDto = optional.get();
			if(cuntaoCountyWhitenameDto.getCountyId() > 0) {
				throw new RuntimeException(cuntaoCountyWhitenameDto.getCountyName() + "已经开了县点");
			}
		}else {
			throw new RuntimeException("该县不在白名单里，暂不能开县");
		}
	}
}
