package com.taobao.cun.auge.jingwei.station;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.DeleteEvent;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.InsertEvent;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;
import com.google.common.base.Strings;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.log.bo.SyncLogBo;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.service.PartnerService;
import com.taobao.cun.endor.dto.BizUserRole;
import com.taobao.cun.endor.dto.User;
import com.taobao.cun.endor.exception.UserNotExistRuntimeException;
import com.taobao.cun.endor.service.UserRoleService;
import com.taobao.cun.endor.service.UserService;

/**
 * Station状态为NEW的时候将他同步到ENDOR，作为一个组织
 * 当状态变化QUIT的时候，从ENDOR删除
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class EndorPartnerJingweiTask implements InitializingBean{
	@Value("${jingwei.taskid.endor.partner}")
	private String taskId;
	
	private Client client;
	
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private UserService userService;
	@Resource
	private PartnerService partnerService;
	@Resource
	private SyncLogBo syncLogBo;
	@Resource
	private PartnerInstanceBO partnerInstanceBO;
	
	private Long rootId = 2L;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
			@Override
			public Result onReceiveMessage(List<EventMessage> messages) {
				for(EventMessage msg : messages){
					handleMessage(msg);
				}
				return Result.ACK_AND_NEXT;
			}
			private void handleMessage(EventMessage msg) {
				SyncLog syncLog = null;
				try{
					if(msg instanceof UpdateEvent){
						UpdateEvent updateEvent = (UpdateEvent) msg;
						List<Map<String, Serializable>> modifiedRows = updateEvent.getModifyRowDataMaps();
						for(int index = 0; index < modifiedRows.size(); index++){
							Map<String, Serializable> modifiedRow = modifiedRows.get(index);
							Map<String, Serializable> rowDataMap = updateEvent.getRowDataMaps().get(index);
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "partner", rowDataMap, modifiedRow).toSyncLog());
							Long taobaoUserId = (Long)(rowDataMap.get("taobao_user_id"));
							Long stationId = (Long)(rowDataMap.get("station_id"));
							String type = (String) rowDataMap.get("type");
							String state = (String) modifiedRow.get("state");
							if(modifiedRow.containsKey("is_deleted") && "y".equals(modifiedRow.get("is_deleted"))){
								deleteUserRole(taobaoUserId, stationId, type);
							}else if(!Strings.isNullOrEmpty(state)){
								if(isNeedSync(state)){
									addUserRole(taobaoUserId, stationId, type);
								}else{
									deleteUserRole(taobaoUserId, stationId, type);
								}
							}
						}
					}else if(msg instanceof InsertEvent){
						List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
						for(Map<String, Serializable> rowDataMap : rowDataMaps){
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "partner", rowDataMap, null).toSyncLog());
							String state = (String) rowDataMap.get("state");
							if(isNeedSync(state)){//无论是怎样插入的（数据订正、创建的...）只要不是QUIT状态，那么都同步到ENDOR
								Long taobaoUserId = (Long)(rowDataMap.get("taobao_user_id"));
								Long stationId = (Long)(rowDataMap.get("station_id"));
								String type = (String) rowDataMap.get("type");
								addUserRole(taobaoUserId, stationId, type);
							}
						}
					}else if(msg instanceof DeleteEvent){
						List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
						for(Map<String, Serializable> rowDataMap : rowDataMaps){
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "partner", rowDataMap, null).toSyncLog());
							Long taobaoUserId = (Long)(rowDataMap.get("taobao_user_id"));
							Long stationId = (Long)(rowDataMap.get("station_id"));
							String type = (String) rowDataMap.get("type");
							deleteUserRole(taobaoUserId, stationId, type);
						}
					}
					syncLog.setState("success");
					syncLogBo.updateState(syncLog);
				}catch(Exception e){
					syncLog.setState("fail");
					syncLog.setErrorMsg(e.getMessage());
					syncLogBo.updateState(syncLog);
				}
			}
			private void addUserRole(Long taobaoUserId, Long stationId, String type) throws Exception{
				PartnerDto partnerDto = partnerService.getNormalPartnerByTaobaoUserId(taobaoUserId);
				if(partnerDto == null){
					throw new Exception("no partner, taobaouserId=" + taobaoUserId);
				}
				User user = new User();
				user.setUserId(String.valueOf(partnerDto.getTaobaoUserId()));
				user.setUserName(partnerDto.getName());
				user.setCreator("sys");
				user.setModifier("sys");
				user.setState("Normal");
				userService.save("cuntaostore", user);
				BizUserRole bizUserRole = new BizUserRole();
				bizUserRole.setBizOrgId(rootId);//不区分组织，验权而已，并不关心组织
				bizUserRole.setBizUserId(String.valueOf(taobaoUserId));
				bizUserRole.setRoleName(type);
				bizUserRole.setCreator("sys");
				bizUserRole.setModifier("sys");
				userRoleService.addBizUserRole("cuntaostore", bizUserRole);
			}
			
			private boolean isNeedSync(String state){
				return PartnerInstanceStateEnum.SERVICING.getCode().equals(state) 
						|| PartnerInstanceStateEnum.DECORATING.getCode().equals(state)
						|| PartnerInstanceStateEnum.CLOSED.getCode().equals(state)
						|| PartnerInstanceStateEnum.CLOSING.getCode().equals(state)
						|| PartnerInstanceStateEnum.QUITING.getCode().equals(state);
						
			}
			
			private void deleteUserRole(Long taobaoUserId, Long bizOrgId, String roleName) {
				//如果还存在服务中的记录，那么不用删除
				PartnerStationRel rel = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SERVICING);
				if(rel != null && roleName.equals(rel.getType())){
					return;
				}
				try{
					userRoleService.deleteBizUserRole("cuntaostore", String.valueOf(taobaoUserId), rootId, roleName);
				}catch(UserNotExistRuntimeException e){
					//忽略
				}
			}});
		client.startTask();
	}
	
}