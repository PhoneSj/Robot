package com.zige.colorrecolibrary;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.colorreco.libface.CRFace;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zige on 2017/8/21.
 */

public class FaceColorRec {
    private ArrayList<DubePerson> persons = new ArrayList<DubePerson>();
    private static final float CONFIDENCE = 0.70f;
    //最低人脸质量
    private static final int MIN_QU = 75;
    private String robotId = "";
    private boolean inited = false;



    public FaceColorRec(String robotId, String uid, String deviceKey, String host) {
        this.robotId = robotId;

        initFace(host, uid, deviceKey);
    }

    private void initFace(final String host, final String uid, final String deviceKey){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                CRFace.getInstance().loadLibrarySys("crface");
                //1:success
                int res = CRFace.getInstance().initSDK("/sdcard/colorreco");
                e.onNext(res);
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<Integer, ObservableSource<DubePersonList>>() {
            @Override
            public ObservableSource<DubePersonList> apply(@NonNull Integer integer) throws Exception {
                String sign = "robotDeviceId" + robotId;
                return RetrofitApi.getInstance(host).getApi().getAllUserByUserId(robotId, uid, deviceKey);
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<DubePersonList>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DubePersonList dubePersonList) {
                persons.clear();
                if ("0000".equals(dubePersonList.getCode())) {
                    if (dubePersonList.getUserInfoList() != null && dubePersonList.getUserInfoList().size() > 0) {
                        for (DubePersonList.UserInfoListBean faceUserListBean : dubePersonList.getUserInfoList()) {
                            if (!TextUtils.isEmpty(faceUserListBean.getFaceInfo1())){
                                String localKey = "";
                                try {
                                    localKey = FaceColorRec.getLocalKey(faceUserListBean.getFaceInfo1());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                DubePerson dubePerson = new DubePerson(faceUserListBean.getUsername(), faceUserListBean.getPersonId()+"", localKey);
                                dubePerson.setType(faceUserListBean.getTitle());
                                dubePerson.setGender(faceUserListBean.getSex() + "");
                                dubePerson.setAge(faceUserListBean.getAge() + "");
                                persons.add(dubePerson);
                            }
                            if (!TextUtils.isEmpty(faceUserListBean.getFaceInfo2())){
                                String localKey = "";
                                try {
                                    localKey = FaceColorRec.getLocalKey(faceUserListBean.getFaceInfo2());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                DubePerson dubePerson = new DubePerson(faceUserListBean.getUsername(), faceUserListBean.getPersonId()+"", localKey);
                                dubePerson.setType(faceUserListBean.getTitle());
                                dubePerson.setGender(faceUserListBean.getSex() + "");
                                dubePerson.setAge(faceUserListBean.getAge() + "");
                                persons.add(dubePerson);
                            }
                            if (!TextUtils.isEmpty(faceUserListBean.getFaceInfo3())){
                                String localKey = "";
                                try {
                                    localKey = FaceColorRec.getLocalKey(faceUserListBean.getFaceInfo3());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                DubePerson dubePerson = new DubePerson(faceUserListBean.getUsername(), faceUserListBean.getPersonId()+"", localKey);
                                dubePerson.setType(faceUserListBean.getTitle());
                                dubePerson.setGender(faceUserListBean.getSex() + "");
                                dubePerson.setAge(faceUserListBean.getAge() + "");
                                persons.add(dubePerson);
                            }
                        }
                    }
                    inited = true;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public String getFaceLandmark(byte[] data, int width, int height) {
        long l = System.currentTimeMillis();
        long cost = l;
        //获取人脸数据
        int faces[] = CRFace.getInstance().detectARGB(data,
                width,
                height, 1, null);
        String _s = faces != null && faces[0] > 0 ? "有人脸" : "无人脸";
        log("颜鉴 : 获取人脸数据 -> " + (System.currentTimeMillis() - l) + "ms, " + _s);
        l = System.currentTimeMillis();
        float feature[] = new float[256];
        if (faces != null && faces[0] > 0) {
            int tmpBox[] = new int[4];
            tmpBox[0] = faces[1];
            tmpBox[1] = faces[2];
            tmpBox[2] = faces[3];
            tmpBox[3] = faces[4];
            int res = CRFace.getInstance().extractARGB(data, width, height, tmpBox, feature);
            log("颜鉴 : 抽取关键点数据 -> " + (System.currentTimeMillis() - l) + "ms");
            l = System.currentTimeMillis();
            //256：正确；-2：人脸框错误；-3：抽取特征失败；-4：图片过大；-5：授权问题
            if (res == 256) {
                String s = "";
                for (float f : feature) {
                    s += (f + ",");
                }
                return s;
            }

        } else {
            return null;
        }

        return null;
    }

    public boolean hasFace(byte[] data, int width, int height) {
        long l = System.currentTimeMillis();
        long cost = l;
        //获取人脸数据
        int faces[] = CRFace.getInstance().detectARGB(data,
                width,
                height, 1, null);
        String _s = faces != null && faces[0] > 0 ? "有人脸" : "无人脸";
        log("颜鉴 : 获取人脸数据 -> " + (System.currentTimeMillis() - l) + "ms, " + _s);
        if (faces != null && faces[0] > 0) {
            log("颜鉴 : 人脸质量 -> " + faces[5]);
            return true;
        } else {
            return false;
        }
    }

    public boolean isMatch(String token, DubePersonList.UserInfoListBean person){
        String[] strs = token.split(",");
        float[] landmarkNew = new float[256];
        int _i = 0;
        for (String _v : strs) {
            try {
                float _f = Float.valueOf(_v);
                landmarkNew[_i] = _f;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            _i++;

        }
        ArrayList<float[]> landmarks = new ArrayList<>();
        if (!TextUtils.isEmpty(person.getFaceInfo1())){
            String[] _strs = FaceColorRec.getLocalKey(person.getFaceInfo1()).split(",");
            float[] landmark = new float[256];
            int i = 0;
            for (String _v : _strs) {
                try {
                    float _f = Float.valueOf(_v);
                    landmark[i] = _f;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;

            }
            landmarks.add(landmark);
        }
        if (!TextUtils.isEmpty(person.getFaceInfo2())){
            String[] _strs = FaceColorRec.getLocalKey(person.getFaceInfo2()).split(",");
            float[] landmark = new float[256];
            int i = 0;
            for (String _v : _strs) {
                try {
                    float _f = Float.valueOf(_v);
                    landmark[i] = _f;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;

            }
            landmarks.add(landmark);
        }
        if (!TextUtils.isEmpty(person.getFaceInfo3())){
            String[] _strs = FaceColorRec.getLocalKey(person.getFaceInfo3()).split(",");
            float[] landmark = new float[256];
            int i = 0;
            for (String _v : _strs) {
                try {
                    float _f = Float.valueOf(_v);
                    landmark[i] = _f;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;

            }
            landmarks.add(landmark);
        }

        for (float[] fs : landmarks){
            float f = CRFace.getInstance().match(landmarkNew, fs);
            if (f > CONFIDENCE){
                return true;
            }
        }
        return false;
    }

    public DubePerson getPersonByARGB(int width, int height, byte[] data) {
        long l = System.currentTimeMillis();
        long cost = l;
        //获取人脸数据
        int faces[] = CRFace.getInstance().detectARGB(data,
                width,
                height, 1, null);
        String _s = faces != null && faces[0] > 0 ? "有人脸" : "无人脸";
        log("颜鉴 : 获取人脸数据 -> " + (System.currentTimeMillis() - l) + "ms, " + _s);
        l = System.currentTimeMillis();
        float feature[] = new float[256];
        if (faces != null && faces[0] > 0) {
            int tmpBox[] = new int[4];
            tmpBox[0] = faces[1];
            tmpBox[1] = faces[2];
            tmpBox[2] = faces[3];
            tmpBox[3] = faces[4];
            int res = CRFace.getInstance().extractARGB(data, width,
                    height, tmpBox, feature);
            log("颜鉴 : 抽取关键点数据 -> " + (System.currentTimeMillis() - l) + "ms");
            l = System.currentTimeMillis();
            //256：正确；-2：人脸框错误；-3：抽取特征失败；-4：图片过大；-5：授权问题
            if (res == 256) {
                DubePerson d = null;
                float maxConfidence = 0f;
                for (DubePerson person : persons) {
                    //isExtractOk = true;
                    String[] _strs = person.getFaceId().split(",");
                    float[] landmark = new float[256];
                    int i = 0;
                    for (String _v : _strs) {
                        try {
                            float _f = Float.valueOf(_v);
                            landmark[i] = _f;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;

                    }
                    float f = CRFace.getInstance().match(landmark, feature);
                    if (f > maxConfidence) {
                        maxConfidence = f;
                        d = person;

                    }
                    log("颜鉴 : 对比关键点数据 -> " + (System.currentTimeMillis() - l) + "ms");
                    log("颜鉴 : score -> " + f);
                    log("颜鉴 : 总共消耗 -> " + (System.currentTimeMillis() - cost) + "ms");
                }

                if (maxConfidence >= CONFIDENCE) {
                    log("颜鉴 : person -> " + d);
                    return d;
                }
                return new DubePerson("", "-1", "");
            }

        } else {
            return new DubePerson("", "-1", "");
        }

        return new DubePerson("", "-1", "");
    }

    private void log(String s) {
        Log.e("FaceColorRec", s);
    }

    // 加密
    public static String getServiceKey(String str) {
        String encodedString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        return encodedString;
    }

    // 解密
    public static String getLocalKey(String s) {
        String decodedString = new String(Base64.decode(s, Base64.DEFAULT));
        return decodedString;
    }

    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    public static String SHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"), 0, text.length());
            byte[] sha1hash = md.digest();
            return byteArrayToHex(sha1hash).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);

    }

    public byte[] getPixelsBGRA(Bitmap image) {

        ByteBuffer buf_img1 = ByteBuffer.allocate(image.getWidth() * image.getHeight() * 4);
        image.copyPixelsToBuffer(buf_img1);
        return buf_img1.array();

//        // calculate how many bytes our image consists of
//        int bytes = image.getByteCount();
//
//        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
//        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
//
//        byte[] temp = buffer.array(); // Get the underlying array containing the data.
//
//        byte[] pixels = new byte[temp.length]; // Allocate for BGRA
//
//        // Copy pixels into place
//        for (int i = 0; i < (temp.length / 4); i++) {
//
//            pixels[i * 4] = temp[i * 4 + 2];        //B
//            pixels[i * 4 + 1] = temp[i * 4 + 1];//G
//            pixels[i * 4 + 2] = temp[i * 4];       //R
//            pixels[i * 4 + 3] = temp[i * 4 + 3];        //A
//
//        }
//
//        return pixels;
    }
}
