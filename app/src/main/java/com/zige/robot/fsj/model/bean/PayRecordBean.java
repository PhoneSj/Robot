package com.zige.robot.fsj.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class PayRecordBean {

    /**
     * total : 2
     * page : 0
     * rows : [{"orderNo":"079376029f944ce39fd449743e486bf4","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505789141000},{"orderNo":"29c1acfd84614bca9389693e38998d79","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505823087000},{"orderNo":"3b4897521e074699888bd5027cc676b7","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505785889000},{"orderNo":"4c36996d1ed14ddf956ac781ba85bbae","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505819937000},{"orderNo":"50ea690ecbd44e1f804c50355970a7ca","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505815982000},{"orderNo":"6019e447e4754ca5af3f44e329ef91a7","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505819591000},{"orderNo":"7343ac7e676a455da0ddff4880ff8f16","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505786164000},{"orderNo":"8aebed999d134a528ca75565edd8b443","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505789176000},{"orderNo":"aa42ea0a238e4e2696b5453103178a7c","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505876088000},{"orderNo":"adb8dde2d66f456db5c62b3f49308734","robotId":"75a58e603b5d8c86","fee":1,"chargeTime":null,"createTime":1505802768000}]
     */

    private int total;
    private int page;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * orderNo : 079376029f944ce39fd449743e486bf4
         * robotId : 75a58e603b5d8c86
         * fee : 1
         * chargeTime : null
         * createTime : 1505789141000
         */

        private String orderNo;
        private String robotId;
        private int fee;
        private int chargeTime;
        private long createTime;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRobotId() {
            return robotId;
        }

        public void setRobotId(String robotId) {
            this.robotId = robotId;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getChargeTime() {
            return chargeTime;
        }

        public void setChargeTime(int chargeTime) {
            this.chargeTime = chargeTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
