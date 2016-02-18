package com.mingchaogui.twiggle.http_param;


import com.litesuits.http.annotation.HttpCacheExpire;
import com.litesuits.http.annotation.HttpCacheMode;
import com.litesuits.http.annotation.HttpID;
import com.litesuits.http.annotation.HttpMethod;
import com.litesuits.http.annotation.HttpUri;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.request.param.HttpParam;
import com.litesuits.http.request.param.HttpRichParamModel;
import com.mingchaogui.twiggle.auth.WeiboAuth;
import com.mingchaogui.twiggle.model.UsersShow;

import java.util.concurrent.TimeUnit;

@HttpUri("https://api.weibo.com/2/users/show.json")
@HttpMethod(HttpMethods.Get)
@HttpID(1)
/**
 * 优先使用缓存
 */
@HttpCacheMode(CacheMode.CacheFirst)
/**
 * 缓存有效期
 */
@HttpCacheExpire(value = 3, unit = TimeUnit.MINUTES)
public class UsersShowParam extends HttpRichParamModel<UsersShow> {

    @HttpParam("access_token")
    public String accessToken;// 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
    @HttpParam("uid")
    public String uid;// 需要查询的用户ID，参数uid与screen_name二者必选其一，且只能选其一
    @HttpParam("screen_name")
    public String screenName;// 需要查询的用户昵称，参数uid与screen_name二者必选其一，且只能选其一

    public UsersShowParam(WeiboAuth auth) {
        this.accessToken = auth.getAccessToken();
        this.uid = auth.getUid();
    }
}
