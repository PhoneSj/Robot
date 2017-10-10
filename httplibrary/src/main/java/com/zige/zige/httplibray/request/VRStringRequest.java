package com.zige.zige.httplibray.request;

import android.content.Context;

import com.zige.zige.httplibray.AuthFailureError;
import com.zige.zige.httplibray.Listener;
import com.zige.zige.httplibray.NetworkResponse;
import com.zige.zige.httplibray.Response;
import com.zige.zige.httplibray.toolbox.RequestParams;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 * <p/>
 * Created by user on 2016/4/26.
 */
public class VRStringRequest extends BaseRequest<String>{

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response or error message
     */
    private Map<String, String> mArgs = new HashMap<>();
    private Context context;
    private HttpEntity mHttpEntity;


    public VRStringRequest(int method, String url, Listener<String> listener) {
        super(method, url, listener);
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     */
    public VRStringRequest(String url, Listener<String> listener) {
        super(Method.GET, url, listener);
    }

    public VRStringRequest(String url,Map<String,String> mArgs, Listener<String> listener) {
        super(Method.GET, formatUrl(url,mArgs), listener);
    }

    public VRStringRequest(int method, String url,Map<String,String> mArgs, Listener<String> listener) {
        super(method, formatUrl(url,mArgs), listener);
    }

    public VRStringRequest(String url, Map<String,String> mArgs, RequestParams requestParams, Listener<String> listener) {
        super(Method.POST, formatUrl(url,mArgs), listener);
        if (requestParams != null) {
            try {
                mHttpEntity = requestParams.getEntity();
            } catch (IOException e) {/**Ignored**/}
        }
    }

//    @Override
//    public Map<String, String> getParams() throws AuthFailureError {
//        return mArgs;
//    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (mHttpEntity != null) {
            try {
                mHttpEntity.writeTo(baos);
            } catch (IOException e) {/**Ignored**/}
        }
        return baos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        if (mHttpEntity != null) {
            return mHttpEntity.getContentType().getValue();
        }
        return super.getBodyContentType();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, response.charset);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, response);
    }
}
