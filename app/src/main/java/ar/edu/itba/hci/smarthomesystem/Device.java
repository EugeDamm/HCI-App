package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import api.Api;
import api.Error;

public class Device implements Parcelable, Serializable {

    private final String TAG = "Device";
    private String id;
    private String name;
    private String typeId;
    private String meta;

    public Device(String id, String name, String typeId, String meta) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getMeta() {
        return meta;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return String.format("%s", this.name);
    }

    protected Device(Parcel in) {
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
