package ar.edu.itba.hci.smarthomesystem;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import api.Error;

public class ErrorHandler extends Exception {
    public static void handleError(VolleyError error, Activity activity) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }
        Log.e("ERROR", error.toString());
        if(activity != null) {
            if (activity.getBaseContext() != null) {
                String text = activity.getResources().getString(R.string.error_message);
                if (response != null)
                    text += " " + response.getDescription().get(0);
                Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            }
        }
    }
}
