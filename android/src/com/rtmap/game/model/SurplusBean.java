package com.rtmap.game.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yxy
 * on 2017/8/15.
 */

public class SurplusBean implements Serializable {

    /**
     * status : 200
     * message : 成功
     * data : [{"id":4695,"num":40000,"issue":1117,"surplus_count":38883},{"id":4701,"num":3,"issue":3,"surplus_count":0},{"id":4732,"num":3,"issue":3,"surplus_count":0}]
     */


    private int status;
    private String message;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4695
         * num : 40000
         * issue : 1117
         * surplus_count : 38883
         */

        private int id;
        private int num;
        private int issue;
        private int surplusCount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getIssue() {
            return issue;
        }

        public void setIssue(int issue) {
            this.issue = issue;
        }

        public int getSurplusCount() {
            return surplusCount;
        }

        public void setSurplusCount(int surplusCount) {
            this.surplusCount = surplusCount;
        }
    }
}
