package com.mingchaogui.twiggle.http;


import android.content.Context;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.concurrent.SchedulePolicy;

/**
 * LiteHttp对象的封装、管理类
 */
public class HttpWrapper {

    private static LiteHttp http;

    public static void init(Context context) {
        if (http == null) {
            HttpConfig config = new HttpConfig(context)
                    .setDebugged(true)// 调试开关，打开后将会输出日志，App发布时记得关闭
                    .setGlobalHttpListener(null)// 设置全局监听器，可以协助监听所有请求的各个过程和结果
                    .setDefaultMaxRetryTimes(3)// 设置全局默认重试最大次数
                    .setDetectNetwork(true)// 设置连接前是否判断网络，若为true，判断无可用网络后直接结束请求。它需要设置context，才能有效
                    .setDoStatistics(true)// 设置全局是否开启流量、耗时等统计
                    .setTimeOut(10000, 10000)// 设置连接和读取超时时间
                    .setSchedulePolicy(SchedulePolicy.LastInFirstRun);// 设置全局默认请求调度策略，SchedulePolicy有 LastInFirstRun后进先执行 和 FirstInFistRun先进先执行

            http = LiteHttp.newApacheHttpClient(config);
        }
    }

    public static LiteHttp getHttp() {
        return http;
    }
}
