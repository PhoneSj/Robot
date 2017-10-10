package com.zige.robot.bean;

import java.util.List;

/**
 * Created by lidingwei on 2017/7/6 0006.
 */
public class ColorBean {




    private List<ColorBeanItem> list;

    public List<ColorBeanItem> getList() {
        return list;
    }

    public void setList(List<ColorBeanItem> list) {
        this.list = list;
    }

    public static class ColorBeanItem{

        private int alpha;
        private int red;
        private int green;
        private int blue;

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }


        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public ColorBeanItem(int alpha, int red, int green, int blue) {
            this.alpha = alpha;
            this.red   = red;
            this.green = green;
            this.blue  = blue;
        }
    }


}