package com.zige.colorrecolibrary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zige on 2017/8/21.
 */

public class DubePersonList {
    /**
     * code : 0000
     * message : 成功
     * userInfoList : [{"username":"007","age":null,"title":null,"personId":"1502849784060","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170816_101517.jpg"},{"username":"008","age":null,"title":null,"personId":"1502850914567","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170816_103427.jpg"},{"username":"01","age":null,"title":null,"personId":"0399aa7736730a39f54ea85afbeeabbf","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170816_110424.jpg"},{"username":"林大帅一","age":99,"title":"哥哥","personId":"1502864410416","sex":1,"urlFace1":"http://r.mento.ai/IMG_20170816_071322.jpg"},{"username":"柳树","age":null,"title":null,"personId":"-1","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170821_134932.jpg"},{"username":"333","age":null,"title":null,"personId":"-11.494027","sex":0,"urlFace1":null},{"username":"333","age":null,"title":null,"personId":"-11.494027,6.6729007,-10.239399,10.784286,-11.832237,7.9293633,13.730293,-3.9168253,-8.511157,3.5203648,4.93328,-11.675706,-8.366643,-4.9283695,-5.685634,8.868105,15.511188,4.7083316,22.14344,13.536538,-6.0159273,-7.830031,-7.823435,1.5740722,3.3446863,-13.744383,2.276909,-6.337411,-3.5688996,16.433657,6.8877053,-13.5429125,2.3632052,-2.1884413,12.412715,-5.0532365,-4.420659,-2.4729815,-10.539148,12.860466,-1.1671977,4.0969586,7.8017206,-8.806517,15.162819,-14.797838,9.852023,9.426738,18.73494,3.5556104,8.973603,-15.524259,-1.334969,1.4582943,12.86969,-16.193546,4.9822254,2.775888,20.290958,-4.8696404,-12.999149,11.273623,-13.015183,-3.447359,4.583958,2.308997,13.849163,-2.0021653,8.468562,-6.582308,-14.421715,-5.2680144,14.263844,-1.5003768,-0.2659163,4.4247427,3.4072251,0.71511936,6.5744014,0.04792119,5.969858,-7.760525,-0.9914744,8.724032,-7.012834,1.7616491,7.529767,-3.6371076,-2.501961,3.2288141,14.425382,-1.1308646,14.688792,3.7485008,-12.07353,4.8860626,1.7382346,-5.0554414,13.00237,1.0275147,10.306189,-0.93530744,-3.2427201,2.120253,-13.488516,-1.4621037,-2.9104044,11.427881,-6.591972,14.577817,-6.287976,-0.4669019,-14.41481,-1.3266804,-22.236858,-5.775948,-0.38846424,7.265823,-14.029999,19.626486,-20.699055,10.298654,4.147623,-11.840094,3.4439032,-15.434387,-2.0956523,0.49935105,4.6967254,-6.278685,-12.642318,4.2482452,-7.07363,3.8061316,3.7406611,-5.049212,-3.0777326,14.877795,6.8181715,0.39004096,16.443947,21.001698,0.7927256,13.02562,18.720797,10.048404,-10.493763,-13.405271,13.407057,-7.628065,-13.56502,0.6404695,-4.6708755,-7.2527127,10.585429,-3.0314047,-0.03508028,-10.563329,-0.9598037,-10.093665,1.3377818,-23.880966,-13.542554,-8.237209,-6.983985,-0.2605105,-7.9711046,-6.4745173,17.68084,3.3140202,11.887982,-1.0628523,-2.4710462,2.1377866,-3.4093783,-10.235935,-6.8388186,6.531388,-7.2294,7.582688,-19.666845,3.1002944,17.627085,1.8982797,1.2063582,-3.6223724,-9.00421,-9.240464,10.726263,-10.147872,-7.7843924,-2.2501197,-12.433702,-8.768848,0.29204866,-5.6483593,5.000458,10.109092,-11.552229,0.051460907,-4.61681,3.5907695,-5.7104,8.807527,2.058085,5.5410013,-9.539903,6.819349,12.225315,22.532585,-10.318938,-2.0441175,2.274442,5.2846937,3.129113,15.200552,-19.81152,-16.397305,19.785973,-10.386865,-7.9634027,-10.231192,9.469007,-7.2417545,-17.930887,-5.503596,2.468055,6.4367805,-0.41291842,-0.4568457,2.2342846,-3.82038,-7.9998584,6.3520775,4.884177,17.977112,-10.362311,24.485819,4.272186,16.790733,10.145502,-4.720929,2.8458204,4.549418,5.47285,-5.6976614,-0.7207378,-14.097473,10.635262,1.5075778,-9.603215,-0.392418,-6.4430866,-1.1737003,-2.6155145,2.6737537,","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170821_140132.jpg"},{"username":"233","age":null,"title":"爸爸","personId":"LTQuNzkwMTc2NCw1LjkxODQ1OCwtMTAuMzYxMDY2LDE5LjY1NDY4NCwtMTAuMDkwNjQ4LDYuNjk5\nNjQyNyw2LjYxMjIzLC0xLjM2NDk2MjYsLTExLjY0NzQ4Myw1LjEyMjc0ODQsMC42NTczNzQ1Niwt\nMi44MDIyMzU0LC02LjE1MTc4OSwtOC40ODYyMzIsLTUuNzA4NjI4LDQuODY1MDA2LDExLjAwNzc5\nOSwtNi4yNDk4OTEzLDEzLjI5NzU0Miw4LjQ5NjExOTUsLTAuOTg4MDI3MywtNi45MjA4MzUsLTYu\nNjY1NzEzLDYuNDA0MzA3LDUuNjI1OTk0NywtNC4zNjE1NDgsNS4yOTAxNjIsLTEuNzYyNTQwNSwt\nOS45NTMxNTcsMTEuMzQ0NDQ5LDguNDMwMDg4LC04Ljk2NTYxNSwtMi4yNTUzNTEzLC00LjU2NDc1\nOSwyMC4zMzY0NDksNS4zMzE3NTA0LC01LjU2MDMzNSwtNS44MDc5NzEsLTE0LjI3MTg1MjUsMTMu\nNzAzMDc5LDUuMTE5NDE3Nyw0LjQzMjM0OCw1LjY3MjA5NDMsLTEwLjYzMjkzNywxOS4wNTMyNDQs\nLTkuMTc0MjI0LDAuNTM1MDYzNzQsOC42Nzc2MiwxMS42MjE4MTEsNS4yNTQxNjM3LDAuNjAwMzQw\nMywtOS4xMTk5MDUsLTMuNDI4OTIzMSw1LjQ4NjE1NDYsNi4yODkxNjkzLC0xNC4zOTk3NDQsMi41\nMzI1ODczLDAuMDY5MjM0NDE2LDE1Ljc4OTEzOSwtNS42MzAxOTcsLTE3Ljc2OTMyNywxMy4wODM3\nMiwtOS42ODU1MDMsMS4yOTM3MTMzLDYuNDk1MTMyLC0yLjEzMTYwNDIsMTIuNzM3MTQyLC0wLjQ5\nNDA1NzMzLDIuMjY0NDY2OCwtOS4wMzg5NDksLTExLjA1NjgwMiwtMi45MzA1NCwxNy42OTQ2MDEs\nLTcuMDIzMDk2LC0wLjI5MTA3Nzg1LDIuNzcxOTY3LC0yLjUwNzM2NTIsNy43NzU1NDIzLDkuMTAw\nMTU5LC04LjE2NjQ3NCw5Ljk4Nzg4MiwtNy43ODU2NzAzLDcuMDc1Njc0LDIuMjI1MjIwNywtNS4z\nMzEzNjEzLDguMDQzMTk2LDguMTU3NTcxLC0xMC4xNDA0MTQsLTEuNzYzNDU3NSwxLjE1NjQyOSwx\nNS45NzM0MzgsNi42NDE3Njk0LDMuNTIzMzkwOCwzLjM5MDI1NTIsLTguOTcwNjc1LDIuNTAwODU3\nNCwyLjQwNDQzMiwtNC4xNDU5MDgsMTMuNzYyOTk5LC0xLjE2MzMxOTEsOC4xODMyNDUsLTkuMDg0\nNzMsLTEuMjEwMzkyNiwzLjY2NjUzMjMsLTE0LjQ5MTY0OSwtNi44MDQ3NjQsLTEwLjc1NzcxLDku\nNTM4MDc4LC02LjAxMTE3NjYsMTIuOTU3MTM0LC03LjA0ODQ1Miw3LjMyOTYwNjUsLTExLjk5OTgw\nNCwtMi45OTQ4NDQ0LC0xNy42MjIwMjUsMS45Mjc1NjcsLTUuNjQ3MjIyNSw2LjQzMTMxNSwtNS41\nMjU0MDgzLDguMjc4Mzk4NSwtMTQuMjM1ODM1LDQuMTM3NTU1LC0yLjgwOTEzMTksLTkuODA4Mzkz\nNSwtNi45MDA2Njc3LC0xNS4yNDA5MzcsLTQuNDg3OTU5LDUuMDQ3ODA1Myw2LjczMjEyMDUsLTEx\nLjkwODEzMSwtMi4yNzcyODUzLDQuODQ5NTMzLC03LjM5Mzg3NTYsOS40MDg4ODksNC43NTI3NTcs\nLTYuNDU4Mjk2Myw3LjgzMzg4NywxMi40ODQzNjQ1LDIuOTM0NTk5Miw0LjI1OTE1MjQsMTQuMTMw\nNjYzLDI4LjIzOTI5NCwxLjk5Njk0ODUsMTAuMjEzMTI1LDE4LjM2MTk0Niw5LjA0NjY0NywtMTIu\nNzcwMDExLC0xMi4xMTY0MjUsMTYuNTQ1MzA3LC03LjEwODg3NTMsLTEyLjIzOTM5NSwwLjU3MTIy\nMTIzLC02LjY3MDgwOTcsLTkuMDU0MDE4LDguNTY1NDM1LDkuMDUxMzIzLC01LjQ2MDE5LC00LjU0\nMDY1NjYsLTYuMDIxNDk1LC0xNS45NjgyOSwtNi4xMTk2MDQsLTIyLjEyOTU2OCwtMTAuODUzNzE0\nLC0yLjA1NTU4MDQsLTMuMzUwNjE2MiwtMi4yMjM5ODQ3LC05LjAwNDc4MiwtMy40NjA5MDQsMTEu\nNzY2ODYzLC0xLjgyMDA3MDMsMTEuMDkxMzQ0LC0yLjU3NDU2ODcsLTEuNTMxNzI2NSwtNC4xODEx\nOTY3LC05LjE3NDk2NiwtMi4zNTkxOTUsLTcuMTE3Njc1LDEwLjgyMzk0MSwtOC4wNjkyNzMsMTQu\nOTg0NDUsLTE1LjUwOTY0NCwtMi42MzUyNTk0LDEzLjM0NTkxMiwtMS4zNDEyOTQ4LDguMTc0NzE3\nLC0wLjExNzgyNTE4LC0xMC4wNjk2NTM1LC03LjEzMTMzLDguMjg0NjkxLC02Ljk0MjQ1OTYsLTEy\nLjE0NTA2LDIuOTc1MDI3LC0xNC40NjgzMDMsLTExLjIwNTIxNiwtMC42Mzc2MDg2LC0xMi44NDIw\nOCwxLjUwODIxNzUsNS43NjAwMTEsLTYuMzU0NzQwNiwtMC41MDM1NjI2LC04LjQyMTMxOCw3LjYz\nMTkzOCw0LjIyMzQ1NTQsMTEuMzgwMTc1LDAuMDI5MjQ1MTM4LDMuMzkyMDE2LC0xMi40NTA0NTks\nLTAuNDg3Njg3MjYsNi4wMTgyNDY3LDIyLjUyODkzNiwtOC4wMDI0NTMsMS4zNjMwNTI0LC0yLjAw\nMDgxODUsMi4yMDMxODAzLDIuODE2NzUyLDExLjEyODEyOCwtMTUuMTYwOTkyLC0xNy41NTgxMDUs\nNi42MTQ0NTI0LC02LjMwOTEwNiwtNC40MDY1NDksLTEzLjEyODEyOSw1LjI5ODM3OSwtNy42NjIy\nNjYzLC0xNy4wNjY1MTcsLTAuNDQ3NTA5NTYsMy43NDE0MjM0LDkuNTE2ODU1LC0xMC4yMjk3MDEs\nNi45NjQ0NDgsMTAuNjkxMTg5LDEuNDc5MzczNywwLjc5NDkzNzU1LDEwLjk5MzA5NSw1LjEwMjc3\nNCwxOS42MTAyNDksLTE2LjU4NTExMiwxMi4wNTQ3NTksLTUuMDIzMiw5Ljc2ODIwMyw3LjYwMDAw\nMiwtNi4xNDg2NjQsNi43NTk3OSwtMC40NTc0OTEyMiwtMC45NzAyMzQ3NSwtNi40NjEwNTQzLC0y\nLjI2ODUxOTksLTguODU1MTAzNSwxMy44NjYwOTEsLTkuMzI4NzQ1LC03Ljg5NTU5MzYsMTEuMDAy\nNDg3LC04Ljk0NTU0NywyLjAyNzA4MSw1LjY0MzEwMzYsLTEuMDExNzE3Nyw=","sex":0,"urlFace1":"http://r.mento.ai/IMG_20170821_174802.jpg"}]
     */

