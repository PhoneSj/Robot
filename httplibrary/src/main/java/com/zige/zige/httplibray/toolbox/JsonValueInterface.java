package com.zige.zige.httplibray.toolbox;

/**
 * 功能：
 * <p/>
 * Created by user on 2016/4/26.
 */
public interface JsonValueInterface {

    /**
     * Returns the escaped, ready-to-be used value of this encapsulated object.
     *
     * @return byte array holding the data to be used (as-is) in a JSON object
     */
    byte[] getEscapedJsonValue();
}