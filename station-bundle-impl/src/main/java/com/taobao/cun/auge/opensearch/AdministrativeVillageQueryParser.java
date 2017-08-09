package com.taobao.cun.auge.opensearch;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public class AdministrativeVillageQueryParser implements OpenSearchParser {
    @Override
    public OpenSearchSearchResult parse(String search) {
        OpenSearchSearchResult reurnResult = new OpenSearchSearchResult();
        JSONObject json = JSONObject.parseObject(search);
        if (json != null) {
            if ("FAIL".equals(json.getString("status"))) {
                throw new RuntimeException(json.get("errors").toString());
            } else if ("OK".equals(json.getString("status"))) {
                JSONObject result = JSONObject.parseObject(json.getString("result"));
                reurnResult.setFindNum(result.getIntValue("viewtotal"));
                reurnResult.setAllNum(result.getIntValue("total"));
                reurnResult.setReturnNum(result.getIntValue("num"));
                reurnResult.setSearchTime(result.getIntValue("searchtime"));
                reurnResult.setSuccess(true);
                List<AdministrativeVillageQueryResult> returnList = new ArrayList<AdministrativeVillageQueryResult>();
                JSONArray jsonArray = JSONArray.parseArray(result.getString("items"));
                if (jsonArray != null && jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jo = (JSONObject) jsonArray.get(i);
                        AdministrativeVillageQueryResult administrativeVillageQueryResult = JSONObject.parseObject(jo.getString("fields"), AdministrativeVillageQueryResult.class);
                        returnList.add(administrativeVillageQueryResult);
                    }
                }
                reurnResult.setItems(returnList);
            }
        }
        return reurnResult;
    }
}
