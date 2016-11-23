package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

public class LevelExamingResult implements Serializable {

    private static final long serialVersionUID = 8682215953115205099L;
    /**
     * 是否通过了所有分发的晋升考试
     */
    private boolean           isPassedAllExams;
    
    /**
     * 没有通过的晋升考试层级(晋升S7需要通过S5,S6,S7的所有考试.)
     */
    private List<String>      notPassLevelExams;
    
    /**
     * 通过的层级考试
     */
    private List<String>      passedLevelExams;

    public LevelExamingResult(){super();}
    public LevelExamingResult(boolean isPassedAllExams, List<String> notPassLevelExams, List<String> passedLevels) {
        super();
        this.isPassedAllExams = isPassedAllExams;
        this.notPassLevelExams = notPassLevelExams;
        this.passedLevelExams = passedLevels;
    }

    public boolean isPassedAllExams() {
        return isPassedAllExams;
    }

    public void setPassedAllExams(boolean isPassedAllExams) {
        this.isPassedAllExams = isPassedAllExams;
    }

    public List<String> getNotPassLevelExams() {
        return notPassLevelExams;
    }

    public void setNotPassLevelExams(List<String> notPassLevelExams) {
        this.notPassLevelExams = notPassLevelExams;
    }
    
    public List<String> getPassedLevelExams() {
        return passedLevelExams;
    }
    
    public void setPassedLevelExams(List<String> passedLevelExams) {
        this.passedLevelExams = passedLevelExams;
    }

}
