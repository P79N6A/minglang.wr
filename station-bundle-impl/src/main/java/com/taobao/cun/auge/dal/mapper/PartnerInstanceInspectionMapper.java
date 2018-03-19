package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstanceInspection;
import com.taobao.cun.auge.dal.example.PartnerInstanceInspectionExample;

/**
 * 巡检查询Mapper
 * @author zhenhuan.zhangzh
 *
 */

public interface PartnerInstanceInspectionMapper {

	Page<PartnerInstanceInspection> selectPartnerInstanceInspectionByExample(PartnerInstanceInspectionExample example);
}
