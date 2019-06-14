package ar.edu.itba.hci.smarthomesystem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import api.Api;
import api.Error;

public class DevicesViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Device>> devices;
    private String roomId;

    public DevicesViewModel(@NonNull Application application) {
        super(application);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public MutableLiveData<ArrayList<Device>> getDevices() {
        if(devices == null) {
            devices = new MutableLiveData<>();
            loadDevices();
        }
        return devices;
    }

    private void loadDevices() {
        Api.getInstance(getApplication()).getDevicesForRoom(new Response.Listener<ArrayList<Device>>() {
            @Override
            public void onResponse(ArrayList<Device> response) {
                devices.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        }, roomId);
    }

    private void handleError(VolleyError error) {
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
        Log.e("RepositoryClass", error.toString());
        String text = "Error";
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(getApplication().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
