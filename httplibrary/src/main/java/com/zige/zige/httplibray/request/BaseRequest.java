package com.zige.zige.httplibray.request;

import android.util.Log;

import com.zige.zige.httplibray.Delivery;
import com.zige.zige.httplibray.HttpUtils;
import com.zige.zige.httplibray.Listener;
import com.zige.zige.httplibray.NetworkResponse;
import com.zige.zige.httplibray.Request;
import com.zige.zige.httplibray.Response;
import com.zige.zige.httplibray.SHA;
import com.zige.zige.httplibray.ServerError;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

/**
 * 功能：
 * <p/>
 * Created by user on 2016/4/26.
 */
public abstract class BaseRequest<T> extends Request<T> {
    private static String msCookie = null;

    public BaseRequest(int method, String url, Listener<T> listener) {
        super(method, url, listener);
        Log.d("httpurl  == ", url);
        if (msCookie != null && msCookie.length() > 0) {
            addHeader("Cookie", msCookie);
        }
    }

    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    public byte[] handleResponse(HttpResponse response, Delivery delivery) throws IOException, ServerError {
        String cookie = HttpUtils.getHeader(response, "Set-Cookie");
        if (cookie != null && cookie.length() > 0) {
            msCookie = cookie;
        }
        return super.handleResponse(response, delivery);
    }

    public static String formatUrl(String requestUri, Map<String, String> requestArg) {
        StringBuilder stringBuilder = new StringBuilder();
        String shacode = "";
        if (requestArg != null && !requestArg.isEmpty()) {
            String[] args = requestArg.keySet().toArray(new String[requestArg.size()]);
            Arrays.sort(args);

            for(String key : args){
                stringBuilder.append(stringBuilder.length() == 0 ? "?" : "&");
                try {
                    stringBuilder.append(key).append("=").append(URLEncoder.encode((requestArg.get(key) == null ? "" : requestArg.get(key)),"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                shacode += key + (requestArg.get(key) == null ? "" : requestArg.get(key));
            }
        }

        shacode = HttpUtils.appKey + shacode + HttpUtils.Secret;
        try {
            shacode = SHA.SHA1(shacode).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuilder.append(stringBuilder.length() == 0 ? "?" : "&");
        stringBuilder.append("sign").append("=").append(shacode);
        return stringBuilder.insert(0, requestUri).toString();
    }

    public static String formatUrl(String requestUrl, String requestUri, Map<String, String> requestArg) {
        StringBuilder stringBuilder = new StringBuilder();
        if (requestArg != null && !requestArg.isEmpty()) {
            for (Map.Entry<String, String> entry : requestArg.entrySet()) {
                stringBuilder.append(stringBuilder.length() == 0 ? "?" : "&");
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return stringBuilder.insert(0, requestUri).insert(0, requestUrl).toString();
    }
}
