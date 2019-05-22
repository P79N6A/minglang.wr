package com.taobao.cun.auge.level.dto;

import java.io.Serializable;
import java.util.Objects;

public class TownLevelDto implements Serializable {

	private static final long serialVersionUID = 8160607257608356049L;

	private Long id;

    private String provinceName;

    private String countyName;

    private String townName;

    private String townCode;

    private String level;
    
    private Long townPopulation;

    private String countyCode;

    private String provinceCode;

    private String cityName;

    private String cityCode;

    private Integer mTaobaoUserNum;

    private Integer coverageRate;

    private Long elecPredictionGmv;

    private Long taobaoGmv;
    
    private String chengguanTown;
    
    private TownLevelStationRuleDto townLevelStationRuleDto;

    public String getChengguanTown() {
		return chengguanTown;
	}

	public void setChengguanTown(String chengguanTown) {
		this.chengguanTown = chengguanTown;
	}

	public String getLevelDesc() {
    	return TownLevelEnum.valueOf(level).getDesc();
    }
    
	public TownLevelStationRuleDto getTownLevelStationRuleDto() {
		return townLevelStationRuleDto;
	}

	public void setTownLevelStationRuleDto(TownLevelStationRuleDto townLevelStationRuleDto) {
		this.townLevelStationRuleDto = townLevelStationRuleDto;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getTownPopulation() {
		return townPopulation;
	}

	public void setTownPopulation(Long townPopulation) {
		this.townPopulation = townPopulation;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getmTaobaoUserNum() {
		return mTaobaoUserNum;
	}

	public void setmTaobaoUserNum(Integer mTaobaoUserNum) {
		this.mTaobaoUserNum = mTaobaoUserNum;
	}

	public Integer getCoverageRate() {
		return coverageRate;
	}

	public void setCoverageRate(Integer coverageRate) {
		this.coverageRate = coverageRate;
	}

	public Long getElecPredictionGmv() {
		return elecPredictionGmv;
	}

	public void setElecPredictionGmv(Long elecPredictionGmv) {
		this.elecPredictionGmv = elecPredictionGmv;
	}

	public Long getTaobaoGmv() {
		return taobaoGmv;
	}

	public void setTaobaoGmv(Long taobaoGmv) {
		this.taobaoGmv = taobaoGmv;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TownLevelDto other = (TownLevelDto) obj;
		return Objects.equals(id, other.id);
	}
}
