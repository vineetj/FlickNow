package NetworkEngine;

import android.content.Context;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vineetjain.flicknow.BuildConfig;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import Listener.UpdateGsonListener;


public class VolleyGsonRequest<T> extends Request<T> {

    public static final String TAG = VolleyGsonRequest.class.getSimpleName();
    private int httpMethodType;
    private final String requestBody;
    private Gson mGson;
    private UpdateGsonListener<T> listener;
    private Class classz;
    private final String PROTOCOL_CHARSET = "utf-8";
    private String serviceType;
    private int requestType;
    private Context context;


    private VolleyGsonRequest(int method, String url, UpdateGsonListener<T> listener, Class classz,  String requestBody) {
        super(method, url, listener);
        this.listener = listener;
        this.httpMethodType = method;
        mGson = new Gson();
        this.classz = classz;
        this.requestBody = requestBody;
    }

    public static <T> VolleyGsonRequest processRequest(String url, UpdateGsonListener<T> updateListener, Class clasz, String requestBody, int methodType) {
        if (BuildConfig.DEBUG) {
        }
        if (methodType == NetworkEngine.NOT_SET) {
            methodType = TextUtils.isEmpty(requestBody) ? Method.GET : Method.POST;
        }
        return new VolleyGsonRequest(methodType, url, updateListener, clasz, requestBody);
    }


    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    public void setServiceType(String serviceType, int requestType) {
        this.serviceType = serviceType;
        this.requestType = requestType;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> mHeadersMap = new HashMap<>();
        String requestData;
        String url = getOriginUrl();
        if (httpMethodType != Method.GET) {
            requestData = requestBody;
        } else if (httpMethodType == Method.GET && url.contains("?")) {
            requestData = url.substring(url.indexOf("?") + 1);
        } else {
            requestData = null;
        }

        if (requestData != null) {

              // POST REQUEST ( Request body is present)
            mHeadersMap.put("Content-Type", "application/json; charset=utf-8");

            return mHeadersMap;
        } else {

            // GET REQUEST ( Request body is null)
            mHeadersMap.put("Content-Type", "application/json; charset=utf-8");
            return mHeadersMap;
        }
    }

    /*@Override
    protected VolleyError parseNetworkError(VolleyError volleyError){
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }*/

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            final String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

            Object serviceResponse = mGson.fromJson(json, classz);
           // return Response.success(parentResponseModel, parentResponseModel.isSuccessResponse() ? parseIgnoreCacheHeaders(json, response) : HttpHeaderParser.parseCacheHeaders(response));

            return Response.success(serviceResponse, parseIgnoreCacheHeaders(json, response));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return Response.error(new ParseError(ex));
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            return Response.error(new ParseError(ex));
        }
    }



    @Override
    public String getCacheKey() {
        return TextUtils.isEmpty(serviceType) ? super.getCacheKey() : serviceType + requestType;
    }

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 3 mins, ttl == 24 hours.
     *
     * @param json
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseIgnoreCacheHeaders(String json, NetworkResponse
            response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = 0; // will always refresh
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = json.getBytes();
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

    @Override
    protected void deliverResponse(Object response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
