package com.example.clocksystem.utils;

import java.util.Base64;

/**
 * @author 94548
 */
public class ImageConvert {
    /**
     * 图片转为Base64编码
     * @param picture 图片
     * @return Base64字符串
     */
    public static String convertByteArrayToBase64(byte[] picture){
        return Base64.getEncoder().encodeToString(picture);
    }
}
