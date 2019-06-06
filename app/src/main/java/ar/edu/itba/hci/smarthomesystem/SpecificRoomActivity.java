package ar.edu.itba.hci.smarthomesystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.List;

import api.*;
import api.Error;

public class SpecificRoomActivity extends AppCompatActivity {

    private static final String TAG = "SpecificRoomActivity";
    List<Device> list;
    Context context = this;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        setContentView(R.layout.specific_room);
        linearLayout = findViewById(R.id.specificLinearLayout);
        if(bundle != null) {
            final Room room = (Room) bundle.get("room_name");
            if(actionBar != null)
                actionBar.setTitle(room.getName());
            Api.getInstance(this).getDevicesForRoom(new Response.Listener<ArrayList<Device>>() {
                @Override
                public void onResponse(ArrayList<Device> response) {
                    list = response;
                    if(list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            final int index = i;
                            Button btn = new Button(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 20;
                            params.leftMargin = 20;
                            params.rightMargin = 20;
                            btn.setLayoutParams(params);
                            btn.setText(list.get(i).toString());
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, SpecificDeviceActivity.class);
                                    final Device deviceToExpand = list.get(index);
                                    Bundle bundleToAdd = new Bundle();
                                    bundleToAdd.putString("device_name", deviceToExpand.getName());
                                    bundleToAdd.putString("device_id", deviceToExpand.getId());
                                    bundleToAdd.putString("device_type_id", deviceToExpand.getTypeId());
                                    bundleToAdd.putParcelable("go_back", room);
                                    intent.putExtras(bundleToAdd);
                                    startActivity(intent);
                                }
                            });
                            linearLayout.addView(btn);
                        }
                    } else {
                        //TODO no quiero un boton pero no me estaba saliendo con texto je
                        Log.d(TAG, "onResponse: el cuarto esta vacio");
                        Button btn = new Button(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 20;
                        params.leftMargin = 20;
                        params.rightMargin = 20;
                        btn.setLayoutParams(params);
                        btn.setText(R.string.no_devices_added);
                        linearLayout.addView(btn);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleError(error);
                }
            }, room.getId());
        }
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
        Log.e(TAG, error.toString());
        String text = getResources().getString(R.string.error_message);
        if (response != null)
            text += " " + response.getDescription().get(0);
        Toast.makeText(SpecificRoomActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
