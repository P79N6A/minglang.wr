package com.taobao.cun.auge.station.enums;

public enum StationType {

	STATION,
	
	STORE;
	
	private int type;
	
    private StationType(){
    	type = (1 << ordinal());
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
