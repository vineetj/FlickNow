package NetworkEngine;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;



public class VolleyManager {

    private static final String TAG = VolleyManager.class.getSimpleName();
    private static VolleyManager mVolleyManager;
    private RequestQueue mRequestQueue;
    private Context mContext;


    public static void clearSingleton() {
        mVolleyManager = null;
    }

    private VolleyManager(Context context) {
        mContext = context;
            mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static VolleyManager getInstance(Context context) {
        if (mVolleyManager == null) {
            mVolleyManager = new VolleyManager(context);
        }
        return mVolleyManager;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 24, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		req.setShouldCache(false);

        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}