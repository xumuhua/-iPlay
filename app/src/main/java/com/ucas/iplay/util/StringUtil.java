package com.ucas.iplay.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ivanchou on 3/15/15.
 */
public class StringUtil {
    public static String parseStringToMD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; ++i) {
                sb.append(Integer.toHexString((bytes[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}
