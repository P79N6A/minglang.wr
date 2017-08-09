package com.taobao.cun.auge.opensearch;


import java.util.ArrayList;
import java.util.List;

public class OpenSearchSearchResult {
        private boolean isSuccess  = false;
        private String errorMsg;
        protected int   searchTime = 0;
        private String  searchURL;  

        protected int     findNum;  
        protected int     allNum; 
        protected int     restrictNum; 
        protected int     returnNum;  
        protected boolean hasPrevPage;  
        protected boolean hasNextPage;  

        private List items = new ArrayList();
        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public int getSearchTime() {
            return searchTime;
        }

        public void setSearchTime(int searchTime) {
            this.searchTime = searchTime;
        }

        public String getSearchURL() {
            return searchURL;
        }

        public void setSearchURL(String searchURL) {
            this.searchURL = searchURL;
        }

        public int getFindNum() {
            return findNum;
        }

        public void setFindNum(int findNum) {
            this.findNum = findNum;
        }

        public int getAllNum() {
            return allNum;
        }

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public int getRestrictNum() {
            return restrictNum;
        }

        public void setRestrictNum(int restrictNum) {
            this.restrictNum = restrictNum;
        }

        public int getReturnNum() {
            return returnNum;
        }

        public void setReturnNum(int returnNum) {
            this.returnNum = returnNum;
        }

        public boolean isHasPrevPage() {
            return hasPrevPage;
        }

        public void setHasPrevPage(boolean hasPrevPage) {
            this.hasPrevPage = hasPrevPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
