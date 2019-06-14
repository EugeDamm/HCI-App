package api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ar.edu.itba.hci.smarthomesystem.Device;
import ar.edu.itba.hci.smarthomesystem.Room;
import ar.edu.itba.hci.smarthomesystem.State;
import devices.DeviceType;

public class Api {
    private static Api instance;
    private static RequestQueue requestQueue;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    // Use IP 192.168.0.16 when running Android on a real phone and Euge's PC
    // Use IP 192.168.0.12 when running Android on a real phone and Euge's Macbook
    private final String URL = "http://192.168.0.12:8080/api/";
    private final String TAG = "Api";

    private Api(Context context) {
        this.requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static synchronized Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    public String addRoom(Room room, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Room> request =
                new GsonRequest<Room, Room>(Request.Method.POST, url, room, "room", new TypeToken<Room>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String modifyRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + room.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Boolean> request =
                new GsonRequest<Room, Boolean>(Request.Method.PUT, url, room, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteRoom(String id, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getRoom(String id, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Room> request =
                new GsonRequest<Object, Room>(Request.Method.GET, url, null, "room", new TypeToken<Room>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getRooms(Response.Listener<ArrayList<Room>> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/";
        GsonRequest<Object, ArrayList<Room>> request =
                new GsonRequest<Object, ArrayList<Room>>(Request.Method.GET, url, null, "rooms", new TypeToken<ArrayList<Room>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public void cancelRequest(String uuid) {
        if ((uuid != null) && (requestQueue != null)) {
            requestQueue.cancelAll(uuid);
        }
    }

    public String getDevicesForRoom(Response.Listener<ArrayList<Device>> listener, Response.ErrorListener errorListener, String roomId) {
        String url = URL + "rooms/" + roomId + "/devices";
        GsonRequest<Object, ArrayList<Device>> request =
                new GsonRequest<Object, ArrayList<Device>>(Request.Method.GET, url, null, "devices", new TypeToken<ArrayList<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDeviceState(Response.Listener<State> listener, Response.ErrorListener errorListener, String deviceId) {
        String url = URL + "devices/" + deviceId + "/getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        GsonRequest<Object, State> request =
                new GsonRequest<Object, State>(Request.Method.PUT, url, null, "result", new TypeToken<State>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String toggleDevice(Response.Listener<Boolean> listener, Response.ErrorListener errorListener, String deviceId, String status) {
        String url = URL + "devices/" + deviceId;
        switch (status) {
            case "opened":
                url += "/close";
                break;
            case "opening":
                url += "/close";
                break;
            case "closed":
                url += "/open";
                break;
            case "closing":
                url += "/open";
                break;
            case "on":
                url += "/turnOff";
                break;
            case "off":
                url += "/turnOn";
                break;
            case "locked":
                url += "/unlock";
                break;
            case "unlocked":
                url += "/lock";
                break;
            case "active":
                url += "/stop";
                break;
            case "inactive":
                url +="/start";
                break;
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        GsonRequest<Boolean, Boolean> request =
                new GsonRequest<Boolean, Boolean>(Request.Method.PUT, url, null, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setBrightness(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, int brightness) {
        String url = URL + "devices/" + deviceId + "/setBrightness";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        Integer[] data = {brightness};
        GsonRequest<Integer[], String> request =
                new GsonRequest<Integer[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setMode(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setMode";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setTemperature(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, int temperature) {
        String url = URL + "devices/" + deviceId + "/setTemperature";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        Integer[] data = {temperature};
        GsonRequest<Integer[], String> request =
                new GsonRequest<Integer[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setFreezerTemperature(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, int temperature) {
        String url = URL + "devices/" + deviceId + "/setFreezerTemperature";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        Integer[] data = {temperature};
        GsonRequest<Integer[], String> request =
                new GsonRequest<Integer[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setVerticalSwing(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setVerticalSwing";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setHorizontalSwing(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setHorizontalSwing";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setFanSpeed(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setVerticalSwing";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setGrill(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setGrill";
        Log.d(TAG, "setGrill: " + mode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setHeat(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setHeat";
        Log.d(TAG, "setGrill: " + mode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setConvection(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String mode) {
        String url = URL + "devices/" + deviceId + "/setConvection";
        Log.d(TAG, "setGrill: " + mode);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {mode};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setColor(Response.Listener<String> listener, Response.ErrorListener errorListener, String deviceId, String color) {
        String url = URL + "devices/" + deviceId + "/setColor";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        String[] data = {color};
        GsonRequest<String[], String> request =
                new GsonRequest<String[], String>(Request.Method.PUT, url, data, "result", new TypeToken<String>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }


}
