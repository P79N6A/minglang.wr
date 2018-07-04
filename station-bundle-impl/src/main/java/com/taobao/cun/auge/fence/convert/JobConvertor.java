package com.taobao.cun.auge.fence.convert;

import java.util.Collections;

import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;

/**
 * Created by xiao on 18/7/3.
 */
public class JobConvertor {

    public final static String TEMPLATE_UPDATE = "TEMPLATE_UPDATE";

    public final static String TEMPLATE_OPEN = "TEMPLATE_OPEN";

    public final static String TEMPLATE_CLOSE = "TEMPLATE_CLOSE";

    public final static String STATION_DELETE = "STATION_DELETE";

    public static FenceInstanceJob convertToFenceTemplateJob (Long templateId, String type) {
        if (TEMPLATE_UPDATE.equals(type)) {
            TemplateUpdateFenceInstanceJob updateJob = new TemplateUpdateFenceInstanceJob();
            updateJob.setTemplateId(templateId);
            return updateJob;
        } else if (TEMPLATE_OPEN.equals(type)) {
            TemplateOpenFenceInstanceJob openJob = new TemplateOpenFenceInstanceJob();
            openJob.setTemplateIds(Collections.singletonList(templateId));
            return openJob;
        } else if (TEMPLATE_CLOSE.equals(type)) {
            TemplateCloseFenceInstanceJob closeJob = new TemplateCloseFenceInstanceJob();
            closeJob.setTemplateIds(Collections.singletonList(templateId));
            return closeJob;
        }
        return null;
    }

    public static FenceInstanceJob convertToFenceStationJob (Long templateId, Long stationId, String type) {
        if (STATION_DELETE.equals(type)) {
            StationDeleteFenceInstanceJob job = new StationDeleteFenceInstanceJob();
            job.setStationId(stationId);
            job.setTemplateIds(Collections.singletonList(templateId));
            return job;
        }
        return null;
    }

}
