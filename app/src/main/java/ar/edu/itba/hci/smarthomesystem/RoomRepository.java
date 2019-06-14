package ar.edu.itba.hci.smarthomesystem;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class RoomRepository {

    private Application application;
    private MutableLiveData<ArrayList<Room>> rooms;
    private Context context;
    private Api api;
    private final String TAG = "Rooms";
    private boolean flag = false;
    private boolean pase = false;

    public RoomRepository(@NonNull Application application) {
        this.application = application;
        this.context = this.application.getApplicationContext();
        this.api = Api.getInstance(this.context);
        if (this.rooms == null)
            this.rooms = new MutableLiveData<>();
    }

    public void fetchRooms() {
        api.getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                rooms.setValue(response);
                saveRooms(response, rooms);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    public void saveRooms(ArrayList<Room> response, MutableLiveData<ArrayList<Room>> withValueSet) {
        Log.d(TAG, "getRooms: response = " + response);
        Log.d(TAG, "getRooms: rooms.getValue() = " + withValueSet.getValue());
        this.rooms.setValue(response);
        getRooms();
    }

    public MutableLiveData<ArrayList<Room>> getRooms() {
        Log.d(TAG, "getRooms: would return = " + rooms.getValue());
        return rooms;
    }


}
