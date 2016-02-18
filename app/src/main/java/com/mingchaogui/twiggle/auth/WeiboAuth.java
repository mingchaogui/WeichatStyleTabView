package com.mingchaogui.twiggle.auth;


import java.io.Serializable;

public class WeiboAuth implements Serializable {

    private String mUid = "";
    private String mAccessToken = "";
    private String mRefreshToken = "";
    private long mExpiresTime = 0L;
    private String mPhoneNum = "";

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return mExpiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        mExpiresTime = expiresTime;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }
}
