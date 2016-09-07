package com.taobao.cun.auge.station.dto;

public class StationEnterStatusDto {

	private StationStatusDto sumitted;
	
	private StationStatusDto confirmed;
	
	private StationStatusDto unpayCourse;

	private StationStatusDto unpayDecorate;
	
	private StationStatusDto unsigned;
	
	private StationStatusDto decWaitAudit;
	
	private StationStatusDto servicing;

	public StationStatusDto getSumitted() {
		return sumitted;
	}

	public void setSumitted(StationStatusDto sumitted) {
		this.sumitted = sumitted;
	}

	public StationStatusDto getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(StationStatusDto confirmed) {
		this.confirmed = confirmed;
	}

	public StationStatusDto getUnpayCourse() {
		return unpayCourse;
	}

	public void setUnpayCourse(StationStatusDto unpayCourse) {
		this.unpayCourse = unpayCourse;
	}

	public StationStatusDto getUnpayDecorate() {
		return unpayDecorate;
	}

	public void setUnpayDecorate(StationStatusDto unpayDecorate) {
		this.unpayDecorate = unpayDecorate;
	}

	public StationStatusDto getUnsigned() {
		return unsigned;
	}

	public void setUnsigned(StationStatusDto unsigned) {
		this.unsigned = unsigned;
	}

	public StationStatusDto getDecWaitAudit() {
		return decWaitAudit;
	}

	public void setDecWaitAudit(StationStatusDto decWaitAudit) {
		this.decWaitAudit = decWaitAudit;
	}

	public StationStatusDto getServicing() {
		return servicing;
	}

	public void setServicing(StationStatusDto servicing) {
		this.servicing = servicing;
	}

	
}
