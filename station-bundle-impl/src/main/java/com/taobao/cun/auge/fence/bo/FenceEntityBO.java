package com.taobao.cun.auge.fence.bo;

/**
 * Created by xiao on 18/6/20.
 */
public interface FenceEntityBO {

    void enableEntityListByTemplateId(Long templateId, String operator);

    void disableEntityListByTemplateId(Long templateId, String operator);

}
