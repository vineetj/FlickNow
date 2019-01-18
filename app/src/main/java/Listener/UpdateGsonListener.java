package Listener;
import android.content.Context;


import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;


import java.io.UnsupportedEncodingException;

import NetworkEngine.VolleyExceptionUtil;


public abstract class UpdateGsonListener<T> implements Listener<T>, ErrorListener {

    private final Context mContext;
    private int reqType;
    private onUpdateViewListener onUpdateViewListener;
    private Context activityContext;

    public void setActivityContext(Context activityContext) {
        this.activityContext = activityContext;
    }

    public UpdateGsonListener(Context context, onUpdateViewListener onUpdateView, int reqType) {
        this.reqType = reqType;
        this.onUpdateViewListener = onUpdateView;
        mContext = context;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null && response.statusCode != 200) {
                onUpdateViewListener.returnResponse(handleServerError(error), false, reqType);
            } else {
                try {
                    onUpdateViewListener.returnResponse(VolleyExceptionUtil.getErrorMessage(error), false, reqType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onUpdateViewListener.returnResponse(VolleyExceptionUtil.getErrorMessage(error), false, reqType);

        }
    }

    private static Object handleServerError(Object err) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;

        if (response != null) {

            String statusCode = Integer.toString(response.statusCode);
            if (!statusCode.startsWith("2")) {
                try {

                    final String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    Object serviceResponse; /*= new Gson().fromJson(json, ErrorResponseModel.class);*/
                    serviceResponse = null;
                    return serviceResponse;
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    return VolleyExceptionUtil.getErrorMessage(ex);

                } catch (JsonSyntaxException ex) {
                    ex.printStackTrace();
                    return VolleyExceptionUtil.getErrorMessage(ex);
                }
            }

        }
        return null;
    }

    @Override
    public void onResponse(Object responseObj) {
        if (mContext == null || onUpdateViewListener == null) {
            return;
        }
        try {
            updateResult(responseObj, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            updateResult(VolleyExceptionUtil.getErrorMessage(ex), false);
        }

    }

     private void updateResult(Object responseObject, boolean isSuccess) {
        try {
            if (onUpdateViewListener != null) {
                onUpdateViewListener.returnResponse(responseObject, isSuccess, reqType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onJsonResponse(String jsonResponse);

}