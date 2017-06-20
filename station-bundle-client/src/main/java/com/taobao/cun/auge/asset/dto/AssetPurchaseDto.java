package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 资产采购Dto
 * @author quanzhu.wangqz
 *
 */
public class AssetPurchaseDto extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 7100435184292730173L;
	
    private String poNo;
    
    private Long  ownerOrgId;
    
    private String ownerOrgName;
    
    private String ownerName;
    
    private String ownerWorkno;
    
    private List<AssetPurchaseDetailDto> detailDto;

	public List<AssetPurchaseDetailDto> getDetailDto() {
		return detailDto;
	}

	public void setDetailDto(List<AssetPurchaseDetailDto> detailDto) {
		this.detailDto = detailDto;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getOwnerOrgId() {
		return ownerOrgId;
	}

	public void setOwnerOrgId(Long ownerOrgId) {
		this.ownerOrgId = ownerOrgId;
	}

	public String getOwnerOrgName() {
		return ownerOrgName;
	}

	public void setOwnerOrgName(String ownerOrgName) {
		this.ownerOrgName = ownerOrgName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerWorkno() {
		return ownerWorkno;
	}

	public void setOwnerWorkno(String ownerWorkno) {
		this.ownerWorkno = ownerWorkno;
	}
}