    private String code;
    private String message;
    private List<UserInfoListBean> faceUserList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserInfoListBean> getUserInfoList() {
        return faceUserList;
    }

    public void setUserInfoList(List<UserInfoListBean> userInfoList) {
        this.faceUserList = userInfoList;
    }

    public static class UserInfoListBean implements Parcelable{

        /**
         * username : 007
         * age : null
         * title : null
         * personId : 397
         * sex : 0
         * urlFace1 : http://r.mento.ai/IMG_20170816_101517.jpg
         * urlFace2 : null
         * urlFace3 : null
         * faceInfo1 : null
         * faceInfo2 : null
         * faceInfo3 : null
         */

        private String username;
        private int age;
        private String title;
        private int personId;
        private int sex;
        private String urlFace1;
        private String urlFace2;
        private String urlFace3;
        private String faceInfo1;
        private String faceInfo2;
        private String faceInfo3;
        private String faceId1;
        private String faceId2;
        private String faceId3;


        protected UserInfoListBean(Parcel in) {
            username = in.readString();
            age = in.readInt();
            title = in.readString();
            personId = in.readInt();
            sex = in.readInt();
            urlFace1 = in.readString();
            urlFace2 = in.readString();
            urlFace3 = in.readString();
            faceInfo1 = in.readString();
            faceInfo2 = in.readString();
            faceInfo3 = in.readString();
            faceId1 = in.readString();
            faceId2 = in.readString();
            faceId3 = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(username);
            dest.writeInt(age);
            dest.writeString(title);
            dest.writeInt(personId);
            dest.writeInt(sex);
            dest.writeString(urlFace1);
            dest.writeString(urlFace2);
            dest.writeString(urlFace3);
            dest.writeString(faceInfo1);
            dest.writeString(faceInfo2);
            dest.writeString(faceInfo3);
            dest.writeString(faceId1);
            dest.writeString(faceId2);
            dest.writeString(faceId3);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserInfoListBean> CREATOR = new Creator<UserInfoListBean>() {
            @Override
            public UserInfoListBean createFromParcel(Parcel in) {
                return new UserInfoListBean(in);
            }

            @Override
            public UserInfoListBean[] newArray(int size) {
                return new UserInfoListBean[size];
            }
        };

        public String getFaceId1() {
            return faceId1;
        }

        public void setFaceId1(String faceId1) {
            this.faceId1 = faceId1;
        }

        public String getFaceId2() {
            return faceId2;
        }

        public void setFaceId2(String faceId2) {
            this.faceId2 = faceId2;
        }

        public String getFaceId3() {
            return faceId3;
        }

        public void setFaceId3(String faceId3) {
            this.faceId3 = faceId3;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPersonId() {
            return personId;
        }

        public void setPersonId(int personId) {
            this.personId = personId;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUrlFace1() {
            return urlFace1;
        }

        public void setUrlFace1(String urlFace1) {
            this.urlFace1 = urlFace1;
        }

        public String getUrlFace2() {
            return urlFace2;
        }

        public void setUrlFace2(String urlFace2) {
            this.urlFace2 = urlFace2;
        }

        public String getUrlFace3() {
            return urlFace3;
        }

        public void setUrlFace3(String urlFace3) {
            this.urlFace3 = urlFace3;
        }

        public String getFaceInfo1() {
            return faceInfo1;
        }

        public void setFaceInfo1(String faceInfo1) {
            this.faceInfo1 = faceInfo1;
        }

        public String getFaceInfo2() {
            return faceInfo2;
        }

        public void setFaceInfo2(String faceInfo2) {
            this.faceInfo2 = faceInfo2;
        }

        public String getFaceInfo3() {
            return faceInfo3;
        }

        public void setFaceInfo3(String faceInfo3) {
            this.faceInfo3 = faceInfo3;
        }

    }
}
