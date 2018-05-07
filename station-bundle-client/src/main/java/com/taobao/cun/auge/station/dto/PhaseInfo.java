package com.taobao.cun.auge.station.dto;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class PhaseInfo implements Serializable{
    private static final long serialVersionUID = -1319353485191831335L;
    private String type;

    private String className;

    private String desc;

    private String event;

    private List<StepInfo> stepInfos = Lists.newArrayList();

    public List<StepInfo> getStepInfos() {
        return stepInfos;
    }

    public void setStepInfos(List<StepInfo> stepInfos) {
        this.stepInfos = stepInfos;
    }

    public String getType() {
        return type;
    }

    public void addStepInfo(StepInfo stepInfo) {
        this.stepInfos.add(stepInfo);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
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
        PhaseInfo other = (PhaseInfo) obj;
        if (className == null) {
            if (other.className != null) {
                return false;
            }
        } else if (!className.equals(other.className)) {
            return false;
        }
        if (desc == null) {
            if (other.desc != null) {
                return false;
            }
        } else if (!desc.equals(other.desc)) {
            return false;
        }
        if (event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!event.equals(other.event)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
