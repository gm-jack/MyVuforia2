package com.rtmap.game.model;

import java.io.Serializable;

/**
 * Created by yxy
 * on 2016/10/13.
 */

public class BaseBean implements Serializable{

    /**
     * code : 0
     * data : {"otherPoint":108}
     * msg : 成功
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * otherPoint : 108
         */

        private int otherPoint;

        public int getOtherPoint() {
            return otherPoint;
        }

        public void setOtherPoint(int otherPoint) {
            this.otherPoint = otherPoint;
        }
    }
}
