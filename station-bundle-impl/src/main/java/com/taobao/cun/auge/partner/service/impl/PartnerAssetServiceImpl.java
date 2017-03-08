package com.taobao.cun.auge.partner.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.partner.service.PartnerAssetService;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.common.resultmodel.PagedResultModel;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.asset.CuntaoAssetDto;
import com.taobao.cun.dto.asset.CuntaoAssetQueryCondition;
import com.taobao.cun.service.asset.CuntaoAssetService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerAssetService")
@HSFProvider(serviceInterface = PartnerAssetService.class)
public class PartnerAssetServiceImpl implements PartnerAssetService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerAssetService.class);
	

	@Autowired
	CuntaoAssetService cuntaoAssetService;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public boolean isBackAsset(Long instanceId) {
		try {
			CuntaoAssetQueryCondition queryCondition = new CuntaoAssetQueryCondition();

			queryCondition.setPartnerInstanceId(instanceId);
			queryCondition.getPage().setStart(0);
			queryCondition.getPage().setSize(1000);
			
			PagedResultModel<List<CuntaoAssetDto>> r = cuntaoAssetService.queryByPage(queryCondition, buildSystemContextDto());

			if (r.isSuccess()) {
				return 0 == r.getTotalResultSize();
			} else {
				String error = getErrorMessage("isBackAsset", "instanceId:" + instanceId, null);
				logger.error(error);
				throw new AugeServiceException("查询资产失败，请稍后再试！");
			}
		} catch (Exception e) {
			String error = getErrorMessage("isBackAsset", "instanceId:" + instanceId, e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException("查询资产失败，请稍后再试！");
		}
	}

	private ContextDto buildSystemContextDto() {
		ContextDto contextDto = new ContextDto();
		contextDto.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		contextDto.setAppId("1");
		return contextDto;
	}

	private String getErrorMessage(String methodName, String param, String error) {
		StringBuilder sb = new StringBuilder();
		sb.append("PartnerAssetService-Error|").append(methodName).append("(.param=").append(param).append(").")
				.append("errorMessage:").append(error);
		return sb.toString();
	}

}
