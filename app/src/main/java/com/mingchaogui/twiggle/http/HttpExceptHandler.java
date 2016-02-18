package com.mingchaogui.twiggle.http;


import com.litesuits.http.data.HttpStatus;
import com.litesuits.http.exception.ClientException;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.HttpServerException;
import com.litesuits.http.exception.NetException;
import com.litesuits.http.exception.ServerException;
import com.litesuits.http.exception.handler.HttpExceptionHandler;
import com.mingchaogui.twiggle.controller.BaseActivity;
import com.mingchaogui.twiggle.controller.BaseFragment;
import com.mingchaogui.twiggle.R;

/**
 * 一个通用的Http异常处理器
 */
public class HttpExceptHandler extends HttpExceptionHandler {

    private BaseActivity mActivity;

    public HttpExceptHandler(BaseActivity activity) {
        mActivity = activity;
    }

    public HttpExceptHandler(BaseFragment fragment) {
        mActivity = fragment.getBaseActivity();
    }

    @Override
    protected void onClientException(HttpClientException e, ClientException e1) {
        int resId;
        switch (e.getExceptionType()) {
            case UrlIsNull:
                resId = R.string.http_except_url_is_null;

                break;

            case ContextNeeded:// some action need app context
                resId = R.string.http_except_context_needed;

                break;

            case PermissionDenied:
                resId = R.string.http_except_permission_denied;

                break;

            case SomeOtherException:
                resId = R.string.http_except_client;

                break;

            default:
                resId = R.string.http_except_undefine;

                break;
        }
        handleExceptionMessage(resId);
    }

    @Override
    protected void onNetException(HttpNetException e, NetException e1) {
        int resId;
        switch (e.getExceptionType()) {
            case NetworkNotAvilable:
                resId = R.string.http_except_network_not_avilable;

                break;

            case NetworkUnstable:// maybe retried but fail
                resId = R.string.http_except_network_unstable;

                break;

            case NetworkDisabled:
                resId = R.string.http_except_network_disabled;

                break;

            default:
                resId = R.string.http_except_undefine;

                break;
        }

        handleExceptionMessage(resId);
    }

    @Override
    protected void onServerException(HttpServerException e, ServerException e1, HttpStatus httpStatus) {
        int resId;
        switch (e.getExceptionType()) {
            case ServerInnerError:
                resId = R.string.http_except_server_inner_error;

                break;

            case ServerRejectClient:// maybe retried but fail
                resId = R.string.http_except_server_reject_client;

                break;

            case RedirectTooMuch:
                resId = R.string.http_except_redirect_too_much;

                break;

            default:
                resId = R.string.http_except_undefine;

                break;
        }
        handleExceptionMessage(resId);
    }

    public void handleExceptionMessage(int resId) {
        mActivity.getToastor().showToast(resId);
    }

    public void release() {
        // 释放Activity的引用
        mActivity = null;
    }
}
