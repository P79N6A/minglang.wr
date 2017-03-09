package com.taobao.cun.auge.station.dto;

public class StationQuitStatusDto {

	private StationStatusDto quitApplying;
	
	private StationStatusDto quitApplyConfirmed;
	
	private StationStatusDto quitAuditing;
	
	private StationStatusDto closedWaitThaw;
	
	private StationStatusDto quit;

	public StationStatusDto getQuitApplying() {
		return quitApplying;
	}

	public void setQuitApplying(StationStatusDto quitApplying) {
		this.quitApplying = quitApplying;
	}

	public StationStatusDto getQuitApplyConfirmed() {
		return quitApplyConfirmed;
	}

	public void setQuitApplyConfirmed(StationStatusDto quitApplyConfirmed) {
		this.quitApplyConfirmed = quitApplyConfirmed;
	}

	public StationStatusDto getQuitAuditing() {
		return quitAuditing;
	}

	public void setQuitAuditing(StationStatusDto quitAuditing) {
		this.quitAuditing = quitAuditing;
	}

	public StationStatusDto getClosedWaitThaw() {
		return closedWaitThaw;
	}

	public void setClosedWaitThaw(StationStatusDto closedWaitThaw) {
		this.closedWaitThaw = closedWaitThaw;
	}

	public StationStatusDto getQuit() {
		return quit;
	}

	public void setQuit(StationStatusDto quit) {
		this.quit = quit;
	}
}
