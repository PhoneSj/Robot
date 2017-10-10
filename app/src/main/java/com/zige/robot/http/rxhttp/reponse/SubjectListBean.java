package com.zige.robot.http.rxhttp.reponse;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class SubjectListBean {

    /**
     * code : 200
     * data : {"hasNextPage":false,"list":[{"icon":"http","subjectId":13234124121313,"subjectName":"语文"}]}
     * message : SUCCESS
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * hasNextPage : false
         * list : [{"icon":"http","subjectId":13234124121313,"subjectName":"语文"}]
         */

        private boolean hasNextPage;
        private List<ListBean> list;

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * icon : http
             * subjectId : 13234124121313
             * subjectName : 语文
             */

            private String icon;
            private long subjectId;
            private String subjectName;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public long getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(long subjectId) {
                this.subjectId = subjectId;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }
        }
    }
}
