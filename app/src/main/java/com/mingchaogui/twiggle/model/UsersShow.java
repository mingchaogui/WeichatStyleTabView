package com.mingchaogui.twiggle.model;


import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mingchaogui.twiggle.R;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsersShow implements Serializable {

    @SerializedName("id")
    public long uid;// 用户UID
    @SerializedName("idstr")
    public String uidStr;// 字符串型的用户UID
    @SerializedName("screen_name")
    public String screenName;// 用户昵称
    @SerializedName("name")
    public String name;// 友好显示名称
    @SerializedName("province")
    public long provinceId;// 用户所在省级ID
    @SerializedName("city")
    public long cityId;// 用户所在城市ID
    @SerializedName("location")
    public String location;// 用户所在地
    @SerializedName("description")
    public String description;// 用户个人描述
    @SerializedName("url")
    public String blogUrl;// 用户博客地址
    @SerializedName("profile_image_url")
    public String profileImageUrl;// 用户头像地址（中图），50×50像素
    @SerializedName("profile_url")
    public String profileUrl;// 用户的微博统一URL地址
    @SerializedName("domain")
    public String domain;// 用户的个性化域名
    @SerializedName("weihao")
    public String weihao;// 用户的微号
    @SerializedName("gender")
    public String gender;// 性别，m：男、f：女、n：未知
    @SerializedName("followers_count")
    public long followersCount;// 粉丝数
    @SerializedName("friends_count")
    public long friendsCount;// 关注数
    @SerializedName("statuses_count")
    public long statusesCount;// 微博数
    @SerializedName("favourites_count")
    public long favouritesCount;// 收藏数
    @SerializedName("created_at")
    public String createdAt;// 用户创建（注册）时间
    @SerializedName("following")
    public boolean following;// 暂未支持
    @SerializedName("allow_all_act_msg")
    public boolean allowAllActMsg;// 是否允许所有人给我发私信，true：是，false：否
    @SerializedName("geo_enabled")
    public boolean geoEnabled;// 是否允许标识用户的地理位置，true：是，false：否
    @SerializedName("verified")
    public boolean verified;// 是否是微博认证用户，即加V用户，true：是，false：否
    @SerializedName("verified_type")
    public int verifiedType;// 暂未支持
    @SerializedName("remark")
    public String remark;// 用户备注信息，只有在查询用户关系时才返回此字段
    @SerializedName("status")
    public Object status;// 用户的最近一条微博信息字段
    @SerializedName("allow_all_comment")
    public boolean allowAllComment;// 是否允许所有人对我的微博进行评论，true：是，false：否
    @SerializedName("avatar_large")
    public String avatarLarge;// 用户头像地址（大图），180×180像素
    @SerializedName("avatar_hd")
    public String avatarHd;// 用户头像地址（高清），高清头像原图
    @SerializedName("verified_reason")
    public String verifiedReason;// 认证原因
    @SerializedName("follow_me")
    public boolean followMe;// 该用户是否关注当前登录用户，true：是，false：否
    @SerializedName("online_status")
    public int onlineStatus;// 用户的在线状态，0：不在线、1：在线
    @SerializedName("bi_followers_count")
    public long biFollowersCount;// 用户的互粉数
    @SerializedName("lang")
    public String lang;// 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语

    public String getDisplayOnlineStatus(Context context) {
        return isOnline() ?
                context.getString(R.string.txt_online) : context.getString(R.string.txt_offline);
    }

    public String getDisplayCreateTime() {
        try {
            SimpleDateFormat usFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
            Date date = usFormat.parse(createdAt);
            return SimpleDateFormat.getDateTimeInstance().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt;
        }
    }

    public String getDisplayGender(Context context) {
        int resId;
        if (gender == null) {
            resId = R.string.txt_gender_unknown;
        } else {
            switch (gender) {
                case "m":
                    resId = R.string.txt_gender_male;

                    break;

                case "f":
                    resId = R.string.txt_gender_female;

                    break;

                default:
                    resId = R.string.txt_gender_unknown;

                    break;
            }
        }

        return context.getString(resId);
    }

    public boolean isOnline() {
        return onlineStatus == 1;
    }
}
