package com.taobao.cun.auge.opensearch;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StationQueryOpenSearchParser implements OpenSearchParser {
    @Override
    public OpenSearchSearchResult parse(String search) {
        OpenSearchSearchResult reurnResult = new OpenSearchSearchResult();
        JSONObject json = JSONObject.parseObject(search);
        if(json!=null){
            if("FAIL".equals(json.getString("status"))){
                throw new RuntimeException(json.get("errors").toString());
            }else if("OK".equals(json.getString("status"))){
                JSONObject result = JSONObject.parseObject(json.getString("result"));
                reurnResult.setFindNum(result.getIntValue("viewtotal"));
                reurnResult.setAllNum(result.getIntValue("total"));
                reurnResult.setReturnNum(result.getIntValue("num"));
                reurnResult.setSearchTime(result.getIntValue("searchtime"));
                reurnResult.setSuccess(true);
                List<StationBuyerQueryListVo> returnList = new ArrayList<StationBuyerQueryListVo>();
                JSONArray jsonArray = JSONArray.parseArray(result.getString("items"));
                if(jsonArray!=null&&jsonArray.size()>0){
                    for(int i=0;i<jsonArray.size();i++){
                        JSONObject jo =(JSONObject)jsonArray.get(i);
                        StationBuyerQueryListVo stationBuyerQueryListVo = JSONObject.parseObject(jo.getString("fields"), StationBuyerQueryListVo.class);
                        stationBuyerQueryListVo.setVariableValue(getVariableValue(jo.getString("variableValue")));
                        returnList.add(stationBuyerQueryListVo);
                    }
                }
                reurnResult.setItems(returnList);
            }
        }
        return reurnResult;
    }

    public OrginOpenSearchReturnVariableValue getVariableValue(String variableValue){
        JSONObject variableValueObjecct = JSONObject.parseObject(variableValue);
        if (variableValueObjecct==null) {
            return null;
        }
        String distanceValue = variableValueObjecct.getString("distance_value");
        if (StringUtil.isBlank(distanceValue) || distanceValue.length()<=1) {
            return null;
        }
        OrginOpenSearchReturnVariableValue orginOpenSearchReturnVariableValue = new OrginOpenSearchReturnVariableValue();
        List<String> extInfoList = new ArrayList<String>();
        extInfoList.add(distanceValue.substring(1, distanceValue.length() - 1));
        orginOpenSearchReturnVariableValue.setExtInfo(extInfoList);
        return orginOpenSearchReturnVariableValue;
    }
}
