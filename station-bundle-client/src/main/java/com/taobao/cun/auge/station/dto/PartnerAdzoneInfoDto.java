package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerAdzoneInfoDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4341153662556589640L;
	/**
     * 村淘站点ID
     */
    private Long stationId;
    /**
     * 村小二淘宝ID
     */
    private Long taobaoUserId;
    /**
     * 阿里妈妈PID
     */
    private String pid;
    
    private Boolean lxState;
    
    public Boolean getLxState() {
		return lxState;
	}

	public void setLxState(Boolean lxState) {
		this.lxState = lxState;
	}

	public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
