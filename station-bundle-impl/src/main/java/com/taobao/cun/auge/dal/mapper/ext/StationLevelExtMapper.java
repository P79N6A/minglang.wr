package com.taobao.cun.auge.dal.mapper.ext;

import org.apache.ibatis.annotations.Param;

public interface StationLevelExtMapper {

	int countTownTPS(@Param("townCode")String townCode);
	
	int countTownHZD(@Param("townCode")String townCode);
	
	int countTownStation(@Param("townCode")String townCode);
	
	int countTownYoupin(@Param("townCode")String townCode);
	
	int countTransYoupin(@Param("townCode")String townCode);
	
	int countTransHZD(@Param("townCode")String townCode);
	
	int countTransTPS(@Param("townCode")String townCode);
}
