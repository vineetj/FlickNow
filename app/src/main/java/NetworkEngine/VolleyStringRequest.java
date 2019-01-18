package NetworkEngine;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;


public class VolleyStringRequest extends StringRequest {

    public static final String TAG = VolleyStringRequest.class.getSimpleName();
    private final String mRequestBody;
    private final String PROTOCOL_CHARSET = "utf-8";


    public VolleyStringRequest(int method, String url, String requestbody, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mRequestBody = requestbody;
    }

    public String getBodyContentType() {
        return "application/json";
    }


    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG, "Unsupported Encoding while trying to get the bytes of %s using %s" + "---requestBod---" + mRequestBody);
            return null;
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            if(WebServerUrl.isEncryptionON){
//                parsed = APIEncodeDecode.decryptAES256(parsed.trim());
//            }
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}