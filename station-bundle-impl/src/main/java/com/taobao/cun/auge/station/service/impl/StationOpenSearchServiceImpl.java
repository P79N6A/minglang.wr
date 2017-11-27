package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensearch.javasdk.CloudsearchSearch;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRelExample;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoCainiaoStationRelMapper;
import com.taobao.cun.auge.opensearch.OpenSearchManager;
import com.taobao.cun.auge.opensearch.OpenSearchSearchResult;
import com.taobao.cun.auge.opensearch.StationBuyerQueryListDtoConverter;
import com.taobao.cun.auge.opensearch.StationSearchConfig;
import com.taobao.cun.auge.opensearch.StationSearchServiceConstants;
import com.taobao.cun.auge.station.dto.StationOpenSearchDto;
import com.taobao.cun.auge.station.dto.StationOpenSearchQueryModel;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerTpgService;
import com.taobao.cun.auge.station.service.StationOpenSearchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.mtop.common.Result;
@Service("stationOpenSearchService")
@HSFProvider(serviceInterface = StationOpenSearchService.class)
public class StationOpenSearchServiceImpl implements StationOpenSearchService {
	private static final Logger logger = LoggerFactory
			.getLogger(StationOpenSearchServiceImpl.class);
	@Autowired
	OpenSearchManager openSearchManager;
	@Autowired
	PartnerTpgService partnerTpgService;
	@Autowired
	TairCache tairCache;

	@Autowired
    CuntaoCainiaoStationRelMapper cuntaoCainiaoStationRelMapper;

