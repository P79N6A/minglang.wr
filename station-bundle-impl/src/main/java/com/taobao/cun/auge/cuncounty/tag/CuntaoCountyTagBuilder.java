package com.taobao.cun.auge.cuncounty.tag;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import reactor.util.function.Tuple2;

/**
 * 标签构建接口
 *
 * @author chengyu.zhoucy
 */
public interface CuntaoCountyTagBuilder {
    /**
     * 构建标签
     * @param tuple
     * @return
     */
    Tuple2<CountyTag, CuntaoCountyListItem> build(Tuple2<CountyTag, CuntaoCountyListItem> tuple);
}
