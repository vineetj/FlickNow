package NetworkEngine;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


public class VolleyExceptionUtil {


	/**
	 * @param pException
	 * @return
	 */
	public static String getErrorMessage(Exception pException) {
		if (pException instanceof URISyntaxException) {
			return "Sorry! We are unable to process this request right now. Please try again later.";
		}
		if (pException instanceof UnknownHostException) {
			return "Sorry! We are unable to process this request right now. Please try again later.";
		}
		if (pException instanceof SocketException || pException instanceof TimeoutError  || pException instanceof JsonSyntaxException) {
			return "Sorry! We are unable to process this request right now. Please try again later.";
		}
		if (pException instanceof IOException || pException instanceof NoConnectionError) {
			return "Sorry! We are unable to process this request right now. Please try again later.";
		}
		return "Sorry! We are unable to process this request right now. Please try again later.";
	}
}