	@Override
	public Result<List<StationOpenSearchDto>> queryStationsByName(
			String stationName) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		try {
			validate(stationName);
		} catch (Exception e) {
			logger.error("BusinessException", e);
			result.setSuccess(false);
			result.setMsgCode("FAIL_BIZ_BUSSINESS_ERROR");
			result.setMsgInfo(e.getMessage());
			return result;
		}
		return buildMtopResult(buildStationNameSearch(stationName));
	}

	@Override
	public Result<List<StationOpenSearchDto>> queryStationsByName(
			String userId, String stationName) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		Result<Object> ret = checkQueryValid(userId);
		if (null == ret || !ret.isSuccess()) {
			result.setSuccess(false);
			result.setMsgCode(ret.getMsgCode());
			result.setMsgInfo(ret.getMsgInfo());
			return result;
		}
		return queryStationsByName(stationName);
	}

	@Override
	public Result<List<StationOpenSearchDto>> queryStationsByLngAndLat(
			String lng, String lat) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		if (StringUtils.isBlank(lng)||StringUtils.isBlank(lat)){
			logger.error("ParamException,lng or lat is null");
			result.setSuccess(false);
			result.setMsgCode("FAIL_BIZ_PARAM_ERROR");
			result.setMsgInfo("lng or lat is null");
			return result;
		}
		return buildMtopResult(buildLngAndLatSearch(lng, lat));
	}

	@Override
	public Result<List<StationOpenSearchDto>> queryStationsByLngAndLatLimit(
			String lng, String lat, String taobaoUserId) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String key = "queryStationsByLngAndLat_" + taobaoUserId + "_"
				+ sdf.format(new Date());
		Integer value = tairCache.get(key);
		if (value != null && value > StationSearchConfig.USER_VISIT_TIMES) {
			logger.error("visit more than "
					+ StationSearchConfig.USER_VISIT_TIMES);
			result.setSuccess(false);
			result.setMsgCode("FAIL_BIZ_BUSSINESS_ERROR");
			result.setMsgInfo("访问次数超过" + StationSearchConfig.USER_VISIT_TIMES
					+ "次");
			return result;
		}
		tairCache.incr(key, 1, 1, 3600 * 24);
		return queryStationsByLngAndLat(lng, lat);
	}

	@Override
	public Result<List<StationOpenSearchDto>> queryStationByAddressCode(
			StationOpenSearchQueryModel stationQueryModel) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		result.setSuccess(false);
		if (!validQueryModel(stationQueryModel)) {
			return result;
		}
		Result<Object> ret = checkQueryValid(stationQueryModel.getUserId() + "");
		if (null == ret || !ret.isSuccess()) {
			result.setSuccess(false);
			result.setMsgCode(ret.getMsgCode());
			result.setMsgInfo(ret.getMsgInfo());
			return result;
		}
		try {
			List<StationOpenSearchDto> stationDtos = null;
			Long code = validAddressCode(stationQueryModel.getVillageCode());
			if (null == code || code == 0) {
				stationDtos = getStationBySearch(buildAddressCodeSearch(stationQueryModel));
			} else {
				stationDtos = getStationBySearch(buildVillageCodeSearch(code));
				if (null == stationDtos || stationDtos.size() == 0) {
					stationDtos = getStationBySearch(buildAddressCodeSearch(stationQueryModel));
				}
			}
			if (null == stationDtos) {
				result.setModel(new ArrayList<StationOpenSearchDto>());
				result.setSuccess(true);
				return result;
			}
			result.setSuccess(true);
			result.setModel(stationDtos);
			return result;
		} catch (Exception e) {
			result.setSuccess(true);
			result.setModel(new ArrayList<StationOpenSearchDto>());
		}
		return result;
	}

	@Override
	public Result<Boolean> queryStationByVillageCode(Long villageCode) {
		Result<Boolean> result = new Result<Boolean>();
		result.setSuccess(false);
		if (null == villageCode || villageCode == 0) {
			return result;
		}
		Result<List<StationOpenSearchDto>> stds = buildMtopResult(buildVillageCodeSearch(villageCode));
		if (null == stds || !stds.isSuccess() || null == stds.getModel()) {
			result.setSuccess(false);
			return result;
		}
		if (stds.getModel().size() == 0) {
			result.setSuccess(true);
			result.setModel(false);
			return result;
		}
		result.setSuccess(true);
		result.setModel(true);
		return result;
	}

	private boolean validQueryModel(StationOpenSearchQueryModel model) {
		if (null == model) {
			return false;
		}
		Long userId = model.getUserId();
		if (null == userId || userId == 0) {
			return false;
		}
		String areaCode = model.getAreaCode();
		String townCode = model.getTownCode();
		if (StringUtils.isBlank(townCode) && StringUtils.isBlank(areaCode)) {
			return false;
		}
		String lng = model.getLng();
		String lat = model.getLat();
		if (StringUtils.isBlank(lng) || StringUtils.isBlank(lat)) {
			return false;
		}
		return true;
	}

	private Long validAddressCode(String code) {
		if (StringUtils.isBlank(code)) {
			return 0L;
		}
		try {
			Long codeLong = Long.valueOf(code);
			if (null == codeLong || codeLong == 0) {
				return 0L;
			}
			return codeLong;
		} catch (Exception e) {
			return 0L;
		}
	}

	private Result<Object> checkQueryValid(String userId) {
		Result<Object> result = new Result<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String key = "queryStationsByName_Day_" + userId + "_"
				+ sdf.format(new Date());
		String key1 = "queryStationsByName_Month_" + userId + "_"
				+ sdf.format(new Date());
		Integer value = tairCache.get(key1);
		if (value != null
				&& value > StationSearchConfig.SEARCH_VISIT_MONTH_TIME) {
			logger.info("visit more than "
					+ StationSearchConfig.SEARCH_VISIT_MONTH_TIME);
			result.setSuccess(false);
			result.setMsgCode("FAIL_BIZ_BUSSINESS_ERROR");
			result.setMsgInfo("月访问次数超过"
					+ StationSearchConfig.SEARCH_VISIT_MONTH_TIME + "次");
			return result;
		}

		Integer dayValue = tairCache.get(key);
		if (value != null && value > StationSearchConfig.SEARCH_VISIT_DAY_TIME) {
			logger.info("visit more than "
					+ StationSearchConfig.SEARCH_VISIT_DAY_TIME);
			result.setSuccess(false);
			result.setMsgCode("FAIL_BIZ_BUSSINESS_ERROR");
			result.setMsgInfo("日访问次数超过"
					+ StationSearchConfig.SEARCH_VISIT_DAY_TIME + "次");
			return result;
		}

		tairCache.incr(key, 1, 1, 3600 * 24);
		tairCache.incr(key1, 1, 1, 3600 * 24 * 30);
		result.setSuccess(true);
		return result;
	}

	private CloudsearchSearch buildAddressCodeSearch(
			StationOpenSearchQueryModel queryModel) {
		CloudsearchSearch cloudsearchSearch = openSearchManager
				.getCloudsearchSearch();
		String townCode = queryModel.getTownCode();
		if (!StringUtils.isBlank(townCode) && !townCode.equals("0")) {
			cloudsearchSearch.setQueryString("town:'"
					+ queryModel.getTownCode() + "'");
		} else {
			cloudsearchSearch.setQueryString("county:'"
					+ queryModel.getAreaCode() + "'");
		}
		cloudsearchSearch
				.addFilter("state=\"SERVICING\" OR state=\"DECORATING\"");
		cloudsearchSearch.setPair("longtitude_in_query:"
				+ Double.parseDouble(queryModel.getLng()) / 100000d
				+ ",latitude_in_query:"
				+ Double.parseDouble(queryModel.getLat()) / 100000d);
		cloudsearchSearch.addSort("RANK", CloudsearchSearch.SORT_INCREASE);
		cloudsearchSearch.setFormulaName("distance_value");
		return cloudsearchSearch;
	}

	private CloudsearchSearch buildVillageCodeSearch(Long villageCode) {
		CloudsearchSearch cloudsearchSearch = openSearchManager
				.getCloudsearchSearch();
		if (null == villageCode || villageCode == 0) {
			return null;
		} else {
			cloudsearchSearch.setQueryString("village:'" + villageCode + "'");
		}

		cloudsearchSearch
				.addFilter("state=\"SERVICING\" OR state=\"DECORATING\"");

		return cloudsearchSearch;
	}

	private List<StationOpenSearchDto> getStationBySearch(
			CloudsearchSearch cloudsearchSearch) {
		if (null == cloudsearchSearch) {
			return new ArrayList<StationOpenSearchDto>();
		}
		OpenSearchSearchResult openSearchSearchResult = openSearchManager
				.doQuery(cloudsearchSearch);
		if (null == openSearchSearchResult
				|| !openSearchSearchResult.isSuccess()) {
			return new ArrayList<StationOpenSearchDto>();
		}
		if (null == openSearchSearchResult.getItems()
				|| openSearchSearchResult.getItems().size() == 0) {
			return new ArrayList<StationOpenSearchDto>();
		}
		List<StationOpenSearchDto> stationDtos = realStationExitFilter(openSearchSearchResult);
		if (null == stationDtos || stationDtos.size() == 0) {
			return new ArrayList<StationOpenSearchDto>();
		}
		if (stationDtos.size() <= StationSearchServiceConstants.QUERY_BY_DISTANCE_FETCH_SIZE) {
			return stationDtos;
		}
		return stationDtos.subList(
				StationSearchServiceConstants.QUERY_BY_DISTANCE_FETCH_START,
				StationSearchServiceConstants.QUERY_BY_DISTANCE_FETCH_SIZE);
	}

	private Result<List<StationOpenSearchDto>> buildMtopResult(
			CloudsearchSearch cloudsearchSearch) {
		Result<List<StationOpenSearchDto>> result = new Result<List<StationOpenSearchDto>>();
		try {
			OpenSearchSearchResult openSearchSearchResult = openSearchManager
					.doQuery(cloudsearchSearch);
			if (openSearchSearchResult.isSuccess()) {
				result.setSuccess(true);
				result.setModel(realStationExitFilter(openSearchSearchResult));
				if (result.getModel() != null
						&& result.getModel().size() >= StationSearchServiceConstants.QUERY_BY_NAME_FETCH_SIZE) {
					result.setMsgInfo("您的关键词搜索范围太广，请输入村服务点或者代购员名字搜索。");
				}
			} else {
				result.setSuccess(true);
				result.setModel(new ArrayList<StationOpenSearchDto>());
			}
		} catch (Exception e) {
			logger.error("buildMtopResult Exception", e);
			result.setSuccess(true);
			result.setModel(new ArrayList<StationOpenSearchDto>());
		}
		return result;
	}

	// 存在实际站点的数据才作为返回的结果集
	private List<StationOpenSearchDto> realStationExitFilter(
			OpenSearchSearchResult openSearchSearchResult) {
		List<StationOpenSearchDto> rm = new ArrayList<StationOpenSearchDto>();
		List<StationOpenSearchDto> orgData = StationBuyerQueryListDtoConverter
				.convertStationBuyerQueryListVoList(openSearchSearchResult
						.getItems());
		if (orgData == null) {
			return rm;
		}
		for (StationOpenSearchDto st : orgData) {
			CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
			Criteria criteria = example.createCriteria();
			criteria.andIsDeletedEqualTo("n");
			criteria.andObjectIdEqualTo(st.getId());
			criteria.andTypeEqualTo("STATION");
			List<CuntaoCainiaoStationRel> rel = cuntaoCainiaoStationRelMapper.selectByExample(example);
			if (rel.size()>0&& !partnerTpgService.hasTpgTag(st.getUserId())) {
				rm.add(st);
			}
		}

		return rm;
	}

	private CloudsearchSearch buildLngAndLatSearch(String lng, String lat) {
		CloudsearchSearch cloudsearchSearch = openSearchManager
				.getCloudsearchSearch();
		cloudsearchSearch.setQueryString("''");
		cloudsearchSearch
				.setStartHit(StationSearchServiceConstants.QUERY_BY_DISTANCE_FETCH_START);
		cloudsearchSearch
				.setHits(StationSearchServiceConstants.QUERY_BY_DISTANCE_FETCH_SIZE);
		cloudsearchSearch.setRerankSize(2000);
		cloudsearchSearch
				.addFilter("state=\"SERVICING\" OR state=\"DECORATING\"");
		cloudsearchSearch.setPair("longtitude_in_query:"
				+ Double.parseDouble(lng) / 100000d + ",latitude_in_query:"
				+ Double.parseDouble(lat) / 100000d);
		cloudsearchSearch.addSort("RANK", CloudsearchSearch.SORT_INCREASE);
		cloudsearchSearch.setFormulaName("distance_value");

		return cloudsearchSearch;
	}


	private CloudsearchSearch buildStationNameSearch(String name) {
		CloudsearchSearch cloudsearchSearch = openSearchManager
				.getCloudsearchSearch();
		String[] params = getQueryParams(name);
		if (null == params || params.length == 0) {
			cloudsearchSearch.setQueryString("name_sws:'" + name
					+ "' OR applier_name_sws:'" + name + "'");
		} else if (params.length == 1) {
			cloudsearchSearch.setQueryString("name_sws:'" + params[0]
					+ "' OR applier_name_sws:'" + params[0] + "'");
		} else {
			cloudsearchSearch.setQueryString("(name_sws:'" + params[0]
					+ "' AND applier_name_sws:'" + params[1] + "')"
					+ "OR (name_sws:'" + params[1] + "'AND applier_name_sws:'"
					+ params[0] + "')");
		}

		cloudsearchSearch.setStartHit(StationSearchServiceConstants.QUERY_BY_NAME_FETCH_START);
		cloudsearchSearch.setHits(StationSearchServiceConstants.QUERY_BY_NAME_FETCH_SIZE);
		cloudsearchSearch.addFilter("state=\"SERVICING\" OR state=\"DECORATING\"");
		cloudsearchSearch.setFormulaName("docmatch");
		return cloudsearchSearch;
	}

	private void validate(String name) {
		if (StringUtils.isBlank(name)) {
			throw new RuntimeException("name is null!");
		}
		if (StationSearchServiceConstants.STATION_SEARCH_BLACK_STRING
				.contains("," + name + ",")) {
			throw new AugeBusinessException("FAIL_BIZ_BLACK_LIST_ERROR", "您的关键词"
				+ name + "搜索范围太广，请输入村服务点或者代购员名字搜索");
		}
	}

	private String[] getQueryParams(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}

		return StringUtils.split(name, ' ');
	}

	public void setPartnerTpgService(PartnerTpgService partnerTpgService) {
		this.partnerTpgService = partnerTpgService;
	}
}
