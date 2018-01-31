package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.StationNumConfig;
import com.taobao.cun.auge.dal.domain.StationNumConfigExample;
import com.taobao.cun.auge.dal.domain.StationNumConfigExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationNumConfigMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("stationNumConfigBO")
public class StationNumConfigBOImpl implements StationNumConfigBO {
	
	@Autowired
	StationNumConfigMapper stationNumConfigMapper;
	
	@Autowired
	private StationBO stationBO;
	
	@Override
	public StationNumConfig getConfigByProvinceCode(String provinceCode,
			StationNumConfigTypeEnum typeEnum) {
		ValidateUtils.notNull(provinceCode);
		ValidateUtils.notNull(typeEnum);
		StationNumConfigExample example = new StationNumConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andProvinceCodeEqualTo(provinceCode);
		criteria.andTypeEqualTo(typeEnum.getCode());
		
		List<StationNumConfig> res = stationNumConfigMapper.selectByExample(example);
		return ResultUtils.selectOne(res);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateSeqNum(String provinceCode,
			StationNumConfigTypeEnum typeEnum, String seqNum) {
		ValidateUtils.notNull(provinceCode);
		ValidateUtils.notNull(typeEnum);
		ValidateUtils.notNull(seqNum);
		
		StationNumConfigExample example = new StationNumConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andProvinceCodeEqualTo(provinceCode);
		criteria.andTypeEqualTo(typeEnum.getCode());
		
		StationNumConfig record = new StationNumConfig();
		record.setSeqNum(seqNum);
		DomainUtils.beforeUpdate(record, "sys");
		
		stationNumConfigMapper.updateByExampleSelective(record, example);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateSeqNumByStationNum(String provinceCode,
			StationNumConfigTypeEnum typeEnum, String stationNum) {
		StationNumConfig sc = getConfigByProvinceCode(provinceCode,typeEnum);
		String pre =getStationNumPre(sc).toString();
		String seqNum = stationNum.replace(pre, "");
		updateSeqNum(provinceCode,typeEnum,seqNum);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public String createStationNum(String provinceCode,
			StationNumConfigTypeEnum typeEnum,int level) {
		if (level== 20) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"村点编号创建异常");
		}
		StationNumConfig sc = getConfigByProvinceCode(provinceCode,typeEnum);
		if (sc==null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"村点编号配置信息不存在");
		}
		String seqNum = sc.getSeqNum();
		Integer  i = Integer.parseInt(seqNum);
		String str =  String.format("%0"+seqNum.length()+"d", ++i);  //字符串加一    
		
		StringBuilder sb =getStationNumPre(sc);
		sb.append(str);
		String stationNum = sb.toString();
		
		
		int count = stationBO.getStationCountByStationNum(stationNum);
		if(count < 1){
			return stationNum;
		}
		updateSeqNumByStationNum(provinceCode,typeEnum,stationNum);
		createStationNum(provinceCode,typeEnum,++level);
		return stationNum;
	}
	
	private StringBuilder  getStationNumPre(StationNumConfig sc){
		StringBuilder sb = new StringBuilder();
		//sb.append("NO.");
		sb.append(StringUtils.upperCase(sc.getProvinceAb()));
		sb.append(StringUtils.upperCase(sc.getType()));
		return sb;
	}
}
