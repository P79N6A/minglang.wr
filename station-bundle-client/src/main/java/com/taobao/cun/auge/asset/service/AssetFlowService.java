package com.taobao.cun.auge.asset.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.common.PageDto;

public interface AssetFlowService {
	
    /**
     * 分页查询资产申请列表
     * @param CuntaoAssetFlowQueryDto
     * @return
     */
	 public PageDto<CuntaoAssetFlowDto> queryByPage(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto);
	 
	 /**
	 * 获得流程相关信息
	 * @param assetFlowId
	 * @return CuntaoAssetFlowDto
	 */
	public CuntaoAssetFlowDto getFlowById(Long assetFlowId);
	
	/**
	 * 保存资产流程
	 * @param cuntaoAssetFlowDto
	 * @param contextDto
	 * @return flowId
	 */
	public void saveFlow(CuntaoAssetFlowDto cuntaoAssetFlowDto, String operator);
	
	/**
	 * 获得资产概况
	 * @param applyOrgId
	 * @return
	 */
	public Map<String, List<CuntaoAssetSituationDto>> getAssetSituation(Long applyOrgId);
	
	/**
	 * 更新资产流程
	 * @param cuntaoAssetFlowDto
	 * @param contextDto
	 * @return
	 */
	public void updateFlow(CuntaoAssetFlowDto cuntaoAssetFlowDto,String operator);
	
	/**
	 * 审核资产流程
	 * @param cuntaoAssetFlowDto
	 * @param contextDto
	 * @return
	 */
	public void audit(CuntaoAssetFlowAuditDto cuntaoAssetFlowAuditDto,String operator);
	/**
	 * 资产流程导出excel
	 * @param cuntaoAssetFlowDetailForExcelDO
	 * @return List<CuntaoAssetFlowDetailForExcelDO>
	 */
	public List<CuntaoAssetFlowDetailForExcelDto> getDetailForExcel(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto);
	
	/**
	 * 资产流程导出excel总数
	 * @param cuntaoAssetFlowDetailForExcelDO
	 * @return List<CuntaoAssetFlowDetailForExcelDO>
	 */
	public Integer getDetailCountForExcel(CuntaoAssetFlowQueryDto cuntaoAssetFlowQueryDto);
	
	/**
	 * 撤回资产流程
	 * @param cuntaoAssetFlowDto
	 * @param contextDto
	 * @return
	 */
	public void cancelFlow(Long assetFlowId,String operator);
	
	/**
	 * 删除资产流程
	 * @param cuntaoAssetFlowDto
	 * @param contextDto
	 * @return
	 */
	public void deleteFlow(Long assetFlowId,String operator);
	
	/**
	 * 批量更新资产申请明细
	 * @param details
	 * @return
	 * @throws Exception
	 */
	public void updateFlowDetails(List<CuntaoAssetFlowDetailDto> details,String operator);
}
