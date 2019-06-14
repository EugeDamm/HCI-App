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

public class RoomListViewModel extends AndroidViewModel {
        private MutableLiveData<ArrayList<Room>> rooms;

    public RoomListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Room>> getRooms() {
            if (rooms == null) {
                rooms = new MutableLiveData<ArrayList<Room>>();
                loadRooms();
            }
            return rooms;
        }

    private void loadRooms() {
        Api.getInstance(getApplication()).getRooms(new Response.Listener<ArrayList<Room>>() {
            @Override
            public void onResponse(ArrayList<Room> response) {
                rooms.setValue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
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

