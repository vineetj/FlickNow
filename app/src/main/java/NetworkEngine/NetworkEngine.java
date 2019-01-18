package NetworkEngine;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vineetjain.flicknow.R;


import java.io.UnsupportedEncodingException;

import Listener.UpdateGsonListener;
import Listener.onUpdateViewListener;
import Utils.NetworkAvailability;


public class NetworkEngine {

    private static final String TAG = NetworkEngine.class.getSimpleName();
    public static final int NOT_SET = -1;
    private static NetworkEngine instance;
    private static Context mContext;
    private String url;
    private Class clasz;
    private String serviceType;
    private int requestType;
    private onUpdateViewListener listener;

    private int httpMethodType = NOT_SET;
    private boolean cacheResponse = false;//default value is false

    private NetworkEngine() {

    }

    public static NetworkEngine with(Context context) {
        mContext = context;
        instance = new NetworkEngine();
        return instance;
    }

    public NetworkEngine setUrl(String url) {
        this.url = url;
        return instance;
    }

    public NetworkEngine setCacheResponse(boolean flag) {
        this.cacheResponse = flag;
        return instance;
    }

    public NetworkEngine setClassType(Class clasz) {
        this.clasz = clasz;
        return instance;
    }

    public NetworkEngine setUpdateViewListener(onUpdateViewListener listener) {
        this.listener = listener;
        return instance;
    }



    public NetworkEngine setServiceType(String serviceType) {
        this.serviceType = serviceType;
        return instance;
    }

    public NetworkEngine setRequestType(int requestType) {
        this.requestType = requestType;
        return instance;
    }

    public NetworkEngine setHttpMethodType(int httpMethodType) {
        this.httpMethodType = httpMethodType;
        return instance;
    }





    public void build() {

        String requestBody = null; // for POST Request

        UpdateGsonListener updateGsonListener = new UpdateGsonListener<Object>(mContext, listener, requestType) {


                @Override
                public void onJsonResponse(String jsonResponse) {
                }

            };

        if(!NetworkAvailability.isNetworkAvailable(mContext))
        {
            listener.returnResponse(mContext.getResources().getString(R.string.no_internet),false,requestType);
        }

     else {
            VolleyGsonRequest volleyGsonRequest = VolleyGsonRequest.processRequest(url, updateGsonListener, clasz, requestBody, httpMethodType);
            volleyGsonRequest.setContext(mContext.getApplicationContext());
            volleyGsonRequest.setShouldCache(cacheResponse);
            volleyGsonRequest.setServiceType(serviceType, requestType);

            Cache cache = VolleyManager.getInstance(mContext.getApplicationContext()).getRequestQueue().getCache();
            Cache.Entry entry = cache.get(serviceType + requestType);
            if (entry != null) { //INVALIDATING CACHE TO STORE NEW RESPONSE IN CACHE
                cache.invalidate(serviceType + requestType, true); //need to invalidate current cache data, this will instruct volley not use (cache + network call), just use network call
                //and keep cache for case when net is off.
            }
            VolleyManager.getInstance(mContext.getApplicationContext()).addToRequestQueue(volleyGsonRequest, serviceType + "_" + requestType);
        }
    }




    public interface OnJsonResponse {
        void onRawJsonReceived(String jsonResponse);
    }



    public interface OnCachedResponse {
        void onCachedResponseRecieved(boolean isCacheAvailable, Object responseObject, int requestType);
    }

}
