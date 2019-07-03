package com.taobao.cun.auge.contactrecord.service;

import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordAddDto;

/**
 * 政府拜访小记写服务
 *
 * @author chengyu.zhoucy
 */
public interface CuntaoGovContactRecordWrite {
    /**
     * 添加小记
     * @param cuntaoGovContactRecordAddDto
     * @return
     */
    Long add(CuntaoGovContactRecordAddDto cuntaoGovContactRecordAddDto);

    /**
     * 删除小记
     * @param id
     * @param operator
     */
    void delete(Long id, String operator);
}
