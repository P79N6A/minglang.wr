package com.taobao.cun.auge.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.user.service.PartnerRoleImportService;
import com.taobao.cun.crius.oss.client.FileStoreService;
import com.taobao.cun.endor.base.client.EndorApiClient;
import com.taobao.cun.endor.base.dto.UserRoleAddDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 导入角色
 * 
 * @author chengyu.zhoucy
 *
 */
@HSFProvider(serviceInterface = PartnerRoleImportService.class)
public class PartnerRoleImportServiceImpl implements PartnerRoleImportService {
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	@Resource
	private EndorApiClient storeEndorApiClient;
	@Resource
	private FileStoreService fileStoreService;
	
	@Override
	public String importRoleByStationIds(List<Long> stationIds, Long orgId, String roleName, String creator) {
		final String identifier = creator + "_" + System.currentTimeMillis() + ".json";
		ImportInfo importInfo = new ImportInfo();
		importInfo.setCreateTime(new Date());
		importInfo.setCreator(creator);
		importInfo.setOrgId(orgId);
		importInfo.setRoleName(roleName);
		importInfo.setStationIds(stationIds);
		
		final AtomicInteger successNum = new AtomicInteger(0);
		final AtomicInteger failNum = new AtomicInteger(0);
		final List<ErrorMsg> errorMsgs = Lists.newArrayList();
		importInfo.setErrorMsgs(errorMsgs);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Long stationId : stationIds) {
					boolean success = false;
					PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
					if(partnerStationRel == null) {
						errorMsgs.add(new ErrorMsg(stationId, "找不到村小二"));
					}else {
						if(!partnerStationRel.getType().equals(PartnerInstanceTypeEnum.TP.getCode()) && !partnerStationRel.getType().equals(PartnerInstanceTypeEnum.TPS.getCode())) {
							errorMsgs.add(new ErrorMsg(stationId, "不能添加该类型的合伙人：" + partnerStationRel.getType()));
						}else if(!isServicing(partnerStationRel)) {
							errorMsgs.add(new ErrorMsg(stationId, "该服务站不在服务中"));
						}else {
							UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
							userRoleAddDto.setCreator(creator);
							userRoleAddDto.setEndTime(DateUtil.parseDate("2050-01-01"));
							userRoleAddDto.setOrgId(orgId);
							userRoleAddDto.setRoleName(roleName);
							userRoleAddDto.setUserId(String.valueOf(partnerStationRel.getTaobaoUserId()));
							try {
								storeEndorApiClient.getUserRoleServiceClient().addUserRole(userRoleAddDto, null);
								success = true;
							}catch(Exception e) {
								errorMsgs.add(new ErrorMsg(stationId, ExceptionUtils.getStackFrames(e)));
							}
						}
						
						if(success) {
							successNum.incrementAndGet();
						}else {
							failNum.incrementAndGet();
						}
					}
				}
				importInfo.setFailNum(failNum.get());
				importInfo.setSuccessNum(successNum.get());
				fileStoreService.saveFile(identifier, identifier, JSON.toJSONString(importInfo).getBytes(),"application/json");
			}}).start();
		
		return "http://crius.cn-hangzhou.oss-cdn.aliyun-inc.com/" + identifier;
	}
	
	/**
	 * 检查站点状态
	 * @param station
	 * @return
	 */
	private boolean isServicing(PartnerStationRel partnerStationRel) {
		return partnerStationRel.getState().equals(StationStatusEnum.DECORATING.getCode()) || 
				partnerStationRel.getState().equals(StationStatusEnum.SERVICING.getCode()) ||
				partnerStationRel.getState().equals(StationStatusEnum.CLOSING.getCode());
	}
	public static class ImportInfo{
		@JSONField(ordinal=1)
		private String creator;
		@JSONField(ordinal=5)
		private Long orgId;
		@JSONField(ordinal=10)
		private String roleName;
		@JSONField(ordinal=20)
		private Date createTime;
		@JSONField(ordinal=30)
		private int successNum;
		@JSONField(ordinal=40)
		private int failNum;
		@JSONField(ordinal=50)
		private List<Long> stationIds;
		@JSONField(ordinal=60)
		private List<ErrorMsg> errorMsgs;
		
		public Long getOrgId() {
			return orgId;
		}
		public void setOrgId(Long orgId) {
			this.orgId = orgId;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		public String getCreator() {
			return creator;
		}
		public void setCreator(String creator) {
			this.creator = creator;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public int getSuccessNum() {
			return successNum;
		}
		public void setSuccessNum(int successNum) {
			this.successNum = successNum;
		}
		public int getFailNum() {
			return failNum;
		}
		public void setFailNum(int failNum) {
			this.failNum = failNum;
		}
		public List<Long> getStationIds() {
			return stationIds;
		}
		public void setStationIds(List<Long> stationIds) {
			this.stationIds = stationIds;
		}
		public List<ErrorMsg> getErrorMsgs() {
			return errorMsgs;
		}
		public void setErrorMsgs(List<ErrorMsg> errorMsgs) {
			this.errorMsgs = errorMsgs;
		}
	}
	public static class ErrorMsg{
		private Long stationId;
		
		private String[] msgs;
		public ErrorMsg(Long stationId, String[] msgs) {
			this.stationId = stationId;
			this.msgs = msgs;
		}
		
		public ErrorMsg(Long stationId, String msg) {
			this(stationId, new String[] {msg});
		}
		public Long getStationId() {
			return stationId;
		}

		public void setStationId(Long stationId) {
			this.stationId = stationId;
		}
		public String[] getMsgs() {
			return msgs;
		}
		public void setMsgs(String[] msgs) {
			this.msgs = msgs;
		}
	}
}
