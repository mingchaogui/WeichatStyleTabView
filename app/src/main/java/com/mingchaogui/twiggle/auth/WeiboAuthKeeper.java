package com.mingchaogui.twiggle.auth;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * 微博授权信息管理类
 */
public class WeiboAuthKeeper {

    private static final String FILE_NAME = "weibo_auth";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_PHONE_NUM = "phone_num";

    // 使用SharedPreferences来存储微博授权信息
    private SharedPreferences mPreferences;

    public WeiboAuthKeeper(Context context) {
        mPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isEmpty() {
        return !mPreferences.contains(KEY_UID);
    }

    public WeiboAuth read() {
        String uid = mPreferences.getString(KEY_UID, "");
        String accessToken = mPreferences.getString(KEY_ACCESS_TOKEN, "");
        String refreshToken = mPreferences.getString(KEY_REFRESH_TOKEN, "");
        long expiresIn = mPreferences.getLong(KEY_EXPIRES_IN, 0L);
        String phoneNum = mPreferences.getString(KEY_PHONE_NUM, "");

        WeiboAuth auth = new WeiboAuth();
        auth.setUid(uid);
        auth.setAccessToken(accessToken);
        auth.setRefreshToken(refreshToken);
        auth.setExpiresTime(expiresIn);
        auth.setPhoneNum(phoneNum);

        return auth;
    }

    public void write(WeiboAuth auth) {
        if (auth == null) {
            clear();
        } else {
            mPreferences.edit()
                    .putString(KEY_UID, auth.getUid())
                    .putString(KEY_ACCESS_TOKEN, auth.getAccessToken())
                    .putString(KEY_REFRESH_TOKEN, auth.getRefreshToken())
                    .putLong(KEY_EXPIRES_IN, auth.getExpiresTime())
                    .putString(KEY_PHONE_NUM, auth.getPhoneNum())
                    .apply();
        }
    }

    public void clear() {
        mPreferences.edit()
                .clear()
                .apply();
    }
}
