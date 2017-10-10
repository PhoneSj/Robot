package com.zige.zige.httplibray.toolbox;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能：
 * <p/>
 * Created by user on 2016/4/26.
 */
public class RequestParams implements Serializable {
    public final static String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public final static String APPLICATION_JSON = "application/json";
    protected final static String LOG_TAG = "RequestParams";
    protected boolean isRepeatable;
    protected boolean useJsonStreamer;
    protected boolean autoCloseInputStreams;
    protected final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();
    protected final ConcurrentHashMap<String, StreamWrapper> streamParams = new ConcurrentHashMap<String, StreamWrapper>();
    protected final ConcurrentHashMap<String, FileWrapper> fileParams = new ConcurrentHashMap<String, FileWrapper>();
    protected final ConcurrentHashMap<String, Object> urlParamsWithObjects = new ConcurrentHashMap<String, Object>();
    protected final ConcurrentHashMap<String, List<StreamWrapper>> streamListParams = new ConcurrentHashMap<String, List<StreamWrapper>>();
    protected final ConcurrentHashMap<String, List<FileWrapper>> fileListParams = new ConcurrentHashMap<String, List<FileWrapper>>();
    protected String contentEncoding = HTTP.UTF_8;

    /**
     * Sets content encoding for return value of {@link #getParamString()} and {@link
     * #createFormEntity()} <p>&nbsp;</p> Default encoding is "UTF-8"
     *
     * @param encoding String constant from {@link org.apache.http.protocol.HTTP}
     */
    public void setContentEncoding(final String encoding) {
        if (encoding != null) {
            this.contentEncoding = encoding;
        } else {
            Log.d(LOG_TAG, "setContentEncoding called with null attribute");
        }
    }

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public RequestParams() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new RequestParams instance containing the key/value string params from the
     * specified map.
     *
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single initial key/value
     * string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {{
            put(key, value);
        }});
    }

    /**
     * Constructs a new RequestParams instance and populate it with multiple initial key/value
     * string param.
     *
     * @param keysAndValues a sequence of keys and values. Objects are automatically converted to
     *                      Strings (including the value {@code null}).
     * @throws IllegalArgumentException if the number of arguments isn't even.
     */
    public RequestParams(Object... keysAndValues) {
        int len = keysAndValues.length;
        if (len % 2 != 0)
            throw new IllegalArgumentException("Supplied arguments must be even");
        for (int i = 0; i < len; i += 2) {
            String key = String.valueOf(keysAndValues[i]);
            String val = String.valueOf(keysAndValues[i + 1]);
            put(key, val);
        }
    }

    /**
     * Adds a key/value string pair to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * Adds a file to the request.
     *
     * @param key  the key name for the new param.
     * @param file the file to add.
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file) throws FileNotFoundException {
        put(key, file, null, null);
    }

    /**
     * Adds a file to the request with custom provided file name
     *
     * @param key            the key name for the new param.
     * @param file           the file to add.
     * @param customFileName file name to use instead of real file name
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, String customFileName, File file) throws FileNotFoundException {
        put(key, file, null, customFileName);
    }

    /**
     * Adds a file to the request with custom provided file content-type
     *
     * @param key         the key name for the new param.
     * @param file        the file to add.
     * @param contentType the content type of the file, eg. application/json
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file, String contentType) throws FileNotFoundException {
        put(key, file, contentType, null);
    }

    /**
     * Adds a file to the request with both custom provided file content-type and file name
     *
     * @param key            the key name for the new param.
     * @param file           the file to add.
     * @param contentType    the content type of the file, eg. application/json
     * @param customFileName file name to use instead of real file name
     * @throws java.io.FileNotFoundException throws if wrong File argument was passed
     */
    public void put(String key, File file, String contentType, String customFileName) throws FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        if (key != null) {
            fileParams.put(key, new FileWrapper(file, contentType, customFileName));
        }
    }

    /**
     * Add a file to the request
     * @param key the key name for the new param.
     * @param file the file to add.
     * @throws FileNotFoundException throws if wrong file argument was passed.
     */
    public void putArray(String key, File file) throws FileNotFoundException {
        putArray(key, file, null, null);
    }

    /**
     * Add a file to the request
     * @param key the key name for the new param.
     * @param customFileName file name to use instead of real file name.
     * @param file the file to add.
     * @throws FileNotFoundException
     */
    public void putArray(String key, String customFileName, File file) throws FileNotFoundException {
        putArray(key, file, null, customFileName);
    }

    /**
     * Add a file to the request
     * @param key the key name for the new param.
     * @param file the file to add.
     * @param contentType the content type of the file, eg. application/json.
     * @throws FileNotFoundException
     */
    public void putArray(String key, File file, String contentType) throws FileNotFoundException {
        putArray(key, file, contentType, null);
    }

    /**
     * Add a file to the request
     * @param key the key name for the new param.
     * @param file the file to add.
     * @param contentType the content type of the file, eg. application/json.
     * @param customFileName file name to use instead of real file name.
     * @throws FileNotFoundException
     */
    public void putArray(String key, File file, String contentType, String customFileName) throws FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException();
        }
        if (key != null) {
            List<FileWrapper> fileWrappers = fileListParams.get(key);
            if (fileWrappers == null) {
                fileWrappers = new ArrayList<FileWrapper>();
                fileListParams.put(key, fileWrappers);
            }
            fileWrappers.add(new FileWrapper(file, contentType, customFileName));
        }
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     */
    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key    the key name for the new param.
     * @param stream the input stream to add.
     * @param name   the name of the stream.
     */
    public void put(String key, InputStream stream, String name) {
        put(key, stream, name, null);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, InputStream stream, String name, String contentType) {
        put(key, stream, name, contentType, autoCloseInputStreams);
    }

    /**
     * Adds an input stream to the request.
     *
     * @param key         the key name for the new param.
     * @param stream      the input stream to add.
     * @param name        the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     * @param autoClose   close input stream automatically on successful upload
     */
    public void put(String key, InputStream stream, String name, String contentType, boolean autoClose) {
        if (key != null && stream != null) {
            streamParams.put(key, StreamWrapper.newInstance(stream, name, contentType, autoClose));
        }
    }

    /**
     * Add an input stream to the request
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     */
    public void putArray(String key, InputStream stream) {
        putArray(key, stream, null);
    }

    /**
     * Add an input stream to the request
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param name the name of the stream.
     */
    public void putArray(String key, InputStream stream, String name) {
        putArray(key, stream, name, null);
    }

    /**
     * Add an input stream to the request
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param name the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     */
    public void putArray(String key, InputStream stream, String name, String contentType) {
        putArray(key, stream, name, contentType, autoCloseInputStreams);
    }

    /**
     * Add an input stream to the request
     * @param key the key name for the new param.
     * @param stream the input stream to add.
     * @param name the name of the stream.
     * @param contentType the content type of the file, eg. application/json
     * @param autoClose close input stream automatically on successful upload
     */
    public void putArray(String key, InputStream stream, String name, String contentType, boolean autoClose) {
        if (key != null && stream != null) {
            List<StreamWrapper> streamWrappers = streamListParams.get(key);
            if (streamWrappers == null) {
                streamWrappers = new ArrayList<StreamWrapper>();
                streamListParams.put(key, streamWrappers);
            }
            streamWrappers.add(StreamWrapper.newInstance(stream, name, contentType, autoClose));
        }
    }

    /**
     * Adds param with non-string value (e.g. Map, List, Set).
     *
     * @param key   the key name for the new param.
     * @param value the non-string value object for the new param.
     */
    public void put(String key, Object value) {
        if (key != null && value != null) {
            urlParamsWithObjects.put(key, value);
        }
    }

    /**
     * Adds a int value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value int for the new param.
     */
    public void put(String key, int value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds a long value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value long for the new param.
     */
    public void put(String key, long value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds string value to param which can have more than one value.
     *
     * @param key   the key name for the param, either existing or new.
     * @param value the value string for the new param.
     */
    public void add(String key, String value) {
        if (key != null && value != null) {
            Object params = urlParamsWithObjects.get(key);
            if (params == null) {
                // Backward compatible, which will com.zige.robot.Result in "k=v1&k=v2&k=v3"
                params = new HashSet<String>();
                this.put(key, params);
            }
            if (params instanceof List) {
                ((List<Object>) params).add(value);
            } else if (params instanceof Set) {
                ((Set<Object>) params).add(value);
            }
        }
    }

    /**
     * Removes a parameter from the request.
     *
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key) {
        urlParams.remove(key);
        streamParams.remove(key);
        fileParams.remove(key);
        urlParamsWithObjects.remove(key);
        streamListParams.remove(key);
        fileListParams.remove(key);
    }

    /**
     * Check if a parameter is defined.
     *
     * @param key the key name for the parameter to check existence.
     * @return Boolean
     */
    public boolean has(String key) {
        return urlParams.get(key) != null ||
                streamParams.get(key) != null ||
                fileParams.get(key) != null ||
                urlParamsWithObjects.get(key) != null ||
                streamListParams.get(key) != null ||
                fileListParams.get(key) != null;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, StreamWrapper> entry : streamParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("STREAM");
        }

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        List<BasicNameValuePair> params = getParamsList(null, urlParamsWithObjects);
        for (BasicNameValuePair kv : params) {
            if (result.length() > 0)
                result.append("&");

            result.append(kv.getName());
            result.append("=");
            result.append(kv.getValue());
        }

        return result.toString();
    }

    public void setHttpEntityIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public void setUseJsonStreamer(boolean useJsonStreamer) {
        this.useJsonStreamer = useJsonStreamer;
    }

    /**
     * Set global flag which determines whether to automatically close input streams on successful
     * upload.
     *
     * @param flag boolean whether to automatically close input streams
     */
    public void setAutoCloseInputStreams(boolean flag) {
        autoCloseInputStreams = flag;
    }

    /**
     * Returns an HttpEntity containing all request parameters.
     *
     * @return HttpEntity resulting HttpEntity to be included along with {@link
     * org.apache.http.client.methods.HttpEntityEnclosingRequestBase}
     * @throws IOException if one of the streams cannot be read
     */
    public HttpEntity getEntity() throws IOException {
        if (useJsonStreamer) {
            return createJsonStreamerEntity();
        } else if (streamParams.isEmpty() && fileParams.isEmpty() && streamListParams.isEmpty() && fileListParams.isEmpty()) {
            return createFormEntity();
        } else {
            return createMultipartEntity();
        }
    }

    private HttpEntity createJsonStreamerEntity() throws IOException {
        JsonStreamerEntity entity = new JsonStreamerEntity(!fileParams.isEmpty() || !streamParams.isEmpty());

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add non-string params
        for (ConcurrentHashMap.Entry<String, Object> entry : urlParamsWithObjects.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add stream params
        for (ConcurrentHashMap.Entry<String, StreamWrapper> entry : streamParams.entrySet()) {
            StreamWrapper stream = entry.getValue();
            if (stream.inputStream != null) {
                entity.addPart(entry.getKey(),
                        StreamWrapper.newInstance(stream.inputStream, stream.name, stream.contentType, stream.autoClose)
                );
            }
        }

        return entity;
    }

    private HttpEntity createFormEntity() {
        try {
            return new UrlEncodedFormEntity(getParamsList(), contentEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "createFormEntity failed", e);
            return null; // Can happen, if the 'contentEncoding' won't be HTTP.UTF_8
        }
    }

    private HttpEntity createMultipartEntity() throws IOException {
        SimpleMultipartEntity entity = new SimpleMultipartEntity();
        entity.setIsRepeatable(isRepeatable);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPartWithCharset(entry.getKey(), entry.getValue(), contentEncoding);
        }

        // Add non-string params
        List<BasicNameValuePair> params = getParamsList(null, urlParamsWithObjects);
        for (BasicNameValuePair kv : params) {
            entity.addPartWithCharset(kv.getName(), kv.getValue(), contentEncoding);
        }

        // Add stream params
        for (ConcurrentHashMap.Entry<String, StreamWrapper> entry : streamParams.entrySet()) {
            StreamWrapper stream = entry.getValue();
            if (stream.inputStream != null) {
                entity.addPart(entry.getKey(), stream.name, stream.inputStream,
                        stream.contentType);
            }
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType, fileWrapper.customFileName);
        }

        for (ConcurrentHashMap.Entry<String, List<StreamWrapper>> entry : streamListParams.entrySet()) {
            for (StreamWrapper streamWrapper : entry.getValue()) {
                if (streamWrapper.inputStream != null) {
                    entity.addPart(entry.getKey(), streamWrapper.name, streamWrapper.inputStream, streamWrapper.contentType);
                }
            }
        }

        for (ConcurrentHashMap.Entry<String, List<FileWrapper>> entry : fileListParams.entrySet()) {
            for (FileWrapper fileWrapper : entry.getValue()) {
                entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType, fileWrapper.customFileName);
            }
        }

        return entity;
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        lparams.addAll(getParamsList(null, urlParamsWithObjects));

        return lparams;
    }

    private List<BasicNameValuePair> getParamsList(String key, Object value) {
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (value instanceof Map) {
            Map map = (Map) value;
            List list = new ArrayList<Object>(map.keySet());
            // Ensure consistent ordering in query string
            if (list.size() > 0 && list.get(0) instanceof Comparable) {
                Collections.sort(list);
            }
            for (Object nestedKey : list) {
                if (nestedKey instanceof String) {
                    Object nestedValue = map.get(nestedKey);
                    if (nestedValue != null) {
                        params.addAll(getParamsList(key == null ? (String) nestedKey : String.format("%s[%s]", key, nestedKey),
                                nestedValue));
                    }
                }
            }
        } else if (value instanceof List) {
            List list = (List) value;
            int listSize = list.size();
            for (int nestedValueIndex = 0; nestedValueIndex < listSize; nestedValueIndex++) {
                params.addAll(getParamsList(String.format("%s[%d]", key, nestedValueIndex), list.get(nestedValueIndex)));
            }
        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int arrayLength = array.length;
            for (int nestedValueIndex = 0; nestedValueIndex < arrayLength; nestedValueIndex++) {
                params.addAll(getParamsList(String.format("%s[%d]", key, nestedValueIndex), array[nestedValueIndex]));
            }
        } else if (value instanceof Set) {
            Set set = (Set) value;
            for (Object nestedValue : set) {
                params.addAll(getParamsList(key, nestedValue));
            }
        } else {
            params.add(new BasicNameValuePair(key, value.toString()));
        }
        return params;
    }

    protected String getParamString() {
        return URLEncodedUtils.format(getParamsList(), contentEncoding);
    }

    public static class FileWrapper implements Serializable {
        public final File file;
        public final String contentType;
        public final String customFileName;

        public FileWrapper(File file, String contentType, String customFileName) {
            this.file = file;
            this.contentType = contentType;
            this.customFileName = customFileName;
        }
    }

    public static class StreamWrapper {
        public final InputStream inputStream;
        public final String name;
        public final String contentType;
        public final boolean autoClose;

        public StreamWrapper(InputStream inputStream, String name, String contentType, boolean autoClose) {
            this.inputStream = inputStream;
            this.name = name;
            this.contentType = contentType;
            this.autoClose = autoClose;
        }

        static StreamWrapper newInstance(InputStream inputStream, String name, String contentType, boolean autoClose) {
            return new StreamWrapper(
                    inputStream,
                    name,
                    contentType == null ? APPLICATION_OCTET_STREAM : contentType,
                    autoClose);
        }
    }
}
