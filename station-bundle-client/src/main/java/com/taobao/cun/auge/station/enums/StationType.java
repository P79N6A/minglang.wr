package com.taobao.cun.auge.station.enums;

public enum StationType {

	STATION(1 << 0),
	
	STORE(1 << 1);
	
	private int type;
	
	
    private StationType(int type){
    	this.type = type;
    }

	public int getType() {
		return type;
	}

	public static boolean hasType(int types, StationType stationType) {
	        return (types & stationType.getType()) != 0;
	}
	
	public static int configType(int types, StationType stationType, boolean state) {
	        if (state) {
	        	types |= stationType.getType();
	        } else {
	        	types &= ~stationType.getType();
	        }
	        return types;
	}
	

}
