package com.evansappwriter.liveyourlist.core;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.evansappwriter.liveyourlist.LifeYourListApplication;
import com.evansappwriter.liveyourlist.R;
import com.evansappwriter.liveyourlist.util.Keys;
import com.evansappwriter.liveyourlist.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by markevans on 8/3/16.
 */
public class APIService {
    private static final String TAG = "APISERVICE";

    private static final String HTTP_AUTH_ERROR = "authorization_failure";
    private static final String HTTP_UNKNOWNHOST_EXCEPTION = "UnknownHostException";
    private static final String HTTP_TIMEOUT_EXCEPTION = "ConnectTimeoutException";

    private static APIService mInstance = null;

    private static final int TIMEOUT_READ = 60000; // ms
    private static final int TIMEOUT_CONNECT = 15000; // ms

    // Standard and Demo ROMS have different api_key
    private static final String API_KEY = "";

    @SuppressWarnings("ConstantConditions")
    private static final String REST_API = "";

    public static final String ENDPOINT_MESSAGES = "/users/messages";
    public static final String ENDPOINT_MESSAGE_COST = "/users/messages/cost";
    public static final String ENDPOINT_MESSAGE_DELETE = "/users/messages/delete";
    public static final String ENDPOINT_DRAFTS  = "/users/messages/drafts";
    public static final String ENDPOINT_DRAFT_DELETE  = "/users/messages/draft_delete";
    public static final String ENDPOINT_CONTACTS = "/users/contacts";

    private static Resources mRes;

    public interface OnUIResponseHandler {
        void onSuccess(String payload);
        void onFailure(String errorTitle, String errorText, int dialogId);
    }

    // private constructor prevents instantiation from other classes
    private APIService() {

    }

    /**
     * Creates a new instance of TelmateService.
     */
    public static APIService getInstance() {

        if (mInstance == null) {
            mInstance = new APIService();
        }

        mRes = LifeYourListApplication.getContext().getResources();

        return mInstance;
    }

    /**
     * *******************************************************************************************************
     */

    public void get(final String endpoints, Bundle params, final OnUIResponseHandler handler) {
        Bundle urlParams = getAuthBundle();
        if (params != null) {
            urlParams.putAll(params);
        }

        String uri = REST_API + endpoints;
        uri += "?" + encodeUrl(urlParams);

        Utils.printLogInfo(TAG, "API URL: " + uri);
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.setTimeout(TIMEOUT_READ);
        aClient.get(uri, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Utils.printLogInfo(TAG, "- Successful !: " + statusCode);

                processSuccessRepsonse(handler, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Utils.printLogInfo(TAG, "- Failed !: " + statusCode);

                processFailureRepsonse(handler, new String(responseBody), e.toString());
            }
        });
    }

    public void post(String endpoints, Bundle params, final OnUIResponseHandler handler) {
        Bundle urlParams = getAuthBundle();

        String uri = REST_API + endpoints;
        uri += "?" + encodeUrl(urlParams);

        RequestParams requestparams = new RequestParams();
        for (String key : params.keySet()) {
            requestparams.put(key, params.get(key).toString());
        }

        Utils.printLogInfo(TAG, "API URL: " + uri);
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.setTimeout(TIMEOUT_READ);
        aClient.post(uri, requestparams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Utils.printLogInfo(TAG, "- Successful !: " + statusCode);

                processSuccessRepsonse(handler, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Utils.printLogInfo(TAG, "- Failed !: " + statusCode);

                processFailureRepsonse(handler, new String(responseBody), e.toString());
            }
        });
    }

    public void post(String endpoints, String entity, String contentType, final OnUIResponseHandler handler) {
        Bundle urlParams = getAuthBundle();

        String uri = REST_API + endpoints;
        uri += "?" + encodeUrl(urlParams);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        Utils.printLogInfo(TAG, "API URL: " + uri);
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.setTimeout(TIMEOUT_READ);
        aClient.post(LifeYourListApplication.getContext(), uri, stringEntity, contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Utils.printLogInfo(TAG, "- Successful !: " + statusCode);

                processSuccessRepsonse(handler, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Utils.printLogInfo(TAG, "- Failed !: " + statusCode);

                processFailureRepsonse(handler, responseBody != null ? new String(responseBody) : null, e.toString());
            }
        });
    }

    private Bundle getAuthBundle() {
        Bundle params = new Bundle();

        params.putString(PARAM_API_KEY, API_KEY);

        return params;
    }

    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(200);
        boolean first = true;
        Set<String> keySet = parameters.keySet();

        for (String key : keySet) {
            Object parameter = parameters.get(key);

            if (!(parameter instanceof String)) {
                continue;
            }

            if (first) {
                first = false;
            } else {
                sb.append('&');
            }
            try {
                sb.append(URLEncoder.encode(key, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                Utils.printStackTrace(e);
            }
            sb.append('=');
            try {
                sb.append(URLEncoder.encode(parameters.getString(key), HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                Utils.printStackTrace(e);
            }
        }
        return sb.toString();
    }

    private void processSuccessRepsonse(OnUIResponseHandler handler, String payload) {
        handler.onSuccess(payload);
    }

    private void processFailureRepsonse(OnUIResponseHandler handler, String payload, String exception) {
        String errorTitle = "";
        String errorText = "";
        int dialogId;
        if (payload != null) {
            BundledData data = new BundledData(APIParser.TYPE_PARSER_ERROR);
            data.setHttpData(payload);
            APIParser.parseResponse(data);

            String error = (String) data.getAuxData()[0];
            String errorDetail = (String) data.getAuxData()[1];
            if (HTTP_AUTH_ERROR.equals(error)) {

                dialogId = Keys.DIALOG_GENERAL_ERROR;
            } else if (error.equals("")) {
                errorTitle = "";
                errorText = "";
                dialogId = Keys.DIALOG_GENERAL_ERROR;
            } else {
                errorTitle = error;
                errorText = errorDetail;
                dialogId = Keys.DIALOG_GENERAL_ERROR;
            }
            Utils.printLogInfo(TAG, "API error: ", errorDetail);
        } else {
            if (exception.contains(HTTP_UNKNOWNHOST_EXCEPTION)) {

            } else if (exception.contains(HTTP_TIMEOUT_EXCEPTION)){

            } else {
                // general error taken care of on UI level
            }
            dialogId = Keys.DIALOG_GENERAL_ERROR;
            Utils.printLogInfo(TAG, "API error: ", exception.toString());
        }

        handler.onFailure(errorTitle, errorText, dialogId);
    }

    // PARAMS >>>>>>>>>

    public static final String PARAM_API_KEY = "api_key";
}
